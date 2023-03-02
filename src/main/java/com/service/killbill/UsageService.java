package com.service.killbill;
import com.configuration.property.KillBillApiProperties;
import com.model.MeteringData;
import com.repository.MeteringDataRepository;
import com.service.meteringdb.MeteringDataService;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.UsageApi;
import org.killbill.billing.client.model.gen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UsageService {
    private static final Logger log =  LogManager.getLogger(UsageService.class);
    private final KillBillHttpClient killBillClient;
    private final KillBillApiProperties apiProperties;
    private final Executor executor;
    @Autowired
    BundleService bundleService;
    @Autowired
    MeteringDataRepository meteringDataRepository;
    @Autowired
    MeteringDataService meteringDataService;
    final long RETRY_INTERVAL_SECONDS=5;
    final long MAX_RETRIES=6;
    AccountApi accountApi;
    UsageApi usageApi;
    int FIRST_ITEM=0;
    public UsageService(KillBillHttpClient killBillClient, KillBillApiProperties apiProperties, Executor executor) {
        this.killBillClient = killBillClient;
        this.executor= executor;
        this.apiProperties = apiProperties;
        usageApi= new UsageApi(killBillClient);
        accountApi=new AccountApi(killBillClient);
    }
    /**
     * Records subscription usage in KillBill system.
     *
     * @param body The subscription usage record to be recorded.
     * @param inputOptions The request options, including followLocation, accept and content-type headers.
     * @return The HTTP response from the KillBill system.
     * @throws KillBillClientException if there is an error while sending the HTTP request.
     */
    public Response recordUsage(final SubscriptionUsageRecord body, final @NotNull RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling recordUsage");
        final String uri = "/1.0/kb/usages";
        final RequestOptions.RequestOptionsBuilder inputOptionsBuilder = inputOptions.extend();
        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        inputOptionsBuilder.withFollowLocation(followLocation);
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_ACCEPT, "application/json");
        inputOptionsBuilder.withHeader(KillBillHttpClient.HTTP_HEADER_CONTENT_TYPE, "application/json");
        final RequestOptions requestOptions = inputOptionsBuilder.build();
       return killBillClient.doPost(uri, body, requestOptions);
    }

    public RolledUpUsage getUsageBySubscriptionId(final UUID subscriptionId, LocalDate startDate, LocalDate endDate) throws KillBillClientException {
        return usageApi.getUsage(subscriptionId,"kWh",startDate,endDate, apiProperties.getRequestOptions());
    }
    /**
     * Maps a subscription ID, event time, and consumed amount to a SubscriptionUsageRecord object.
     * This method is annotated with @Async to indicate that its executed asynchronously.
     *
     * @param subscriptionId the ID of the subscription in KillBill where subscription and Smart Meter have one to one relation
     * @param eventTime the time of the metering data
     * @param consumedAmount the amount of the consumed energy
     * @return a SubscriptionUsageRecord object containing the mapped usage record
     */@Async// Run this method on a separate thread
    public SubscriptionUsageRecord mapUsageRecord(String subscriptionId, DateTime eventTime, long consumedAmount) {
        return new SubscriptionUsageRecord()
                .setSubscriptionId(UUID.fromString(subscriptionId))
                .setUnitUsageRecords(Arrays.asList(
                        new UnitUsageRecord()
                                .setUnitType("kWh")
                                .setUsageRecords(Arrays.asList(
                                        new UsageRecord(eventTime.toLocalDate(), consumedAmount)
                                ))));}

    /**
     *  The method pushingUsageRecord() pushes usage records to the KillBill.
     *  The method first retrieves the external keys (smart meters) and their corresponding subscription IDs from KillBill.
     *  Based on this data, the method retrieves metering data from database. This data is then pushed in parallel to KillBill.
     *  Once the data has been successfully pushed, the method marks the corresponding records in the database as retrieved.
     **/
    @Scheduled(fixedRate = 180000) // Run this method every 180 seconds
    @Async
    public void pushingUsageRecord() throws KillBillClientException {
        // Get a map of smart meter IDs (external key) and their corresponding subscription IDs
        Map<String, UUID> smartMeterAndSubscriptionIds = bundleService.getSmartMeterAndSubscriptionIds();
        List<String> smartMeterIds = smartMeterAndSubscriptionIds.keySet().stream().collect(Collectors.toList());
        // Get the metering data that is not retrieved to KillBill and have subscription in KillBill
        List<MeteringData> smartMeterRecords = meteringDataService.returnMeteringData(smartMeterIds);
        if(smartMeterRecords.size()>0){

        List<MeteringData> retrievedMeteringData = pushUsageRecordKillBill(smartMeterAndSubscriptionIds, smartMeterRecords);
        //update the records that successfully pushed to KillBill
        meteringDataService.updateMeteringData(retrievedMeteringData);}}
    /**
     * Pushes usage records to KillBill for the given smart meter records and subscription IDs.
     * This message is working concurrently using executor.execute() , with retry mechanism to record that failed to reach KillBill
     * @param smartMeterSubscriptionIds a map of subscription IDs mapped to their corresponding smart meter IDs
     * @param smartMeterRecords a list of smart meter records to be pushed to KillBill
     * @return a list of metering data that was successfully retrieved
     */
    //@Async
    public List<MeteringData> pushUsageRecordKillBill(Map<String,UUID> smartMeterSubscriptionIds,List<MeteringData> smartMeterRecords){
        List<MeteringData> retrievedMeteringData=new ArrayList<>();
        Map<String, Integer> retryAttempts = new HashMap<>();
        for (MeteringData meteringRecord: smartMeterRecords) {
            executor.execute(() -> {
                int retries = 0;
                retryAttempts.getOrDefault(meteringRecord.getSmartMeterId(), 0);
                while (retries < MAX_RETRIES) {
                    try {
                        retryAttempts.put(meteringRecord.getSmartMeterId(), retries + 1);
                        SubscriptionUsageRecord subscriptionUsageRecord = mapUsageRecord("" + smartMeterSubscriptionIds.get(meteringRecord.getSmartMeterId()), meteringRecord.getProcessTime(), (long) meteringRecord.getEnergyTotal());
                        Response response = recordUsage(subscriptionUsageRecord, apiProperties.getRequestOptions());
                        System.out.println("response status: " + response.getStatusCode());
                        if (response.getStatusCode() == 201) {
                            meteringRecord.setRetrieved(true);
                            retrievedMeteringData.add(meteringRecord);}
                        break;
                    } catch (Exception e) {
                        retries++;
                        log.error("Error pushing smart meter data: " + meteringRecord.getSmartMeterId() + ". Retrying...", e);
                        try {TimeUnit.SECONDS.sleep(RETRY_INTERVAL_SECONDS);
                        } catch (InterruptedException ie) {
                            log.error("Interrupted while sleeping between retries.", ie);
                        }}}});
    }
            return retrievedMeteringData;
    }
}

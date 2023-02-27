package com.Metersdata.springboot.service.killbill;
import com.Metersdata.springboot.configuration.property.KillBillApiProperties;
import com.Metersdata.springboot.model.MeteringData;
import com.Metersdata.springboot.repository.MeteringDataRepository;
import com.Metersdata.springboot.service.meteringdb.MeteringDataService;
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
    public RolledUpUsage getUsageApiBySubscriptionId(final UUID subscriptionId, LocalDate startDate, LocalDate endDate) throws KillBillClientException {
        return usageApi.getUsage(subscriptionId,"kWh",startDate,endDate, apiProperties.getRequestOptions());
    }
    @Async
    public SubscriptionUsageRecord mapUsageRecord(String subId,DateTime time,long amount) {
        SubscriptionUsageRecord subscriptionUsageRecord = new SubscriptionUsageRecord();
        subscriptionUsageRecord.setSubscriptionId(UUID.fromString(subId));
        UnitUsageRecord unitUsageRecord = new UnitUsageRecord();
        unitUsageRecord.setUnitType("kWh");
        UsageRecord usageRecord =new UsageRecord(time.toLocalDate(), amount);
        unitUsageRecord.setUsageRecords( Arrays.asList(usageRecord));
        subscriptionUsageRecord.setUnitUsageRecords(Arrays.asList(unitUsageRecord));
        return subscriptionUsageRecord;
    }
    @Scheduled(fixedRate = 180000)
    @Async
    public void pushingUsageRcord() throws KillBillClientException {
        Map<String, UUID> externalKeySubscriptionId = bundleService.getExternalKeySubscription();
        Map<String, Integer> retryAttempts = new HashMap<>();
        List<String> externalKey = externalKeySubscriptionId.keySet().stream().collect(Collectors.toList());
        List<MeteringData> smartMeterRecords = meteringDataService.returnMeteringData(externalKey);
        if(smartMeterRecords.size()>0){
        List<MeteringData> retrievedMeteringData = pushUsageRecordKillBill(externalKeySubscriptionId, smartMeterRecords);
        meteringDataService.updateMeteringData(retrievedMeteringData);}}
    @Async
    public List<MeteringData> pushUsageRecordKillBill(Map<String,UUID> externalKeySubscriptionIds,List<MeteringData> smartMeterRecords) throws KillBillClientException {
        List<MeteringData> retrievedMeteringData=new ArrayList<>();
        Map<String, Integer> retryAttempts = new HashMap<>();
        for (MeteringData meteringRecord: smartMeterRecords) {
            executor.execute(() -> {
                int retries = 0;
                retryAttempts.getOrDefault(meteringRecord.getSmartMeterId(), 0);
                while (retries < MAX_RETRIES) {
                    try {
                        retryAttempts.put(meteringRecord.getSmartMeterId(), retries + 1);
                        SubscriptionUsageRecord subscriptionUsageRecord = mapUsageRecord("" + externalKeySubscriptionIds.get(meteringRecord.getSmartMeterId()), meteringRecord.getProcessTime(), (long) meteringRecord.getEnergyTotal());
                        Response response = recordUsage(subscriptionUsageRecord, apiProperties.getRequestOptions());
                        System.out.println("response status: " + response.getStatusCode());
                        if (response.getStatusCode() == 201) {
                            System.out.println("status:200");
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

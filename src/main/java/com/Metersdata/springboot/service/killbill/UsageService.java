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
import org.killbill.billing.client.api.gen.InvoiceApi;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

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
    /*
      //@Scheduled(fixedRate = 3000)
      public void sendUsageRecord() throws KillBillClientException {
       /* SubscriptionUsageRecord subscriptionUsageRecord= dummyUsageRecord(UUID.fromString("c4ac5d96-fdfe-45f5-8f5c-d46794e5364f"));
         if(recordUsage(subscriptionUsageRecord, apiProperties.getRequestOptions())==null){
                 System.out.print("failed to add usage record");
             }
      }

      public SubscriptionUsageRecord dummyUsageRecord(UUID subId) {
          SubscriptionUsageRecord subscriptionUsageRecord = new SubscriptionUsageRecord();
          subscriptionUsageRecord.setSubscriptionId(subId);
          UnitUsageRecord unitUsageRecord = new UnitUsageRecord();
          unitUsageRecord.setUnitType("kWh");
          UsageRecord usageRecord =new UsageRecord(DateTime.now().toLocalDate(), (long)(Math.random() * (10 - 1) + 1));
          unitUsageRecord.setUsageRecords( Arrays.asList(usageRecord));
          subscriptionUsageRecord.setUnitUsageRecords(Arrays.asList(unitUsageRecord));
          return subscriptionUsageRecord;
      }
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

    public RolledUpUsage getUsageApi(final UUID accountId, LocalDate startDate, LocalDate endDate) throws KillBillClientException {
        Bundle bundle= accountApi.getAccountBundles(accountId,null,null, apiProperties.getRequestOptions()).get(FIRST_ITEM);//zero to return the first and the only one
        List<Bundle> bundles= accountApi.getAccountBundles(accountId,null,null, apiProperties.getRequestOptions());
        return (bundle==null)? null:usageApi.getUsage(bundle.getSubscriptions().get(FIRST_ITEM).getSubscriptionId(),"kWh",startDate,endDate, apiProperties.getRequestOptions());
    }

   @Scheduled(fixedRate = 180000)
    @Async
    public void pushingUsageRcord() throws KillBillClientException {
        Map<String,UUID> externalKeySubscriptionId = bundleService.getExternalKeySubscription();
        Map<String, Integer> retryAttempts = new HashMap<>();
        for (String externalKey:externalKeySubscriptionId.keySet()) {

            executor.execute(() -> {
                int retries = 0;
                retryAttempts.getOrDefault(externalKey, 0);
                while (retries < MAX_RETRIES) {

                    try {
                        retryAttempts.put(externalKey, retries + 1);
                        pushUsageRecord(externalKey);


                        break;
                    } catch (Exception e) {
                        retries++;
                        log.error("Error pushing smart meter data: " +externalKey + ". Retrying...", e);
                        try {
                            TimeUnit.SECONDS.sleep(RETRY_INTERVAL_SECONDS);
                        } catch (InterruptedException ie) {
                            log.error("Interrupted while sleeping between retries.", ie);
                        }
                    }
                }
                if (retries == MAX_RETRIES) {
                    log.error("Failed to push the usage record: " + externalKey + " after " + MAX_RETRIES + " retries.");
                }
            });
        }

    }

    public SubscriptionUsageRecord mapUsageRecord(String subId,long time,long amount) {
        SubscriptionUsageRecord subscriptionUsageRecord = new SubscriptionUsageRecord();
        subscriptionUsageRecord.setSubscriptionId(UUID.fromString(subId));
        UnitUsageRecord unitUsageRecord = new UnitUsageRecord();
        unitUsageRecord.setUnitType("kWh");
        UsageRecord usageRecord =new UsageRecord(DateTime.now().toLocalDate(), amount);
        unitUsageRecord.setUsageRecords( Arrays.asList(usageRecord));
        subscriptionUsageRecord.setUnitUsageRecords(Arrays.asList(unitUsageRecord));
        return subscriptionUsageRecord;
    }
    public void pushUsageRecord(String externalKey) throws KillBillClientException {
        List<MeteringData> smartMeterRecords = meteringDataService.returnByMeterId(externalKey);
       String subscriptionId=""+bundleService.getExternalKeySubscription(externalKey);
        for (MeteringData meteringRecord: smartMeterRecords) {
            SubscriptionUsageRecord subscriptionUsageRecord = mapUsageRecord(subscriptionId,meteringRecord.getProcessTime(), (long) meteringRecord.getEnergyTotal());
            Response response= recordUsage(subscriptionUsageRecord, apiProperties.getRequestOptions());
            System.out.println("response status: "+response.getStatusCode());
            if (response.getStatusCode()==201){
                meteringRecord.setRetrieved(true);
                meteringDataRepository.save(meteringRecord);
            }
        }
    }
}




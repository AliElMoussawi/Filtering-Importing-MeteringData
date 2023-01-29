package com.Metersdata.springboot.services.killbill;
import com.Metersdata.springboot.configurations.killbill.property.KillBillApiProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.asynchttpclient.Response;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.UsageApi;
import org.killbill.billing.client.model.gen.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.killbill.billing.client.api.gen.InvoiceApi;

import java.util.Arrays;
import java.util.UUID;

@Service
public class UsageService {
    private final KillBillHttpClient killBillClient;
    private final KillBillApiProperties apiProperties;
    UsageApi usageApi;
    InvoiceApi invoiceApi;
    AccountApi accountApi;
    //zero to return the first and the only one
    int FIRST_ITEM=0;
    public UsageService(KillBillHttpClient killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.apiProperties = apiProperties;
        usageApi= new UsageApi(killBillClient);
        invoiceApi= new InvoiceApi(killBillClient);
        accountApi=new AccountApi(killBillClient);
    }

    //@Scheduled(fixedRate = 3000)
    public void sendUsageRecord() throws KillBillClientException {

       SubscriptionUsageRecord subscriptionUsageRecord= dummyUsageRecord(UUID.fromString("c4ac5d96-fdfe-45f5-8f5c-d46794e5364f"));
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
    public Response recordUsage(final SubscriptionUsageRecord body, final RequestOptions inputOptions) throws KillBillClientException {
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
        return (bundle==null)? null:usageApi.getUsage(bundle.getSubscriptions().get(FIRST_ITEM).getSubscriptionId(),"kWh",startDate,endDate, apiProperties.getRequestOptions());
    }
}




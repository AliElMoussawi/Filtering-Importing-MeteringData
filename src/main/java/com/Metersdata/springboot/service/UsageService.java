package com.Metersdata.springboot.service;
import com.Metersdata.springboot.configuration.KillBill.Api.UsageApiConfiguration;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.asynchttpclient.Response;
import org.joda.time.DateTime;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.client.api.gen.UsageApi;
import org.killbill.billing.client.model.gen.SubscriptionUsageRecord;
import org.killbill.billing.client.model.gen.UnitUsageRecord;
import org.killbill.billing.client.model.gen.UsageRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UsageService {
    private final KillBillHttpClient killBillClient;
    private final UsageApiConfiguration usageApiConfiguration;
    UsageApi usageApi;

    public UsageService(KillBillHttpClient killBillClient,UsageApiConfiguration usageApiConfiguration) {
        this.killBillClient = killBillClient;
        this.usageApiConfiguration = usageApiConfiguration;
        usageApi= new UsageApi(killBillClient);

    }

    @Scheduled(fixedRate = 3000000)
    public void sendUsageRecord() throws KillBillClientException {

       SubscriptionUsageRecord subscriptionUsageRecord= dummyUsageRecord(UUID.fromString("c4ac5d96-fdfe-45f5-8f5c-d46794e5364f"));


            System.out.println(""+recordUsage(subscriptionUsageRecord, usageApiConfiguration.getRequestOptions()));


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
        System.out.println(inputOptions.getTenantApiKey()+" /"+ inputOptions.getTenantApiSecret());
       return killBillClient.doPost(uri, body, requestOptions);
    }



}




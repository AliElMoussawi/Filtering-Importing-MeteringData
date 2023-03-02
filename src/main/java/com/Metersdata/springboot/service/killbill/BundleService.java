package com.Metersdata.springboot.service.killbill;

import com.Metersdata.springboot.configuration.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.model.Bundles;
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.Subscription;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BundleService {

    private final KillBillApiProperties apiProperties;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;

    public BundleService(KillBillHttpClient  killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }
    // get the externalKey of all subscriptions from KillBill
    // externalKey represents the SmartMeterId
    @Cacheable(cacheNames ="SubscriptionExternalKeyCache",  cacheManager ="cacheSubscriptionManager")
    public  Map<String ,UUID> getSmartMeterAndSubscriptionIds() throws KillBillClientException{
        //external Key == Smart Meter ID
        Map<String ,UUID> externalKeySubscriptionId = new HashMap<>();
    Iterator<Bundle> bundleList = bundleApi.getBundles(apiProperties.getRequestOptions()).iterator();
        while (bundleList.hasNext()) {
            for(Subscription subscription:bundleList.next().getSubscriptions()) {
                if(subscription.getCancelledDate()==null){
                externalKeySubscriptionId.put(subscription.getExternalKey(),subscription.getSubscriptionId());
            }}}
        return externalKeySubscriptionId;}
    }

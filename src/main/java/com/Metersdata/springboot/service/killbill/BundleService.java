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
    @Cacheable(cacheNames ="SubscriptionExternalKeyCache" ,  cacheManager ="cacheSubscriptionManager")
    public  Map<String ,UUID> getExternalKeySubscription() throws KillBillClientException{
        Map<String ,UUID> externalKeySubscriptionId = new HashMap<>();
    Bundles bundles =bundleApi.getBundles(apiProperties.getRequestOptions());
    Iterator<Bundle> bundleList = bundles.iterator();
        while (bundleList.hasNext()) {
            Bundle bundle =bundleList.next();
            List<Subscription> subscriptions =bundle.getSubscriptions();
            for(Subscription subscription:subscriptions) {
                if(subscription.getCancelledDate()==null){
                externalKeySubscriptionId.put(subscription.getExternalKey(),subscription.getSubscriptionId());
            }}
        }
        return externalKeySubscriptionId;
}
    public UUID getExternalKeySubscription(String externalKey ) throws KillBillClientException {
        Map<String, UUID> externalKeySubscriptionId = getExternalKeySubscription();
        return externalKeySubscriptionId.get(externalKey);
    }
    }

package com.Metersdata.springboot.services.killbill;

import com.Metersdata.springboot.configurations.killbill.property.KillBillApiProperties;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.github.benmanes.caffeine.cache.Cache;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
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


    @Cacheable(value = "SubscriptionExternalKeyCache")
    public  Map<String ,UUID> getExternalKeySubscription() throws KillBillClientException{
        Map<String ,UUID> externalKeySubscriptionId = new HashMap<>();
    Bundles bundles =bundleApi.getBundles(apiProperties.getRequestOptions());
    Iterator<Bundle> bundleList = bundles.iterator();
        while (bundleList.hasNext()) {
            List<Subscription> subscriptions = bundleList.next().getSubscriptions();
            for(Subscription subscription:subscriptions) {
                externalKeySubscriptionId.put(subscription.getExternalKey(),subscription.getSubscriptionId());
            }
        }
        return externalKeySubscriptionId;
}

}

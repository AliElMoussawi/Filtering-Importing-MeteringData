package com.Metersdata.springboot.services.killbill;

import com.Metersdata.springboot.configurations.killbill.property.KillBillApiProperties;
import com.github.benmanes.caffeine.cache.Cache;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.api.gen.InvoiceApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
import org.killbill.billing.client.api.gen.UsageApi;
import org.killbill.billing.client.model.gen.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriptionService {

    private final KillBillApiProperties apiProperties;

    SubscriptionApi subscriptionApi;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;
    private final Cache<String, Subscription> subscriptionCache;

    public SubscriptionService(KillBillHttpClient  killBillClient, Cache<String, Subscription> subscriptionCache,KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.subscriptionCache = subscriptionCache;
        subscriptionApi= new SubscriptionApi(killBillClient);
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }

    public Subscription getSubscriptionbyExternalKey(String externalKey) throws KillBillClientException {
        Subscription subscription = subscriptionCache.getIfPresent(externalKey);
        if (subscription != null) {
            return subscription;
        }
        subscription =null; //subscriptionApi.getSubscriptionByKey(externalKey,);
        subscriptionCache.put(externalKey, subscription);
        return subscription;
    }
}

package com.Metersdata.springboot.services.killbill;

import com.Metersdata.springboot.configurations.killbill.property.KillBillApiProperties;
import com.github.benmanes.caffeine.cache.Cache;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
import org.killbill.billing.client.model.Bundles;
import org.killbill.billing.client.model.gen.Subscription;
import org.springframework.stereotype.Service;

@Service
public class BundleService {

    private final KillBillApiProperties apiProperties;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;
    private final Cache<String, Subscription> subscriptionCache;

    public BundleService(KillBillHttpClient  killBillClient, Cache<String, Subscription> subscriptionCache, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.subscriptionCache = subscriptionCache;
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }

    public Bundles getBundles() throws KillBillClientException {
        return bundleApi.getBundles(apiProperties.getRequestOptions());
    }
}

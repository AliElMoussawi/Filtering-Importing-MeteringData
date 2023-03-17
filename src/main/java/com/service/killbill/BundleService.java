package com.service.killbill;

import com.configuration.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.Subscription;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BundleService {
    private final KillBillApiProperties apiProperties;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;

    public BundleService(KillBillHttpClient  killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }
    /**
     * Retrieves the smart meter IDs and their subscription IDs using a cache to improve performance.
     * The results of this method will be cached and subsequent calls to the method with the same
     * parameters will return the cached results instead of executing the method again.
     *
     * @return Map<String, UUID> containing smart meter IDs and subscription IDs.
     * @throws KillBillClientException if there was an error retrieving the list of Bundles or Subscriptions.
     *
     * @Cacheable(cacheNames ="SubscriptionExternalKeyCache",  cacheManager ="cacheSubscriptionManager")
     */
    @Cacheable(cacheNames ="SubscriptionSmartMeterCache",  cacheManager ="cacheSubscriptionManager")
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

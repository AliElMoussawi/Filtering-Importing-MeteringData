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

import java.util.Map;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final KillBillApiProperties apiProperties;
    @Autowired
    BundleService bundleService;
    SubscriptionApi subscriptionApi;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;

    public SubscriptionService(KillBillHttpClient  killBillClient,KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        subscriptionApi= new SubscriptionApi(killBillClient);
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }

    public UUID getSubscriptionbyExternalKey(String externalKey) throws KillBillClientException {
        Map<String, UUID> stringUUIDMap = bundleService.getExternalKeySubscription();
        return stringUUIDMap.get(externalKey);
    }
}

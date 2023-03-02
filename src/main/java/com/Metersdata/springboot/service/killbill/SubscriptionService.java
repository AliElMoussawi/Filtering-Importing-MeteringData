package com.Metersdata.springboot.service.killbill;

import com.Metersdata.springboot.configuration.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
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
        Map<String, UUID> stringUUIDMap = bundleService.getSmartMeterAndSubscriptionIds();
        return stringUUIDMap.get(externalKey);
    }
}

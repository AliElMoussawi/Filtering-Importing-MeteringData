package com.Metersdata.springboot.services.killbill;

import com.Metersdata.springboot.configurations.killbill.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.InvoiceApi;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    private final KillBillHttpClient killBillClient;
    private final KillBillApiProperties apiProperties;

    InvoiceApi invoiceApi;
    AccountApi accountApi;

    int FIRST_ITEM=0;
    public InvoiceService(KillBillHttpClient killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.apiProperties = apiProperties;
        invoiceApi= new InvoiceApi(killBillClient);
        accountApi=new AccountApi(killBillClient);
    }





}




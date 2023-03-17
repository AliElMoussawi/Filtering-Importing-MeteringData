package com.service.killbill;

import com.configuration.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.InvoiceApi;
import org.killbill.billing.client.model.Invoices;
import org.killbill.billing.client.model.gen.Invoice;
import org.killbill.billing.util.api.AuditLevel;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class InvoiceService {
    private final KillBillHttpClient killBillClient;
    private final KillBillApiProperties apiProperties;
    InvoiceApi invoiceApi;
    AccountApi accountApi;
    public InvoiceService(KillBillHttpClient killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.apiProperties = apiProperties;
        invoiceApi= new InvoiceApi(killBillClient);
        accountApi=new AccountApi(killBillClient);
    }

    public Iterator<Invoice> getAllInvoices() throws KillBillClientException {
        Invoices invoices= invoiceApi.getInvoices(0L,100L, AuditLevel.NONE,apiProperties.getRequestOptions());
        return invoices.stream().iterator();
    }
}




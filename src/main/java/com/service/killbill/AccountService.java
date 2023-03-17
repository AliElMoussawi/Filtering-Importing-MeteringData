package com.service.killbill;

import com.configuration.property.KillBillApiProperties;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.InvoiceApi;
import org.killbill.billing.client.model.Bundles;
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.Invoice;
import org.killbill.billing.util.api.AuditLevel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {
    private final KillBillHttpClient killBillClient;
    private final KillBillApiProperties apiProperties;
    InvoiceApi invoiceApi;
    AccountApi accountApi;
    public AccountService(KillBillHttpClient killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        this.apiProperties = apiProperties;
        invoiceApi= new InvoiceApi(killBillClient);
        accountApi=new AccountApi(killBillClient);
    }

    /**
     * Get the subscriptions for the specified account.
     * Account can have several subscriptions (each subscription represents Smart Meter)
     * @param accountId is the costumer account in KillBill
     * @return list of subscription Ids
     * @throws KillBillClientException
     */
    public List<UUID> getAccountSubscriptions(UUID accountId) throws KillBillClientException {
        Bundles bundles =accountApi.getAccountBundles(accountId,null,null,apiProperties.getRequestOptions());
        Iterator<Bundle> bundleList = bundles.iterator();
        List<UUID> subscriptionIds=new ArrayList<>();
        if(bundleList.hasNext()){
            //get zero because the relation between subscription and bundle is one to one
            subscriptionIds.add(bundleList.next().getSubscriptions().get(0).getSubscriptionId());}
        return subscriptionIds;
    }
    /**
     * Retrieves the invoices for the specified account and returns them as a map of invoice IDs to HTML strings.
     *
     * @param accountId the ID of the account to retrieve invoices for
     * @return a map of invoice IDs to HTML strings for the specified account
     * @throws KillBillClientException if there is an error retrieving the invoices
     */
    public Map<UUID,String> getAccountInvoices(UUID accountId) throws KillBillClientException {
        Iterator<Invoice> invoices = accountApi.getInvoicesForAccount(accountId, null, null, Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), AuditLevel.NONE, apiProperties.getRequestOptions()).iterator();
        Map<UUID,String> invoiceHtml=new HashMap<>();
        if(invoices.hasNext()) {
            UUID invoiceId=invoices.next().getInvoiceId();
        invoiceHtml.put(invoiceId,invoiceApi.getInvoiceAsHTML(invoiceId,apiProperties.getRequestOptions()));
        }
        return invoiceHtml;
    }


}
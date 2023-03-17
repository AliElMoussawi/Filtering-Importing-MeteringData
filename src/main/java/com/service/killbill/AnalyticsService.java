package com.service.killbill;

import com.configuration.property.KillBillApiProperties;
import com.dto.killbill.UsageRecordDto;
import org.joda.time.DateTime;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.api.gen.BundleApi;
import org.killbill.billing.client.api.gen.SubscriptionApi;
import org.killbill.billing.client.model.gen.RolledUpUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    private final KillBillApiProperties apiProperties;
    UsageService usageService;
    BundleService bundleService;
    SubscriptionApi subscriptionApi;
    BundleApi bundleApi;
    private final KillBillHttpClient killBillClient;
    public AnalyticsService(KillBillHttpClient  killBillClient, KillBillApiProperties apiProperties) {
        this.killBillClient = killBillClient;
        subscriptionApi= new SubscriptionApi(killBillClient);
        bundleApi=new BundleApi(killBillClient);
        this.apiProperties=apiProperties;
    }
    public Map<String, List<UsageRecordDto>> getUsageGroupBy(String groupedBy, DateTime startDate, DateTime endDate) throws KillBillClientException {

        System.out.println("getUsageGroupBy("+groupedBy+")");
        Map<String, UUID> smartMeterAndSubscriptionIds = bundleService.getSmartMeterAndSubscriptionIds();
        Map<String, List<UsageRecordDto>> smartMeterUsageRecord = new HashMap<>();
        for (String smartMeter : smartMeterAndSubscriptionIds.keySet()) {
            List<UsageRecordDto> usageSmartMeter = getUsageByGroupedPeriod(smartMeterAndSubscriptionIds.get(smartMeter), groupedBy, startDate, endDate);
            smartMeterUsageRecord.put(smartMeter, usageSmartMeter);
        }
        return smartMeterUsageRecord;
    }
    private List<UsageRecordDto> getUsageByGroupedPeriod(UUID subscriptionId, String groupedBy, DateTime startDate, DateTime endDate) throws KillBillClientException {
        List<UsageRecordDto> usageData = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            DateTime oldStartDate=startDate;
            switch (groupedBy) {
                case "Daily":
                    startDate = startDate.plusDays(1);
                    break;
                case "Weekly":
                    startDate = startDate.plusWeeks(1);
                    break;
                case "Monthly":
                    startDate = startDate.plusMonths(1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid grouping parameter");
            }
            RolledUpUsage usage = usageService.getUsageBySubscriptionId(subscriptionId,  oldStartDate.toLocalDate(),startDate.toLocalDate());
            long amount=0;
            if(usage.getRolledUpUnits().size()==1){
                amount=usage.getRolledUpUnits().get(0).getAmount();}
        usageData.add(new UsageRecordDto(amount, usage.getStartDate(), usage.getEndDate()));}
        return usageData;
    }
}

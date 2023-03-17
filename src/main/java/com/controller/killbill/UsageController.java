package com.controller.killbill;

import com.dto.killbill.UsageRecordRequestDto;
import com.service.killbill.BundleService;
import com.service.killbill.UsageService;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.model.gen.RolledUpUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/usage")
@CrossOrigin
public class UsageController {
    BundleService bundleService;
    UsageService usageService;
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity< RolledUpUsage> getUsagebySubscriptionId(@RequestBody UsageRecordRequestDto usageRecordRequest) throws KillBillClientException {
        RolledUpUsage rolledUpUsage=usageService.getUsageBySubscriptionId(usageRecordRequest.getSubscriptionId(),usageRecordRequest.getStartDate(),usageRecordRequest.getEndDate());
    return new ResponseEntity<>(rolledUpUsage, HttpStatus.OK);}

    //check the analysis plugin that is provided by killbill
    // and then visualize it.

    public ResponseEntity<Integer> getAllUsageMonthyBase() throws KillBillClientException {
        Map<String, UUID> smartMeterAndSubscriptionIds = bundleService.getSmartMeterAndSubscriptionIds();
        for(UUID subscriptionId:smartMeterAndSubscriptionIds.values()) {
        }// usageService.getUsageBySubscriptionId();}
        return null;
    }//for each smart meter all usage per month and per week


}

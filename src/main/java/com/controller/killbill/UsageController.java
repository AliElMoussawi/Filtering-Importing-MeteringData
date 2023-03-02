package com.controller.killbill;

import com.dto.killbill.UsageRecordRequestDto;
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

@Controller
@RequestMapping("/usage")
@CrossOrigin
public class UsageController {
    @Autowired
    UsageService usageService;
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity< RolledUpUsage> getUsagebySubscriptionId(@RequestBody UsageRecordRequestDto usageRecordRequest) throws KillBillClientException {
        RolledUpUsage rolledUpUsage=usageService.getUsageBySubscriptionId(usageRecordRequest.getSubscriptionId(),usageRecordRequest.getStartDate(),usageRecordRequest.getEndDate());
    return new ResponseEntity<>(rolledUpUsage, HttpStatus.OK);}

}

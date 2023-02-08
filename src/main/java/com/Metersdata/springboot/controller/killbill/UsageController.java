package com.Metersdata.springboot.controller.killbill;

import com.Metersdata.springboot.dto.killbill.UsageRecordRequestDto;
import com.Metersdata.springboot.service.killbill.UsageService;
import org.joda.time.LocalDate;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.api.gen.UsageApi;
import org.killbill.billing.client.model.gen.Bundle;
import org.killbill.billing.client.model.gen.RolledUpUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.management.MemoryNotificationInfo;
import java.util.UUID;

@Controller
@RequestMapping("/usage")
@CrossOrigin
public class UsageController {
    @Autowired
    UsageService usageService;

   // @RequestMapping(method = RequestMethod.POST, value = "/")
  //  public ResponseEntity< RolledUpUsage> getUsagebyId(@RequestBody UsageRecordRequestDto usageRecordRequest) throws Exception {
    //    RolledUpUsage usageRecord=usageService.getUsageApi(usageRecordRequest.getAccountId(),usageRecordRequest.getStartDate(),usageRecordRequest.getEndDate());
      //  return (usageRecord==null)? null: new ResponseEntity<>(usageRecord, HttpStatus.OK);
    //}


    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity< RolledUpUsage> getUsagebySubscriptionId(@RequestBody UsageRecordRequestDto usageRecordRequest) throws KillBillClientException {
        RolledUpUsage rolledUpUsage=usageService.getUsageApiBySubscriptionId(usageRecordRequest.getSubscriptionId(),usageRecordRequest.getStartDate(),usageRecordRequest.getEndDate());
    return new ResponseEntity<>(rolledUpUsage, HttpStatus.OK);}

}

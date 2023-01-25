package com.Metersdata.springboot.controller;

import com.Metersdata.springboot.request.UsageRecordRequest;
import com.Metersdata.springboot.service.UsageService;
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
    public ResponseEntity< RolledUpUsage> getUsagebyId(@RequestBody UsageRecordRequest usageRecordRequest) throws Exception {
        RolledUpUsage usageRecord=usageService.getUsageApi(usageRecordRequest.getAccountId(),usageRecordRequest.getStartDate(),usageRecordRequest.getEndDate());
        return (usageRecord==null)? null: new ResponseEntity<>(usageRecord, HttpStatus.OK);
    }


}

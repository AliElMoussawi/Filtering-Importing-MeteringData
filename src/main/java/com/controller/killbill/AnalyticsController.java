package com.controller.killbill;

import com.dto.killbill.AnalyticsRequestDto;
import com.dto.killbill.UsageRecordDto;
import com.service.killbill.AccountService;
import com.service.killbill.AnalyticsService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/analytics")
public class AnalyticsController {
    AnalyticsService analyticsService;
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Map<String, List<UsageRecordDto>>> getUsageByGroup(@RequestBody AnalyticsRequestDto analyticsRequestDto) throws Exception {
        Map<String, List<UsageRecordDto>> usageByGroup = analyticsService.getUsageGroupBy(analyticsRequestDto.getGroupedBy(), analyticsRequestDto.getStartDate(), analyticsRequestDto.getEndDate());
        if(usageByGroup!=null) System.out.println("not null");
         return (usageByGroup!=null? new ResponseEntity<>(usageByGroup, HttpStatus.OK): new ResponseEntity<>(usageByGroup, HttpStatus.NO_CONTENT));
    }
    @RequestMapping(method = RequestMethod.GET, value = "/weekly")
    public ResponseEntity<Map<String, List<UsageRecordDto>>> getUsageGroupedByWeekly() throws Exception {
        AnalyticsRequestDto analyticsRequestDto=new AnalyticsRequestDto();
        analyticsRequestDto.setGroupedBy("Weekly");
        analyticsRequestDto.setStartDate(new DateTime("2023-01-01"));
        analyticsRequestDto.setEndDate(new DateTime());
        return getUsageByGroup(analyticsRequestDto);
    }
}

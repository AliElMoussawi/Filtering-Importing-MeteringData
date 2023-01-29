package com.Metersdata.springboot.controllers.mongodb;

import com.Metersdata.springboot.dto.killbill.UsageRecordDto;
import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.services.killbill.UsageService;
import com.Metersdata.springboot.services.meteringdb.ConcentratorService;
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
@RequestMapping("/concentrator")
@CrossOrigin
public class ConcentratorController {
    @Autowired
    ConcentratorService concentratorService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public Concentrator createConcentrator() throws Exception {
        return concentratorService.createConcentrator();
    }


    }

package com.Metersdata.springboot.controllers.mongodb;
import com.Metersdata.springboot.dto.mongodb.SmartMeterConcentratorDto;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.services.meteringdb.SmartMeterConcentratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/smartMeterConcentrator")
@CrossOrigin
public class SmartMeterConcentratorController {
    @Autowired
    SmartMeterConcentratorService smartMeterConcentratorService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public SmartMeterConcentrator createConcentrator(@RequestBody SmartMeterConcentratorDto smartMeterConcentratorDto) throws Exception {
        return smartMeterConcentratorService.createSmartMeterConcentrator(smartMeterConcentratorDto.getConcentratorId() ,smartMeterConcentratorDto.getSmartMeterId());
    }


    }

package com.Metersdata.springboot.controllers.mongodb;
import com.Metersdata.springboot.dto.mongodb.SmartMeterConcentratorDto;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.services.meteringdb.SmartMeterConcentratorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/smartMeterConcentrator")
@CrossOrigin
public class SmartMeterConcentratorController {
    @Autowired
    SmartMeterConcentratorService smartMeterConcentratorService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<SmartMeterConcentrator> createConcentrator(@RequestBody @NotNull SmartMeterConcentratorDto smartMeterConcentratorDto) throws Exception {
        SmartMeterConcentrator smartMeterConcentrator = smartMeterConcentratorService.createSmartMeterConcentrator(smartMeterConcentratorDto.getConcentratorId(),smartMeterConcentratorDto.getSmartMeterId());
        return (smartMeterConcentrator == null) ? null : new ResponseEntity<>(smartMeterConcentrator, HttpStatus.OK);
    }
}

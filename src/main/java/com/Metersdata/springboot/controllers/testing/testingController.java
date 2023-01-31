package com.Metersdata.springboot.controllers.testing;

import com.Metersdata.springboot.dto.mongodb.requestDto;
import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.services.meteringdb.ConcentratorService;
import com.Metersdata.springboot.util.data.EnergyConsumptionData;
import com.Metersdata.springboot.util.generator.EnergyConsumptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/testing")
@CrossOrigin
public class testingController {

    @RequestMapping(method = RequestMethod.POST, value = "/t")
    public ResponseEntity<EnergyConsumptionData> createEnergyConcumption(@RequestBody requestDto id) throws Exception {
        EnergyConsumptionData energyConsumptionData= new EnergyConsumptionGenerator().generateData(id.getId());
        System.out.println(id);
        return (energyConsumptionData ==null)? null: new ResponseEntity<>(energyConsumptionData, HttpStatus.OK);
    }


    }

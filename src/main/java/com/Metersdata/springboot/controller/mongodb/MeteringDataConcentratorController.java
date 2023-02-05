package com.Metersdata.springboot.controller.mongodb;
import com.Metersdata.springboot.dto.mongodb.SmartMeterConcentratorDto;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.service.meteringdb.MeteringDataConcentratorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meteringDataConcentrator")
@CrossOrigin
public class MeteringDataConcentratorController {
    @Autowired
    MeteringDataConcentratorService meteringDataConcentratorService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<SmartMeterConcentrator> createConcentrator(@RequestBody @NotNull SmartMeterConcentratorDto smartMeterConcentratorDto) throws Exception {
        SmartMeterConcentrator smartMeterConcentrator = meteringDataConcentratorService.createSmartMeterConcentrator(smartMeterConcentratorDto.getConcentratorId(),smartMeterConcentratorDto.getSmartMeterId());
        return (smartMeterConcentrator == null) ? null : new ResponseEntity<>(smartMeterConcentrator, HttpStatus.OK);
    }
}

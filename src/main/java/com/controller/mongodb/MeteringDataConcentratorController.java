package com.controller.mongodb;
import com.dto.mongodb.SmartMeterConcentratorDto;
import com.model.SmartMeterConcentrator;
import com.service.meteringdb.MeteringDataConcentratorService;
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
       if(smartMeterConcentrator == null) return new ResponseEntity<>(smartMeterConcentrator,HttpStatus.ALREADY_REPORTED);
       return new ResponseEntity<>(smartMeterConcentrator,HttpStatus.OK);
    }
}

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
@RequestMapping("/smartMeterConcentrator")
@CrossOrigin
public class MeteringDataConcentratorController {
    @Autowired
    MeteringDataConcentratorService meteringDataConcentratorService;

    /**
     * This method creates a relation between the smartMeter and the concentrator used by the simulator
     *
     * @param smartMeterConcentratorDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<SmartMeterConcentrator> createSmartMeterConcentratorRelation(@RequestBody @NotNull SmartMeterConcentratorDto smartMeterConcentratorDto)  {
        SmartMeterConcentrator smartMeterConcentrator = meteringDataConcentratorService.createSmartMeterConcentrator(smartMeterConcentratorDto.getConcentratorId(),smartMeterConcentratorDto.getSmartMeterId());
       if(smartMeterConcentrator == null) return new ResponseEntity<>(smartMeterConcentrator,HttpStatus.ALREADY_REPORTED);
       return new ResponseEntity<>(smartMeterConcentrator,HttpStatus.OK);
    }
}

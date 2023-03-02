package com.Metersdata.springboot.service.meteringdb;

import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.repository.MeteringDataConcentratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
//for simulator
@Service
public class MeteringDataConcentratorService {
    @Autowired
    MeteringDataConcentratorRepository meteringDataConcentratorRepository;

    //create a relation between the smart meter and concentrator for the simulator
    public SmartMeterConcentrator createSmartMeterConcentrator(UUID concentratorId,UUID smartMeterId){
        if(meteringDataConcentratorRepository.existsBySmartMeterId(smartMeterId)){
            return null;}
        SmartMeterConcentrator smartMeterConcentrator=new SmartMeterConcentrator(UUID.randomUUID(),concentratorId,smartMeterId);
        return meteringDataConcentratorRepository.insert(smartMeterConcentrator);
    }

}

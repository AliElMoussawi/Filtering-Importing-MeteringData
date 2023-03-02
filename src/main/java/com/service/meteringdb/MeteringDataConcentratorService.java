package com.service.meteringdb;

import com.model.SmartMeterConcentrator;
import com.repository.MeteringDataConcentratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
//for simulator
@Service
public class MeteringDataConcentratorService {
    @Autowired
    MeteringDataConcentratorRepository meteringDataConcentratorRepository;

    /**
     * Create a relation between the smart meter and concentrator for the simulator
    */
    public SmartMeterConcentrator createSmartMeterConcentrator(UUID concentratorId, UUID smartMeterId){
        if(meteringDataConcentratorRepository.existsBySmartMeterId(smartMeterId)){
            return null;}
        SmartMeterConcentrator smartMeterConcentrator=new SmartMeterConcentrator(UUID.randomUUID(),concentratorId,smartMeterId);
        return meteringDataConcentratorRepository.insert(smartMeterConcentrator);
    }

}

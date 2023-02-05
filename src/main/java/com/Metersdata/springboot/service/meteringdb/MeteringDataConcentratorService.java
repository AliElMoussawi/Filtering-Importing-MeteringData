package com.Metersdata.springboot.service.meteringdb;

import com.Metersdata.springboot.model.SmartMeterConcentrator;
import com.Metersdata.springboot.repository.MeteringDataConcentratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeteringDataConcentratorService {
    @Autowired
    MeteringDataConcentratorRepository meteringDataConcentratorRepository;

    public SmartMeterConcentrator createSmartMeterConcentrator(UUID concentratorId,UUID smartMeterId){
        SmartMeterConcentrator smartMeterConcentrator=new SmartMeterConcentrator(UUID.randomUUID(),concentratorId,smartMeterId);
        return meteringDataConcentratorRepository.insert(smartMeterConcentrator);
    }

}

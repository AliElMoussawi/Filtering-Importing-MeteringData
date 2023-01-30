package com.Metersdata.springboot.services.meteringdb;

import com.Metersdata.springboot.dao.ConcentratorRepository;
import com.Metersdata.springboot.dao.SmartMeterConcentratorRepository;
import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.model.SmartMeterConcentrator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class SmartMeterConcentratorService {

    @Autowired(required = true)
    SmartMeterConcentratorRepository smartMeterConcentratorRepository;

    public SmartMeterConcentrator createSmartMeterConcentrator(UUID concentratorId,UUID smartMeterId){
        SmartMeterConcentrator smartMeterConcentrator=new SmartMeterConcentrator(UUID.randomUUID(),concentratorId,smartMeterId);
        return smartMeterConcentratorRepository.insert(smartMeterConcentrator);
    }

}

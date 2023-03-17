package com.service.meteringdb;

import com.dto.mongodb.ConcentratorDto;
import com.repository.ConcentratorRepository;
import com.model.Concentrator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class ConcentratorService {

    ConcentratorRepository concentratorRepository;
    /**
     * Add concentrator to the database
     */
    public Concentrator createConcentrator(){
        Concentrator concentrator=new Concentrator(UUID.randomUUID() ,"Spain", Math.abs(new Random().nextLong())  ,UUID.randomUUID() ,new DateTime() );
        return concentratorRepository.insert(concentrator);
    }
    public Concentrator createConcentrator(ConcentratorDto concentratorDto){
        Concentrator concentrator=new Concentrator(concentratorDto.getConcentratorId(),concentratorDto.getLocaltion(), concentratorDto.getSerialNumber()  ,concentratorDto.getProduct(),new DateTime(concentratorDto.getManufactureDate()) );
        return concentratorRepository.insert(concentrator);
    }
}

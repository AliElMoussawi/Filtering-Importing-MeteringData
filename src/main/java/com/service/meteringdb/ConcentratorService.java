package com.service.meteringdb;

import com.repository.ConcentratorRepository;
import com.model.Concentrator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class ConcentratorService {

    @Autowired
    ConcentratorRepository concentratorRepository;
    /**
     * Add concentrator to the database
     */
    public Concentrator createConcentrator(){
        Concentrator concentrator=new Concentrator(UUID.randomUUID() ,"Spain", Math.abs(new Random().nextLong())  ,UUID.randomUUID() ,new DateTime() );
        return concentratorRepository.insert(concentrator);
    }
    public Concentrator createConcentrator(UUID concentratorId,String localtion, long serialNumber, UUID product, DateTime manufactureDate){
        Concentrator concentrator=new Concentrator(concentratorId,localtion, serialNumber  ,product,new DateTime(manufactureDate) );
        return concentratorRepository.insert(concentrator);
    }
}

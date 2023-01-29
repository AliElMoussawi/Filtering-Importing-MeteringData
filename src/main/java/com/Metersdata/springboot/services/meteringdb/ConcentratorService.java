package com.Metersdata.springboot.services.meteringdb;

import com.Metersdata.springboot.dao.ConcentratorRepository;
import com.Metersdata.springboot.model.Concentrator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ConcentratorService {

    @Autowired(required = true)
    ConcentratorRepository concentratorRepository;

    public Concentrator createConcentrator(){
        Concentrator concentrator=new Concentrator(UUID.randomUUID() ,"Spain", Math.abs(new Random().nextLong())  ,UUID.randomUUID() ,new DateTime() );
        return concentratorRepository.insert(concentrator);
    }

}

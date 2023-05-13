package com.controller.mongodb;

import com.dto.mongodb.CustomerInfoDto;
import com.model.CustomerInfo;
import com.repository.CustomerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/customerInfo")
public class CustomerInfoController {
    @Autowired
    CustomerInfoRepository customerInfoRepository;
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<CustomerInfo> createCustomerInfo(@RequestBody CustomerInfoDto customerInfoDTo) throws Exception {
        System.out.println(customerInfoDTo.getUserEmail());
        System.out.println(customerInfoDTo.getCustomerAccountId());
        CustomerInfo newCustomerInfo=new CustomerInfo(UUID.randomUUID(),customerInfoDTo.getUserEmail(),customerInfoDTo.getCustomerAccountId());
        CustomerInfo customerInfo=customerInfoRepository.insert(newCustomerInfo);
        return (customerInfo==null)? null: new ResponseEntity<>(customerInfo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<CustomerInfo> getGustomerInfoByEmail(@RequestBody CustomerInfoDto customerInfoDTo){
        CustomerInfo customerInfo =customerInfoRepository.findByUserEmail(customerInfoDTo.getUserEmail());
        return (customerInfo==null)?  new ResponseEntity<>(null,HttpStatus.NOT_FOUND): new ResponseEntity<>(customerInfo, HttpStatus.OK);
    }

    }

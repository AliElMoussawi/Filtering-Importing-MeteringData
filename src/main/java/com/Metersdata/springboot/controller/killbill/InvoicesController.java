package com.Metersdata.springboot.controller.killbill;

import com.Metersdata.springboot.service.killbill.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/invoice")
@CrossOrigin
public class InvoicesController {
    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<List<UUID>> getSubscriptionsbyAccountId(@RequestBody String accountId) throws Exception {
       List<UUID> subscriptions=accountService.getAccountSubscriptions(UUID.fromString(accountId));
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }
}

package com.Metersdata.springboot.controller.killbill;

import com.Metersdata.springboot.dto.killbill.AccountRequestDto;
import com.Metersdata.springboot.service.killbill.AccountService;
import org.killbill.billing.client.KillBillClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/account")
@CrossOrigin
public class AccountController {
    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<List<UUID>> getSubscriptionsbyAccountId(@RequestBody AccountRequestDto accountRequestDto) throws Exception {
       List<UUID> subscriptions=accountService.getAccountSubscriptions(accountRequestDto.getAccountId());
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/invoices")
    public ResponseEntity<Map<UUID, String>> getAccountInvoices(@RequestBody AccountRequestDto accountRequestDto) throws KillBillClientException {
        Map<UUID, String> invoices = accountService.getAccountInvoices(accountRequestDto.getAccountId());
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }
}

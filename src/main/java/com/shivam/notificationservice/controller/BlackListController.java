package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.responseBody.GenericResponse;
import com.shivam.notificationservice.requestBody.PhoneNumbersRequestBody;
import com.shivam.notificationservice.services.BlackListService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/blacklist")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class BlackListController {
    BlackListService blackListService;
    @GetMapping
    public GenericResponse<List<String>,List<String>,List<String>> getAllBlacklistedNumbers() throws Exception {
        return new GenericResponse<>(blackListService.getAllBlackListedNumber(), null,null);
    }

    @DeleteMapping
    @Transactional
    public GenericResponse<String,String,String> whitelistNumbers(@Valid @RequestBody PhoneNumbersRequestBody phoneNumbersRequestBody) throws Exception {
        return new GenericResponse<>(blackListService.whiteListGiven(phoneNumbersRequestBody.getPhoneNumbers()), null,null);
    }
    @PostMapping
    @Transactional
    public GenericResponse<String,String,String> blacklistNumbers(@Valid @RequestBody PhoneNumbersRequestBody phoneNumbersRequestBody) throws Exception {
        return new GenericResponse<>(blackListService.blackListGiven(phoneNumbersRequestBody.getPhoneNumbers()), null,null);
    }
}

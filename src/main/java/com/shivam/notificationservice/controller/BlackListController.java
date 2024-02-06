package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.RequestBody.PhoneNumbersRequestBody;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.services.BlackListService;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blacklist")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BlackListController {
    BlackListService blackListService;
    @GetMapping
    public GenericResponse<List<String>,List<String>,List<String>> getAllBlacklistedNumbers() throws Exception {
        return new GenericResponse<>(blackListService.getAllBlackListedNumber(), null,null);
    }

    @DeleteMapping
    public GenericResponse<String,String,String> whitelistNumbers(@RequestBody PhoneNumbersRequestBody phoneNumbersRequestBody) throws Exception {
        return new GenericResponse<>(blackListService.whiteListGiven(phoneNumbersRequestBody.getPhoneNumbers()), null,null);
    }
    @PostMapping
    public GenericResponse<String,String,String> blacklistNumbers(@RequestBody PhoneNumbersRequestBody phoneNumbersRequestBody) throws Exception {
        return new GenericResponse<>(blackListService.blackListGiven(phoneNumbersRequestBody.getPhoneNumbers()), null,null);
    }
}

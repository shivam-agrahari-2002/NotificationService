package com.shivam.notificationservice.services;

import com.shivam.notificationservice.Repository.mysql.BlackListRepository;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.mysql.BlackListEntity;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BlackListService {
    BlackListRepository blackListRepository;
    BlacklistCachingService blacklistCachingService;
    public boolean blackListChecker(String phoneNumber) throws Exception{
        if(blacklistCachingService.isPresent(phoneNumber)) return true;
        if(blackListRepository.existsById(phoneNumber)){
            blacklistCachingService.addToSet(phoneNumber);
            return true;
        }
        return false;
    }
    public boolean blackLister(String phoneNumber) throws Exception{
        if(blacklistCachingService.isPresent(phoneNumber)){
            return false;
        }
        if(blackListRepository.existsById(phoneNumber)){
            blackListRepository.save(new BlackListEntity(phoneNumber));
            return true;
        }
        return false;
    }

    public void whiteLister(String phoneNumber) throws Exception{
        blackListRepository.deleteById(phoneNumber);
        if(blacklistCachingService.isPresent(phoneNumber)) blacklistCachingService.removeFromSet(phoneNumber);
    }

    public List<String> getAllBlackListedNumber() throws Exception{
        List<BlackListEntity> numbers = blackListRepository.findAll();
        List<String> allNumbers = new ArrayList<>();
        for(BlackListEntity num: numbers) {
            allNumbers.add(num.getPhoneNumber());
        }
        return allNumbers;
    }

    public String whiteListGiven(List<String> phoneNumbers) throws Exception{
        if(!PhoneNumberValidator.checkAllPhoneNumber(phoneNumbers)){
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST,"check your phone numbers"));
        }
        for(String phoneNumber: phoneNumbers) {
            if(blackListRepository.existsById(phoneNumber)) {
                whiteLister(phoneNumber);
            }
        }
        return "Sucessfully Whitelisted";
    }

    public String blackListGiven(List<String> phoneNumbers) throws Exception{
        if(!PhoneNumberValidator.checkAllPhoneNumber(phoneNumbers)){
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST,"check your phone numbers"));
        }
        for(String phoneNumber: phoneNumbers) {
            blackLister(phoneNumber);
        }
        return "Sucessfully Blacklisted";
    }
}


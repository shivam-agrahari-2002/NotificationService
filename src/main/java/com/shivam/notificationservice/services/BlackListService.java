package com.shivam.notificationservice.services;

import com.shivam.notificationservice.repository.mysql.BlackListRepository;
import com.shivam.notificationservice.responseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.mysql.BlackListEntity;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.exception.RepositoryException;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class
BlackListService {
    BlackListRepository blackListRepository;
    BlacklistCachingService blacklistCachingService;
    public boolean blackListChecker(String phoneNumber) throws Exception{
        if(blacklistCachingService.isPresent(phoneNumber)) return true;
        boolean check = false;
        try{
            check = blackListRepository.existsById(phoneNumber);
        } catch (Exception e) {
            log.debug("Mysql threw error while existsById() function");
            throw new RepositoryException("mySql db threw error");
        }
        if(check){
            blacklistCachingService.addToSet(phoneNumber);
            return true;
        }
        return false;
    }
    public boolean blackLister(String phoneNumber) throws Exception{
        if(blacklistCachingService.isPresent(phoneNumber)){
            return false;
        }
        try {
            if (blackListRepository.existsById(phoneNumber)) {
                blackListRepository.save(new BlackListEntity(phoneNumber));
                return true;
            }
        } catch (Exception e) {
            log.debug("Mysql threw error: SAVE() function");
            throw new RepositoryException("Mysql threw error");
        }
        return false;
    }

    public void whiteLister(String phoneNumber) throws Exception{
        try {
            blackListRepository.deleteById(phoneNumber);
        } catch (Exception e){
            log.debug("Mysql threw error: deleteById() function");
            throw new RepositoryException("Mysql threw error");
        }
        if(blacklistCachingService.isPresent(phoneNumber)) blacklistCachingService.removeFromSet(phoneNumber);
    }

    public List<String> getAllBlackListedNumber() throws Exception{
        try {
            List<BlackListEntity> numbers = blackListRepository.findAll();
            List<String> allNumbers = new ArrayList<>();
            for (BlackListEntity num : numbers) {
                allNumbers.add(num.getPhoneNumber());
            }
            return allNumbers;
        } catch (Exception e){
            log.debug("Mysql threw error: findAll() function");
            throw new RepositoryException("Mysql threw error");
        }
    }

    public String whiteListGiven(List<String> phoneNumbers) throws Exception{
        if(PhoneNumberValidator.checkAllPhoneNumber(phoneNumbers)){
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
        if(PhoneNumberValidator.checkAllPhoneNumber(phoneNumbers)){
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST,"check your phone numbers"));
        }
        for(String phoneNumber: phoneNumbers) {
            blackLister(phoneNumber);
        }
        return "Sucessfully Blacklisted";
    }
}


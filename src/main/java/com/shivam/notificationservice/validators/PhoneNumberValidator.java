package com.shivam.notificationservice.validators;


import java.util.List;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final Pattern INDIAN_PHONE_NUMBER_PATTERN = Pattern.compile("^\\+91\\d{10}$");
    private static final Pattern INDIAN_PHONE_NUMBER_PATTERN2 = Pattern.compile("^\\d{10}$");

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) return true;
        return !INDIAN_PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches() && !INDIAN_PHONE_NUMBER_PATTERN2.matcher(phoneNumber).matches();
    }

    public static boolean checkAllPhoneNumber(List<String> phoneNumbers){
        if(phoneNumbers == null) return true;
        for(String phoneNumber: phoneNumbers){
            if(isValidPhoneNumber(phoneNumber)) return true;
        }
        return false;
    }

}

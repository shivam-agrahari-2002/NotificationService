package com.shivam.notificationservice.validators;

public class PageValidator {
    public static boolean checkPageDetails(int pageNumber, int pageSize){
        return pageNumber < 0 || pageSize < 1 || pageSize > 50;
    }
}

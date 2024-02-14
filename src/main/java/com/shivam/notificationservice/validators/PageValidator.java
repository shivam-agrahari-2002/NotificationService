package com.shivam.notificationservice.validators;

import com.shivam.notificationservice.requestBody.PageDetails;

public class PageValidator {
    public static boolean checkPageDetails(PageDetails pageDetails){
        return pageDetails.getPage() < 0 || pageDetails.getSize() < 1 || pageDetails.getSize() > 50;

    }
}

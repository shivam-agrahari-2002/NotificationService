package com.shivam.notificationservice.validators;

import com.shivam.notificationservice.RequestBody.PageDetails;

public class PageValidator {
    public static boolean checkPageDetails(PageDetails pageDetails){
        return pageDetails == null || pageDetails.getPage() == null || pageDetails.getSize() == null || pageDetails.getPage() < 0 || pageDetails.getSize() < 1 || pageDetails.getSize() > 50;

    }
}

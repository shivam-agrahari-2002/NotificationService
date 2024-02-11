package com.shivam.notificationservice.validators;

import com.shivam.notificationservice.requestBody.ElasticSearchTimeRangeRequestBody;

public class TimeValidator {
    public static boolean checkTimings(ElasticSearchTimeRangeRequestBody elasticSearchTimeRangeRequestBody){
        return elasticSearchTimeRangeRequestBody.getEndTime().isAfter(elasticSearchTimeRangeRequestBody.getStartTime());
    }
}

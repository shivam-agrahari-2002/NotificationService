package com.shivam.notificationservice.exception;

import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseData;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public class BadRequestException extends Exception{
    ResponseError responseError;
    public BadRequestException(ResponseError responseError) {
        this.responseError = responseError;
    }

}

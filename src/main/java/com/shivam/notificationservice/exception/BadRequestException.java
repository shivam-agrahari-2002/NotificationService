package com.shivam.notificationservice.exception;

import com.shivam.notificationservice.responseBody.ResponseError;
import lombok.Getter;

@Getter
public class BadRequestException extends Exception{
    ResponseError responseError;
    public BadRequestException(ResponseError responseError) {
        this.responseError = responseError;
    }

}

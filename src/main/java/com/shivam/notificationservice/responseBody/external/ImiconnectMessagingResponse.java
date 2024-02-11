package com.shivam.notificationservice.responseBody.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ImiconnectMessagingResponse {
    List<Response> response;
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Response{
        String code;
        String transid;
        String description;
        String correlationid;
    }
}



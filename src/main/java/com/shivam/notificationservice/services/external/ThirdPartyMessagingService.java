package com.shivam.notificationservice.services.external;
import com.shivam.notificationservice.ResponseBody.external.ImiconnectMessagingResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdPartyMessagingService {

    private final RestTemplate restTemplate;

    public ResponseEntity<ImiconnectMessagingResponse> sendMessage(String key, String msisdn, String correlationId, String url, String message) {
        // Set up the request body
        String requestBody = "{\n" +
                "  \"deliverychannel\": \"sms\",\n" +
                "  \"channels\": {\n" +
                "    \"sms\": {\n" +
                "      \"text\": \""  + message + "\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"destination\": [\n" +
                "    {\n" +
                "      \"msisdn\": [\n" +
                "         \"" + msisdn + "\"\n" +
                "      ],\n" +
                "      \"correlationId\": \"" + correlationId + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Key", key);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ImiconnectMessagingResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ImiconnectMessagingResponse.class);
        return responseEntity;
    }
}


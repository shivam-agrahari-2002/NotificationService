package com.shivam.notificationservice.requestBody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawMessageRequestBody {
    @NotNull(message = "phoneNumber is Mandatory")
    String phoneNumber;
    @NotNull(message = "message is Mandatory")
    String message;
}

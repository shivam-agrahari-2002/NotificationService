package com.shivam.notificationservice.requestBody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawMessageRequestBody {
    @NotNull(message = "phoneNumber is Mandatory")
    String phoneNumber;
    String message;
}

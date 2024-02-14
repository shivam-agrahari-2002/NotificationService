package com.shivam.notificationservice.requestBody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneNumbersRequestBody {
    @NotNull(message = "Please provide a list of phone numbers")
    @NotEmpty(message = "Please provide a list of phone numbers")
    List<String> phoneNumbers;
}

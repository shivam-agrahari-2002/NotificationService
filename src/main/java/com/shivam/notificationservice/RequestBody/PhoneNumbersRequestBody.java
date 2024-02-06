package com.shivam.notificationservice.RequestBody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //TODO read about this anotations
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneNumbersRequestBody {
    @NotNull(message = "Please provide a list of phone numbers")
    @NotEmpty(message = "Please provide a list of phone numbers")
    List<String> phoneNumbers;
}

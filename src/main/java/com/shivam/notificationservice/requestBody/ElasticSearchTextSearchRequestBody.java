package com.shivam.notificationservice.requestBody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElasticSearchTextSearchRequestBody {
    @NotNull(message = "Message is mandatory")
    @NotEmpty(message = "Message should not be empty")
    String text;
    @NotNull(message = "pageDetails is mandatory")
    PageDetails pageDetails;
}

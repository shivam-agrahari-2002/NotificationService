package com.shivam.notificationservice.requestBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Force Lombok to generate a no-args constructor including validation annotations
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElasticSearchTimeRangeRequestBody {
    @NotNull(message = "phoneNumber must not be blank")
    private String phoneNumber;

    @NotNull(message = "startTime must not be null")
    private LocalDateTime startTime;

    @NotNull(message = "endTime must not be null")
    private LocalDateTime endTime;

    @NotNull(message = "pageDetails must not be null")
    private PageDetails pageDetails;
}


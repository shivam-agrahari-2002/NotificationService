package com.shivam.notificationservice.RequestBody;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchTimeRangeRequestBody {
    @NotNull(message = "phoneNumber input required")
    @NotEmpty(message = "phoneNumber input required")
    String phoneNumber;
    @NotNull(message = "startTime Required")
    LocalDateTime startTime;
    @NotNull(message = "endTime Required")
    LocalDateTime endTime;
    PageDetails pageDetails;
}

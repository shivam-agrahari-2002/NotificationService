package com.shivam.notificationservice.RequestBody;

import com.shivam.notificationservice.exception.BadPageRequestException;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class PageDetails {

    @NotNull(message = "input page required")
    @Min(value = 0, message = "page-number should be non-negative")
    private Integer page;

    @NotNull(message = "input page size required")
    @Min(value = 1, message = "page-size should be non-negative")
    private Integer size;
}

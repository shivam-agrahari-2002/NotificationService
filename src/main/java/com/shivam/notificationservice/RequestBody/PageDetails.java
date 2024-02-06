package com.shivam.notificationservice.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnegative;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDetails {

    @NotNull(message = "input page required")
    @NotEmpty(message = "input page required")
    @Min(value = 0,message = "page-number should be non-negative")
    int page;

    @NotNull(message = "input page size required")
    @NotEmpty(message = "input page size required")
    @Min(value = 0,message = "page-size should be non-negative")
    int size;
}

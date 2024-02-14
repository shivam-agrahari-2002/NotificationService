package com.shivam.notificationservice.requestBody;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDetails {
    @NotNull(message = "page index is mandatory")
    private Integer page;
    @NotNull(message = "page size is mandatory")
    private Integer size;
}

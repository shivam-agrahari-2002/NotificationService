package com.shivam.notificationservice.requestBody;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDetails {
    private Integer page;
    private Integer size;
}

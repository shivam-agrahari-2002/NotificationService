package com.shivam.notificationservice.RequestBody;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDetails {
    private Integer page;
    private Integer size;
}

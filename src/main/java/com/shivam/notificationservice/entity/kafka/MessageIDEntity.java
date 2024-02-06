package com.shivam.notificationservice.entity.kafka;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageIDEntity {
    Long Id;
}

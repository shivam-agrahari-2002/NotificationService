package com.shivam.notificationservice.ResponseBody;

import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    Long requestId;
    String comments;
}

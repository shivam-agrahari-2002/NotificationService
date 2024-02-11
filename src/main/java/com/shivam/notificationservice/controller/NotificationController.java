package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.responseBody.GenericResponse;
import com.shivam.notificationservice.responseBody.ResponseData;
import com.shivam.notificationservice.requestBody.RawMessageRequestBody;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.services.MessageBuildingAndQueuingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/sms")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
    MessageBuildingAndQueuingService messageBuildingAndQueuingService;
    @PostMapping("/send")
    public GenericResponse<ResponseData, Object, Object> sendMessage(@RequestBody RawMessageRequestBody rawMessageRequestBody) throws Exception {
        return messageBuildingAndQueuingService.sendToDB(rawMessageRequestBody);
    }

    @GetMapping("/{requestId}")
    public GenericResponse<ProcessedMessageEntity, Object, Object> getInfoByMessageId(@PathVariable Long requestId) throws Exception  {
        return messageBuildingAndQueuingService.getDetails(requestId);
    }
}

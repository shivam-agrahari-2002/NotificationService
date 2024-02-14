package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.responseBody.GenericResponse;
import com.shivam.notificationservice.responseBody.ResponseData;
import com.shivam.notificationservice.requestBody.RawMessageRequestBody;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.services.MessageBuildingAndQueuingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@ResponseBody
@RequestMapping("/sms")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class NotificationController {
    MessageBuildingAndQueuingService messageBuildingAndQueuingService;
    @PostMapping("/send")
    @Transactional
    public GenericResponse<ResponseData, Object, Object> sendMessage(@Valid @RequestBody RawMessageRequestBody rawMessageRequestBody) throws Exception {
        return messageBuildingAndQueuingService.sendToDB(rawMessageRequestBody);
    }

    @GetMapping("/{requestId}")
    public GenericResponse<ProcessedMessageEntity, Object, Object> getInfoByMessageId(@PathVariable @Min(value = 0, message = "provide appropriate message id") Long requestId) throws Exception  {
        return messageBuildingAndQueuingService.getDetails(requestId);
    }
}

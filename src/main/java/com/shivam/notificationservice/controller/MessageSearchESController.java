package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.requestBody.ElasticSearchTextSearchRequestBody;
import com.shivam.notificationservice.requestBody.ElasticSearchTimeRangeRequestBody;
import com.shivam.notificationservice.requestBody.PageDetails;
import com.shivam.notificationservice.responseBody.GenericResponse;
import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import com.shivam.notificationservice.services.LogsAndTextSearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageSearchESController {
    public LogsAndTextSearchService logsAndTextSearchService;
    @GetMapping("/find-by-text")
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findByText(@Valid  @RequestBody ElasticSearchTextSearchRequestBody requestBody)  throws Exception{
        return logsAndTextSearchService.findByMessage(requestBody);
    }
    @GetMapping("/find-in-time")
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findInTimeRange(@Valid @RequestBody ElasticSearchTimeRangeRequestBody requestBody)  throws Exception {
        return logsAndTextSearchService.findByPhoneNumberAndTimeRange(requestBody);
    }
    @GetMapping
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findAll(@RequestBody PageDetails pageDetails)  throws Exception{
        return logsAndTextSearchService.findAll(pageDetails);
    }
}

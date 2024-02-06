package com.shivam.notificationservice.controller;

import com.shivam.notificationservice.RequestBody.ElasticSearchTextSearchRequestBody;
import com.shivam.notificationservice.RequestBody.ElasticSearchTimeRangeRequestBody;
import com.shivam.notificationservice.RequestBody.PageDetails;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import com.shivam.notificationservice.services.LogsAndTextSearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elk")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageSearchESController {
    public LogsAndTextSearchService logsAndTextSearchService;
    @GetMapping("/find-by-text")
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findByText(@RequestBody ElasticSearchTextSearchRequestBody requestBody)  throws Exception{
        return logsAndTextSearchService.findByMessage(requestBody);
    }
    @GetMapping("/find-in-time")
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findInTimeRange(@RequestBody ElasticSearchTimeRangeRequestBody requestBody)  throws Exception {
        return logsAndTextSearchService.findByPhoneNumberAndTimeRange(requestBody);
    }
    @GetMapping
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findAll(@RequestBody PageDetails pageDetails)  throws Exception{
        return logsAndTextSearchService.findAll(pageDetails);
    }
}

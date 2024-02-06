package com.shivam.notificationservice.entity.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.sql.Timestamp;


@Document(indexName = "messages")
//@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageESEntity {
    @Id
    Long id;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    String phoneNumber;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    String message;
    String status;
    String failureCode;
    String failureComment;
    Long createdAt;
    @Field(type = FieldType.Long)
    Long updatedAt;
}

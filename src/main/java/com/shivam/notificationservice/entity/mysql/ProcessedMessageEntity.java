package com.shivam.notificationservice.entity.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_requests")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessedMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "message")
    String message;
    @Column(name = "status")
    String status;
    @Column(name = "failure_code")
    String failureCode;
    @Column(name = "failure_comment")
    String failureComment;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    public ProcessedMessageEntity(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        createdAt = LocalDateTime.now();
    }
}

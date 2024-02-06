package com.shivam.notificationservice.entity.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blacklist")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlackListEntity {
    @Id
    String phoneNumber;
}

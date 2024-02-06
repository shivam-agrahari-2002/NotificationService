package com.shivam.notificationservice.Repository.mysql;

import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends CrudRepository<ProcessedMessageEntity,Long> {

}

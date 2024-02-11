package com.shivam.notificationservice.repository.mysql;

import com.shivam.notificationservice.entity.mysql.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackListEntity,String> {
}

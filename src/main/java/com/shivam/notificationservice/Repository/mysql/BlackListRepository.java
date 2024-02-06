package com.shivam.notificationservice.Repository.mysql;

import com.shivam.notificationservice.entity.mysql.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackListEntity,String> {
}

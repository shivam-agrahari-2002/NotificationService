package com.shivam.notificationservice.repository.elasticsearch;

import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageESRepository extends ElasticsearchRepository<MessageESEntity, Long> {

    Page<MessageESEntity> findByMessageContaining(String searchText, Pageable pageable);

    Page<MessageESEntity> findByPhoneNumberAndCreatedAtBetween(String phoneNumber , Long startTime, Long endTime, Pageable pageable);
}

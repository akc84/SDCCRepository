package com.zp.sdcc.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zp.sdcc.entities.AuditHistory;

@Repository
public interface AuditHistoryRepository extends MongoRepository<AuditHistory,String> {

	//AuditHistory findByUsername(String username);

}
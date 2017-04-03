package com.zp.sdcc.services;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zp.sdcc.dao.AuditHistoryRepository;
import com.zp.sdcc.entities.AuditHistory;

@Service
public class AuditHistoryServiceImpl implements AuditHistoryService{	

	private static final Logger logger = LoggerFactory.getLogger(AuditHistoryServiceImpl.class);

	AuditHistoryRepository auditRepository;		

	public AuditHistoryServiceImpl(AuditHistoryRepository auditRepository) {
		super();
		this.auditRepository = auditRepository;
	}	

	@Override
	public AuditHistory addAuditEntry(String username, String queryString){
		logger.debug("Adding Audit for user "+username+" ::"+queryString);
		AuditHistory queryHistoryForUser = getAuditHistoryForUser(username);		
		queryHistoryForUser.addNewAuditEntry(queryString);		
		saveQueryHistory(queryHistoryForUser);
		return queryHistoryForUser;
	}

	@Override
	public AuditHistory getAuditHistoryForUser(String username) {		
		Optional<AuditHistory> queryHistory = Optional.ofNullable(auditRepository.findOne(username));		
		return queryHistory.orElse(createNewQueryHistoryForUser(username));
	}
	
	private AuditHistory createNewQueryHistoryForUser(String username) {
		return new AuditHistory(username);
	}

	private void saveQueryHistory(AuditHistory queryHistoryForUser) {
		auditRepository.save(queryHistoryForUser);
	}
	

}

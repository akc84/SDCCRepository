package com.zp.sdcc.services;

import com.zp.sdcc.entities.AuditHistory;

public interface AuditHistoryService {

	public AuditHistory addAuditEntry(String username, String auditEntry);

	public AuditHistory getAuditHistoryForUser(String username);

}

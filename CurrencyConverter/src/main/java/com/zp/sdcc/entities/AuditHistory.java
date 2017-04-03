package com.zp.sdcc.entities;

import static com.zp.sdcc.common.CurrencyConverterConstants.AUDIT_ENTRIES_MAX_SIZE;

import java.util.Date;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;

public class AuditHistory {

	@Id
	private String username;

	private LinkedList<AuditEntry> auditEntries;

	public AuditHistory(String username) {
		this.username = username;
		this.auditEntries = new LinkedList<>();
	}
	
	public AuditHistory() {
		super();
	}	

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public LinkedList<AuditEntry> getAuditEntries() {
		return auditEntries;
	}

	public void setAuditEntries(LinkedList<AuditEntry> auditEntries) {
		this.auditEntries = auditEntries;
	}
	
	public void addNewAuditEntry(String queryString){
		AuditEntry newEntry = createNewAuditEntry(queryString);
		if(this.auditEntries.size() == AUDIT_ENTRIES_MAX_SIZE)
			this.auditEntries.removeLast();
	    
		this.auditEntries.addFirst(newEntry);		
	}
	
	private AuditEntry createNewAuditEntry(String queryString) {
		return new AuditEntry(queryString, new Date());
	}	
	
	public String getFormattedString()
	{		
		StringBuilder formatted = new StringBuilder();
		getAuditEntries().stream().forEach((p)->  formatted.append(p.getQueryString())
											  			   //.append("   Query Date:::")
											  			   //.append(getFormattedDateTime(p.getQueryDate()))
											  		       .append("\n"));
		return formatted.toString();
		
	}
}

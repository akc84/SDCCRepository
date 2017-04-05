package com.zp.sdcc.entities;

import java.util.Date;
/**
 * @author AKC
 * Contains audit information of a currency conversion request and result
 * Also records the date when the query was made
 */
public class AuditEntry {

	private String queryString;

	private Date queryDate;

	public AuditEntry() {
		super();
	}
	
	public AuditEntry(String queryString, Date queryDate) {
		super();
		this.queryString = queryString;
		this.queryDate = queryDate;
	}	
	
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public Date getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}
	
}

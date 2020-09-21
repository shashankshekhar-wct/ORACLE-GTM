package com.generatecsvfiles.dto;
/*
 * @author Shashank S
 */
 
public class OracleGTMDeniedPartiesLink {
	
	private String gtmParentDPLID;
	private String gtmChildDPLID;
	private String domainName;
	private String insertUser;
	private String insertDate;
	private String updateUser;
	private String updateDate;
	
	
	public String getGtmParentDPLID() {
		return gtmParentDPLID;
	}
	public void setGtmParentDPLID(String gtmParentDPLID) {
		this.gtmParentDPLID = gtmParentDPLID;
	}
	public String getGtmChildDPLID() {
		return gtmChildDPLID;
	}
	public void setGtmChildDPLID(String gtmChildDPLID) {
		this.gtmChildDPLID = gtmChildDPLID;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getInsertUser() {
		return insertUser;
	}
	public void setInsertUser(String insertUser) {
		this.insertUser = insertUser;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	@Override
	public String toString() {
		return "OracleGTMDeniedPartiesLink [gtmParentDPLID=" + gtmParentDPLID + ", gtmChildDPLID=" + gtmChildDPLID
				+ ", domainName=" + domainName + ", insertUser=" + insertUser + ", insertDate=" + insertDate
				+ ", updateUser=" + updateUser + ", updateDate=" + updateDate + "]";
	}
}

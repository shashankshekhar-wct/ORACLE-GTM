package com.generatecsvfiles.service.OracleGTMDeniedParty;

/**
 * @author Shashank S.
 */

import java.util.List;

public class OracleGTMExportCriteria {

	List<String> selectedDplTypes;
	private List<String> selectedSpecificationNames;
	private String FTPFileLocation;
	private String SFTPFileLocation;
	private String userSelectedSpecificationFileName;

	public String getUserSelectedSpecificationFileName() {
		return userSelectedSpecificationFileName;
	}

	public void setUserSelectedSpecificationFileName(String userSelectedSpecificationFileName) {
		this.userSelectedSpecificationFileName = userSelectedSpecificationFileName;
	}

	public List<String> getSelectedDplTypes() {
		return selectedDplTypes;
	}

	public void setSelectedDplTypes(List<String> selectedDplTypes) {
		this.selectedDplTypes = selectedDplTypes;
	}

	public List<String> getSelectedSpecificationNames() {
		return selectedSpecificationNames;
	}

	public void setSelectedSpecificationNames(List<String> selectedSpecificationNames) {
		this.selectedSpecificationNames = selectedSpecificationNames;
	}

	public String getFTPFileLocation() {
		return FTPFileLocation;
	}

	public void setFTPFileLocation(String fTPFileLocation) {
		FTPFileLocation = fTPFileLocation;
	}

	public String getSFTPFileLocation() {
		return SFTPFileLocation;
	}

	public void setSFTPFileLocation(String sFTPFileLocation) {
		SFTPFileLocation = sFTPFileLocation;
	}

}

package com.generatecsvfiles.dto;

/**
 * @author Shashank S.
 */
public class OracleGTMDeniedParties {

	private String dplId;
	private String firstName;
	private String lastName;
	private String companyName;
	private String vessleName;
	private String addressLine;
	private String city;
	private String province;
	private String postalCode;
	private String dateOFBirth;
	private String placeOFBirth;
	private String passportNumber;
	private String passportIssueCountry;
	private String passpostIssueDate;
	private String altPassportNumber;

	private String altpassportIssueCountry;
	private String altpasspostIssueDate;
	private String drivierLicenseNumber;
	private String drivierLicenseAuthority;
	private String citizenShip;
	private String nationality;
	private String ssn;
	private String nit;
	private String cedula;
	private String deniedCode;
	private String entryDate;
	private Integer entryId;
	private String agencyCode;
	private String rulingVolume;
	private String rulingPageNumber;
	private String federalRegDate;
	private String notes;
	private String gtmDateVersionId;
	private String isInUse;
	private String screeningStatus;
	private String domainName;
	private String dplURL;
	private String dplEntityType;
	private String country1IsoCode;
	private String country2IsoCode;
	private String countryName;

	private String dplStatus;
	private String dplModifiedOn;// update_date
	private String dplName;
	private String dplAddress1;
	private String dplAddress2;

	private String dplCountryOfOrigin;
	private String dplEffectiveDate;
	private String dplExpiryDate;
	private String dplFrcDate;
	private String dplFrc;
	private String dplAgencies;
	private String dplCategory;
	private String dplType;
	private String dplSeeAlso;
	private String dplPrivileges;

	private String dplAddress3;
	private String dplState;
	private String createdOn;
	private String createdBy;
	private String modifiedBy;
	private String dplZip;
	private String dplAddLInfo;

	private String dplImage;
	private String approvedDate;

	private String dunsCode;
	private String cageCode;

	@Override
	public String toString() {
		return "DeniedParties [dplId=" + dplId + ", dplStatus=" + dplStatus + ", dplModifiedOn=" + dplModifiedOn
				+ ", dplName=" + dplName + ", dplAddress1=" + dplAddress1 + ", dplAddress2=" + dplAddress2
				+ ", country1IsoCode=" + country1IsoCode + ", country2IsoCode=" + country2IsoCode + ", countryName="
				+ countryName + ", dplCountryOfOrigin=" + dplCountryOfOrigin + ", dplEffectiveDate=" + dplEffectiveDate
				+ ", dplExpiryDate=" + dplExpiryDate + ", dplFrcDate=" + dplFrcDate + ", dplFrc=" + dplFrc
				+ ", dplAgencies=" + dplAgencies + ", dplCategory=" + dplCategory + ", dplType=" + dplType
				+ ", dplSeeAlso=" + dplSeeAlso + ", dplPrivileges=" + dplPrivileges + ", dplAddress3=" + dplAddress3
				+ ", dplState=" + dplState + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", dplZip=" + dplZip + ", dplAddLInfo=" + dplAddLInfo + ", dplURL=" + dplURL
				+ ", dplEntityType=" + dplEntityType + ", dplImage=" + dplImage + ", approvedDate=" + approvedDate
				+ ", dunsCode=" + dunsCode + ", cageCode=" + cageCode + "]";
	}

	public String getDplId() {
		return dplId;
	}

	public void setDplId(String dplId) {
		this.dplId = dplId;
	}

	public String getDplStatus() {
		return dplStatus;
	}

	public void setDplStatus(String dplStatus) {
		this.dplStatus = dplStatus;
	}

	public String getDplModifiedOn() {
		return dplModifiedOn;
	}

	public void setDplModifiedOn(String dplModifiedOn) {
		this.dplModifiedOn = dplModifiedOn;
	}

	public String getDplName() {
		return dplName;
	}

	public void setDplName(String dplName) {
		this.dplName = dplName;
	}

	public String getDplAddress1() {
		return dplAddress1;
	}

	public void setDplAddress1(String dplAddress1) {
		this.dplAddress1 = dplAddress1;
	}

	public String getDplAddress2() {
		return dplAddress2;
	}

	public void setDplAddress2(String dplAddress2) {
		this.dplAddress2 = dplAddress2;
	}

	public String getCountry1IsoCode() {
		return country1IsoCode;
	}

	public void setCountry1IsoCode(String country1IsoCode) {
		this.country1IsoCode = country1IsoCode;
	}

	public String getCountry2IsoCode() {
		return country2IsoCode;
	}

	public void setCountry2IsoCode(String country2IsoCode) {
		this.country2IsoCode = country2IsoCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getDplCountryOfOrigin() {
		return dplCountryOfOrigin;
	}

	public void setDplCountryOfOrigin(String dplCountryOfOrigin) {
		this.dplCountryOfOrigin = dplCountryOfOrigin;
	}

	public String getDplEffectiveDate() {
		return dplEffectiveDate;
	}

	public void setDplEffectiveDate(String dplEffectiveDate) {
		this.dplEffectiveDate = dplEffectiveDate;
	}

	public String getDplExpiryDate() {
		return dplExpiryDate;
	}

	public void setDplExpiryDate(String dplExpiryDate) {
		this.dplExpiryDate = dplExpiryDate;
	}

	public String getDplFrcDate() {
		return dplFrcDate;
	}

	public void setDplFrcDate(String dplFrcDate) {
		this.dplFrcDate = dplFrcDate;
	}

	public String getDplFrc() {
		return dplFrc;
	}

	public void setDplFrc(String dplFrc) {
		this.dplFrc = dplFrc;
	}

	public String getDplAgencies() {
		return dplAgencies;
	}

	public void setDplAgencies(String dplAgencies) {
		this.dplAgencies = dplAgencies;
	}

	public String getDplCategory() {
		return dplCategory;
	}

	public void setDplCategory(String dplCategory) {
		this.dplCategory = dplCategory;
	}

	public String getDplType() {
		return dplType;
	}

	public void setDplType(String dplType) {
		this.dplType = dplType;
	}

	public String getDplSeeAlso() {
		return dplSeeAlso;
	}

	public void setDplSeeAlso(String dplSeeAlso) {
		this.dplSeeAlso = dplSeeAlso;
	}

	public String getDplPrivileges() {
		return dplPrivileges;
	}

	public void setDplPrivileges(String dplPrivileges) {
		this.dplPrivileges = dplPrivileges;
	}

	public String getDplAddress3() {
		return dplAddress3;
	}

	public void setDplAddress3(String dplAddress3) {
		this.dplAddress3 = dplAddress3;
	}

	public String getDplState() {
		return dplState;
	}

	public void setDplState(String dplState) {
		this.dplState = dplState;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDplZip() {
		return dplZip;
	}

	public void setDplZip(String dplZip) {
		this.dplZip = dplZip;
	}

	public String getDplAddLInfo() {
		return dplAddLInfo;
	}

	public void setDplAddLInfo(String dplAddLInfo) {
		this.dplAddLInfo = dplAddLInfo;
	}

	public String getDplURL() {
		return dplURL;
	}

	public void setDplURL(String dplURL) {
		this.dplURL = dplURL;
	}

	public String getDplEntityType() {
		return dplEntityType;
	}

	public void setDplEntityType(String dplEntityType) {
		this.dplEntityType = dplEntityType;
	}

	public String getDplImage() {
		return dplImage;
	}

	public void setDplImage(String dplImage) {
		this.dplImage = dplImage;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getDunsCode() {
		return dunsCode;
	}

	public String getCageCode() {
		return cageCode;
	}

	public void setDunsCode(String dunsCode) {
		this.dunsCode = dunsCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getVessleName() {
		return vessleName;
	}

	public void setVessleName(String vessleName) {
		this.vessleName = vessleName;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getDateOFBirth() {
		return dateOFBirth;
	}

	public void setDateOFBirth(String dateOFBirth) {
		this.dateOFBirth = dateOFBirth;
	}

	public String getPlaceOFBirth() {
		return placeOFBirth;
	}

	public void setPlaceOFBirth(String placeOFBirth) {
		this.placeOFBirth = placeOFBirth;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportIssueCountry() {
		return passportIssueCountry;
	}

	public void setPassportIssueCountry(String passportIssueCountry) {
		this.passportIssueCountry = passportIssueCountry;
	}

	public String getPasspostIssueDate() {
		return passpostIssueDate;
	}

	public void setPasspostIssueDate(String passpostIssueDate) {
		this.passpostIssueDate = passpostIssueDate;
	}

	public String getAltPassportNumber() {
		return altPassportNumber;
	}

	public void setAltPassportNumber(String altPassportNumber) {
		this.altPassportNumber = altPassportNumber;
	}

	public String getAltpassportIssueCountry() {
		return altpassportIssueCountry;
	}

	public void setAltpassportIssueCountry(String altpassportIssueCountry) {
		this.altpassportIssueCountry = altpassportIssueCountry;
	}

	public String getAltpasspostIssueDate() {
		return altpasspostIssueDate;
	}

	public void setAltpasspostIssueDate(String altpasspostIssueDate) {
		this.altpasspostIssueDate = altpasspostIssueDate;
	}

	public String getDrivierLicenseNumber() {
		return drivierLicenseNumber;
	}

	public void setDrivierLicenseNumber(String drivierLicenseNumber) {
		this.drivierLicenseNumber = drivierLicenseNumber;
	}

	public String getDrivierLicenseAuthority() {
		return drivierLicenseAuthority;
	}

	public void setDrivierLicenseAuthority(String drivierLicenseAuthority) {
		this.drivierLicenseAuthority = drivierLicenseAuthority;
	}

	public String getCitizenShip() {
		return citizenShip;
	}

	public void setCitizenShip(String citizenShip) {
		this.citizenShip = citizenShip;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getDeniedCode() {
		return deniedCode;
	}

	public void setDeniedCode(String deniedCode) {
		this.deniedCode = deniedCode;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public String getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getRulingVolume() {
		return rulingVolume;
	}

	public void setRulingVolume(String rulingVolume) {
		this.rulingVolume = rulingVolume;
	}

	public String getRulingPageNumber() {
		return rulingPageNumber;
	}

	public void setRulingPageNumber(String rulingPageNumber) {
		this.rulingPageNumber = rulingPageNumber;
	}

	public String getFederalRegDate() {
		return federalRegDate;
	}

	public void setFederalRegDate(String federalRegDate) {
		this.federalRegDate = federalRegDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getGtmDateVersionId() {
		return gtmDateVersionId;
	}

	public void setGtmDateVersionId(String gtmDateVersionId) {
		this.gtmDateVersionId = gtmDateVersionId;
	}

	public String getIsInUse() {
		return isInUse;
	}

	public void setIsInUse(String isInUse) {
		this.isInUse = isInUse;
	}

	public String getScreeningStatus() {
		return screeningStatus;
	}

	public void setScreeningStatus(String screeningStatus) {
		this.screeningStatus = screeningStatus;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}

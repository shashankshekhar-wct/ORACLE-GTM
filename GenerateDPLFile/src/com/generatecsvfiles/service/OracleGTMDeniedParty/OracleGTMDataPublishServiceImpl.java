package com.generatecsvfiles.service.OracleGTMDeniedParty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.generatecsvfiles.dto.OracleGTMDeniedParties;
import com.generatecsvfiles.service.OracleGTMDeniedParty.Dao.DataPublishDao;
import com.generatecsvfiles.service.OracleGTMDeniedParty.Dao.DataPublishDaoImpl;
import com.generatecsvfiles.utility.CSVUtils;
import com.generatecsvfiles.utility.CompressFileUtil;
import com.generatecsvfiles.utility.Constants;
import com.generatecsvfiles.utility.PropertyLoader;
import com.generatecsvfiles.utility.WlcsUtil;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.lang3.StringUtils;




/**
 * @author Shashank S.
 */
public class OracleGTMDataPublishServiceImpl implements OracleGTMDataPublishService {



	//@Value("${oraclegtm.xml.ftp.location}")
	private String oracleeFtpLocation;

	//@Value("${pkzipc.location}")
	private String batchFilePath;

	//@Value("${oraclegtm.csv.export.path}")
	private String oracleGTMExportLocation=null;

	
	public static final String FILE_FORMAT_ACCCESS = ".mdb";
	public static final String ACCCESS_TABLE = "DENIED_PARTIES";
	public static final String FILE_FORMAT_XML = ".xml";
	public static final String FILE_FORMAT_ZIP = ".zip";
	public static final String FILE_FORMAT_UNIX_COMPRESSED = ".z";
	public static final String FIXED_WIDTH = "Y";
	public static final String TAB_SEPERATED = "T";
	public static final String CHPOWELL_MIDPRO_FILE = "chpowell_medpro";

	public static final String SAP_ZIP_DIRECTORY = "\\ZIP\\";
	
	private Integer recordsToFetch=1000;

	private String configPath;
	FTPService ftpService=new FTPService();

	// oracle GTM Changes
	//private InputStream fileInputStream;
	private String fileName;

	private static final String commaString = ",";
	private static final String blankString = "";
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
	String dateDirectory = formatter.format(date);
	
	DataPublishDao dataPublishDao=new DataPublishDaoImpl();
	@Override
	public void generateOracleGTMData(OracleGTMExportCriteria oracleGTMExportCriteria) {
		oracleGTMExportLocation=PropertyLoader.getKeyValue("oraclegtm.csv.export.path");
		File oracleExportLocationDirectory = new File(oracleGTMExportLocation
				+ oracleGTMExportCriteria.getUserSelectedSpecificationFileName() + File.separator + dateDirectory);
		//LOG.debug("Inside Process of generation Oracle GTM CSV data");
		//List<Message> status = new ArrayList<Message>();
		//Message message = new Message();
		List<String> messages = new ArrayList<String>();
		try {

			Date currentDataPusblisDate = dataPublishDao.getDateByDateState(Constants.LAST_PUBLISH_DATE_STATE);
			Date lastDataPusblisDate = dataPublishDao.getDateByDateState(Constants.PREVIOUS_PUBLISH_DATE_STATE);

			// get the dpl types selected from UI. xmlExportCriteria contains
			// all the selected drop down values, ftp location on jsp page
			List<String> dplTypes = oracleGTMExportCriteria.getSelectedDplTypes();

			File parentOracleFile = new File(oracleGTMExportLocation);
			if (!parentOracleFile.exists()) {
				parentOracleFile.mkdir();
			}
			if (oracleGTMExportCriteria.getUserSelectedSpecificationFileName() != null
					&& StringUtils.isNotBlank(oracleGTMExportCriteria.getUserSelectedSpecificationFileName())) {

				if (oracleExportLocationDirectory.exists()) {
					WlcsUtil.cleanDirectory(
							oracleGTMExportLocation + oracleGTMExportCriteria.getUserSelectedSpecificationFileName()
									+ File.separator + dateDirectory);
				}
				else {
					File oracleExportLocationParentDirectory = new File(
							oracleGTMExportLocation + oracleGTMExportCriteria.getUserSelectedSpecificationFileName());
					if (!oracleExportLocationParentDirectory.exists()) {
						oracleExportLocationParentDirectory.mkdir();
					}
					oracleExportLocationDirectory.mkdir();
				}
			}
			else {

				File easeExportLocationDirectory = new File(
						oracleGTMExportLocation + Constants.CUSTOM_FILENAME + File.separator + dateDirectory);
				if (easeExportLocationDirectory.exists()) {
					WlcsUtil.cleanDirectory(
							oracleGTMExportLocation + Constants.CUSTOM_FILENAME + File.separator + dateDirectory);
				}
				else {
					easeExportLocationDirectory.mkdirs();
				}

			}

			//messages.add("Existing directory cleaned successfully");

			// call generate csv files here String inputFileName =""; //
			String sourceFiles[] = new String[6];
			String inputFileName = "";
			// File1
			inputFileName = generateGTMContentTypeFile(oracleGTMExportCriteria);
			System.out.println("GTM_CONTENT_TYPE.CSV Created successfully for specification " + oracleGTMExportCriteria.getUserSelectedSpecificationFileName());
			sourceFiles[0] = inputFileName; // File2
			inputFileName = generateGTMContentSourceFile(oracleGTMExportCriteria);
			System.out.println("GTM_CONTENT_SOURCE.CSV Created successfully for specification "+ oracleGTMExportCriteria.getUserSelectedSpecificationFileName());
			sourceFiles[1] = inputFileName; // File3
			inputFileName = generateDataVersionFile(oracleGTMExportCriteria);
			sourceFiles[2] = inputFileName; // File4
			System.out.println("DATA_VERSION.CSV Created successfully for specification "+ oracleGTMExportCriteria.getUserSelectedSpecificationFileName());
			inputFileName = generateOracleGTMDeniedPartyCSV(Constants.ENTIRE, dplTypes, lastDataPusblisDate,
					currentDataPusblisDate, oracleGTMExportCriteria);
			sourceFiles[3] = inputFileName;
			System.out.println("GTM_DPL_DATA.CSV Created successfully for specification "+ oracleGTMExportCriteria.getUserSelectedSpecificationFileName());

			//temp commenting code for inceremental data(record between specific date)
			/*
			 * boolean isIncDataGenerated =
			 * this.generateEaseXML(Constants.INCREMENTAL, dplTypes,
			 * lastDataPusblisDate, currentDataPusblisDate, xmlExportCriteria);
			 * System.out.println("Ease incremetal data generated successfully");
			 * messages.add("Ease Incremental data generation status : " +
			 * isIncDataGenerated); boolean dtControlFileGenerated =
			 * this.generateDateControlFile(lastDataPusblisDate,
			 * currentDataPusblisDate, xmlExportCriteria);
			 * System.out.println("Ease date control file generated successfully");
			 * messages.add("Ease date control data generation status : " +
			 * dtControlFileGenerated);
			 */
			/*
			 * String zipFileName = oracleGTMExportLocation +
			 * xmlExportCriteria.getUserSelectedSpecificationFileName() +
			 * File.separator + fileName + ".zip"; downloadZipFile(sourceFiles,
			 * zipFileName); this.fileName = "ECCNDetailsCsvFiles" + ".zip";
			 */
			String zipName = oracleExportLocationDirectory + File.separator + Constants.ZIP_NAME;
			CompressFileUtil.zipDirectory(oracleExportLocationDirectory, zipName);
			//messages.add("OracleGTM data compression status : " + isCompressed);

			//Boolean isFileUploaded = ftpOracleGTMData(xmlExportCriteria);

			/*
			 * if (isFileUploaded) {
			 * System.out.println("oracleGTM Data Files transfered to ftp server successfully");
			 * messages.add("Oracle data transfer status : " + isCompressed); }
			 */

			//message.setFileName("");
			//message.setMessages(messages);
			//message.setLocation(oracleGTMExportLocation);
			//message.setStatus("SUCCESS");
		}
		catch (Exception e) {

			System.out.println("exception in ease data generation process ");
			//messages.add("Ease data generation failed : " + e.getMessage());
			//message.setStatus("ERROR");
			//message.setMessages(messages);
		}

		// status.add(message);
		// this.emailGenerationStatus(status);
	}

	private void deleteFileIfExists(String fileName) {

		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}

	}

	/**
	 * @param status
	 *            : statuses required , send null in argument if all statuses
	 *            are required
	 * @return : List of string statuses , null for invalid status
	 */
	public List<String> dplStatuses(String status) {

		List<String> statusList = null;
		if (status == null) {

			statusList = new ArrayList<String>() {

				private static final long serialVersionUID = 1L;

				{
					add(Constants.ACTIVE);
					add(Constants.DEACTIVE);
				}
			};
		}

		else if (status.equalsIgnoreCase(Constants.ACTIVE)) {

			statusList = new ArrayList<String>() {

				private static final long serialVersionUID = 1L;

				{
					add(Constants.ACTIVE);
				}
			};
		}
		else if (status.equalsIgnoreCase(Constants.DEACTIVE)) {

			statusList = new ArrayList<String>() {

				private static final long serialVersionUID = 1L;

				{
					add(Constants.DEACTIVE);
				}
			};
		}
		return statusList;

	}

	@Override
	public List<OracleGTMDeniedParties> generateOracleGTMCSV(String type, List<String> dplTypes,
			Date lastDataPusblisDate, Date currentDataPusblisDate, OracleGTMExportCriteria oracleGTMExportCriteria) {

		List<OracleGTMDeniedParties> deniedParties = new ArrayList<OracleGTMDeniedParties>();
		try {

			List<String> dplStatuses = null;
			Integer count;

			// add both active as well as deleted status
			dplStatuses = dplStatuses(null);
			if (type.equalsIgnoreCase(Constants.ENTIRE)) {

				// fileName = EASE_ENTIRE;
				// nonGsaFileName = EASE_ENTIRE_WITHOUT_GSA;

				// dplStatuses = dplStatuses(Constants.ACTIVE);
				count = dataPublishDao.easeEntiredataCount(dplStatuses, dplTypes);
				//System.out.println("number of records for specification " +oracleGTMExportCriteria.getUserSelectedSpecificationFileName() + "is " + count);
			}
			else {

				// fileName = EASE_INC;
				// nonGsaFileName = EASE_INC_WITHOUT_GSA;

				// add both active as well as deleted status
				// dplStatuses = dplStatuses(null);
				count = dataPublishDao.easeIncrementalDataCount(dplStatuses, dplTypes, lastDataPusblisDate,
						currentDataPusblisDate);
				System.out.println("number of records for specification " +oracleGTMExportCriteria.getUserSelectedSpecificationFileName() + " is " + count);
			}

			int start = 1;
			int end = recordsToFetch;
			int lastRecfetched = 0;

			// System.gc();
			while (count > lastRecfetched) {

				if (type.equalsIgnoreCase(Constants.ENTIRE)) {

					deniedParties.addAll(dataPublishDao.getEaseEntireData(dplStatuses, dplTypes, start, end));
				}
				else {
					deniedParties.addAll(dataPublishDao.getEaseEntireData(dplStatuses, dplTypes, start, end));
					//deniedParties.addAll(processDataForOracleCSV(dataPublishDao.getEaseIncrementalData(dplStatuses,
							//dplTypes, start, end, lastDataPusblisDate, currentDataPusblisDate), xmlExportCriteria));
				}

				lastRecfetched = end;
				start = end;
				end = end + recordsToFetch;
			}

			// Exporting entire data including GSA for dpl.xml
			// data.setDeniedParties(deniedParties);

			/*
			 * System.out.println("RECORD COUNT : " + count + " START " + start + " END :"
			 * + end + " Total recors exported: " +
			 * data.getDeniedParties().size());
			 */

			// Boolean isXmlExported = false;
			/*
			 * if (xmlExportCriteria.getUserSelectedSpecificationFileName() !=
			 * null && StringUtils.isNotBlank(xmlExportCriteria.
			 * getUserSelectedSpecificationFileName())) { isXmlExported =
			 * exportService.exportDatatoXML(data, easeExportLocation +
			 * xmlExportCriteria.getUserSelectedSpecificationFileName() +
			 * File.separator + fileName); } else { isXmlExported =
			 * exportService.exportDatatoXML(data, easeExportLocation +
			 * Constants.CUSTOM_FILENAME + File.separator + fileName); }
			 */ 

			// Removing GSA records from the data
			/*
			 * if (isXmlExported) { List<DeniedParty> dpls =
			 * data.getDeniedParties(); Iterator<DeniedParty> iterator =
			 * dpls.iterator(); while (iterator.hasNext()) { DeniedParty dpl =
			 * iterator.next(); if (dpl.getType().equalsIgnoreCase("GSA")) {
			 * iterator.remove(); } } data.setDeniedParties(dpls); System.out.
			 * println("Total recors exported after removing gsa records : " +
			 * dpls.size()); // Exporting entire data including GSA for dpl.xml
			 * if (xmlExportCriteria.getUserSelectedSpecificationFileName() !=
			 * null && StringUtils.isNotBlank(xmlExportCriteria.
			 * getUserSelectedSpecificationFileName())) {
			 * exportService.exportDatatoXML(data, easeExportLocation +
			 * xmlExportCriteria.getUserSelectedSpecificationFileName() +
			 * File.separator + nonGsaFileName); } else {
			 * exportService.exportDatatoXML(data, easeExportLocation +
			 * Constants.CUSTOM_FILENAME + File.separator + nonGsaFileName); }
			 * isXmlGenerated = true; }
			 */
			System.out.println("Total Number DPL data exported for specification "+oracleGTMExportCriteria.getUserSelectedSpecificationFileName()+ " is " +   deniedParties.size());
		}
		catch (Exception e) {

			e.printStackTrace();
			System.out.println("exception while generating oracle gtm data ");
		}
		return deniedParties;

	}


	@Override
	public Boolean ftpOracleGTMData(OracleGTMExportCriteria xmlExportCriteria) {

		StringBuilder path = new StringBuilder();

		if (xmlExportCriteria.getUserSelectedSpecificationFileName() != null
				&& StringUtils.isNotBlank(xmlExportCriteria.getUserSelectedSpecificationFileName())) {
			path.append(oracleGTMExportLocation + xmlExportCriteria.getUserSelectedSpecificationFileName()
					+ File.separator + dateDirectory + File.separator);
		}
		else {
			path.append(oracleGTMExportLocation);
		}

		if (StringUtils.isNotBlank(xmlExportCriteria.getFTPFileLocation())) {
			System.out.println("Transferring directory to FTP server location: " + xmlExportCriteria.getFTPFileLocation());
			return ftpService.transfeDirtoFTPServer(new File(path.toString()), xmlExportCriteria.getFTPFileLocation());
		}
		else {
			System.out.println("Transferring directory to FTP server location: " + oracleeFtpLocation);
			return ftpService.transfeDirtoFTPServer(new File(path.toString()), oracleeFtpLocation);
		}
	}

	@Override
	public Boolean compressUsingPkzipc(OracleGTMExportCriteria xmlExportCriteria) {

		Boolean filesCompressed = false;
		try {

			String command = "";
			if (xmlExportCriteria.getUserSelectedSpecificationFileName() != null
					&& StringUtils.isNotBlank(xmlExportCriteria.getUserSelectedSpecificationFileName())) {
				command = "cmd /c pkzipc_oracle_compression.bat > ouputOracle.txt " + oracleGTMExportLocation
						+ xmlExportCriteria.getUserSelectedSpecificationFileName() + File.separator + dateDirectory
						+ File.separator + " " + oracleGTMExportLocation
						+ xmlExportCriteria.getUserSelectedSpecificationFileName() + File.separator + dateDirectory
						+ File.separator;
			}
			else {

				command = "cmd /c pkzipc_oracle_compression.bat > ouputOracle.txt " + oracleGTMExportLocation
						+ Constants.CUSTOM_FILENAME + File.separator + dateDirectory + File.separator + " "
						+ oracleGTMExportLocation + Constants.CUSTOM_FILENAME + File.separator + dateDirectory
						+ File.separator;
			}

			Runtime.getRuntime().exec(command, null, new File(batchFilePath));
			System.out.println("compressed oracleGTM data files using pkzipc.exe sucessfully");
			filesCompressed = true;
		}
		catch (IOException e) {
			System.out.println("error while compressing oracleGTM data files using pkzipc.exe");
		}
		return filesCompressed;
	}

	/**
	 * generates Oracle GTMContentTypeFile CSV for the collection passed in
	 * 
	 * @param xmlExportCriteria
	 * @throws Exception
	 */
	private String generateGTMContentTypeFile(OracleGTMExportCriteria xmlExportCriteria) throws Exception {

		// String path = PropertyLoader.getKeyValue("save.file.path");
		this.fileName = "GTM_CONTENT_TYPE.CSV";
		String inputFileName = oracleGTMExportLocation + xmlExportCriteria.getUserSelectedSpecificationFileName()
				+ File.separator + dateDirectory + File.separator + fileName;
		FileWriter fw = new FileWriter(inputFileName);
		BufferedWriter bw=null;

		try {
			bw = new BufferedWriter(fw);

			String dplHeader = null;

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_TYPE.HEADER1");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_TYPE.HEADER2");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_TYPE.HEADER3");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_TYPE.HEADER4");
			bw.append(dplHeader + "\n");
		}
		catch (IOException e) {
			System.out.println("error occurred while generating oracle gtm content type CSV");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("error occurred while generating oracle gtm content type CSV");
			e.printStackTrace();
		}
		finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return inputFileName;

	}

	/**
	 * generates Oracle GTMContentsourceFile CSV for the collection passed in
	 * 
	 * @param xmlExportCriteria
	 * @throws Exception
	 */
	private String generateGTMContentSourceFile(OracleGTMExportCriteria xmlExportCriteria) throws Exception {

		this.fileName = "GTM_CONTENT_SOURCE.CSV";
		String inputFileName = oracleGTMExportLocation + xmlExportCriteria.getUserSelectedSpecificationFileName()
				+ File.separator + dateDirectory + File.separator + fileName;
		FileWriter fw = new FileWriter(inputFileName);
		BufferedWriter bw =null;

		try {

			 bw = new BufferedWriter(fw);

			 String dplHeader = null;

				dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_SOURCE.HEADER1");
				bw.append(dplHeader + "\n");

				dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_SOURCE.HEADER2");
				bw.append(dplHeader + "\n");

				dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_SOURCE.HEADER3");
				bw.append(dplHeader + "\n");

				dplHeader = PropertyLoader.getKeyValue("DPL.GTM_CONTENT_SOURCE.HEADER4");
				bw.append(dplHeader + "\n");
				
		}
		catch (IOException e) {
			System.out.println("error occurred while generating oracle gtm content source CSV");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("error occurred while generating oracle gtm content source CSV");
			e.printStackTrace();
		}
		finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return inputFileName;
	}

	/**
	 * generates Oracle GTM data version CSV for the collection passed in
	 * 
	 * @param dataList
	 * @param fileName
	 * @throws throws
	 *             Exception
	 */

	private String generateDataVersionFile(OracleGTMExportCriteria xmlExportCriteria) throws Exception {

		this.fileName = "DATA_VERSION.CSV";
		String inputFileName = oracleGTMExportLocation + xmlExportCriteria.getUserSelectedSpecificationFileName()
				+ File.separator + dateDirectory + File.separator + fileName;
		FileWriter fw = new FileWriter(inputFileName);
		BufferedWriter bw = null;

		try {

			bw = new BufferedWriter(fw);
			String dplHeader = null;
			
			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_DATA_VERSION.HEADER1");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_DATA_VERSION.HEADER2");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_DATA_VERSION.HEADER3");
			bw.append(dplHeader + "\n");

			dplHeader = PropertyLoader.getKeyValue("DPL.GTM_DATA_VERSION.HEADER4");
			bw.append(dplHeader + "\n");
		}
		catch (IOException e) {
			System.out.println("error occurred while oracle gtm data version CSV");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("error occurred while oracle gtm data version CSV");
			e.printStackTrace();
		}
		finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return inputFileName;

	}

	/*
	 * private void emailGenerationStatus(List<Message> messages) {
	 * 
	 * try { Map<String, List<Message>> model = new HashMap<String,
	 * List<Message>>(); model.put("returnMessages", messages); String text =
	 * VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
	 * "/template/returnMessage.vm", "UTF-8", model); String[] emailto =
	 * emailserviceDao.getParamValue("FILE_GENERATION_STATUS_REPORT").split(",");
	 * String subject = "File Generation Report " + new
	 * SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
	 * emailSender.sendMail("wlcs.production@gmail.com", emailto, emailto, emailto,
	 * subject, text, "test"); } catch (Exception e) {
	 * System.out.println("Exception while sending email ", e); } }
	 */

	/*
	 * Generate denided party data and export it into csv for oracle gtm
	 */
	private String generateOracleGTMDeniedPartyCSV(String type, List<String> dplTypes, Date lastDataPusblisDate,
			Date currentDataPusblisDate, OracleGTMExportCriteria oracleGTMExportCriteria) throws Exception {
		this.fileName = "GTM_DENIED_PARTY.CSV";
		String inputFileName = oracleGTMExportLocation + oracleGTMExportCriteria.getUserSelectedSpecificationFileName()
				+ File.separator + dateDirectory + File.separator + fileName;
		FileWriter fw = new FileWriter(inputFileName);

		try {

			BufferedWriter bw = new BufferedWriter(fw);

			List<String> headerList1 = new ArrayList<>();

			for (int i = 1; i <= 45; i++) {
				if (i == 1) {
					headerList1.add("GTM_DENIED_PARTY");
				}
				else {
					headerList1.add("");
				}
			}
			// for header1
			CSVUtils.writeLine(fw, headerList1);

			List<String> headerList2 = new ArrayList<>();

			headerList2.add("GTM_DENIED_PARTY_GID");
			headerList2.add("GTM_DENIED_PARTY_XID");
			headerList2.add("FIRST_NAME");
			headerList2.add("LAST_NAME");
			headerList2.add("COMPANY_NAME");
			headerList2.add("VESSEL_NAME");
			headerList2.add("ADDRESS_LINE_1");
			headerList2.add("ADDRESS_LINE_2");
			headerList2.add("ADDRESS_LINE_3");
			headerList2.add("CITY");
			headerList2.add("PROVINCE");
			headerList2.add("POSTAL_CODE");
			headerList2.add("COUNTRY");
			headerList2.add("EFFECTIVE_DATE");
			headerList2.add("EXPIRATION_DATE");
			headerList2.add("DATE_OF_BIRTH");
			headerList2.add("PLACE_OF_BIRTH");
			headerList2.add("PASSPORT_NUMBER");
			headerList2.add("PASSPORT_ISSUING_COUNTRY");
			headerList2.add("PASSPORT_ISSUING_DATE");
			headerList2.add("ALT_PASSPORT_NUMBER");
			headerList2.add("ALT_PASSPORT_ISSUING_COUNTRY");
			headerList2.add("ALT_PASSPORT_ISSUING_DATE");
			headerList2.add("DRIVER_LICENSE_NUMBER");
			headerList2.add("DRIVER_LICENSE_AUTHORITY");
			headerList2.add("CITIZENSHIP");
			headerList2.add("NATIONALITY");
			headerList2.add("SSN");
			headerList2.add("NIT");
			headerList2.add("CEDULA");
			headerList2.add("DENIAL_CODE");
			headerList2.add("ENTRY_DATE");
			headerList2.add("ENTRY_ID");
			headerList2.add("AGENCY_CODE");
			headerList2.add("RULING_VOLUME");
			headerList2.add("RULING_PAGE_NUMBER");
			headerList2.add("FEDERAL_REGULATION_DATE");
			headerList2.add("NOTES");
			headerList2.add("GTM_DATA_VERSION_GID");
			headerList2.add("IS_INUSE");
			headerList2.add("SCREENING_STATUS");
			headerList2.add("FEDERAL_REGULATION_URL");
			headerList2.add("DOMAIN_NAME");
			headerList2.add("COUNTRY_CODE");

			// for header2
			CSVUtils.writeLine(fw, headerList2);

			List<String> headerList3 = new ArrayList<>();

			for (int i = 1; i <= 45; i++) {
				if (i == 1) {
					headerList3.add("EXEC SQL ALTER SESSION SET NLS_DATE_FORMAT = 'YYYYMMDDHH24MISS'");
				}
				else {
					headerList3.add("");
				}
			}

			// for header3
			CSVUtils.writeLine(fw, headerList3);

			headerList1 = null;
			headerList2 = null;
			headerList3 = null;

			List<OracleGTMDeniedParties> deniedPartiesList = generateOracleGTMCSV(Constants.ENTIRE, dplTypes,
					lastDataPusblisDate, currentDataPusblisDate, oracleGTMExportCriteria);
			writeLineInCSVForDeniedPartyList(fw, deniedPartiesList);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			fw.flush();
			fw.close();
		}

		return inputFileName;
	}

	private void writeLineInCSVForDeniedPartyList(FileWriter fw, List<OracleGTMDeniedParties> deniedParties)
			throws IOException, Exception {

		for (OracleGTMDeniedParties deniedParty : deniedParties) {

			List<String> list = new ArrayList<>();

			list.add(deniedParty.getDplId());
			list.add(deniedParty.getDplId());
			list.add(deniedParty.getFirstName());
			list.add(deniedParty.getLastName());
			list.add(deniedParty.getCompanyName());
			list.add(deniedParty.getVessleName());
			list.add(deniedParty.getDplAddress1());
			list.add(deniedParty.getDplAddress2());
			list.add(deniedParty.getAddressLine());
			list.add(deniedParty.getCity());
			list.add(deniedParty.getDplState());
			list.add(deniedParty.getDplZip());
			list.add(deniedParty.getCountryName());
			list.add(deniedParty.getDplEffectiveDate());
			list.add(deniedParty.getDplExpiryDate());
			list.add(deniedParty.getDateOFBirth());
			list.add(deniedParty.getPlaceOFBirth());
			list.add(deniedParty.getPassportNumber());
			list.add(deniedParty.getPassportIssueCountry());
			list.add(deniedParty.getPasspostIssueDate());
			list.add(deniedParty.getAltPassportNumber());
			list.add(deniedParty.getAltpassportIssueCountry());
			list.add(deniedParty.getAltpasspostIssueDate());
			list.add(deniedParty.getDrivierLicenseNumber());
			list.add(deniedParty.getDrivierLicenseAuthority());
			list.add(deniedParty.getCitizenShip());
			list.add(deniedParty.getNationality());
			list.add(deniedParty.getSsn());
			list.add(deniedParty.getNit());
			list.add(deniedParty.getCedula());
			list.add(deniedParty.getDeniedCode());
			list.add(deniedParty.getEntryDate());
			list.add(deniedParty.getEntryId().toString());
			list.add(deniedParty.getAgencyCode());
			list.add(deniedParty.getRulingVolume());
			list.add(deniedParty.getRulingPageNumber());
			list.add(deniedParty.getFederalRegDate());
			list.add(deniedParty.getNotes());
			list.add(deniedParty.getGtmDateVersionId());
			list.add(deniedParty.getIsInUse());
			list.add(deniedParty.getScreeningStatus());
			list.add(deniedParty.getDplURL());
			list.add(deniedParty.getDomainName());
			list.add(deniedParty.getCountry1IsoCode());

			// handle commma inside value
			CSVUtils.writeLine(fw, list, ' ', ' ');

		}

	}

	private void downloadZipFile(String sourceFiles[], String destTempFileName) {
		// String path = PropertyLoader.getKeyValue("save.file.path");
		// destTempFileName = path.concat(destTempFileName);
		byte buffer[] = new byte[1024];
		try {
			FileOutputStream fout = new FileOutputStream(destTempFileName);
			ZipOutputStream zout = new ZipOutputStream(fout);
			for (int i = 0; i < sourceFiles.length; i++) {
				FileInputStream fin = new FileInputStream(sourceFiles[i]);
				zout.putNextEntry(new ZipEntry(sourceFiles[i]));
				int length;
				while ((length = fin.read(buffer)) > 0)
					zout.write(buffer, 0, length);
				zout.closeEntry();

				fin.close();
				File f = new File(sourceFiles[i]);
				f.delete();
			}

			zout.close();
			// FileInputStream fileInputStream = new FileInputStream(new
			// File(destTempFileName));

		}
		catch (Exception ex) {
			System.out.println("Unable to  export file for data type : ");

		}
		finally {
			try {
				(new File(destTempFileName)).delete();
			}
			catch (Exception ex) {
			}
		}
	}

	@Override
	public void generateOracleGTMDataFromConfigFile() {

		List<String[]> specifications = new ArrayList<String[]>();

		String oracleGTMSpecificationfileName = PropertyLoader.getKeyValue("oraclegtm.csv.client.config.path"); 

		try {
			// InitialContext context = new InitialContext();
			// Context xmlNode = (Context) context.lookup("java:comp/env");
			// configPath = (String) xmlNode.lookup("config");
			// String oracleGTMSpecificationfileName = configPath +
			// "oraclegtm-specification.csv";
			System.out.println("loading Specification data from file for oracle GTM " + oracleGTMSpecificationfileName);
			File specificationFile = new File(oracleGTMSpecificationfileName);
			if (specificationFile.exists()) {
				System.out.println("oracle gtm data file exists " + specificationFile);
				// read csv file in csv reader
				FileInputStream fis = new FileInputStream(specificationFile);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.ISO_8859_1);
				CSVReader reader = new CSVReader(isr);
				specifications = reader.readAll();
				System.out.println("Number of Total specification in oracle-gtm is " + (specifications.size()-1));

				List<OracleGTMExportCriteria> listSpecification = new ArrayList<OracleGTMExportCriteria>();
				for (int i = 1; i < specifications.size(); i++) {

					String[] record = specifications.get(i);
					OracleGTMExportCriteria oracleGTMExportCriteria = new OracleGTMExportCriteria();
					oracleGTMExportCriteria.setUserSelectedSpecificationFileName(record[0]);
					String selectedDplTypeArray[] = record[1].split(",");
					List<String> selectedDplTypeList = Arrays.asList(selectedDplTypeArray);
					oracleGTMExportCriteria.setSelectedDplTypes(selectedDplTypeList);
					String selectedDplColumnNamesArray[] = record[2].split(",");
					List<String> selectedDplColumnNames = Arrays.asList(selectedDplColumnNamesArray);
					oracleGTMExportCriteria.setSelectedSpecificationNames(selectedDplColumnNames);
					oracleGTMExportCriteria.setFTPFileLocation(record[3]);
					generateOracleGTMData(oracleGTMExportCriteria);
					listSpecification.add(oracleGTMExportCriteria);
				}
			}
			else {
				System.out.println(
						"oracle gtm specification file not exists ito this location," + oracleGTMSpecificationfileName);
			}
		}
		catch (Exception e) {
			System.out.println("Failed to generate oracle GTM Denied data from file ");
		}

	}

	public DataPublishDao getDataPublishDao() {
		return dataPublishDao;
	}

	public void setDataPublishDao(DataPublishDao dataPublishDao) {
		this.dataPublishDao = dataPublishDao;
	}

	
	public void setRecordsToExport(Integer recordsToExport) {
		this.recordsToFetch = recordsToExport;
	}

	

	
	public String getOracleGTMExportLocation() {
		return oracleGTMExportLocation;
	}

	public void setOracleGTMExportLocation(String oracleGTMExportLocation) {
		this.oracleGTMExportLocation = oracleGTMExportLocation;
	}

	public String getOracleeFtpLocation() {
		return oracleeFtpLocation;
	}

	public void setOracleeFtpLocation(String oracleeFtpLocation) {
		this.oracleeFtpLocation = oracleeFtpLocation;
	}

	public static void main(String[] args) {
		try {
			OracleGTMDataPublishService om = new OracleGTMDataPublishServiceImpl();
			om.generateOracleGTMDataFromConfigFile();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

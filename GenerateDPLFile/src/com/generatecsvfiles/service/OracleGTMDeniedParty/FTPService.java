package com.generatecsvfiles.service.OracleGTMDeniedParty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.generatecsvfiles.utility.CompressFileUtil;


public class FTPService {

	// FTP
	//@Value("${ftp.host.name}")
	private String FTPHostName;

	//@Value("${ftp.user.name}")
	private String FTPUserName;

	//@Value("${ftp.password}")
	private String FTPPassword;

	//@Value("${ftp.port.no}")
	private Integer FTPPortNo;

	// SFTP
	//@Value("${sftp.host.name}")
	private String SFTPHostName;

	//@Value("${sftp.user.name}")
	private String SFTPUserName;

	//@Value("${sftp.password}")
	private String SFTPPassword;

	//@Value("${sftp.port.no}")
	private Integer SFTPPortNo;


	public Boolean transferFiletoFTPServer(String fileToTransfer, String serverLocation, String fileName) {

		FTPClient ftpClient = new FTPClient();
		FileInputStream inputStream = null;
		Boolean isFileTranfered = false;
		try {
			ftpClient.setDefaultTimeout(10000);
			ftpClient.connect(FTPHostName);
			boolean login = ftpClient.login(FTPUserName, FTPPassword);

			// Added by Sarang to resolve issue with the time out
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();

			if (login) {

				System.out.println("Connection established...");
				inputStream = new FileInputStream(fileToTransfer);

				isFileTranfered = ftpClient.storeFile(serverLocation + fileName, inputStream);

				System.out.println("File uploaded status for file : " + fileName + " STATUS :" + isFileTranfered);

				Boolean isLoggedOff = ftpClient.logout();
				System.out.println("logout .. status : :" + isLoggedOff);
			}

		}
		catch (SocketException e) {
			System.out.println("Exception while uploading file ");
		}
		catch (IOException e) {
			System.out.println("Exception while uploading file ");
		}
		finally {
			try {
				ftpClient.disconnect();
			}
			catch (IOException e) {
				System.out.println("Exception while uploading file ");
			}
		}
		return isFileTranfered;

	}

	public Boolean transfeDirtoFTPServer(File dir, String serverLocation) {

		FTPClient ftpClient = new FTPClient();
		FileInputStream inputStream = null;
		Boolean isFileTranfered = false;

		try {

			List<String> files = new ArrayList<String>();
			CompressFileUtil.populateFilesList(dir, files);

			if (files.size() == 0) {

				System.out.println("directory us empty");
				return isFileTranfered;
			}

			ftpClient.setDefaultTimeout(10000);
			ftpClient.connect(FTPHostName);
			boolean login = ftpClient.login(FTPUserName, FTPPassword);
			
			// Added by Sarang to resolve issue with the time out
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
						
			if (login) {

				System.out.println("Connection established...");

				for (String file : files) {

					inputStream = new FileInputStream(file);
					isFileTranfered = ftpClient.storeFile(serverLocation + StringUtils.substringAfterLast(file, "\\"),
							inputStream);
					System.out.println("File uploaded status for file : " + file + " STATUS :" + isFileTranfered);
				}

				Boolean isLoggedOff = ftpClient.logout();
				System.out.println("logout .. status : :" + isLoggedOff);
			}

		}
		catch (SocketException e) {
			System.out.println("Exception while uploading file ");
		}
		catch (IOException e) {
			System.out.println("Exception while uploading file ");
		}
		catch (Exception e) {
			System.out.println("Exception while uploading file ");
		}
		finally {

			try {
				ftpClient.disconnect();
			}
			catch (IOException e) {
				System.out.println("Exception while diconectiong FTP client");
			}
		}
		return isFileTranfered;

	}

	

	
	public void setFTPHostName(String fTPHostName) {
		FTPHostName = fTPHostName;
	}

	public void setFTPUserName(String fTPUserName) {
		FTPUserName = fTPUserName;
	}

	public void setFTPPassword(String fTPPassword) {
		FTPPassword = fTPPassword;
	}

	public void setFTPPortNo(Integer fTPPortNo) {
		FTPPortNo = fTPPortNo;
	}

	public void setSFTPHostName(String sFTPHostName) {
		SFTPHostName = sFTPHostName;
	}

	public void setSFTPUserName(String sFTPUserName) {
		SFTPUserName = sFTPUserName;
	}

	public void setSFTPPassword(String sFTPPassword) {
		SFTPPassword = sFTPPassword;
	}

	public void setSFTPPortNo(Integer sFTPPortNo) {
		SFTPPortNo = sFTPPortNo;
	}
}

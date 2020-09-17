package com.generatecsvfiles.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.generatecsvfiles.service.OracleGTMDeniedParty.OracleGTMDataPublishService;
import com.generatecsvfiles.service.OracleGTMDeniedParty.OracleGTMDataPublishServiceImpl;

/**
*
* @author SONU KUMAR
*/
public class CSVGeneratorTester {
	
	
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		String enterFileName;
		String countryCode=null;
		// Prompts the User
		System.out.println("Please enter fileName which you want to generate. File name should be from (ECCN_CSV, SCH_B, HS_DETAILS, DPL_DATA)");
		enterFileName = input.next();
		
		List<String> fileList = new ArrayList<>();
		fileList.add("ECCN_CSV");
		fileList.add("SCH_B");
		fileList.add("HS_DETAILS");
		fileList.add("DPL_DATA");
		
		if(!fileList.contains(enterFileName)) {
			System.out.println("Wrong FileName...File name should be from (ECCN_CSV, SCH_B, HS_DETAILS, DPL_DATA).");
		}else if(enterFileName.equalsIgnoreCase("DPL_DATA")) {
			OracleGTMDataPublishService dplData = new OracleGTMDataPublishServiceImpl();
			dplData.generateOracleGTMDataFromConfigFile();
		}
		else {
			System.out.println("Please enter Country Code:");
			countryCode = input.next();
			System.out.println("Please wait....");
		}
	
	}
	
	
}

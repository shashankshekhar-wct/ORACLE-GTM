package com.generatecsvfiles.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.generatecsvfiles.service.OracleGTMDeniedParty.OracleGTMDataPublishService;
import com.generatecsvfiles.service.OracleGTMDeniedParty.OracleGTMDataPublishServiceImpl;

/**
*
* @author Shashank s
*/
public class DPLGeneratorMain {
	
	
	
	public static void main(String[] args) {
			
			System.out.println("::::::::DPL Data Generation start for Oracle GTM at: " + new Date() + ":::::::::");
			OracleGTMDataPublishService dplData = new OracleGTMDataPublishServiceImpl();
			dplData.generateOracleGTMDataFromConfigFile();
		
	}
	
	
}

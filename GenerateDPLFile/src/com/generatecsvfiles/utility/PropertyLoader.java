
package com.generatecsvfiles.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author SONU KUMAR
 */
public class PropertyLoader {

    private static Properties properties = new Properties();
    private static String folderName = "D:\\GenerateCSVFile\\";
    
    private PropertyLoader() {}

    static{
        InputStream is = null;
        try {
            is = ClassLoader.class.getResourceAsStream("/dataConfig.properties");
            properties.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	 /*static {
	        if (folderName != null) {
	            File configFile = new File(folderName + "dataConfig.properties");
	            try {
	                InputStream is = new FileInputStream(configFile);
	                properties.load(is);
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	                System.out.println("exception loading properties file");
	            }
	        }


	    }*/
	
    /**
     * returns the kep value
     *
     * @param key
     * @return
     */
    public static String getKeyValue(String key) {
        return properties.getProperty(key);
    }
    
    
    
}

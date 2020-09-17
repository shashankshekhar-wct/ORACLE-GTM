package com.generatecsvfiles.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class CompressFileUtil {


	public static Boolean gzipFile(String sourceFile, String destinatonFile) {

		byte[] buffer = new byte[1024];
		Boolean isFileCompressed = false;

		try {

			FileOutputStream fileOutputStream = new FileOutputStream(destinatonFile);

			GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);

			FileInputStream fileInput = new FileInputStream(sourceFile);

			int bytes_read;

			while ((bytes_read = fileInput.read(buffer)) > 0) {
				gzipOuputStream.write(buffer, 0, bytes_read);
			}
			System.out.println("The file was compressed successfully to :" + destinatonFile);
			fileInput.close();

			gzipOuputStream.finish();
			gzipOuputStream.close();

			System.out.println("The file was compressed successfully!");
			isFileCompressed = true;

		}
		catch (IOException ex) {

			System.out.println("exception while compressing file ");
			ex.printStackTrace();
		}

		return isFileCompressed;
	}

	public static Boolean zipFile(String sourceFile, String destinatonFile) {

		System.out.println("gzipping file " + sourceFile);
		byte[] buffer = new byte[1024];
		Boolean isFileCompressed = false;
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOuputStream = null;
		try {

			fileOutputStream = new FileOutputStream(destinatonFile);

			zipOuputStream = new ZipOutputStream(fileOutputStream);
			// add a new Zip Entry to the ZipOutputStream
			ZipEntry ze = new ZipEntry(new File(sourceFile).getName());
			zipOuputStream.putNextEntry(ze);

			FileInputStream fileInput = new FileInputStream(sourceFile);

			int bytes_read;

			while ((bytes_read = fileInput.read(buffer)) > 0) {
				zipOuputStream.write(buffer, 0, bytes_read);
			}
			System.out.println("The file was compressed successfully to :" + destinatonFile);
			fileInput.close();

			zipOuputStream.finish();
			zipOuputStream.close();

			System.out.println("The file was compressed successfully!");
			isFileCompressed = true;

		}
		catch (Exception ex) {

			System.out.println("exception while compressing file ");
			try {
				if (zipOuputStream != null)
					zipOuputStream.close();
				if (fileOutputStream != null)
					fileOutputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
		}

		return isFileCompressed;
	}

	/**
	 * This method zips the directory
	 * 
	 * @param dir
	 * @param zipDirName
	 */
	public static Boolean zipDirectory(File dir, String zipDirName) {
		Boolean isFileCompressed = false;
		try {
			
			List<String> files = new ArrayList<String>();

			populateFilesList(dir, files);

			// now zip files one by one
			// create ZipOutputStream to write to the zip file
			FileOutputStream fos = new FileOutputStream(zipDirName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (String filePath : files) {
				System.out.println("Zipping " + filePath);

				// for ZipEntry we need to keep only relative file path, so we
				// used substring on absolute path
				ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
				zos.putNextEntry(ze);
				// read the file and write to ZipOutputStream
				FileInputStream fis = new FileInputStream(filePath);
				byte[] buffer = new byte[1024];
				int len;

				while ((len = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
			fos.close();
			System.out.println("The file was compressed successfully into " +zipDirName);
			isFileCompressed=true;
		}
		catch (IOException e) {
			System.out.println("exception while zipping file");
		}
		return isFileCompressed;
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static void populateFilesList(File dir, List<String> files) throws IOException {
		File[] filesArr = dir.listFiles();
		for (File file : filesArr) {
			if (file.isFile())
				files.add(file.getAbsolutePath());
			else
				populateFilesList(file, files);
		}
	}

}

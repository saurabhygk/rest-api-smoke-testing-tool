package com.estt.config.creator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by saurabh.yagnik on 12/15/16.
 */
public class ConfigFileCreatorUtil {
	/**
	 * This method will convert normal contents into JSON format string
	 * 
	 * @param endPoints : Normal json content
	 * @return String : return the JSON formatted string
	 * @throws Exception : throws Exception if error occurs
	 */
	public static String writerJsonStringPretty(String endPoints) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(endPoints, Object.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * This method is used to write file into specified directory.
	 * 
	 * @param configFileLoc : Location to store created configuration file
	 * @param fileName      : file name to be assign to create file
	 * @param response      : contents to be written into file
	 * @throws Exception : throws exception if error occurs
	 */
	public static void writeFile(String configFileLoc, String fileName, String response) throws Exception {
		BufferedWriter writer = null;
		File directoryFile = new File(configFileLoc);
		try {
			if (!directoryFile.exists()) {
				directoryFile.mkdir();
			}
			File file = new File(configFileLoc.concat("/").concat(fileName));
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file));
			writer.append(response);

		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			closeFile(writer);
		}
	}

	/**
	 * This method is used to close file
	 * 
	 * @param writer : {@link BufferedWriter} object to be close
	 * @throws Exception : throws exception if error occurs
	 */
	private static void closeFile(BufferedWriter writer) throws Exception {
		try {
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}

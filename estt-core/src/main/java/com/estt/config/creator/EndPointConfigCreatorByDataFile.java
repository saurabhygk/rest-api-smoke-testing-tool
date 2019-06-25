package com.estt.config.creator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by saurabh.yagnik on 12/15/16.
 */
public class EndPointConfigCreatorByDataFile {

	private String configFileLoc = "";
	private String dataFilePath = "";
	private String DATA_FILE_NAME = "datafile.txt";
	private String JSON_FORMAT = ".json";
	private String PROPERTIES_FORMAT = ".properties";
	private String DEFAULT_CONFIG_FILE_NAME = "EndPointConfig".concat(JSON_FORMAT);
	private String DEFAULT_ERROR_CODES_FILE_NAME = "ErrorCodes".concat(PROPERTIES_FORMAT);

	public EndPointConfigCreatorByDataFile(String configFileLoc, String dataFilePath) {
		this.configFileLoc = configFileLoc;
		this.dataFilePath = dataFilePath;
	}

	/**
	 * This method is used to create configuration files by reading datafile.txt
	 *
	 * @throws Exception : throws exception if error occurs
	 */
	public void createConfigurationFiles(String providedConfigFileName, String providedErrorCodesFileName)
			throws Exception {
		File dataFile = new File(dataFilePath.concat("/").concat(DATA_FILE_NAME));
		if (dataFile == null || !dataFile.exists()) {
			System.out.println(
					" Either data file does not exists or create data file with name like,".concat(DATA_FILE_NAME));
			return;
		}

		DATA_FILE_NAME = dataFilePath.concat("/").concat(DATA_FILE_NAME);
		StringBuilder sbEndPoints = new StringBuilder();
		sbEndPoints.append("[");
		sbEndPoints.append("{");
		List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_NAME));
		for (String line : lines) {
			if (StringUtils.isEmpty(line)) {
				sbEndPoints.append("},{");
				continue;
			}

			if (line.contains("ENDOFREAD")) {
				String endPoints = sbEndPoints.toString();
				endPoints = endPoints.substring(0, endPoints.length() - 2);
				endPoints = endPoints.concat("]");
				endPoints = endPoints.replaceAll("\\p{Cc}", "");

				if (StringUtils.isNotEmpty(providedConfigFileName)) {
					providedConfigFileName = providedConfigFileName.concat(JSON_FORMAT);
				} else {
					providedConfigFileName = DEFAULT_CONFIG_FILE_NAME;
				}

				ConfigFileCreatorUtil.writeFile(configFileLoc, providedConfigFileName,
						ConfigFileCreatorUtil.writerJsonStringPretty(endPoints));
				continue;
			}

			if (line.toLowerCase().contains("ERRORCODES".toLowerCase())) {
				String[] errorCodes = line.split("\\|");
				String errorCode = errorCodes[1].trim();
				boolean errorCodeContainsComma = errorCode.substring(errorCode.length() - 1, errorCode.length())
						.equals(",") ? true : false;
				if (null != errorCodes && errorCodeContainsComma) {
					errorCode = errorCode.substring(0, errorCode.length() - 1);
				}

				if (StringUtils.isNotEmpty(providedErrorCodesFileName)) {
					providedErrorCodesFileName = providedErrorCodesFileName.concat(PROPERTIES_FORMAT);
				} else {
					providedErrorCodesFileName = DEFAULT_ERROR_CODES_FILE_NAME;
				}
				ConfigFileCreatorUtil.writeFile(configFileLoc, providedErrorCodesFileName, errorCode);
				break;
			}
			String[] splitLine = line.split("\\|");
			if (null != splitLine) {
				String key = splitLine[0].trim();
				String value = splitLine[1].trim();
				if ("SQ".equalsIgnoreCase(key)) {
					sbEndPoints.append("\"sequence\":" + "\"" + value.toUpperCase() + "\"").append(",");
				}
				if ("M".equalsIgnoreCase(key)) {
					sbEndPoints.append("\"methodType\":" + "\"" + value.toUpperCase() + "\"").append(",");
				} else if ("U".equalsIgnoreCase(key)) {
					sbEndPoints.append("\"url\":" + "\"" + value + "\"");
				} else if ("RH".equalsIgnoreCase(key)) {
					sbEndPoints.append(",");
					sbEndPoints.append("\"requestHeaders\":" + "\"" + value + "\"");
				} else if ("RP".equalsIgnoreCase(key)) {
					sbEndPoints.append(",");
					if (value.contains("\"")) {
						value = value.replace("\"", "\\\"");
					}
					sbEndPoints.append("\"requestParam\":" + "\"" + value + "\"");
				} else if ("RB".equalsIgnoreCase(key)) {
					sbEndPoints.append(",");
					if (value.toString().contains("\"")) {
						value = value.toString().replace("\"", "\\\"");
					}
					sbEndPoints.append("\"requestBody\":" + "\"" + value + "\"");
				} else if ("ERC".equalsIgnoreCase(key)) {
					sbEndPoints.append(",");
					if (value.toString().contains("\"")) {
						value = value.toString().replace("\"", "\\\"");
					}
					sbEndPoints.append("\"expectedResponseCode\":" + "\"" + value + "\"");
				} else if ("ER".equalsIgnoreCase(key)) {
					sbEndPoints.append(",");
					if (value.toString().contains("\"")) {
						value = value.toString().replace("\"", "\\\"");
					}
					sbEndPoints.append("\"expectedResponse\":" + "\"" + value + "\"");
				}
			}
		}

	}
}

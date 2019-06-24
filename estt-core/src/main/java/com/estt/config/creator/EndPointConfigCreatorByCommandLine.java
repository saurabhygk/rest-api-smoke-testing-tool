package com.estt.config.creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by saurabh.yagnik on 12/15/16.
 */
public class EndPointConfigCreatorByCommandLine {

	private String configFileLoc = "";
	public EndPointConfigCreatorByCommandLine(String configFileLoc){
		this.configFileLoc = configFileLoc;
	}

	/**
	 * This method is used to create configuration files under configuration file location
	 * @throws Exception : throws exception if error occurs
	 */
	public void createConfigurationFiles() throws Exception{
		StringBuilder sbEndPoints = new StringBuilder();

		System.out.println("Please provide end point (s) configuration details:");
		sbEndPoints.append("[");
		BufferedReader bufferedReader = null;
		while(true){
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter End point configuration detail:");
			sbEndPoints.append("{");
			System.out.println("Please provide Sequence number in which sequence end point should be tested. (Example: 1, 2, 3, 4....):");
			String sequence = bufferedReader.readLine();
			if (StringUtils.isEmpty(sequence)) {
				System.out.println("Please provide Sequence number in which sequence end point should be tested. (Example: 1, 2, 3, 4....):");
				sequence = bufferedReader.readLine();
			}
			sbEndPoints.append("\"sequence\":"+"\""+sequence.trim().toUpperCase()+"\"").append(",");
			
			System.out.println("Please provide Method Type (Example: GET, POST, PUT, DELETE only these fours are supported):");
			List<String> methodTypes = Arrays.asList(new String[] {"GET", "POST", "PUT", "DELETE"});
			String methodType = bufferedReader.readLine();
			if ((StringUtils.isEmpty(methodType)) || (!StringUtils.isEmpty(methodType) && !methodTypes.contains(methodType))) {
				System.out.println("Provided method Type:[" + methodType + "] not support for now, please enter again either GET, POST, PUT, DELETE end points to test.!!");
				methodType = bufferedReader.readLine();
				if ((StringUtils.isEmpty(methodType)) || (!StringUtils.isEmpty(methodType) && !methodTypes.contains(methodType))) {
					System.out.println("Please process again with genuine values..!!");
					System.exit(1);
				}
			}
			sbEndPoints.append("\"methodType\":"+"\""+methodType.trim().toUpperCase()+"\"").append(",");
			
			System.out.println("Please provide end point URL:");
			String url = bufferedReader.readLine();
			if (StringUtils.isEmpty(url)) {
				System.out.println("Please provide end point URL:");
				url = bufferedReader.readLine();
			}
			sbEndPoints.append("\"url\":"+"\""+url.trim()+"\"");

			System.out.println("Please provide Request Headers, comma separated for multiple parameter(s) [OPTIONAL]:");
			String requestHeaders = bufferedReader.readLine();
			if(StringUtils.isNotEmpty(requestHeaders)) {
				sbEndPoints.append(",");
				sbEndPoints.append("\"requestHeaders\":" + "\"" + requestHeaders.trim() + "\"");
			}

			System.out.println("Please provide Request Parameter(s), comma separated for multiple parameter(s) [OPTIONAL]:");
			String requestParameters = bufferedReader.readLine();
			if(StringUtils.isNotEmpty(requestParameters)) {
				sbEndPoints.append(",");
				if(requestParameters.contains("\"")){
					requestParameters = requestParameters.trim().replace("\"", "\\\"");
				}
				sbEndPoints.append("\"requestParam\":" + "\"" + requestParameters.trim() + "\"");
			}

			if (!"GET".equalsIgnoreCase(methodType)) {
				System.out.println("Please provide Request Body, if not giving next option please provide double enter:");
				StringBuffer requestBody = new StringBuffer();
				//int ch = 0;
				String line = "";
				
				while((line = bufferedReader.readLine()) != null || (line = bufferedReader.readLine()) == null) {
					if (StringUtils.isEmpty(line)) {
						break;
					}
					String[] tokens = line.split("\\s");
					for(String token : tokens) {
						requestBody.append(token);
					}
				}
				
				/*
				 * while((ch =  bufferedReader.read()) != '\n') { requestBody.append((char)ch); }
				 */
				String requestBody1 = requestBody.toString().trim();
				if(StringUtils.isNotEmpty(requestBody1)) {
					sbEndPoints.append(",");
					if(requestBody.toString().contains("\"")){
						requestBody1 = requestBody.toString().trim().replace("\"", "\\\"");
					}
					sbEndPoints.append("\"requestBody\":" + "\"" + requestBody1 + "\"");
				}
			}
			
			System.out.println("Please provide Expected Response Code [OPTIONAL]:");
			String expectedResponseCode = bufferedReader.readLine();
			if(StringUtils.isNotEmpty(expectedResponseCode)) {
				sbEndPoints.append(",");
				sbEndPoints.append("\"expectedResponseCode\":" + "\"" + expectedResponseCode + "\"");
			}
			
			System.out.println("Please provide Expected Response to be match with returned response [OPTIONAL]:");
			String expectedResponse = bufferedReader.readLine();
			if(StringUtils.isNotEmpty(expectedResponse)) {
				sbEndPoints.append(",");
				sbEndPoints.append("\"expectedResponse\":" + "\"" + expectedResponse + "\"");
			}
			
			sbEndPoints.append("},");
			System.out.println();
			System.out.print("WOULD YOU LIKE TO ADD MORE END POINT?(Y/N)");
			String nextEP = bufferedReader.readLine();

			if("n".equalsIgnoreCase(nextEP.trim())){
				String endPoints = sbEndPoints.toString();
				endPoints = endPoints.substring(0,endPoints.length()-1);
				endPoints = endPoints.concat("]");
				endPoints = endPoints.replaceAll("\\p{Cc}", "");
				System.out.println("****************************");
				System.out.println("Please provide Error Codes, respected to your SYSTEM like Database error code, system error code etc. [OPTIONAL]:");
				System.out.println("****************************");
				String errorCodes = bufferedReader.readLine();
				
				if (StringUtils.isNotEmpty(errorCodes)) {
					if (null != errorCodes){
						ConfigFileCreatorUtil.writeFile(configFileLoc, "ErrorCodes.properties", errorCodes);
					}
				}
				ConfigFileCreatorUtil.writeFile(this.configFileLoc, "EndPointConfig.json", ConfigFileCreatorUtil.writerJsonStringPretty(endPoints));
				
				closeReader(bufferedReader);
				break;
			}
		}

	}
	
	private static void closeReader(BufferedReader bufferedReader) throws IOException{
		if(null != bufferedReader){
			bufferedReader.close();
		}
	}

}

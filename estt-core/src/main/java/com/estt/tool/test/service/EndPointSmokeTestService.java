package com.estt.tool.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.estt.tool.test.domain.EndPointConfig;
import com.estt.tool.test.domain.Response;
import com.estt.tool.test.logic.ConsumeEndPointLogic;
import com.estt.tool.test.redis.helper.BaseRedisHelper;
import com.estt.tool.test.utility.Utility;
import com.estt.tool.test.validator.ResponseValidator;

/**
 * Created by saurabh.yagnik on 2016/10/14.
 */
@Service
public class EndPointSmokeTestService {

	// static values initialization of for files
	private static final String STATUS_FILE_NAME = "/status";

	private static final String ENDPOINT_CONFIG = "EndPointConfig";

	private static final String ERROR_CODES = "ErrorCodes";

	private static final String ENDPOINT_CONFIG_JSON = ENDPOINT_CONFIG.concat(".json");

	private static final String ERROR_CODES_PROPERTIES = ERROR_CODES.concat(".properties");

	// maintain counter of success or failure, which will be used to render realtime
	// chart
	Integer successCount = 0;
	Integer errorCount = 0;
	
	@Value("${redis.key.timeout:1}")
	private Integer redisKeyTimout;
	
	@Value("${redis.key.timeUnit:DAYS}")
	String timeUnit;

	@Autowired
	private ResponseValidator responseValidator;

	@Autowired
	private ConsumeEndPointLogic consumeEndPoint;
	
	@Autowired
	private BaseRedisHelper<Object> redisHelper;

	public void testEndPoints(String... arguments) throws Exception {
		boolean noConfigFilePresent = false;
		String statusFileName = arguments[0].concat(STATUS_FILE_NAME.concat("_").concat(arguments[1]).concat(".txt"));
		Map<String, String> filesWithValues = listFilesForFolder(arguments[0]);
		if (filesWithValues.size() == 0) {
			noConfigFilePresent = true;
		} else if (!filesWithValues.containsKey(ENDPOINT_CONFIG) || !filesWithValues.containsKey(ERROR_CODES)) {
			noConfigFilePresent = true;
		} else {
			if (filesWithValues.size() != 0) {
				// read json file and convert to list of endpoint configuration object
				List<EndPointConfig> endPointConfigs = Utility.readEndPointConfigJson(filesWithValues);
				Collections.sort(endPointConfigs);
				// read error codes configuration file
				List<String> errorCodes = Utility.readConfigurationFile(filesWithValues);
				if (CollectionUtils.isNotEmpty(endPointConfigs)) {
					testEndPoints(endPointConfigs, errorCodes, statusFileName, arguments[1]);
				}
			}
		}
		if (noConfigFilePresent) {
			System.out.println("Please provide configuration files with name " + ENDPOINT_CONFIG_JSON + " and/or "
					+ ERROR_CODES_PROPERTIES + " respectively...!!!");
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * This is service method which consume the end point and returns the response
	 * 
	 * @param endPoints   : list of end points from EndPoints.properties file
	 * 
	 * @param errorCodes: list of error codes from ErrorCodes.properties file
	 * @return List : list of response after testing of each end point
	 * @throws Exception : throws exception if error occurs
	 */
	public void testEndPoints(List<EndPointConfig> endPoints, List<String> errorCodes, String statusFileDirPath, String userId)
			throws Exception {
		List<String> statusLines = new ArrayList<String>();

		// variable initialization
		List<String> headersList = null;
		String url = "";
		for (EndPointConfig endPoint : endPoints) {
			redisStoreOperation(userId.concat("-STATUS"), 0);
			try {
				headersList = new ArrayList<String>();
				if (null == endPoint) {
					continue;
				}

				Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoint);
				url = endPointConfigParameters.get("url");

				if (StringUtils.isNotEmpty(endPointConfigParameters.get("headersString"))
						&& !"NA".equalsIgnoreCase(endPointConfigParameters.get("headersString"))) {
					headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
				}

				Map<String, String> requestParameters = prepareGetRequestParameters(endPoint);

				String requestBody = endPointConfigParameters.get("data");

				try {
					Response endPointResponse = consumeEndPoint.consume(endPointConfigParameters, requestParameters,
							headersList, requestBody);

					if (null == endPointResponse && !StringUtils.isEmpty(endPoint.getExpectedResponse())) {
						errorCount++;
						redisStoreOperation(userId.concat("-EC"), errorCount);
						
						statusLines.add("ERROR ->[".concat(url).concat("] has returned").concat(
								" null response where there is expected response configured in config file. Either change config file or check your API response"));
					} else if (null == endPointResponse && StringUtils.isEmpty(endPoint.getExpectedResponse())) {
						statusLines.add("SUCCESS ->[".concat(url).concat("] executed successfully."));
						successCount++;
						redisStoreOperation(userId.concat("-SC"), successCount);
					} else {
						String responseCode = endPointResponse.getStatusCode();
						if (null != responseCode && !responseCode
								.equalsIgnoreCase(endPointConfigParameters.get("expectedResponseCode"))) {
							statusLines.add(
									"ERROR ->[".concat(url).concat("] has returned [").concat(responseCode.toString())
											.concat("] which is different than expected response code: ")
											.concat(endPointConfigParameters.get("expectedResponseCode")));
							errorCount++;
							redisStoreOperation(userId.concat("-EC"), errorCount);
						} else {
							statusLines.add(processResponseEntity(endPointConfigParameters.get("url"), endPointResponse,
									errorCodes, endPoint.getExpectedResponse(), userId));
						}
					}
				} catch (Exception e) {
					statusLines.add("ERROR ->[".concat(url).concat("] thrown an exception: [")
							.concat(e != null ? e.getMessage() : "EXCEPTION").concat("]"));
					errorCount++;
					redisStoreOperation(userId.concat("-EC"), errorCount);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}

		if (CollectionUtils.isNotEmpty(statusLines)) {
			Utility.writeFile(statusFileDirPath, statusLines);
		}
		redisStoreOperation(userId.concat("-STATUS_FILEPATH"), statusFileDirPath);
		redisStoreOperation(userId.concat("-DATA"), statusLines);
		redisStoreOperation(userId.concat("-STATUS"), 1);
	}

	private void redisStoreOperation(String key, Object value) {
		try {
			redisHelper.putValueWithExpireTime(key, value, redisKeyTimout, TimeUnit.valueOf(timeUnit));
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * This method process the response of end point
	 * 
	 * @param url        : End point URL
	 * @param response   : End point response
	 * @param errorCodes : List of error codes from ErrorCodes.properties file
	 * @throws Exception : throws exception if error occurs
	 */
	public String processResponseEntity(String url, Response response, List<String> errorCodes, String expectedResponse, String userId)
			throws Exception {
		String message = "";
		String responseBody = response.getResponseBody();
		String contentType = response.getResponseHeader();
		message = responseValidator.validateResponse(responseBody, errorCodes, expectedResponse, contentType);

		if (errorCodes.contains(message)) {
			errorCount++;
			redisStoreOperation(userId.concat("-EC"), errorCount);
			
			return "ERROR ->[".concat(url).concat("] has returned ").concat("error response code:\"").concat(message)
					.concat("\", please check your API for more error check.");
		} else if (null != message && "INVALID".equalsIgnoreCase(message) && !StringUtils.isEmpty(expectedResponse)) {
			errorCount++;
			redisStoreOperation(userId.concat("-EC"), errorCount);
			
			return "ERROR ->[".concat(url).concat("] has returned ").concat("different response then expected")
					.concat(", please check your API for more error check.");
		} else if (null != message && "INVALID".equalsIgnoreCase(message) && StringUtils.isEmpty(expectedResponse)) {
			errorCount++;
			redisStoreOperation(userId.concat("-EC"), errorCount);
			
			return "ERROR ->[".concat(url).concat("] having some error, please check your API log.");
		} else {
			successCount++;
			redisStoreOperation(userId.concat("-SC"), successCount);
			
			return "SUCCESS ->[".concat(url).concat("] executed successfully and matched with expected configuration.");
		}
	}

	/**
	 * This method is used to prepare MultiValue Map for end point configuration
	 * 
	 * @param endPoint : Configured end point
	 * @return returns {@link MultiValueMap}
	 * @throws Exception : Throws exception if operation fails
	 */
	public MultiValueMap<String, String> prepareRequestParameters(EndPointConfig endPoint) throws Exception {
		MultiValueMap<String, String> requestParamsMap = new LinkedMultiValueMap<String, String>();
		if (StringUtils.isNotEmpty(endPoint.getRequestParam())) {
			String requestParameter = endPoint.getRequestParam().trim();
			String[] parameters = null;
			if (requestParameter.contains("&")) {
				parameters = requestParameter.split("&");
				for (String param : parameters) {
					String[] params = param.split("=");
					requestParamsMap.add(params[0], params[1]);
				}
			} else {
				parameters = requestParameter.split("=");
				requestParamsMap.add(parameters[0], parameters[1]);
			}
		}
		return requestParamsMap;
	}

	/**
	 * THis method is used to prepare map for configured request parameters
	 * 
	 * @param endPoint : configured end point
	 * @return returns {@link Map}
	 * @throws Exception : throws exception if operation fails
	 */
	public Map<String, String> prepareGetRequestParameters(EndPointConfig endPoint) throws Exception {
		Map<String, String> requestParamsMap = new HashMap<>();
		String requestParam = endPoint.getRequestParam();
		if (StringUtils.isNotEmpty(requestParam)) {
			requestParam = Utility.replacePlaceHolderWithDate(requestParam);
			String requestParameter = requestParam.trim();
			String[] parameters = null;
			if (requestParameter.contains("&")) {
				parameters = requestParameter.split("&");
				for (String param : parameters) {
					String[] params = param.split("=");
					requestParamsMap.put(params[0], params[1]);
				}
			} else {
				parameters = requestParameter.split("=");
				requestParamsMap.put(parameters[0], parameters[1]);
			}
		}
		return requestParamsMap;
	}

	public Map<String, Number> getStatusData() {

		Map<String, Number> pieData = new HashMap<String, Number>();

		pieData.put("SUCCESS", successCount * 100);
		pieData.put("ERROR", errorCount * 100);

		return pieData;
	}

	public static Map<String, String> listFilesForFolder(final String configFileLocation) throws Exception {
		HashMap<String, String> fileWithContentMap = new HashMap<String, String>();
		File endPointConfigFile = new File(configFileLocation.concat("/").concat(ENDPOINT_CONFIG_JSON));
		File errorCodesFiles = new File(configFileLocation.concat("/").concat(ERROR_CODES_PROPERTIES));
		if (endPointConfigFile == null || !endPointConfigFile.exists() || errorCodesFiles == null
				|| !errorCodesFiles.exists()) {
			return fileWithContentMap;
		}

		try (FileInputStream inputStream = new FileInputStream(
				configFileLocation.concat("/").concat(ENDPOINT_CONFIG_JSON))) {
			String endpoints = IOUtils.toString(inputStream);
			fileWithContentMap.put(ENDPOINT_CONFIG, endpoints);
		}
		try (FileInputStream inputStream = new FileInputStream(
				configFileLocation.concat("/").concat(ERROR_CODES_PROPERTIES))) {
			String errorCodes = IOUtils.toString(inputStream);
			fileWithContentMap.put(ERROR_CODES, errorCodes);
		}
		return fileWithContentMap;
	}
}
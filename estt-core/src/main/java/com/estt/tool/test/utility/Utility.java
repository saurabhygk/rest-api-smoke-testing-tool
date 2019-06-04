package com.estt.tool.test.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.estt.tool.test.domain.EndPointConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Created by saurabh.yagnik on 2016/10/12.
 */
public class Utility {

    private static final String ENDPOINT_CONFIG = "EndPointConfig";

    private static final String ERROR_CODES = "ErrorCodes";
    
    private static final String DATE_PLACE_HOLDER = "yyyy-MM-dd";

    public static List<EndPointConfig> readEndPointConfigJson(Map<String,String> filesWithValues) throws  FileNotFoundException, IOException{
        List<EndPointConfig> endPoints = new ArrayList<EndPointConfig>();
        String endPointConfigs = filesWithValues.get(ENDPOINT_CONFIG);
        ObjectMapper objMapper = new ObjectMapper();
        endPoints = objMapper.readValue(endPointConfigs, TypeFactory.defaultInstance().constructCollectionType(List.class, EndPointConfig.class));
        return endPoints;
    }

    /**
     * This utility method is used to read configuration files
     * @param filesWithValues : configuration file name
     * @return List :  returns the list of lines
     * @throws Exception : throws exception if error occurs
     */
    public static List<String> readConfigurationFile(Map<String,String> filesWithValues) throws Exception{
        List<String> properties = new ArrayList<String>();
        String errorCodes = filesWithValues.get(ERROR_CODES);
        properties = new ArrayList<String>(Arrays.asList(errorCodes.split(",")));

        return properties;
    }

    /**
     * This utility method used to write list of response into report
     * @param statusFileDir : file name of report
     * @param responses : response with url, error code
     * @throws Exception : throws exception if error occurs
     */
    public static void writeFile(String statusFileDir, List<String> responses) throws Exception{
        File downloadFile = new File(statusFileDir);
        BufferedWriter writer = null;
        String directoryPath = statusFileDir.substring(0, statusFileDir.indexOf(statusFileDir));
        File directoryFile = new File(directoryPath);
        try {
            // if directory does not exists : make directory to prevent any exception
            if(!directoryFile.exists()){
                directoryFile.mkdir();
            }
            if(!downloadFile.exists())
            {
                downloadFile.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(downloadFile));
            for(String response : responses) {
                writer.append(response);
                writer.newLine();
            }
        } catch (Exception ex) {
        } finally {
            closeFile(writer);
        }
    }

    /**
     * This method close the connection of buffered writer
     * @param writer : {@link BufferedWriter} object
     * @throws Exception : throws exception if error occurs
     */
    public static void closeFile(BufferedWriter writer) throws Exception{
       if(writer != null) {
		   writer.flush();
		   writer.close();
	   }
    }

    /**
     * This method is used to created headers for end point
     * @param headers : {@link List} headers
     * @return : {@link MultiValueMap} headers to use in request
     */
    public static MultiValueMap<String, String> createHeaders(List<String> headers) {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
        for(String header : headers){
            String[] splitHeaders = header.split(":");
            headersMap.add(splitHeaders[0].trim(),splitHeaders[1].trim());
            if(splitHeaders[0].trim().toLowerCase().contains("content-type")) {
              if (splitHeaders[1].trim().toLowerCase().contains("text/plain")) {
                    headersMap.add("Accept", "text/plain");
              }if (splitHeaders[1].trim().toLowerCase().contains("application/xml")) {
                    headersMap.add("Accept", "application/xml");
              }if (splitHeaders[1].trim().toLowerCase().contains("application/json")) {
                    headersMap.add("Accept", "application/json");
              }
            }
        }

        return headersMap;
    }

    /**
     * This method is used to build HTTP Headers
     * @param headersList : Configured headers in end point
     * @return Headers[]
     */
    public static Header[] getHeaders(List<String> headersList){
        MultiValueMap<String, String> headersMvM = createHeaders(headersList);
        Header[] headers = new Header[headersMvM.size()];
        Header header = null;
        int i = 0;
        for(String key : headersMvM.keySet()){
            header = new BasicHeader(key, (String) headersMvM.get(key).get(0));
            headers[i] = header;
            i++;
        }
        return headers;
    }

    /**
     * This method will replace URL without https or https://domain name/example
     * @param url  : URL with domain name
     * @return : returns URL without domain name and http/https
     * @throws Exception : throws Exception if error occurs
     */
    public static String replaceUrl(String url) throws Exception{
        url = url.contains("http") ? url.replaceAll("http://","") : url;
        if(url.contains("https")){
            url = url.contains("https") ? url.replaceAll("https://","") : "";
        }
        if(StringUtils.isEmpty(url)){
            throw new Exception("Invalid URL");
        }
        url = url.substring(url.indexOf("/"), url.length());
        return url.trim();
    }

    /**
     * This method us used to append configured request parameters
     * @param url : actual URL
     * @param requestParameters : configured request parameters
     * @return appended URL with request parameters
     * @throws Exception : throws exception if operation fails
     */
    public static String appendEncodedRequestParameters(String url, Map<String, String> requestParameters) throws Exception {
        if (requestParameters.size() > 0) {
            url = url.concat("?");
            for (String key : requestParameters.keySet()) {
                String value = URLEncoder.encode(requestParameters.get(key), "UTF-8");
                url = url.concat(key).concat("=").concat(value).concat("&");
            }
            if (url.lastIndexOf("&") > -1) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }
    
	/**
	 * This method parse the end point from EndPoints.properties file
	 * 
	 * @param endPoint : Configured end point
	 * @return return the key-value pair of configured end point
	 * @throws Exception : throws exception if error occurs
	 */
	public static Map<String, String> mapEndPointParameters(EndPointConfig endPoint) throws Exception {
		Map<String, String> parameterMap = new HashMap<String, String>();

		parameterMap.put("methodType", endPoint.getMethodType());
		parameterMap.put("url", endPoint.getUrl());
		parameterMap.put("port", endPoint.getPort());
		String requestHeaders = endPoint.getRequestHeaders();
		if (StringUtils.isNotEmpty(requestHeaders)) {
			parameterMap.put("headersString", requestHeaders.trim());
		}
		if (StringUtils.isNotEmpty(endPoint.getRequestBody())) {
			parameterMap.put("data", replacePlaceHolderWithDate(endPoint.getRequestBody()));
		}
		parameterMap.put("expectedResponse", endPoint.getExpectedResponse());
		parameterMap.put("expectedResponseCode", endPoint.getExpectedResponseCode());
		return parameterMap;
	}
	
	/**
	 * This method replace the "yyyy-MM-dd" with current date. If more than one format found then increment by 1 day from current date and replace it with value.
	 * 
	 * @param request : request body or request parameter
	 * 
	 * @return replaced format with actual value of date
	 */
	public static String replacePlaceHolderWithDate(String request) {
		StringBuffer sb = new StringBuffer();
		if (request.contains(DATE_PLACE_HOLDER)) {
			String dynamicDate = "";
			String body = request;
			Pattern p = Pattern.compile(DATE_PLACE_HOLDER);
			Matcher m = p.matcher(body);
			Calendar c = Calendar.getInstance();
			dynamicDate = DateFormatUtils.format(c, "yyyy-MM-dd");
			while (m.find()) {
				m.appendReplacement(sb, dynamicDate);
				c.add(Calendar.DATE, 1);
				dynamicDate = DateFormatUtils.format(c, "yyyy-MM-dd");
			}
			m.appendTail(sb);
			return sb.toString();
		}
		return request;
	}
    
}
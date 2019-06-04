package com.estt.tool.test.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import com.estt.tool.test.custom.CustomHttpDelete;
import com.estt.tool.test.domain.Response;
import com.estt.tool.test.utility.Utility;

/**
 * Created by saurabh.yagnik on 2016/10/19.
 */
@Service
public class ConsumeEndPointLogic {

	/**
	 * This method is used to consume end point
	 * 
	 * @param endPointConfigParameters : configured end point parameters
	 * @param requestParameters        : configured request parameters for URL
	 * @param headersList              : configured HTTP header list
	 * @param requestBody              : configured request body
	 * @return returns {@link Response} object with HTTP response status code and
	 *         body
	 * @throws Exception : throws exception if operation fails
	 */
	public Response consume(Map<String, String> endPointConfigParameters, Map<String, String> requestParameters,
			List<String> headersList, String requestBody) throws Exception {
		if ("GET".equalsIgnoreCase(endPointConfigParameters.get("methodType"))) {
			return consumeGetEndPoint(endPointConfigParameters, requestParameters, headersList);
		} else if ("POST".equalsIgnoreCase(endPointConfigParameters.get("methodType"))) {
			return consumePostEndPoint(endPointConfigParameters, requestParameters, headersList, requestBody);
		} else if ("DELETE".equalsIgnoreCase(endPointConfigParameters.get("methodType"))) {
			return consumeDeleteEndPoint(endPointConfigParameters, requestParameters, headersList, requestBody);
		} else if ("PUT".equalsIgnoreCase(endPointConfigParameters.get("methodType"))) {
			return consumePutEndPoint(endPointConfigParameters, requestParameters, headersList, requestBody);
		}
		return null;
	}

	/**
	 * This method is used to consume HTTP GET method
	 * 
	 * @param endPointConfigParameters : HTTP GET method end point configuration
	 *                                 parameters
	 * @param requestParameters        : configured request parameters
	 * @param headersList              : HTTP Header list
	 * @return : Returns {@link Response} object with HTTP Response status and body
	 * @throws Exception : throws exception if operation fails
	 */
	private Response consumeGetEndPoint(Map<String, String> endPointConfigParameters,
			Map<String, String> requestParameters, List<String> headersList) throws Exception {
		Response response = new Response();
		boolean pathVariableURL = false;
		String url = endPointConfigParameters.get("url");
		if (StringUtils.isEmpty(url)) {
			throw new Exception("End point URL to call is empty");
		}
		try {
			if (url.contains("{") && url.contains("}")) {
				pathVariableURL = true;
			}
			if (pathVariableURL) {
				url = replacePathVariableWithValue(url, requestParameters);
			} else {
				url = Utility.appendEncodedRequestParameters(url, requestParameters);
			}

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			if (null != headersList && !headersList.isEmpty()) {
				for (String header : headersList) {
					String[] splitHeaders = header.split(":");
					request.addHeader(splitHeaders[0].trim(), splitHeaders[1].trim());
				}
			}
			HttpResponse httpResponse = httpClient.execute(request);
			response = readHttpResponse(httpResponse);
		} catch (Exception ex) {
			throw new Exception(ex.getCause());
		}
		return response;
	}

	/**
	 * This method is used to consume HTTP POST method
	 * 
	 * @param endPointConfigParameters : HTTP POST method end point configuration
	 *                                 parameters
	 * @param requestParameters        : configured request parameters
	 * @param headersList              : HTTP Header list
	 * @param requestBody              : configured request body
	 * @return : Returns {@link Response} object with HTTP Response status and body
	 * @throws Exception : throws exception if operation fails
	 */
	private Response consumePostEndPoint(Map<String, String> endPointConfigParameters,
			Map<String, String> requestParameters, List<String> headersList, String requestBody) throws Exception {
		Response response = new Response();
		String url = endPointConfigParameters.get("url");
		if (StringUtils.isEmpty(url)) {
			throw new Exception("End point URL to call is empty");
		}
		try {
			url = Utility.appendEncodedRequestParameters(url, requestParameters);
			HttpPost postRequest = new HttpPost(url);
			postRequest.setHeaders(Utility.getHeaders(headersList));
			StringEntity body = new StringEntity(StringUtils.isNotEmpty(requestBody) ? requestBody : "");
			postRequest.setEntity(body);

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResponse = httpClient.execute(postRequest);
			response = readHttpResponse(httpResponse);
		} catch (Exception ex) {
			throw new Exception(ex.getCause());
		}
		return response;
	}

	/**
	 * This method is used to consume HTTP DELETE method
	 * 
	 * @param endPointConfigParameters : HTTP DELETE method end point configuration
	 *                                 parameters
	 * @param requestParameters        : configured request parameters
	 * @param headersList              : HTTP Header list
	 * @param requestBody              : configured request body
	 * @return : Returns {@link Response} object with HTTP Response status and body
	 * @throws Exception : throws exception if operation fails
	 */
	private Response consumeDeleteEndPoint(Map<String, String> endPointConfigParameters,
			Map<String, String> requestParameters, List<String> headersList, String requestBody) throws Exception {
		Response response = new Response();
		boolean pathVariableURL = false;
		HttpResponse httpResponse = null;
		String url = endPointConfigParameters.get("url");
		if (StringUtils.isEmpty(url)) {
			throw new Exception("End point URL to call is empty");
		}
		try {
			if (endPointConfigParameters.get("url").contains("{")
					&& endPointConfigParameters.get("url").contains("}")) {
				pathVariableURL = true;
			}
			if (pathVariableURL) {
				url = replacePathVariableWithValue(url, requestParameters);
			} else {
				url = Utility.appendEncodedRequestParameters(url, requestParameters);
			}
			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.setHeaders(Utility.getHeaders(headersList));
			HttpClient httpClient = HttpClientBuilder.create().build();
			if (!pathVariableURL && StringUtils.isNotEmpty(requestBody)) {
				CustomHttpDelete customHttpDelete = new CustomHttpDelete(url);
				customHttpDelete.setHeaders(Utility.getHeaders(headersList));
				StringEntity body = new StringEntity(StringUtils.isNotEmpty(requestBody) ? requestBody : "");
				customHttpDelete.setEntity(body);
				httpResponse = httpClient.execute(customHttpDelete);
			} else {
				httpResponse = httpClient.execute(httpDelete);
			}
			response = readHttpResponse(httpResponse);
		} catch (Exception ex) {
			throw new Exception(ex.getCause());
		}
		return response;
	}

	/**
	 * This method is used to consume HTTP PUT method
	 * 
	 * @param endPointConfigParameters : HTTP PUT method end point configuration
	 *                                 parameters
	 * @param requestParameters        : configured request parameters
	 * @param headersList              : HTTP Header list
	 * @param requestBody              : configured request body
	 * @return : Returns {@link Response} object with HTTP Response status and body
	 * @throws Exception : throws exception if operation fails
	 */
	private Response consumePutEndPoint(Map<String, String> endPointConfigParameters,
			Map<String, String> requestParameters, List<String> headersList, String requestBody) throws Exception {
		Response response = new Response();
		String url = endPointConfigParameters.get("url");
		if (StringUtils.isEmpty(url)) {
			throw new Exception("End point URL to call is empty");
		}
		try {
			url = Utility.appendEncodedRequestParameters(url, requestParameters);
			HttpPut putRequest = new HttpPut(url);
			putRequest.setHeaders(Utility.getHeaders(headersList));
			StringEntity body = new StringEntity(StringUtils.isNotEmpty(requestBody) ? requestBody : "");
			putRequest.setEntity(body);

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResponse = httpClient.execute(putRequest);
			response = readHttpResponse(httpResponse);
		} catch (Exception ex) {
			throw new Exception(ex.getCause());
		}
		return response;
	}

	/**
	 * This method is used to render {@link Response} object with status code and
	 * response body
	 * 
	 * @param httpResponse : {@link HttpResponse} of httpClient
	 * @return : returns {@link Response} object with status code and response body
	 * @throws Exception : throws exception if operation fails
	 */
	private Response readHttpResponse(HttpResponse httpResponse) throws Exception {
		Response response = new Response();
		Header[] headerArr = httpResponse.getAllHeaders();
		for (Header h : headerArr) {
			if ("content-type".equalsIgnoreCase(h.getName())) {
				response.setResponseHeader(h.getValue());
				break;
			}
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
		String output = "";
		while ((output = br.readLine()) != null) {
			response.setResponseBody(output);
		}
		response.setStatusCode(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
		return response;
	}

	/**
	 * This method is used to replace path variables with actual value in URL
	 * 
	 * @param url               : URL with {path_variable}
	 * @param requestParameters : request parameters to be replace with path
	 *                          variables
	 * @return Returns the replaced URL
	 */
	private String replacePathVariableWithValue(String url, Map<String, String> requestParameters) {
		StringBuilder sbUrl = new StringBuilder();
		String[] https = url.split("//");
		sbUrl.append(https[0]).append("/");
		String[] paths = https[1].split("/");
		for (String path : paths) {
			if (path.contains("{") && path.contains("}")) {
				String pathValue = path.substring(path.indexOf("{") + 1, path.length() - 1);
				sbUrl.append("/").append(requestParameters.get(pathValue));
			} else {
				sbUrl.append("/").append(path);
			}
		}
		return sbUrl.toString();
	}
}

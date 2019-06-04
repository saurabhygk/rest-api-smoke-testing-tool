package com.estt.tool.test.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by saurabh.yagnik on 2016/10/24.
 */
public class EndPointConfig implements Comparable<EndPointConfig> {

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private Long sequence;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String methodType;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String url;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String requestHeaders;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String requestBody;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String requestParam;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String port;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String expectedResponse;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String expectedResponseCode;

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getExpectedResponse() {
		return expectedResponse;
	}

	public void setExpectedResponse(String expectedResponse) {
		this.expectedResponse = expectedResponse;
	}

	public String getExpectedResponseCode() {
		return expectedResponseCode;
	}

	public void setExpectedResponseCode(String expectedResponseCode) {
		this.expectedResponseCode = expectedResponseCode;
	}

	@Override
	public int compareTo(EndPointConfig endPointConfig) {
		if (getSequence() == null || endPointConfig.getSequence() == null) {
			return 0;
		}
		return getSequence().compareTo(endPointConfig.getSequence());
	}

}
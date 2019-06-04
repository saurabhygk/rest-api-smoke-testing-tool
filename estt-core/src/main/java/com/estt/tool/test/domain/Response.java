package com.estt.tool.test.domain;

/**
 * Created by saurabh.yagnik on 2016/10/19.
 */
public class Response {
	private String statusCode;
	private String responseBody;
	private String responseHeader;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(String responseHeader) {
		this.responseHeader = responseHeader;
	}

}

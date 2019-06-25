package com.estt.tool.test.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by saurabh.yagnik on 2016/10/17.
 */
@Component
public class ResponseValidator {

	public String validateResponse(String responseBody, List<String> errorCodes, String expectedResponse,
			String contentType) throws Exception {

		if (StringUtils.isEmpty(responseBody) || StringUtils.isEmpty(expectedResponse)) {
			return null;
		}

		String body = "";
		if (contentType.toLowerCase().contains("xml")) {
			body = responseBody.replaceAll("\"", "");
		} else {
			body = StringUtils.isNotEmpty(responseBody) ? responseBody.replace("\"", "\\\"") : "";
		}

		if (body.toLowerCase().contains("error")) {
			for (String errorCode : errorCodes) {
				if (body.toLowerCase().contains(errorCode.toLowerCase())) {
					return errorCode;
				}
			}
		}

		if (StringUtils.isNotEmpty(expectedResponse)) {
			if (body.contains(expectedResponse)) {
				return "VALID";
			}
		}
		return "INVALID";
	}
}

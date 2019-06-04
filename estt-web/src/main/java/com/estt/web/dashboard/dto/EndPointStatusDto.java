package com.estt.web.dashboard.dto;

public class EndPointStatusDto {

	private String url;

	private String status;

	private String error;

	public EndPointStatusDto(String url, String status, String error) {
		this.url = url;
		this.status = status;
		this.error = error;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}

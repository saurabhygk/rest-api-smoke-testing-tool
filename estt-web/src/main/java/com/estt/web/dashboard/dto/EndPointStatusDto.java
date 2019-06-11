package com.estt.web.dashboard.dto;

public class EndPointStatusDto {

	private String url;

	private String status;

	private String description;

	public EndPointStatusDto(String url, String status, String description) {
		this.url = url;
		this.status = status;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

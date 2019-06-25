package com.estt.tool.test.exception;

/**
 * Created by saurabh.yagnik on 2016/10/14.
 */
public class EndPointSmokeTestException extends Exception {

	private static final long serialVersionUID = 1L;

	public EndPointSmokeTestException() {
		super();
	}

	public EndPointSmokeTestException(String message) {
		super(message);
	}

	public EndPointSmokeTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public EndPointSmokeTestException(Throwable cause) {
		super(cause);
	}

	protected EndPointSmokeTestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

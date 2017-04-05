package com.zp.sdcc.exceptions;

import static com.zp.sdcc.common.CurrencyConverterConstants.UNAVAILABLE;

public class ExternalServiceNotRespondingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3159011240160233998L;

	public ExternalServiceNotRespondingException(String serviceName) {
		super(new StringBuilder(serviceName).append(UNAVAILABLE).toString());
	}

	public String toString() {
		return "External Service error. Please try again.";
	}

}

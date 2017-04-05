package com.zp.sdcc.services;

import java.math.BigDecimal;

import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;

/**
 * @author AKC
 * Common Interface to be implemented by all Currency Service Adapters
 */
public interface ExternalCurrencyServiceAdapter {

	public BigDecimal getCurrencyExchangeRate(String[] params)
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException;

}

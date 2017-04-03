package com.zp.sdcc.services;

import java.math.BigDecimal;

import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;

public interface ExternalCurrencyServiceAdapter {

	public BigDecimal getCurrencyExchangeRate(String[] params) throws ConversionRateNotFoundException, ExternalServiceNotRespondingException;

}

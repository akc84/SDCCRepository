package com.zp.sdcc.services;

import static com.zp.sdcc.common.CurrencyConverterUtil.getFormattedDate;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zp.sdcc.entities.CurrencyConversionVO;
import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;
/**
 * @author AKC
 * Delegates call to ExternalCurrencyServiceAdapter to get exchangeRate.
 * Performs conversion, audit and returns result.
 */
@Component
public class CurrencyConversionDelegatorService {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionDelegatorService.class);

	ExternalCurrencyServiceAdapter externalService;

	UserService userService;

	AuditHistoryService auditHistoryService;

	public CurrencyConversionDelegatorService(ExternalCurrencyServiceAdapter externalService, UserService userService,
			AuditHistoryService auditHistoryService) {
		super();
		this.externalService = externalService;
		this.userService = userService;
		this.auditHistoryService = auditHistoryService;
	}

	public CurrencyConversionVO performCurrencyConversion(CurrencyConversionVO conversionInfo)
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {

		BigDecimal exchangeRate = areSourceTargetCurrencySame(conversionInfo) ? BigDecimal.ONE
				: externalService.getCurrencyExchangeRate(getParameters(conversionInfo));

		logger.debug("ExchangeRate::" + exchangeRate.toString());
		deriveConvertedAmount(conversionInfo, exchangeRate);
		performAudit(conversionInfo);
		return conversionInfo;
	}

	private void deriveConvertedAmount(CurrencyConversionVO conversionInfo, BigDecimal exchangeRate) {
		BigDecimal convertedAmount = conversionInfo.getAmountToConvert().multiply(exchangeRate);
		conversionInfo.setConvertedAmount(convertedAmount);
	}

	private void performAudit(CurrencyConversionVO conversionInfo) {
		String auditString = conversionInfo.getAuditString();
		auditHistoryService.addAuditEntry(userService.findLoggedInUsername(), auditString);
	}

	private boolean areSourceTargetCurrencySame(CurrencyConversionVO conversionInfo) {
		return conversionInfo.getSourceCurrency().equalsIgnoreCase(conversionInfo.getTargetCurrency());
	}

	private String[] getParameters(CurrencyConversionVO conversionInfo) {
		return new String[] { getFormattedDate(conversionInfo.getConversionDate()), conversionInfo.getSourceCurrency(),
				conversionInfo.getTargetCurrency() };
	}
}

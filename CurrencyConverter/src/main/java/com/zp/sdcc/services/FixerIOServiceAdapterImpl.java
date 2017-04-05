package com.zp.sdcc.services;

import static com.zp.sdcc.common.CurrencyConverterConstants.SUPPORTED_DATE_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;

/**
 * @author AKC
 * Adapter for fixer.io foreign exchange rate Service
 */
@Service
public class FixerIOServiceAdapterImpl implements ExternalCurrencyServiceAdapter {

	private static final String JSON_RATE_KEY = "rates";

	private static final String MIN_SUPPORTED_DATE = "2000-01-01";

	private static final Logger logger = LoggerFactory.getLogger(FixerIOServiceAdapterImpl.class);

	private static final String URI = "http://api.fixer.io/{date}?base={from}&symbols={to}";

	private RestTemplate restTemplate;

	public FixerIOServiceAdapterImpl(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
		setTimeOutForRestTemplate(); // TODO Externalize this
	}

	@Cacheable("exchangeRate")
	public BigDecimal getCurrencyExchangeRate(String[] params)
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		validateRequestParams(params);
		String response = invokeExternalService(params);
		return deriveExchangeRateFromResponse(params, response);
	}

	private String invokeExternalService(String[] params) throws ExternalServiceNotRespondingException {
		logger.info("Invoking api.fixer.io with parameters::" + Arrays.toString(params));
		try {
			String response = restTemplate.getForObject(URI, String.class, params[0], params[1], params[2]);
			logger.info("Received response from api.fixer.io");
			return response;
		} catch (Exception e) {
			logger.error("Exception occured while calling api.fixer.io ::", e);
			throw new ExternalServiceNotRespondingException("api.fixer.io");
		}
	}

	private BigDecimal deriveExchangeRateFromResponse(String[] params, String response)
			throws ConversionRateNotFoundException {
		JSONObject rate = new JSONObject(response).getJSONObject(JSON_RATE_KEY);

		if (rate.has(params[2]))
			return new BigDecimal(rate.get(params[2]).toString());
		else
			throw new ConversionRateNotFoundException(params);
	}

	private void validateRequestParams(String[] params) throws ConversionRateNotFoundException {
		validateRequestDate(params[0]);
	}

	private void validateRequestDate(String requestedDate) throws ConversionRateNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat(SUPPORTED_DATE_FORMAT);
		try {
			if (sdf.parse(requestedDate).before(sdf.parse(MIN_SUPPORTED_DATE)))
				throw new ConversionRateNotFoundException(
						"External Service does not support queries before " + MIN_SUPPORTED_DATE);
		} catch (ParseException e) {
			throw new ConversionRateNotFoundException("Invalid Date Format");
		}
	}

	private void setTimeOutForRestTemplate() {
		if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
			HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			rf.setReadTimeout(15 * 1000);
			rf.setConnectTimeout(15 * 1000);
		}
	}

}

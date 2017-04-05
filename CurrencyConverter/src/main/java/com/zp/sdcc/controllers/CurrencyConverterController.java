package com.zp.sdcc.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.*;
import static com.zp.sdcc.common.CurrencyConverterConstants.CURRENCY_CONVERTER_RESPONSE_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.ROOT_MAPPING;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.zp.sdcc.entities.CurrencyConversionVO;
import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;
import com.zp.sdcc.services.AuditHistoryService;
import com.zp.sdcc.services.CurrencyConversionDelegatorService;
import com.zp.sdcc.services.UserService;

/**
 * @author AKC
 * Controller to handle get and post requests to /currencyConverter.
 */
@Controller
public class CurrencyConverterController {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterController.class);

	@Autowired
	CurrencyConversionDelegatorService currencyServiceDelegator;

	@Autowired
	UserService userService;

	@Autowired
	AuditHistoryService auditHistoryService;

	@Value("#{'${currency.list}'.split(',')}")
	private List<String> supportedCurrencies;

	@GetMapping(value = { ROOT_MAPPING, CURRENCY_CONVERTER_REQUEST_MAPPING })
	public String currencyConverterMapping(ModelMap model) {
		return defaultHandler(model);
	}

	@PostMapping(value = CURRENCY_CONVERTER_REQUEST_MAPPING)
	public String handleCurrencyConversionRequest(
			@ModelAttribute(CURRENCY_CONVERTER_FORM) @Valid CurrencyConversionVO currencyConverterForm,
			BindingResult bindingResult, ModelMap model) {
		
		if (!bindingResult.hasErrors()) {
			performCurrencyConversion(currencyConverterForm, model);
		}
		return defaultHandler(model);
	}

	private void performCurrencyConversion(CurrencyConversionVO conversionInfo, ModelMap model) {
		try {
			CurrencyConversionVO result = currencyServiceDelegator.performCurrencyConversion(conversionInfo);
			model.put(RESULT, result);
		} catch (ConversionRateNotFoundException | ExternalServiceNotRespondingException e) {
			model.put(ERROR, e.toString());
			logger.error("Exception caught::", e.getMessage());
		}
	}

	private String defaultHandler(ModelMap model) {
		populateModel(model);
		return CURRENCY_CONVERTER_RESPONSE_MAPPING;
	}

	private CurrencyConversionVO getDefaultValuesForForm() {
		return new CurrencyConversionVO(EUR, new BigDecimal(1.00), GBP, null);
	}

	private String getFormattedQueryHistory(String loggedInUser) {
		return auditHistoryService.getAuditHistoryForUser(loggedInUser).getFormattedString();
	}

	private void populateModel(ModelMap model) {
		model.putIfAbsent(CURRENCY_CONVERTER_FORM, getDefaultValuesForForm());
		String loggedInUser = userService.findLoggedInUsername();
		model.put(USERNAME, loggedInUser);
		model.addAttribute(SUPPORTED_CURRENCIES, supportedCurrencies);
		model.addAttribute(QUERY_HISTORY, getFormattedQueryHistory(loggedInUser));
	}

}

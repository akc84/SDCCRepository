package com.zp.sdcc.test.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.CURRENCY_CONVERTER_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.CURRENCY_CONVERTER_RESPONSE_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.EUR;
import static com.zp.sdcc.common.CurrencyConverterConstants.GBP;
import static com.zp.sdcc.test.TestConstants.BLANK_STRING;
import static com.zp.sdcc.test.TestConstants.CURRENCY_CONVERTER_FORM;
import static com.zp.sdcc.test.TestConstants.DEFAULT_USER;
import static com.zp.sdcc.test.TestConstants.ERROR_DIGITS;
import static com.zp.sdcc.test.TestConstants.ERROR_NOT_BLANK;
import static com.zp.sdcc.test.TestConstants.ERROR_NOT_NULL;
import static com.zp.sdcc.test.TestConstants.ERROR_SIZE;
import static com.zp.sdcc.test.TestConstants.ERROR_TYPE_MISMATCH;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_AMOUNT_TO_CONVERT;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_COVERSION_DATE;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_SOURCE_CURRENCY;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_TARGET_CURRENCY;
import static com.zp.sdcc.test.TestConstants.WEB_INF_CURRENCY_CONVERTER_JSP;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zp.sdcc.controllers.CurrencyConverterController;
import com.zp.sdcc.entities.AuditHistory;
import com.zp.sdcc.entities.CurrencyConversionVO;
import com.zp.sdcc.services.AuditHistoryService;
import com.zp.sdcc.services.CurrencyConversionDelegatorService;
import com.zp.sdcc.services.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CurrencyConverterController.class, includeFilters = @Filter(classes = EnableWebSecurity.class))
public class CurrencyCoverterControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CurrencyConversionDelegatorService currencyServiceDelegator;

	@MockBean
	UserService userService;

	@MockBean
	AuditHistoryService auditHistoryService;

	@MockBean
	private UserDetailsService userDetailsService;

	private MultiValueMap<String, String> requestParameters;

	@Before
	public void setup() {
		requestParameters = populateRequestParameters();
		mockAuditHistory();
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertNull_ValidationErrorOnAmountToConvert() throws Exception {
		// Arrange
		requestParameters.remove(FORM_FIELD_AMOUNT_TO_CONVERT);

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT,
				ERROR_NOT_NULL));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertNegativeNumber_ValidationErrorOnAmountToConvert() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList("-1"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT,
				"DecimalMin"));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertLargerThan11Digits_ValidationErrorOnAmountToConvert() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList("123456789012"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT,
				ERROR_DIGITS));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertMoreThan2DecimalDigits_ValidationErrorOnAmountToConvert() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList("123456789.012"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT,
				ERROR_DIGITS));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertContainsInvalidCharacters_ValidationErrorOnAmountToConvert()
			throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList("12asdsad.012"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT,
				ERROR_TYPE_MISMATCH));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_AmountToConvertBlankString_ValidationErrorOnAmountToConvert() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList(BLANK_STRING));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrors(CURRENCY_CONVERTER_FORM, FORM_FIELD_AMOUNT_TO_CONVERT));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_SourceCurrencyBlankString_ValidationErrorOnSourceCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_SOURCE_CURRENCY, Arrays.asList(BLANK_STRING));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_SOURCE_CURRENCY,
				ERROR_NOT_BLANK));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_SourceCurrencyLessThan3Characters_ValidationErrorOnSourceCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_SOURCE_CURRENCY, Arrays.asList("EU"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(
				model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_SOURCE_CURRENCY, ERROR_SIZE));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_SourceCurrencyMoreThan3Characters_ValidationErrorOnSourceCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_SOURCE_CURRENCY, Arrays.asList("EURO"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(
				model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_SOURCE_CURRENCY, ERROR_SIZE));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_SourceCurrencyNull_ValidationErrorOnSourceCurrency() throws Exception {
		// Arrange
		requestParameters.remove(FORM_FIELD_SOURCE_CURRENCY);

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_SOURCE_CURRENCY,
				ERROR_NOT_BLANK));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_TargetCurrencyNull_ValidationErrorOnTargetCurrency() throws Exception {
		// Arrange
		requestParameters.remove(FORM_FIELD_TARGET_CURRENCY);

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_TARGET_CURRENCY,
				ERROR_NOT_BLANK));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_TargetCurrencyBlankString_ValidationErrorOnTargetCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_TARGET_CURRENCY, Arrays.asList(BLANK_STRING));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_TARGET_CURRENCY,
				ERROR_NOT_BLANK));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_TargetCurrencyLessThan3Characters_ValidationErrorOnTargetCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_TARGET_CURRENCY, Arrays.asList("EU"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(
				model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_TARGET_CURRENCY, ERROR_SIZE));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_TargetCurrencyMoreThan3Characters_ValidationErrorOnTargetCurrency() throws Exception {
		// Arrange
		requestParameters.put(FORM_FIELD_TARGET_CURRENCY, Arrays.asList("EURO"));

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(
				model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, FORM_FIELD_TARGET_CURRENCY, ERROR_SIZE));
	}


	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void getRequest_NoParamters_ForwardsToCurrencyConverterPageNoErrors() throws Exception {
		// Arrange

		// Act
		ResultActions result = mockMvc.perform(get(CURRENCY_CONVERTER_REQUEST_MAPPING));

		// Assert
		result.andExpect(status().isOk()).andExpect(model().attributeExists(CURRENCY_CONVERTER_FORM))
				.andExpect(model().hasNoErrors()).andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
				.andExpect(handler().handlerType(CurrencyConverterController.class))
				.andExpect(handler().methodName("currencyConverterMapping"))
				.andExpect(forwardedUrl(WEB_INF_CURRENCY_CONVERTER_JSP));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_NoParamters_ForwardsToCurrencyConverterPageWithErrors() throws Exception {
		// Arrange

		// Act
		ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING));

		// Assert
		result.andExpect(status().isOk()).andExpect(model().attributeExists(CURRENCY_CONVERTER_FORM))
				.andExpect(model().hasErrors()).andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
				.andExpect(handler().handlerType(CurrencyConverterController.class))
				.andExpect(handler().methodName("handleCurrencyConversionRequest"))
				.andExpect(forwardedUrl(WEB_INF_CURRENCY_CONVERTER_JSP));
	}

	@Test
	@WithMockUser(username = DEFAULT_USER)
	public void postRequest_ValidParamters_ReturnsResult() throws Exception {
		// Arrange
		when(currencyServiceDelegator.performCurrencyConversion(Matchers.any(CurrencyConversionVO.class)))
				.thenAnswer((InvocationOnMock invocation) -> (CurrencyConversionVO) invocation.getArguments()[0]);

		// Act
		ResultActions result = postCurrencyConverterForm();

		// Assert
		result.andExpect(status().isOk()).andExpect(model().attributeExists("result")).andExpect(model().hasNoErrors())
				.andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
				.andExpect(handler().handlerType(CurrencyConverterController.class))
				.andExpect(handler().methodName("handleCurrencyConversionRequest"))
				.andExpect(forwardedUrl(WEB_INF_CURRENCY_CONVERTER_JSP));
	}

	private ResultActions postCurrencyConverterForm() throws Exception {
		ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).params(requestParameters));
		return result;
	}

	private void mockAuditHistory() {
		AuditHistory history = mock(AuditHistory.class);
		when(history.getFormattedString()).thenReturn(anyString());
		when(auditHistoryService.getAuditHistoryForUser(DEFAULT_USER)).thenReturn(history);
	}

	private MultiValueMap<String, String> populateRequestParameters() {
		MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT, Arrays.asList("1232.12"));
		requestParameters.put(FORM_FIELD_SOURCE_CURRENCY, Arrays.asList(EUR));
		requestParameters.put(FORM_FIELD_TARGET_CURRENCY, Arrays.asList(GBP));
		requestParameters.put(FORM_FIELD_COVERSION_DATE, Arrays.asList("2010-01-10"));

		return requestParameters;
	}
}

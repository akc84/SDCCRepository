package com.zp.sdcc.test.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.*;
import static com.zp.sdcc.common.CurrencyConverterConstants.CURRENCY_CONVERTER_RESPONSE_MAPPING;
import static com.zp.sdcc.test.TestConstants.BLANK_STRING;
import static com.zp.sdcc.test.TestConstants.CURRENCY_CONVERTER_FORM;
import static com.zp.sdcc.test.TestConstants.DEFAULT_USER;
import static com.zp.sdcc.test.TestConstants.ERROR_DIGITS;
import static com.zp.sdcc.test.TestConstants.ERROR_NOT_BLANK;
import static com.zp.sdcc.test.TestConstants.ERROR_NOT_NULL;
import static com.zp.sdcc.test.TestConstants.ERROR_SIZE;
import static com.zp.sdcc.test.TestConstants.ERROR_TYPE_MISMATCH;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_AMOUNT_TO_CONVERT;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_SOURCE_CURRENCY;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_TARGET_CURRENCY;
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
	
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertNull_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.remove(FORM_FIELD_AMOUNT_TO_CONVERT);
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_AMOUNT_TO_CONVERT, 
    														ERROR_NOT_NULL));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertNegativeNumber_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList("-1"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_AMOUNT_TO_CONVERT, 
    														"DecimalMin"));
    }    
    
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertLargerThan11Digits_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList("123456789012"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_AMOUNT_TO_CONVERT, 
    														ERROR_DIGITS));
    }    

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertMoreThan2DecimalDigits_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList("123456789.012"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_AMOUNT_TO_CONVERT, 
    														ERROR_DIGITS));
    }      

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertContainsInvalidCharacters_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList("12asdsad.012"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_AMOUNT_TO_CONVERT, 
    														ERROR_TYPE_MISMATCH));
    }       

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_AmountToConvertBlankString_ValidationErrorOnAmountToConvert() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList(BLANK_STRING));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(CURRENCY_CONVERTER_FORM, 
    													 FORM_FIELD_AMOUNT_TO_CONVERT));
    } 

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_SourceCurrencyBlankString_ValidationErrorOnSourceCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_SOURCE_CURRENCY,Arrays.asList(BLANK_STRING));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_SOURCE_CURRENCY, 
    														ERROR_NOT_BLANK));
    } 

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_SourceCurrencyLessThan3Characters_ValidationErrorOnSourceCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_SOURCE_CURRENCY,Arrays.asList("EU"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_SOURCE_CURRENCY, 
    														ERROR_SIZE));
    }

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_SourceCurrencyMoreThan3Characters_ValidationErrorOnSourceCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_SOURCE_CURRENCY,Arrays.asList("EURO"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_SOURCE_CURRENCY, 
    														ERROR_SIZE));
    }   
    
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_SourceCurrencyNull_ValidationErrorOnSourceCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.remove(FORM_FIELD_SOURCE_CURRENCY);
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_SOURCE_CURRENCY, 
    														ERROR_NOT_BLANK));
    } 
 
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_TargetCurrencyNull_ValidationErrorOnTargetCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.remove(FORM_FIELD_TARGET_CURRENCY);
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_TARGET_CURRENCY, 
    														ERROR_NOT_BLANK));
    } 
 
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_TargetCurrencyBlankString_ValidationErrorOnTargetCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_TARGET_CURRENCY,Arrays.asList(BLANK_STRING));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_TARGET_CURRENCY, 
    														ERROR_NOT_BLANK));
    } 
  
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_TargetCurrencyLessThan3Characters_ValidationErrorOnTargetCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_TARGET_CURRENCY,Arrays.asList("EU"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_TARGET_CURRENCY, 
    														ERROR_SIZE));
    } 
    
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_TargetCurrencyMoreThan3Characters_ValidationErrorOnTargetCurrency() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	requestParameters.put(FORM_FIELD_TARGET_CURRENCY,Arrays.asList("EURO"));
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(CURRENCY_CONVERTER_FORM, 
    														FORM_FIELD_TARGET_CURRENCY, 
    														ERROR_SIZE));
    }     
    
 
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void getRegister_NoParamters_ForwardsToCurrencyConverterPageNoErrors() throws Exception
    {
    	//Arrange
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(get(CURRENCY_CONVERTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists(CURRENCY_CONVERTER_FORM))
    		  .andExpect(model().hasNoErrors())
    		  .andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(CurrencyConverterController.class))
    		  .andExpect(handler().methodName("currencyConversionMapping"))
    		  .andExpect(forwardedUrl("/WEB-INF/currencyConverter.jsp"));
    }     

    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_NoParamters_ForwardsToCurrencyConverterPageWithErrors() throws Exception
    {
    	//Arrange
    	mockAuditHistory();
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists(CURRENCY_CONVERTER_FORM))
    		  .andExpect(model().hasErrors())
    		  .andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(CurrencyConverterController.class))
    		  .andExpect(handler().methodName("handleCurrencyConversionRequest"))
    		  .andExpect(forwardedUrl("/WEB-INF/currencyConverter.jsp"));
    }       
    
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void postRegister_ValidParamters_ReturnsResult() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = populateRequestParameters();
    	mockAuditHistory();
    	when(currencyServiceDelegator.performCurrencyConversion(Matchers.any(CurrencyConversionVO.class)))
    	.thenAnswer((InvocationOnMock invocation) -> (CurrencyConversionVO)invocation.getArguments()[0]);
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(CURRENCY_CONVERTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists("result"))
    		  .andExpect(model().hasNoErrors())
    		  .andExpect(view().name(CURRENCY_CONVERTER_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(CurrencyConverterController.class))
    		  .andExpect(handler().methodName("handleCurrencyConversionRequest"))
    		  .andExpect(forwardedUrl("/WEB-INF/currencyConverter.jsp"));
    } 

    private void mockAuditHistory() {
		AuditHistory history = mock(AuditHistory.class);
    	when(history.getFormattedString()).thenReturn(anyString());
    	when(auditHistoryService.getAuditHistoryForUser(DEFAULT_USER)).thenReturn(history);
	}


	private MultiValueMap<String, String> populateRequestParameters() {
		MultiValueMap<String,String> requestParameters = new LinkedMultiValueMap<>();
		requestParameters.put(FORM_FIELD_AMOUNT_TO_CONVERT,Arrays.asList("1232.12"));
		requestParameters.put(FORM_FIELD_SOURCE_CURRENCY,Arrays.asList(EUR));
		requestParameters.put(FORM_FIELD_TARGET_CURRENCY, Arrays.asList(GBP));
		requestParameters.put("conversionDate",Arrays.asList("2010-01-10"));
		
		return requestParameters;
	}	
}

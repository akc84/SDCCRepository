package com.zp.sdcc.test.services;

import static com.zp.sdcc.common.CurrencyConverterUtil.getFormattedDate;
import static com.zp.sdcc.common.CurrencyConverterConstants.*;
import static com.zp.sdcc.test.TestConstants.DEFAULT_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.zp.sdcc.entities.CurrencyConversionVO;
import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;
import com.zp.sdcc.services.AuditHistoryService;
import com.zp.sdcc.services.CurrencyConversionDelegatorService;
import com.zp.sdcc.services.ExternalCurrencyServiceAdapter;
import com.zp.sdcc.services.FixerIOServiceAdapterImpl;
import com.zp.sdcc.services.UserService;

public class CurrencyConversionDelegatorServiceTest {

	ExternalCurrencyServiceAdapter externalService;	

	UserService userService;
	
	AuditHistoryService auditHistoryService;
	
	CurrencyConversionDelegatorService  conversionDelegator;
	
    @Before
    public void setUp(){
    	externalService = mock(FixerIOServiceAdapterImpl.class);
    	userService = mock(UserService.class );
    	auditHistoryService = mock(AuditHistoryService.class);
    	conversionDelegator = new CurrencyConversionDelegatorService(externalService, 
    																 userService,
    																 auditHistoryService);
    }
    
    @Test
    public void performCurrencyConversion_SourceAndTargetCurrencySame_ReturnsSameAmount() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
		//Arrange
		CurrencyConversionVO input = new CurrencyConversionVO(EUR,new BigDecimal(1.00),EUR,null);
		input.setConvertedAmount(null);
		
		//Act
		input = conversionDelegator.performCurrencyConversion(input);
		
		//Assert
		assertThat(input.getAmountToConvert()).isEqualTo(input.getConvertedAmount());
    }
 
    
    @Test
    public void performCurrencyConversion_SourceAndTargetCurrencyNotSame_ReturnsConvertedAmount() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
		//Arrange
    	BigDecimal amountToConvert = new BigDecimal(1.00);
		CurrencyConversionVO input = new CurrencyConversionVO(EUR,amountToConvert,USD,null);
		
		BigDecimal mockedExchangeRate = new BigDecimal(1.32);
		when(externalService.getCurrencyExchangeRate(anyObject())).thenReturn(mockedExchangeRate);
		
		//Act
		input = conversionDelegator.performCurrencyConversion(input);
		
		//Assert
		assertThat(input.getConvertedAmount()).isEqualTo(amountToConvert.multiply(mockedExchangeRate));
    }
 
    
    @Test
    public void performCurrencyConversion_SourceAndTargetCurrencyNotSame_SendsParametersToExternalService() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
		//Arrange
    	Date date = new Date();
		CurrencyConversionVO input = new CurrencyConversionVO(EUR,new BigDecimal(1.00),USD,date);
		
		when(externalService.getCurrencyExchangeRate(anyObject())).thenReturn(new BigDecimal(1.32));    	
		
		ArgumentCaptor<String[]> argCaptor = ArgumentCaptor.forClass(String[].class);

		
		//Act
		input = conversionDelegator.performCurrencyConversion(input);
		
		//Assert
		verify(externalService).getCurrencyExchangeRate(argCaptor.capture());
		assertThat(argCaptor.getValue()).isEqualTo(new String[]{getFormattedDate(date),EUR,USD});
    }

    
    @Test
    public void performCurrencyConversion_SourceAndTargetCurrencyNotSame_SendsParametersToAuditService() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
		//Arrange
		CurrencyConversionVO input = new CurrencyConversionVO(EUR,new BigDecimal(1.00),USD,null);
		
		when(externalService.getCurrencyExchangeRate(anyObject())).thenReturn(new BigDecimal(1.32));    	
		when(userService.findLoggedInUsername()).thenReturn(DEFAULT_USER);
		
		ArgumentCaptor<String> arg1Captor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> arg2Captor = ArgumentCaptor.forClass(String.class);
		
		//Act
		input = conversionDelegator.performCurrencyConversion(input);
		
		//Assert
		verify(auditHistoryService).addAuditEntry(arg1Captor.capture(), arg2Captor.capture());
		assertThat(arg1Captor.getValue()).isEqualTo(DEFAULT_USER); 
		assertThat(arg2Captor.getValue()).isEqualTo(input.getAuditString());
		
    }     
	
}

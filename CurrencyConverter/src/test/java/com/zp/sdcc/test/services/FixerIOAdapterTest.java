package com.zp.sdcc.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;
import com.zp.sdcc.services.ExternalCurrencyServiceAdapter;
import com.zp.sdcc.services.FixerIOServiceAdapterImpl;

@RunWith(SpringRunner.class)
@RestClientTest({FixerIOServiceAdapterImpl.class})
public class FixerIOAdapterTest {
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
       
    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ExternalCurrencyServiceAdapter externalServiceAdapter;
    

    @Test
    public void getCurrencyExchangeRate_ServerRespondsWithRate_ExchangeRateReturned() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
    	//Arrange
    	String[] params =new String[]{"2010-01-01","EUR","USD"};
    	this.server.expect(requestTo("http://api.fixer.io/"+params[0]+"?base="+params[1]+"&symbols="+params[2]))
    			   .andRespond(withSuccess().body("{\"base\":\"EUR\",\"date\":\"2009-12-31\",\"rates\":{\"USD\":1.4406}}"));
    	
    	//Act
    	BigDecimal exchangeRate = externalServiceAdapter.getCurrencyExchangeRate(params);
    	
    	//Assert
    	assertThat(exchangeRate).isEqualTo(new BigDecimal("1.4406"));   	
    	
    }
  
    @Test
    public void getCurrencyExchangeRate_ServerRespondsWithoutRate_ExchangeRateReturned() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
    	//Arrange
    	String[] params =new String[]{"2010-01-01","EUR","USD"};
    	this.server.expect(requestTo("http://api.fixer.io/"+params[0]+"?base="+params[1]+"&symbols="+params[2]))
    				.andRespond(withSuccess().body("{\"base\":\"EUR\",\"date\":\"2009-12-31\",\"rates\":{}}"));
    	
    	this.thrown.expect(ConversionRateNotFoundException.class);
    	
    	//Act
    	externalServiceAdapter.getCurrencyExchangeRate(params);
    	
    	//Assert
    	//exception thrown   	
    }    
 
    @Test
    public void getCurrencyExchangeRate_ServerRespondsWithException_ExchangeRateReturned() throws ConversionRateNotFoundException, ExternalServiceNotRespondingException
    {
    	//Arrange
    	String[] params =new String[]{"2010-01-01","EUR","USD"};
    	this.server.expect(requestTo("http://api.fixer.io/"+params[0]+"?base="+params[1]+"&symbols="+params[2]))
    				.andRespond(MockRestResponseCreators.withServerError());
    	this.thrown.expect(ExternalServiceNotRespondingException.class);
    	
    	//Act
    	externalServiceAdapter.getCurrencyExchangeRate(params);
    	
    	//Assert
    	//exception thrown   	
    }      

}

package com.zp.sdcc.test.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.CURRENCY_CONVERTER_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_REQUEST_MAPPING;
import static com.zp.sdcc.test.TestConstants.DEFAULT_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.zp.sdcc.controllers.CurrencyConverterController;
import com.zp.sdcc.controllers.RegistrationController;
import com.zp.sdcc.services.AuditHistoryService;
import com.zp.sdcc.services.CurrencyConversionDelegatorService;
import com.zp.sdcc.services.UserService;
import com.zp.sdcc.validators.UserValidator;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {CurrencyConverterController.class, RegistrationController.class}, 
            includeFilters = @Filter(classes = {EnableWebSecurity.class} ))
public class ControllerSecurityTest {

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
    
    @MockBean
    private UserValidator validator;
    
    @Test
    public void getRequestToCurrencyConverter_NoAuthentication_RedirectsToLogin() throws Exception
    {
    	//Arrange

    	
    	//Act
    	ResultActions result = mockMvc.perform(get(CURRENCY_CONVERTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().is3xxRedirection())
    		  .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    public void getRequestToRegister_NoAuthentication_ForwardsToRegistrationPage() throws Exception
    {
    	//Arrange
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(get(REGISTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(forwardedUrl("/WEB-INF/register.jsp"));
    }
    
    @Test
    public void getRequestToUnknownResource_NoAuthentication_RedirectsToLogin() throws Exception
    {
    	//Arrange
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(get("/unknown").contentType(MediaType.TEXT_HTML));
        
    	//Assert
    	result.andExpect(status().is3xxRedirection())
		  .andExpect(redirectedUrl("http://localhost/login"));
    }  
    
    @Test
    @WithMockUser(username = DEFAULT_USER)
    public void getRequestToUnknownResource_WithAuthentication_ReturnsNotFound() throws Exception
    {
    	//Arrange
    	
    	
    	//Act
    	ResultActions result = mockMvc.perform(get("/unknown").contentType(MediaType.TEXT_HTML));

    	//Assert
    	result.andExpect(status().isNotFound());
    }     
	
}

package com.zp.sdcc.test.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_RESPONSE_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_RESPONSE_MAPPING;
import static com.zp.sdcc.test.TestConstants.BLANK_STRING;
import static com.zp.sdcc.test.TestConstants.ERROR_NOT_BLANK;
import static com.zp.sdcc.test.TestConstants.ERROR_SIZE;
import static com.zp.sdcc.test.TestConstants.ERROR_TYPE_EMAIL;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_ADDRESS_LINE1;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_CITY;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_COUNTRY;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_DOB;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_EMAILID;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_FIRSTNAME;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_PASSWORD;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_USERNAME;
import static com.zp.sdcc.test.TestConstants.FORM_FIELD_ZIP_CODE;
import static com.zp.sdcc.test.TestConstants.REGISTRATION_FORM;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zp.sdcc.controllers.RegistrationController;
import com.zp.sdcc.services.UserService;
import com.zp.sdcc.validators.UserValidator;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegistrationController.class, includeFilters = @Filter(classes = EnableWebSecurity.class))
public class RegistrationControllerTest {

	@Autowired 
	private MockMvc mockMvc;    

    @MockBean
    private UserService registrationService;
    
    
    @MockBean
    private UserValidator validator;
    
    @MockBean
    private UserDetailsService userDetailsService;


    @Test
    public void postRegister_UsernameSingleCharacter_ValidationErrorOnUsername() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_USERNAME, Arrays.asList("a"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(REGISTRATION_FORM, 
    														FORM_FIELD_USERNAME, 
    														ERROR_SIZE));
    }    

    @Test
    public void postRegister_UsernameBlankString_ValidationErrorOnUsername() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_USERNAME, Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_USERNAME));
    }        
    
    @Test
    public void postRegister_UsernameLongerThan12Characters_ValidationErrorOnUsername() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_USERNAME, Arrays.asList("1234567890abc"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(REGISTRATION_FORM, 
    														FORM_FIELD_USERNAME, 
    														ERROR_SIZE));
    }    
    
    @Test
    public void postRegister_UsernameNull_ValidationErrorOnUsername() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_USERNAME);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_USERNAME));
    }

    @Test
    public void postRegister_PasswordNull_ValidationErrorOnPassword() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_PASSWORD);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_PASSWORD));
    }    
 
    @Test
    public void postRegister_PasswordBlankString_ValidationErrorOnPassword() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_PASSWORD, Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(REGISTRATION_FORM, 
    														FORM_FIELD_PASSWORD, 
    														ERROR_NOT_BLANK));
    }
    
    @Test
    public void postRegister_EmailIdNull_ValidationErrorOnEmailId() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_EMAILID);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_EMAILID));
    }
 
    @Test
    public void postRegister_EmailIdInvalid_ValidationErrorOnEmailId() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_EMAILID,Arrays.asList("abc"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrorCode(REGISTRATION_FORM, 
    														FORM_FIELD_EMAILID, 
    														ERROR_TYPE_EMAIL));
    }   

    @Test
    public void postRegister_EmailIdBlankString_ValidationErrorOnEmailId() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_EMAILID,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													  FORM_FIELD_EMAILID));
    }
    
    @Test
    public void postRegister_DateOfBirthBlankString_ValidationErrorDateOfBirth() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_DOB,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_DOB));    
    }
    
    @Test
    public void postRegister_DateOfBirthInCorrectFormat_ValidationErrorDateOfBirth() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_DOB,Arrays.asList("20/11/2000"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_DOB));
    }  

    @Test
    public void postRegister_DateOfBirthInvalidValue_ValidationErrorDateOfBirth() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_DOB,Arrays.asList("a"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_DOB));
    }

    @Test
    public void postRegister_DateOfBirthNull_ValidationErrorDateOfBirth() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_DOB);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_DOB));
    }

    @Test
    public void postRegister_DateOfBirthFuture_ValidationErrorDateOfBirth() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_DOB,Arrays.asList("3017-01-10"));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_DOB));
    }
 
    @Test
    public void postRegister_addressLine1Null_ValidationErrorAddressLine1() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_ADDRESS_LINE1);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_ADDRESS_LINE1));
    }

    @Test
    public void postRegister_addressLine1BlankString_ValidationErrorAddressLine1() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_ADDRESS_LINE1,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													FORM_FIELD_ADDRESS_LINE1));
    }
    
    @Test
    public void postRegister_CityNull_ValidationErrorCity() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_CITY);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    														FORM_FIELD_CITY));
    }    

    @Test
    public void postRegister_CityBlankString_ValidationErrorCity() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_CITY,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_CITY));
    }        
    
    @Test
    public void postRegister_ZipCodeNull_ValidationErrorZipCode() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_ZIP_CODE);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_ZIP_CODE));
    }    

    @Test
    public void postRegister_ZipCodeBlankString_ValidationErrorZipCode() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_ZIP_CODE,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_ZIP_CODE));
    }     
    
    @Test
    public void postRegister_CountryNull_ValidationErrorCountry() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_COUNTRY);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_COUNTRY));
    }     

    @Test
    public void postRegister_CountryBlankString_ValidationErrorCountry() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_COUNTRY,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_COUNTRY));
    }         
    
    @Test
    public void postRegister_FirstNameNull_ValidationErrorFirstName() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.remove(FORM_FIELD_FIRSTNAME);
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_FIRSTNAME));
    }    

    @Test
    public void postRegister_FirstNameBlankString_ValidationErrorFirstName() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	requestParameters.put(FORM_FIELD_FIRSTNAME,Arrays.asList(BLANK_STRING));
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(model().attributeHasFieldErrors(REGISTRATION_FORM, 
    													 FORM_FIELD_FIRSTNAME));
    }      
    
    @Test
    public void postRegister_NoParameters_ReturnsRegistrationPageWithErrors() throws Exception
    {
    	//Arrange
    	//NOOP
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists(REGISTRATION_FORM))
    		  .andExpect(model().hasErrors())
    		  .andExpect(view().name(REGISTER_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(RegistrationController.class))
    		  .andExpect(handler().methodName("handleRegistrationRequest"))
    		  .andExpect(forwardedUrl("/WEB-INF/register.jsp"));
    }
    
    @Test
    public void getRegister_NoParameters_ReturnsRegistrationPageNoErrors() throws Exception
    {
    	//Arrange
    	//NOOP
    	
    	//Act
    	ResultActions result = mockMvc.perform(get(REGISTER_REQUEST_MAPPING));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists(REGISTRATION_FORM))
    		  .andExpect(model().hasNoErrors())
    		  .andExpect(view().name(REGISTER_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(RegistrationController.class))
    		  .andExpect(handler().methodName("registerMapping"))
    		  .andExpect(forwardedUrl("/WEB-INF/register.jsp"));
    }

    @Test
    public void postRegister_ValidParameters_ForwardsToLoginPageNoErrors() throws Exception
    {
    	//Arrange
    	MultiValueMap<String,String> requestParameters = createRequestParameterStub();
    	
    	//Act
    	ResultActions result = mockMvc.perform(post(REGISTER_REQUEST_MAPPING)
    										   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    										   .params(requestParameters));

    	//Assert
    	result.andExpect(status().isOk())
    		  .andExpect(model().attributeExists(REGISTRATION_FORM))
    		  .andExpect(model().hasNoErrors())
    		  .andExpect(view().name(LOGIN_RESPONSE_MAPPING))
    		  .andExpect(handler().handlerType(RegistrationController.class))
    		  .andExpect(handler().methodName("handleRegistrationRequest"))
    		  .andExpect(forwardedUrl("/WEB-INF/login.jsp"));
    }
    
    
	private MultiValueMap<String, String> createRequestParameterStub() {
		MultiValueMap<String,String> requestParameters = new LinkedMultiValueMap<>();
		requestParameters.put(FORM_FIELD_USERNAME,Arrays.asList("john"));
		requestParameters.put(FORM_FIELD_FIRSTNAME,Arrays.asList("john"));
		requestParameters.put(FORM_FIELD_EMAILID, Arrays.asList("john@john.com"));
		requestParameters.put(FORM_FIELD_PASSWORD,Arrays.asList("testPassword"));
		requestParameters.put(FORM_FIELD_DOB, Arrays.asList("2000-01-01"));
		addAddressDetails(requestParameters);
		return requestParameters;
	}
	
	private void addAddressDetails(MultiValueMap<String,String> requestParameters) {
		requestParameters.put(FORM_FIELD_ADDRESS_LINE1,Arrays.asList("addressLine1"));
		requestParameters.put("address.street",Arrays.asList("streetName"));
		requestParameters.put(FORM_FIELD_CITY, Arrays.asList("cityName"));
		requestParameters.put(FORM_FIELD_ZIP_CODE,Arrays.asList("Zip001"));
		requestParameters.put("address.state", Arrays.asList("state"));
		requestParameters.put(FORM_FIELD_COUNTRY, Arrays.asList("country"));
	}	
}

package com.zp.sdcc.it.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginFeatureSteps extends AbstractStepDef
{

	private WebDriver browser;
	
	@Given("^the login page is open$")
	public void given()
    {
		browser = new FirefoxDriver();
		browser.get("http://localhost:"+port+"/login");
		assertThat(browser.getTitle()).isEqualToIgnoringCase("login");
    }    

	@When("^the following correct credentials are entered and submit is clicked$")
    public void whenSuccessfulLogin(List<List<String>> loginInfo)
    {
		browser.findElement(By.name("username")).sendKeys(loginInfo.get(1).get(0));
		browser.findElement(By.name("password")).sendKeys(loginInfo.get(1).get(1));
		browser.findElement(By.name("submit")).click();
    }	

	@When("^following incorrect credentials are entered and submit is clicked$")
    public void whenUnsuccessfulLogin(List<List<String>> loginInfo)
    {
		browser.findElement(By.name("username")).sendKeys(loginInfo.get(1).get(0));
		browser.findElement(By.name("password")).sendKeys(loginInfo.get(1).get(1));
		browser.findElement(By.name("submit")).click();
		
    }	
	
    @Then("^the Currency Converter Main page is open$")
    public void thenForSuccessfulLogin()
    {
    	assertThat(browser.getTitle()).isEqualToIgnoringCase("currency converter");

    }
    
    @Then("^the Currency Converter Main page is not open$")
    public void thenForUnsuccessfulLogin()
    {
    	assertThat(browser.getTitle()).isNotEqualToIgnoringCase("currency converter");
    }
 
	
    @After
    public void teardown(){
    	//TODO close browser only for active scenario
    	if(browser!=null)
    		browser.close();
    }   
    
}
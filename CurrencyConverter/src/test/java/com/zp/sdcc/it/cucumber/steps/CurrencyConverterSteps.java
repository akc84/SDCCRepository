package com.zp.sdcc.it.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CurrencyConverterSteps extends AbstractStepDef {

	private WebDriver browser;	
	
	@Given("^the Login page is open$")
	public void given()
    {
		browser = new FirefoxDriver();
		browser.get("http://localhost:"+port+"/login");
		assertThat(browser.getTitle()).isEqualToIgnoringCase("login");
    }    

	@And("^the following login credentials are entered$")
    public void andLoginCredentialsEntered(List<List<String>> loginInfo)
    {
		browser.findElement(By.name("username")).sendKeys(loginInfo.get(1).get(0));
		browser.findElement(By.name("password")).sendKeys(loginInfo.get(1).get(1));
    }

	@And("^the submit button is clicked$")
    public void andSubmitButtonClicked()
    {
		browser.findElement(By.name("submit")).click();
    }
	
	@When("^the amount is entered as (\\d+)$")
    public void andAmountEntered(String amount)
    {
		browser.findElement(By.name("amountToConvert")).clear();
		browser.findElement(By.name("amountToConvert")).sendKeys(amount);
    }
	
	@And("^the source currency is selected as (...)$")
    public void andSourceCurrencySelected(String fromCurrency)
    {
		browser.findElement(By.name("sourceCurrency")).sendKeys(fromCurrency);
    }		

	@And("^the target currency is selected as (...)$")
    public void andTargetCurrencySelected(String toCurrency)
    {
		browser.findElement(By.name("targetCurrency")).sendKeys(toCurrency);
    }
	
	@And("^the date is entered as (.*)$")
    public void andDateEntered(String conversionDate)
    {
		browser.findElement(By.name("conversionDate")).clear();
		browser.findElement(By.name("conversionDate")).sendKeys(conversionDate);
    }
	
	@And("^convert button is clicked$")
    public void andConvertButtonClicked()
    {
		browser.findElement(By.name("convert")).click();
    }
	
	@Then("^currency conversion is performed$")
    public void then()
    {
		//String pageSource = browser.getPageSource();
		assertThat(browser.getTitle()).isEqualToIgnoringCase("CURRENCY CONVERTER");
		//TODO match with result string or exception string
    }
    
	
    @After
    public void teardown(){
    	//TODO close browser only for active scenario
    	if(browser!=null)
    		browser.close();
    }
	
}

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

public class RegisterFeatureSteps extends AbstractStepDef {

	private WebDriver browser;

	@Given("^the Registration page is open$")
	public void given() {
		browser = new FirefoxDriver();
		browser.get("http://localhost:" + port + "/register");
		assertThat(browser.getTitle()).isEqualToIgnoringCase("Registration");
	}

	@When("^the following user data is entered$")
	public void whenUserDataEntered(List<List<String>> userData) {
		List<String> columnNames = userData.get(0);
		List<String> columnData = userData.get(1);
		for (int i = 0; i < columnNames.size(); i++)
			browser.findElement(By.name(columnNames.get(i).trim())).sendKeys(columnData.get(i).trim());
	}

	@And("^the following address data is entered$")
	public void andAddressDataEnterd(List<List<String>> addressData) {
		List<String> columnNames = addressData.get(0);
		List<String> columnData = addressData.get(1);
		String prefix = "address.";
		for (int i = 0; i < columnNames.size(); i++)
			browser.findElement(By.name(prefix.concat(columnNames.get(i).trim()))).sendKeys(columnData.get(i).trim());
	}

	@And("^Register button is clicked$")
	public void andRegisterButtonClicked() {
		browser.findElement(By.name("register")).click();
	}

	@Then("^registration is successful and Login page is open$")
	public void thenForSuccessfulRegister() {
		assertThat(browser.getTitle()).isEqualToIgnoringCase("login");

	}

	@Then("^registration is unsuccessful and Login page is not open$")
	public void thenForUnsuccessfulRegister() {
		assertThat(browser.getTitle()).isEqualToIgnoringCase("REGISTRATION");
	}

	@After
	public void teardown() {
		// TODO close browser only for active scenario
		if (browser != null)
			browser.close();
	}
}

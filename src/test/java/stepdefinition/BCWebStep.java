package stepdefinition;

import base.Base;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import page.BCWebStepImpl;
import utilities.*;

import java.io.IOException;

public class BCWebStep extends Base {

	private final BCWebStepImpl BCWebStepImpl = new BCWebStepImpl();

	@Given("Open BC login page")
	public void openLoginPage() throws Throwable {
		System.out.println("Open BC login page");
		ScreenShotUtil.resetNoOfSteps();

		BCWebStepImpl.loginBCWeb();
	}

	@Then("User is on {string} page")
	public void userIsOnPage(String page) throws Throwable {
		switch (page) {
			case "BC home":
				BCWebStepImpl.checkPage(page);
				break;
		}
		System.out.println("User is on " + page + " page.");
	}

	@And("User inputs {string}")
	public void userInputs(String field) throws Throwable {
		switch (field) {
			case "Mobile Number":
			case "Password":
				BCWebStepImpl.userInputs(field);
				break;
		}
		System.out.println("User inputs " + field);
	}

	@And("User clicks {string}")
	public void userClicks(String button) throws Throwable {
		switch (button) {
			case "Login/Register":
			case "Login":
				BCWebStepImpl.userClicks(button);
				break;
		}
		System.out.println("User clicks " + button);
	}

	@Then("User can see {string}")
	public void userCanSee(String message) throws Throwable {
		switch (message) {
			case "套票及積分 & Logout":
				BCWebStepImpl.userCanSee(message);
				break;
		}
		System.out.println("User can see " + message);
	}
}
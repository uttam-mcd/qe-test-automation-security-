package com.application.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserLoginPage{

	WebDriver driver;
		public UserLoginPage(WebDriver driver) throws Exception {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		// TODO Auto-generated constructor stub
	}

		int thread = 2000;

		@FindBy(how = How.ID, using = "username")
		private WebElement userTextBox;
		
		@FindBy(how = How.ID, using = "password-sign-in")
		private WebElement passwordTextBox;
		
		@FindBy(how = How.XPATH, using ="//button[text()='Sign in']")
		private WebElement loginButton;
		
		@FindBy(how = How.XPATH, using ="//button[text()='Show password']")
		private WebElement showPasswordButton;

		@FindBy(how = How.XPATH, using ="//legend[@class='usa-legend']")
		private WebElement signInImage;	
		
		@FindBy(how = How.XPATH, using = "//div[contains(text(),'Username or password is incorrect.')]")
		private WebElement userNamePasswordAuthErrorMessage;
		
		@FindBy(how = How.XPATH, using = "//div[contains(text(),'This field is required')]")
		private WebElement userNamePasswordInputMessage;
		
		@FindBy(how = How.XPATH, using = "//span[contains(text(),'This field is required')]")
		private WebElement fieldRequiredErrorMessage;
		
		@FindBy(how = How.XPATH, using = "//button[@class='usa-accordion__button usa-nav__link  usa-current']")
		private WebElement userNameButton;
		
		@FindBy(how = How.XPATH, using = "//a[@href='/logout']")
		private WebElement logOutButton;

		public void signIn(String userName, String passWord) throws InterruptedException {
			try{(new WebDriverWait(driver, 120)).until(ExpectedConditions
					.presenceOfElementLocated(By.id("username")));}catch(Exception e) {}
			userTextBox.sendKeys(userName);
			passwordTextBox.sendKeys(passWord);
			loginButton.click();
		}
			
		public void logOut(String expectedTitle) throws InterruptedException {
			userNameButton.click();
			logOutButton.click();
			Thread.sleep(2000);
			String title=driver.getTitle();
		}
		
		
		
}
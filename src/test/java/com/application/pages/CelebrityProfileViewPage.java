package com.application.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;


public class CelebrityProfileViewPage{

	int thread = 2000;
	WebDriver driver;
	public CelebrityProfileViewPage() throws Exception {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
}

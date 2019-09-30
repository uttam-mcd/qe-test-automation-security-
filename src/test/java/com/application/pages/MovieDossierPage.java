package com.application.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class MovieDossierPage{

	int thread = 2000;
	WebDriver driver;
	public MovieDossierPage() throws Exception {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
	@FindBy(how = How.XPATH, using ="<>")
	private WebElement criticName;
	
	@FindBy(how = How.XPATH, using ="<>")
	private WebElement criticResponse;
	
	@FindBy(how = How.XPATH, using = "<>")
	private WebElement addNewReferenceButton;
	
	@FindBy(how = How.XPATH, using = "<>")
	private WebElement addReferenceButton;
	
	@FindBy(how = How.XPATH, using = "<>")
	private WebElement cancelButton;
	
	@FindBy(how = How.XPATH, using = "<>")
	private WebElement referenceURLTextBox;
	
	@FindBy(how = How.XPATH, using = "< >")
	private WebElement descriptionTextArea;

}

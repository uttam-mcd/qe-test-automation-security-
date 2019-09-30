package com.application.pages;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class MovieAndCelebritySearchPage{

	int thread = 2000;
	WebDriver driver;
	
	public MovieAndCelebritySearchPage(WebDriver driver) throws Exception {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
	@FindBy(id="search")
	private WebElement searchTextBox;
	
	@FindBy(how = How.XPATH, using = "//button[@type='submit']")
	private WebElement searchButton;
	
	@FindBy(how = How.XPATH, using ="//label[@for='movie_dossier']")
	private WebElement movieRadioButton;
	
	@FindBy(how = How.XPATH, using ="//label[@for='celebrity']")
	private WebElement celebrityRadioButton;

	@FindBy(how = How.XPATH, using ="//h2[text()='Find Movie Dossier and Celebrities']")
	private WebElement searchImage;	
	
	@FindBy(how = How.XPATH, using ="//div[@class='search-error']/p")
	private WebElement searchResultTitle;	
	
	@FindBy(how = How.XPATH, using ="//button[@class='usa-show-password usa-button usa-button--unstyled']")
	private WebElement movieCardImage;

	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieTitle;	
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieImdbRating;	
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieDuration;
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieGenre1;	
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieGenre2;
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement movieGenre3;
	
	@FindBy(how = How.XPATH, using ="//button[@class='usa-show-password usa-button usa-button--unstyled']")
	private WebElement celebrityImage;

	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement celebrityTitle;	
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement celebritySummary;
	
	@FindBy(how = How.XPATH, using ="//img[@src='/img/signin-logo.png']")
	private WebElement moreGenreButton;
	
	@FindBy(how = How.XPATH, using = "//div[@id='u512']/div")
	private List<WebElement> numberOfResults;
	
	@FindBy(how = How.XPATH, using = "//div[@id='u512']//span[contains(text(),'The Godfather')]/../../../preceding-sibling::div[3]/image")
	private WebElement movieImageLink;
	
	/*
	 *  Elements for Header section
	 */
	@FindBy(how = How.XPATH, using = "//a[@title='Home']")
	private WebElement oscarLogoLink;
	
	@FindBy(how = How.XPATH, using = "//nav[@role='navigation']//span")
	private WebElement userNameDrodown;
	
	@FindBy(how = How.XPATH, using = "//a[@title='System Log']")
	private WebElement systemLogLink;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Logout')]")
	private WebElement logoutLink;
	
	
	public void searchForCelebrity(String celebrity) {
		celebrityRadioButton.click();
		searchTextBox.sendKeys(celebrity);
		searchButton.click();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchForMovie(String movie) {
		movieRadioButton.click();
		searchTextBox.sendKeys(movie);
		searchButton.click();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

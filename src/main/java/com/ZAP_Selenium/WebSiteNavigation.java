package com.ZAP_Selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebSiteNavigation {

	WebDriver driver;
	static String BASE_URL = "";
	static String EXCLUDE_URL = "";

	public WebSiteNavigation(WebDriver driver) {
		this.driver = driver;
		this.driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// If browser is send as jenkins parameter or env variables
		if (System.getenv("BASE_URL") != null)
			BASE_URL = System.getenv("BASE_URL");
		else if (System.getProperty("BASE_URL") != null)
			BASE_URL = System.getProperty("BASE_URL");
	}

	public void navigateTo(String url) {
		driver.navigate().to(url);
	}

	public void navigateTo(String url, String user, String pass) {
		driver.navigate().to(url);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<WebElement> eles = driver.findElements(By.xpath("//input"));
		if (eles.size() >= 2) {
			eles.get(0).sendKeys(user);
			eles.get(1).sendKeys(pass);
//			eles.get(0).submit();
			driver.findElement(By.xpath("//input[@type='submit']")).click();
		} else {
			System.err.println("Not able to locate username and password field on url: " + url);
		}
	}
}
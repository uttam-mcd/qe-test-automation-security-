package com.ZAP_Selenium_BrowserDriver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserDriverFactory {

	static WebDriver driver;

	public static WebDriver createChromeDriver(Proxy proxy) {
		System.out.println("Chrome path: " + System.getProperty("webdriver.chrome.driver"));
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		capabilities.setCapability("proxy", proxy);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setHeadless(true);
		options.addArguments("headless");
		options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
		options.addArguments("window-size=1920,1080");
		options.merge(capabilities);
		driver = new ChromeDriver(options);
        return driver;
		
	}

	public static WebDriver createHtmlUnitDriver(Proxy proxy) {
		DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
		capabilities.setCapability("proxy", proxy);
		capabilities.setBrowserName("htmlunit");
		capabilities.setJavascriptEnabled(true);
		return new HtmlUnitDriver(capabilities);

//		WebDriverManager.phantomjs().setup();
//		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
//		capabilities.setCapability("proxy", proxy);
//		capabilities.setBrowserName("phantomjs");
//		return new PhantomJSDriver(capabilities);
	}
}
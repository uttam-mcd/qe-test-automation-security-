package com.ZAP_Selenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.zaproxy.clientapi.core.Alert;

import com.ZAP_Selenium_BrowserDriver.BrowserDriverFactory;
import com.application.pages.UserLoginPage;

import io.github.bonigarcia.wdm.OperatingSystem;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.continuumsecurity.proxy.ScanningProxy;
import net.continuumsecurity.proxy.Spider;
import net.continuumsecurity.proxy.ZAProxyScanner;

public class ZapSecurityPrep {

	static Logger log = Logger.getLogger(ZapSecurityPrep.class.getName());
	private static boolean USE_ZAP_DEAMON = false;
	private static String ZAP_OS = "linux";
//			"windows";
	private static String ZAP_PROXYHOST = "localhost";
	private static int ZAP_PROXYPORT = 8898;
	private static String ZAP_APIKEY = "something";
	private static String MEDIUM = "MEDIUM";
	private static String HIGH = "HIGH";
	private static String BROWSER =
//                  "htmlunit";
			"chrome";
	private static String BROWSER_DRIVER_VERSION = "76.0.3809.126";
	public String testName="";
	boolean didTestPass = false;

	private ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private ThreadLocal<ScanningProxy> zapScanner = new ThreadLocal<ScanningProxy>();
	private ThreadLocal<Spider> zapSpider = new ThreadLocal<Spider>();

//     private ScanningProxy zapScanner;
//     private Spider zapSpider;
//     private WebDriver driver;

	private WebSiteNavigation siteNavigation;
	private final static String[] policyNames = { "directory-browsing", "cross-site-scripting", "sql-injection",
			"path-traversal", "remote-file-inclusion", "server-side-include", "script-active-scan-rules",
			"server-side-code-injection", "external-redirect", "crlf-injection" };
	int currentScanID;

	public void setDriver(WebDriver driver) {
		this.driver.set(driver);
	}

	public WebDriver getDriver() {
		return driver.get();
	}

	public void setZapScanner(ScanningProxy scanproxy) {
		this.zapScanner.set(scanproxy);
	}

	public ScanningProxy getZapScanner() {
		return zapScanner.get();
	}

	public void setZapSpider(Spider spider) {
		this.zapSpider.set(spider);
	}

	public Spider getZapSpider() {
		return zapSpider.get();
	}

	public static String getZAP_OS() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("ZAP_OS") != null)
			ZAP_OS = System.getenv("ZAP_OS");
		else if (System.getProperty("ZAP_OS") != null)
			ZAP_OS = System.getProperty("ZAP_OS");
		return ZAP_OS;
	}

	public static boolean getUSE_ZAP_DEAMON() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("USE_ZAP_DEAMON") != null)
			USE_ZAP_DEAMON = System.getenv("USE_ZAP_DEAMON").toString().toLowerCase().equals("false") ? false : true;
		else if (System.getProperty("USE_ZAP_DEAMON") != null)
			USE_ZAP_DEAMON = System.getProperty("USE_ZAP_DEAMON").toString().toLowerCase().equals("false") ? false
					: true;
		return USE_ZAP_DEAMON;
	}

	public static Integer getZAP_PROXYPORT() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("ZAP_PROXYPORT") != null)
			ZAP_PROXYPORT = Integer.parseInt(System.getenv("ZAP_PROXYPORT"));
		else if (System.getProperty("ZAP_PROXYPORT") != null)
			ZAP_PROXYPORT = Integer.parseInt(System.getProperty("ZAP_PROXYPORT"));
		return ZAP_PROXYPORT;
	}

	public static String getZAP_PROXYHOST() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("ZAP_PROXYHOST") != null)
			ZAP_PROXYHOST = System.getenv("ZAP_PROXYHOST");
		else if (System.getProperty("ZAP_PROXYHOST") != null)
			ZAP_PROXYHOST = System.getProperty("ZAP_PROXYHOST");
		return ZAP_PROXYHOST;
	}

	public static String getZAP_APIKEY() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("ZAP_APIKEY") != null)
			ZAP_APIKEY = System.getenv("ZAP_APIKEY");
		else if (System.getProperty("ZAP_APIKEY") != null)
			ZAP_APIKEY = System.getProperty("ZAP_APIKEY");
		return ZAP_APIKEY;
	}

	public static String getBrowser() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("BROWSER") != null)
			BROWSER = System.getenv("BROWSER");
		else if (System.getProperty("BROWSER") != null)
			BROWSER = System.getProperty("BROWSER");
		return BROWSER;
	}

	public static String getBrowserDriverVersion() {
		// If browser is send as jenkins parameter or env variables
		if (System.getenv("BROWSER_DRIVER_VERSION") != null)
			BROWSER_DRIVER_VERSION = System.getenv("BROWSER_DRIVER_VERSION");
		else if (System.getProperty("BROWSER_DRIVER_VERSION") != null)
			BROWSER_DRIVER_VERSION = System.getProperty("BROWSER_DRIVER_VERSION");
		return BROWSER_DRIVER_VERSION;
	}

	private static Proxy createZapProxyConfiguration() {
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(getZAP_PROXYHOST() + ":" + getZAP_PROXYPORT());
		proxy.setSslProxy(getZAP_PROXYHOST() + ":" + getZAP_PROXYPORT());
		return proxy;
	}

	@BeforeSuite
	public void setUp() throws Exception {
		if (getUSE_ZAP_DEAMON()) {
			if (!(getZAP_OS().toLowerCase().contains("windows")))
				ZapManager.getInstance().startZAP("ZAP/zap.sh");
			else
				ZapManager.getInstance().startZAP("ZAP/zap.bat");
		}
		System.out.println(getZAP_PROXYHOST() + "    " + getZAP_PROXYPORT() + "    " + getZAP_APIKEY());

		setZapScanner(new ZAProxyScanner(getZAP_PROXYHOST(), getZAP_PROXYPORT(), getZAP_APIKEY()));
		getZapScanner().clear();
		getZapScanner().deleteAlerts();

		log.info("Started a new session: Scanner");
//            zapSpider = (Spider) zapScanner;
		setZapSpider((Spider) getZapScanner());
		log.info("Created client to ZAP API");
		log.info("Now will go for browser");

		if (getZAP_OS().toLowerCase().contains("linux")) {
			log.info("Setting Driver for linux chrome");
			System.setProperty("webdriver.chrome.driver",
					"drivers" + File.separator + "linux" + File.separator + "chromedriver");
			log.info("Chrome path: " + System.getProperty("webdriver.chrome.driver"));
		} else if (getZAP_OS().toLowerCase().contains("window")) {
			log.info("Setting Driver for windows chrome");
			WebDriverManager.chromedriver().operatingSystem(OperatingSystem.WIN).version(getBrowserDriverVersion())
					.setup();
		} else if (getZAP_OS().toLowerCase().contains("mac")) {
			log.info("Setting Driver for mac chrome");
			WebDriverManager.chromedriver().operatingSystem(OperatingSystem.MAC).version(getBrowserDriverVersion())
					.setup();
		}

    }

    @BeforeMethod
    public void setToSignIn() throws Exception {

		setDriver(BrowserDriverFactory.createChromeDriver(createZapProxyConfiguration()));

//    driver.navigate().to("https://felix.apps.jetsdev.jets-biometric.com/sign-in");
//    siteNavigation = new WebSiteNavigation(driver);
		BufferedReader reader;
		List<String> storeUrl = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader("URLs.txt"));
			String line = reader.readLine();
			if (line.contains("BASE_URL")) {
				WebSiteNavigation.BASE_URL = line.split("=")[1];
				System.err.println("BASE_URL is set to: " + WebSiteNavigation.BASE_URL);
				line = reader.readLine();
			}
			if (line.contains("EXCLUDE_URL")) {
				WebSiteNavigation.EXCLUDE_URL = line.split("=")[1];
				System.err.println("EXCLUDE_URL is set to: " + WebSiteNavigation.EXCLUDE_URL);
				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		getDriver().navigate().to(WebSiteNavigation.BASE_URL);
		siteNavigation = new WebSiteNavigation(getDriver());
		UserLoginPage uLogin = new UserLoginPage(getDriver());
		uLogin.signIn("superman", "password");
		didTestPass = false;
	}

//    @AfterSuite()
    @AfterMethod(alwaysRun = true)
	public void closeBrowser() throws IOException {
		if(!didTestPass) {
			File f = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
			f.renameTo(new File("target"+File.separator+testName+".jpg"));
		}
		getDriver().close();
    }

    @AfterSuite()
    public void tearDownDriver() throws IOException {
		byte[] htmlReport = getZapScanner().getHtmlReport();
		String PATH_TO_SAVE = "reports";
		File reportDir = new File(PATH_TO_SAVE);
		if (!reportDir.exists())
			FileUtils.forceMkdir(reportDir);
		Path pathToFile = Paths.get(reportDir.getAbsolutePath() + "/report" +
		// "_" + result.getMethod().getMethodName() +
				".html");
		if (new File(pathToFile.toString()).exists())
			(new File(pathToFile.toString())).delete();
		Files.createFile(pathToFile);
//            Files.createDirectories(pathToFile.getParent());
		Files.write(pathToFile, htmlReport, StandardOpenOption.APPEND);
		if (getUSE_ZAP_DEAMON())
			ZapManager.getInstance().stopZap();
	}

	private List<Alert> filterAlerts(List<Alert> alerts) {
		List<Alert> filteredAlerts = new ArrayList<Alert>();
		for (Alert alert : alerts) {
			if (alert.getRisk().equals(Alert.Risk.High) && alert.getConfidence() != Alert.Confidence.Low)
				filteredAlerts.add(alert);
		}
		return filteredAlerts;
	}

	public void setAlert_AttackStrength() {
		for (String ZapPolicyName : policyNames) {
			String ids = activateZapPolicy(ZapPolicyName);
			for (String id : ids.split(",")) {
//                         zapScanner.setScannerAlertThreshold(id, MEDIUM);
//                         zapScanner.setScannerAttackStrength(id, HIGH);
				getZapScanner().setScannerAlertThreshold(id, MEDIUM);
				getZapScanner().setScannerAttackStrength(id, HIGH);
			}
		}
	}

	private String activateZapPolicy(String policyName) {
		String scannerIds = null;
		switch (policyName.toLowerCase()) {
		case "directory-browsing":
			scannerIds = "0";
			break;
		case "cross-site-scripting":
			scannerIds = "40012,40014,40016,40017";
			break;
		case "sql-injection":
			scannerIds = "40018";
			break;
		case "path-traversal":
			scannerIds = "6";
			break;
		case "remote-file-inclusion":
			scannerIds = "7";
			break;
		case "server-side-include":
			scannerIds = "40009";
			break;
		case "script-active-scan-rules":
			scannerIds = "50000";
			break;
		case "server-side-code-injection":
			scannerIds = "90019";
			break;
		case "remote-os-command-injection":
			scannerIds = "90020";
			break;
		case "external-redirect":
			scannerIds = "20019";
			break;
		case "crlf-injection":
			scannerIds = "40003";
			break;
		case "source-code-disclosure":
			scannerIds = "42,10045,20017";
			break;
		case "shell-shock":
			scannerIds = "10048";
			break;
		case "remote-code-execution":
			scannerIds = "20018";
			break;
		case "ldap-injection":
			scannerIds = "40015";
			break;
		case "xpath-injection":
			scannerIds = "90021";
			break;
		case "xml-external-entity":
			scannerIds = "90023";
			break;
		case "padding-oracle":
			scannerIds = "90024";
			break;
		case "el-injection":
			scannerIds = "90025";
			break;
		case "insecure-http-methods":
			scannerIds = "90028";
			break;
		case "parameter-pollution":
			scannerIds = "20014";
			break;
		default:
			throw new RuntimeException("No policy found for: " + policyName);
		}
//            zapScanner.setEnableScanners(scannerIds, true);
		getZapScanner().setEnableScanners(scannerIds, true);
		return scannerIds;
	}

	public void spiderWithZap()

	{
		log.info("Spidering started");
		if (!WebSiteNavigation.EXCLUDE_URL.isEmpty())
//                  zapSpider.excludeFromSpider(WebSiteNavigation.EXCLUDE_URL);
//            zapSpider.setThreadCount(5);
//            zapSpider.setMaxDepth(5);
//            zapSpider.setPostForms(false);
//            zapSpider.spider(WebSiteNavigation.BASE_URL);
//            int currentSpiderID = zapSpider.getLastSpiderScanId();
			getZapSpider().excludeFromSpider(WebSiteNavigation.EXCLUDE_URL);
		getZapSpider().setThreadCount(5);
		getZapSpider().setMaxDepth(5);
		getZapSpider().setPostForms(false);
//		getZapSpider().spider(WebSiteNavigation.BASE_URL);
		getZapSpider().spider(getDriver().getCurrentUrl());
		int currentSpiderID = getZapSpider().getLastSpiderScanId();

		int progressPercent = 0;
		while (progressPercent < 100) {
//                  progressPercent = zapSpider.getSpiderProgress(currentSpiderID);
			progressPercent = getZapSpider().getSpiderProgress(currentSpiderID);
			log.info("Spider is " + progressPercent + "% complete.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//            for (String url : zapSpider.getSpiderResults(currentSpiderID)) {
		for (String url : getZapSpider().getSpiderResults(currentSpiderID)) {
			log.info("Found URL after spider: " + url);
		}
		log.info("Spidering ended");
	}

	public void scanWithZap() {
		log.info("Scanning started");
//            zapScanner.scan(driver.getCurrentUrl());
//            int currentScanId = zapScanner.getLastScannerScanId();
		getZapScanner().scan(getDriver().getCurrentUrl());
		int currentScanId = getZapScanner().getLastScannerScanId();
		int progressPercent = 0;
		while (progressPercent < 100) {
//                  progressPercent = zapScanner.getScanProgress(currentScanId);
			progressPercent = getZapScanner().getScanProgress(currentScanId);
			log.info("Scan is " + progressPercent + "% complete.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("Scanning ended");
	}

	public void checkForSecurity() {
		startTheShow();
	}

	public void startTheShow() {
		log.info("Started spidering");
		spiderWithZap();
		log.info("Ended spidering");
		setAlert_AttackStrength();
//            zapScanner.setEnablePassiveScan(true);
		getZapScanner().setEnablePassiveScan(true);
		log.info("Started scanning");
		scanWithZap();
		log.info("Ended scanning");
//            List<Alert> generatedAlerts = filterAlerts(zapScanner.getAlerts());
		List<Alert> generatedAlerts = filterAlerts(getZapScanner().getAlerts());
		for (Alert alert : generatedAlerts) {
			log.info("Alert: " + alert.getAlert() + " at URL: " + alert.getUrl() + " Parameter: " + alert.getParam()
					+ " CWE ID: " + alert.getCweId());
		}
//            assertThat(generatedAlerts.size(), equalTo(0));
		didTestPass = true;
	}
}

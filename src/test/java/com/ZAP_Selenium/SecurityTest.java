package com.ZAP_Selenium;

import org.testng.annotations.Test;

import com.application.pages.MovieAndCelebritySearchPage;
import com.application.pages.UserLoginPage;

public class SecurityTest extends ZapSecurityPrep {

//	 String baseUrl = "https://felix.apps.jetsdev.jets-biometric.com";
	
	@Test(priority = 0)
	public void checkSearchPage() throws Exception {
		testName = "checkSearchPage";
		Thread.sleep(5000);
		startTheShow();
	}
	
	@Test(priority = 1)
	public void checkClassificationPage() throws Exception {
		testName = "checkClassificationPage";
		getDriver().navigate().to(WebSiteNavigation.BASE_URL+"/classifications");
		startTheShow();
	}
	
	@Test(priority = 2)
	public void checkRawDataPage() throws Exception {
		testName = "checkRawDataPage";
		getDriver().navigate().to(
				WebSiteNavigation.BASE_URL+"/raw-data"
				);
		startTheShow();
	}
	
	@Test(priority = 3)
	public void checkCelebProfilePage() throws Exception {
		testName = "checkCelebprofilePage";
		getDriver().navigate().to(
				WebSiteNavigation.BASE_URL+"celebrity-profile/5d71c71b39aa671b759ae665"
				);
		startTheShow();
	}
	
//	@Test(priority = 0)
//	public void checkLoginUrlPage() {
//		testName = "checkLoginUrlPage";
//		startTheShow();
//	}
//
//	@Test(priority = 1)
//	public void checkSearchPage() throws Exception {
//		testName = "checkSearchPage";
//		UserLoginPage page = new UserLoginPage(getDriver());
//		page.signIn("user", "password");
//		Thread.sleep(10000);
//		startTheShow();
//	}
//
//    @Test(priority = 2)
//    public void checkDevOpsLandingPage() throws Exception {
//        testName = "checkDevOpslandingPage";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("devsecops", "password");
//        Thread.sleep(10000);
//        startTheShow();
//    }
//
//    @Test(priority = 3)
//    public void checkCelebProfile() throws Exception {
//    	testName = "checkCelebProfile";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("supervisor", "password");
//        Thread.sleep(10000);
//        getDriver().navigate().to(WebSiteNavigation.BASE_URL+"/celebrity/kxGismwBlqm5TaJw9qAw");
////        http://jets-oscar-react-app.apps.jetsdev.jetsrecord.com/celebrity/kxGismwBlqm5TaJw9qAw
//        Thread.sleep(5000);
//        startTheShow();
//    }
//
//    @Test(priority = 4)
//    public void checkMovieProfile() throws Exception {
//    	testName = "checkMovieProfile";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("supervisor", "password");
//        Thread.sleep(10000);
//        getDriver().navigate().to(WebSiteNavigation.BASE_URL+"/movie/whGasmwBlqm5TaJw_mST");
////        http://jets-oscar-react-app.apps.jetsdev.jetsrecord.com/movie/whGasmwBlqm5TaJw_mST
//        Thread.sleep(5000);
//        startTheShow();
//    }
//
//    @Test(priority = 5)
//    public void checkDsLandingPage() throws Exception {
//    	testName = "checkDslandingPage";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("datascientist", "password");
//        Thread.sleep(10000);
//        startTheShow();
//    }
//
//    @Test(priority = 6)
//    public void checkMovieSearchResultPageForBU() throws Exception {
//        testName = "checkMovieSearchResultPageForBu";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("user", "password");
//        Thread.sleep(10000);
//        MovieAndCelebritySearchPage searchPage = new MovieAndCelebritySearchPage(getDriver());
//        searchPage.searchForMovie("John Wick");
//        Thread.sleep(10000);
//        startTheShow();
//    }
//
//    @Test(priority = 7)
//    public void checkCelebritySearchResultPageForBU() throws Exception {
//    	testName = "checkCelebritySearchResultPageForBU";
//        UserLoginPage page = new UserLoginPage(getDriver());
//        page.signIn("user", "password");
//        Thread.sleep(10000);
//        MovieAndCelebritySearchPage searchPage = new MovieAndCelebritySearchPage(getDriver());
//        searchPage.searchForCelebrity("Keanu Reeves");
//        Thread.sleep(10000);
//        startTheShow();
//    }
	
}

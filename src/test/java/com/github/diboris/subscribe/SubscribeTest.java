package com.github.diboris.subscribe;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.junit.Assert.assertEquals;

public class SubscribeTest {
    private static final boolean CLOSE_BROWSER_AFTER_TEST = true;

    private WebDriver driver;
    private SubscribeBlogPage subscribeBlogPage;

    @BeforeClass
    public static void setupClass() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        ChromeDriverManager.getInstance().version("73").setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
        subscribeBlogPage = new SubscribeBlogPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null && CLOSE_BROWSER_AFTER_TEST) {
            driver.quit();
        }
    }

    @Test
    public void should_enter_valid_email_subscribe() throws InterruptedException {
        driver.get("https://www.companyfolders.com/proofs");
        subscribeBlogPage.initialize();
        subscribeBlogPage.enterValidEmail("email@example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchTo1Tab();
        assertEquals("FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void should_enter_invalid_email_subscribe() throws InterruptedException {
        driver.get("https://www.companyfolders.com/proofs");
        subscribeBlogPage.initialize();
        subscribeBlogPage.enterValidEmail("email-example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals(1, subscribeBlogPage.getTabsCount());
    }
}

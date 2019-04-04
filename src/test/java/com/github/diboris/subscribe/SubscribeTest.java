package com.github.diboris.subscribe;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubscribeTest {
    private static final boolean CLOSE_BROWSER_AFTER_TEST = true;

    private static WebDriver driver;
    private SubscribeBlogPage subscribeBlogPage;

    @BeforeClass
    public static void startTestSuit() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        ChromeDriverManager.getInstance().version("73").setup();
        driver = new ChromeDriver();
        driver.get("https://www.companyfolders.com/proofs");
    }

    @Before
    public void startTest() {
        subscribeBlogPage = new SubscribeBlogPage(driver);
        subscribeBlogPage.initialize();
    }

    @After
    public void stopTest() {
        if (subscribeBlogPage.getTabsCount() > 1) {
            subscribeBlogPage.switchToSecondTab();
            driver.close();
            subscribeBlogPage.switchToFirstTab();
        }
        driver.navigate().refresh();
    }

    @AfterClass
    public static void stopTestSuit() {
        if (driver != null && CLOSE_BROWSER_AFTER_TEST) {
            driver.quit();
        }
    }

    @Test
    public void positive_test_subscribing_to_blog_with_sample_email_address_input() throws InterruptedException {
        subscribeBlogPage.enterEmail("test@gmail.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_dot_character_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("very.common@example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_non_ascii_characters_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("test@пример.com.au");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_plus_symbol() throws InterruptedException {
        subscribeBlogPage.enterEmail("disposable.style.email.with+symbol@example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_email_contains_hyphen() throws InterruptedException {
        subscribeBlogPage.enterEmail("other.email-with-hyphen@example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_one_letter_local_part_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("x@example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_hypen_in_domain_of_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("exampleindeed@strange-example.com");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_local_domain_name_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("admin@mailserver1");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_space_between_quotes_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("' '@example.nl");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void positive_test_subscribing_to_blog_with_quoted_double_dot_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("'john..doe'@example.nl");
        subscribeBlogPage.clickSubscribe();
        subscribeBlogPage.switchToSecondTab();
        assertEquals("Expected page title to be", "FeedBurner Email Subscription", driver.getTitle());
    }

    @Test
    public void negative_test_subscribing_to_blog_without_input() throws InterruptedException {
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
        assertTrue("Expected error message to be visible on a page", subscribeBlogPage.hasErrorMessage());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_several_ats_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("A@b@c@example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_special_characters_in_local_part_of_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("a\"'b(c)d,e:f;g<h>i[j\\k]l@example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_quoted_strings_in_email() throws InterruptedException {
        subscribeBlogPage.enterEmail("just\"not\"right@example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_local_part_of_email_longer_64_characters() throws InterruptedException {
        subscribeBlogPage.enterEmail("1234567890123456789012345678901234567890123456789012345678901234+x@example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_zero_input() throws InterruptedException {
        subscribeBlogPage.enterEmail("0");
        subscribeBlogPage.clickSubscribe();
        assertTrue("Expected error message to be visible on a page", subscribeBlogPage.hasErrorMessage());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_starts_from_dot() throws InterruptedException {
        subscribeBlogPage.enterEmail(".test@gmail.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_starts_from_single_letter() throws InterruptedException {
        subscribeBlogPage.enterEmail("A");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_starts_from_number() throws InterruptedException {
        subscribeBlogPage.enterEmail("5");
        subscribeBlogPage.clickSubscribe();
        assertTrue("Expected error message to be visible on a page", subscribeBlogPage.hasErrorMessage());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_starts_from_space() throws InterruptedException {
        subscribeBlogPage.enterEmail(" test@gmail.nl");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_with_no_at() throws InterruptedException {
        subscribeBlogPage.enterEmail("Abc.example.com");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }

    @Test
    public void negative_test_subscribing_to_blog_with_email_ends_with_space() throws InterruptedException {
        subscribeBlogPage.enterEmail("test @gmail.nl");
        subscribeBlogPage.clickSubscribe();
        assertEquals("Expected count of opened tabs to be 1", 1, subscribeBlogPage.getTabsCount());
    }


}

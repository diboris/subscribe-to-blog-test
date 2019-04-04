package com.github.diboris.subscribe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SubscribeBlogPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public SubscribeBlogPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
    }

    public void initialize() {
        By subscribeBlogSection = By.xpath("//div[@class='widget-wrap subscribe']//h4");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(subscribeBlogSection));
    }

    public void enterEmail(String name) {
        By inputSelector = By.xpath("//input[@value='Enter your email address']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inputSelector));
        WebElement inputElement = driver.findElement(inputSelector);
        inputElement.click();
        inputElement.clear();
        inputElement.sendKeys(name);
    }

    public void clickSubscribe() throws InterruptedException {
        By subscribeSelector = By.xpath("//input[@type='image']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(subscribeSelector));
        WebElement subscribeElement = driver.findElement(subscribeSelector);
        subscribeElement.click();
        Thread.sleep(500);
    }

    public void switchToSecondTab() {
        if (getTabsCount() > 1) {
            String[] windowHandles = driver.getWindowHandles().toArray(new String[]{});
            driver.switchTo().window(windowHandles[1]);
        }
    }

    public void switchToFirstTab() {
        String[] windowHandles = driver.getWindowHandles().toArray(new String[]{});
        driver.switchTo().window(windowHandles[0]);
    }

    public int getTabsCount() {
        String[] windowHandles = driver.getWindowHandles().toArray(new String[]{});
        return windowHandles.length;
    }

    public boolean hasErrorMessage() {
        return driver.findElements(By.xpath("//div[@data-test-id=error-message]")).size() > 0;
    }
}



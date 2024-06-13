package org.example.laba1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class KalkulyatorKalorii {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.calc.ru/kalkulyator-kalorii.html");
    }

    @Test
    public void testCalorieCalculator() {
        fillInputField(By.id("age"), "35");
        fillInputField(By.id("weight"), "85");
        fillInputField(By.id("sm"), "185");

        Select activityDropdown = new Select(driver.findElement(By.id("activity")));
        activityDropdown.selectByVisibleText("5 раз в неделю");

        WebElement calculateButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
        calculateButton.click();

        pause(9999);

        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), '2686 ккал/день')]")));
        String resultText = resultElement.getText();

        assertEquals("2686 ккал/день", resultText.trim());
    }

    private void fillInputField(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
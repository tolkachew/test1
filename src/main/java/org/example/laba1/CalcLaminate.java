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

public class CalcLaminate{
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get("https://calc.by/building-calculators/laminate.html");
    }

    @Test
    public void testCalculatorCase1() {
        performTest("со смещение на 1/2 длины", 51, 7);
    }

    @Test
    public void testCalculatorCase2() {
        performTest("со смещение на 1/3 длины", 52, 7);
    }

    @Test
    public void testCalculatorCase3() {
        performTest("с использованием отрезанного элемента", 53, 7);
    }

    private void performTest(String layoutMethod, int expectedBoards, int expectedPacks) {
        fillInputField(By.id("ln_room_id"), "500");
        fillInputField(By.id("wd_room_id"), "400");
        fillInputField(By.id("ln_lam_id"), "2000");
        fillInputField(By.id("wd_lam_id"), "200");
        Select layoutDropdown = new Select(driver.findElement(By.id("laying_method_laminate")));
        layoutDropdown.selectByVisibleText(layoutMethod);
        WebElement directionRadio = driver.findElement(By.id("direction-laminate-id1"));
        if (!directionRadio.isSelected()) {
            directionRadio.click();
        }
        driver.findElement(By.xpath("//a[@href='javascript:void(0);']")).click();
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("calc-result")));
        WebElement requiredBoardsElement = resultElement.findElement(By.xpath("//div[contains(text(),'Требуемое количество досок ламината')]/span"));
        int actualBoards = Integer.parseInt(requiredBoardsElement.getText());
        assertEquals(expectedBoards, actualBoards);

        WebElement requiredPacksElement = resultElement.findElement(By.xpath("//div[contains(text(),'Количество упаковок ламината')]/span"));
        int actualPacks = Integer.parseInt(requiredPacksElement.getText());
        assertEquals(expectedPacks, actualPacks);
    }

    private void fillInputField(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
        try {
            Thread.sleep(1000);
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
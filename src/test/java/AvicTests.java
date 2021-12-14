import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.assertTrue;

public class AvicTests {

    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testSetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatFacebookLinkWorks() {
        driver.findElement(By.xpath("//ul[contains(@class, 'footer-soc')]//li//a[contains(@href, 'facebook')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        List<String> tabs = driver.getWindowHandles().stream().toList();
        driver.switchTo().window(tabs.get(1));
        assertTrue(driver.getCurrentUrl().contains("facebook"));
    }

    @Test(priority = 2)
    public void checkIfPriceInEnteredRange() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone");
        driver.findElement(xpath("//button[@class='button-reset search-btn']")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.xpath("//input[contains(@class,'form-control-min')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'form-control-min')]")).sendKeys("13000");
        driver.findElement(By.xpath("//input[contains(@class,'form-control-max')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'form-control-max')]")).sendKeys("14000");
        new WebDriverWait(driver, 30).until(
                driver -> driver.findElement(By.xpath("//div[contains(@class,'open-filter-tooltip')]//a[contains(@href,'#')]")));
        driver.findElement(By.xpath("//div[contains(@class,'open-filter-tooltip')]//a[contains(@href,'#')]")).click();
        List<WebElement> elementList = driver.findElements(xpath("//div[contains(@class,'prise-new')]"));
        for (WebElement webElement : elementList) {
            assertTrue(Integer.parseInt(webElement.getText().split(" ")[0]) > 13000 && Integer.parseInt(webElement.getText().split(" ")[0]) < 14000);
        }
    }

    @Test(priority = 3)
    public void checkIfMapWorks() {
        driver.findElement(xpath("//div[contains(@class,'header')]//a[contains(text(),'Контакты')]")).click();
        List<WebElement> elementList = driver.findElements(xpath("//div[contains(@class,'general-col')]//a[contains(@href,'javascript')]"));
        for (WebElement webElement : elementList) {
            webElement.click();
            new WebDriverWait(driver, 30).until(
                    driver -> driver.findElement(By.xpath("//div[contains(@class,'open-map')]")));
            assertTrue(driver.findElement(By.xpath("//div[contains(@class,'open-map')]//div[@class='google-map']")).isDisplayed());
            driver.findElement(By.xpath("//div[contains(@class,'open-map')]//a[contains(@href,'javascript')]")).click();
        }
    }

    @Test(priority = 4)
    public void checkIfSortByPriceWorks() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone");
        driver.findElement(xpath("//button[@class='button-reset search-btn']")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.xpath("//div[contains(@class,'two-column')]//div[@class='sort-holder']//span[contains(@class,'select2-container--')]")).click();
        driver.findElement(By.xpath("//li[contains(@id,'pricedesc')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        List<WebElement> elementList = driver.findElements(xpath("//div[contains(@class,'prise-new')]"));
        for (int i = 0; i < elementList.size() - 1; i++) {
            assertTrue(Integer.parseInt(elementList.get(i).getText().split(" ")[0]) >= Integer.parseInt(elementList.get(i + 1).getText().split(" ")[0]));
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

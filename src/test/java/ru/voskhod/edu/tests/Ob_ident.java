package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmentable;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Александра on 20.04.2016.
 */
public class Ob_ident {
    private WebDriver driver;
    private Config config;
    private Logger logger = Logger.getLogger(Ob_ident.class);
@BeforeClass
public void initDriver() throws IOException {
    System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
    config= new Config("config.properties");
    WebDriver driver= new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    this.driver=driver;
}
@Test
    public void  ob_ident(){
        driver.get(config.get("url5"));
        logger.info("Тестирование страницы "+config.get("url5"));
        WebElement webElement = driver.findElement(By.xpath("html/body/div/div[3]/div[1]/form/fieldset/table/tbody"));
        List<WebElement> list = webElement.findElements(By.tagName("input"));
        Assert.assertTrue(list.size()==6);
        Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr[2]/td[1]")));
    }


    public boolean isElementPresent(By locator){
        List<WebElement> list = driver.findElements(locator);
        if (list.size()==0){
            return false;
        }else return list.get(0).isEnabled()&&list.get(0).isDisplayed();
    }
@AfterClass
    public void closeDriver(){
    if (driver!=null){
        driver.close();
        driver.quit();

    }
}
}

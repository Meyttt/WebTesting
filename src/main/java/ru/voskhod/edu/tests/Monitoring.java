package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
public class Monitoring {
    private WebDriver driver;
    private Config config;
    private Logger logger = Logger.getLogger(Monitoring.class);

    public Monitoring(WebDriver driver) throws IOException {
        this.driver = driver;
        config=new Config("config.properties");
    }

    @BeforeClass
public void initDriver() throws IOException {
    SSLTool.disableCertificateValidation();
    System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");

    WebDriver driver= new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    this.driver=driver;
}
@Test
    public void monitoring(){
    driver.get(config.get("url6"));
    logger.info("Тестирование страницы "+config.get("url6"));
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/div[1]")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]")).getText(),
            "Данный раздел содержит статистику мониторинга доступности сервисов аккредитованных УЦ");
    //"Фильтр"
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/div[2]/form/fieldset/table/tbody")));
    WebElement webElement = driver.findElement(By.xpath("html/body/div/div[3]/div[2]/form/fieldset/table/tbody"));
    List<WebElement> list = webElement.findElements(By.tagName("input"));
    Assert.assertEquals(list.size(),4);
    //Записи УЦ
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/th[1]")));
    List<WebElement> list1 = driver.findElements(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/td[1]/a"));
    Assert.assertTrue(list1.size()>1);

}

    public boolean isElementPresent(By locator){
        List<WebElement> list = driver.findElements(locator);
        if (list.size()==0){
            return false;
        }else return list.get(0).isDisplayed()&&list.get(0).isEnabled();
    }

@AfterClass
    public void closeDriver(){
    if (driver!=null){
        driver.close();;
        driver.quit();
    }
}
}

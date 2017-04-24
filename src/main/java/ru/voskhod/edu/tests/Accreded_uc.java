package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.LoggerLog;
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
public class Accreded_uc {
    private WebDriver driver;
    private Config config;
    private Logger logger =Logger.getLogger(Accreded_uc.class);

@BeforeClass
public void initDriver() throws IOException {
    System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
    config= new Config("config.properties");
    WebDriver driver= new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    this.driver=driver;
}

@Test
    public void test_reestry() {
    //аккредитованные УЦ
    driver.get(config.get("url4_1"));
    findVoskhod();

    logger.info("Тестирование страницы "+config.get("url4_1"));
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/div[1]/div[1]")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]/div[1]")).getText(),
            "Данный раздел содержит перечень аккредитованных удостоверяющих центров");
    List<WebElement> list = driver.findElements(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/td[2]"));
    Assert.assertTrue(list.size() > 1);
    //Сертификаты УФО
    driver.get(config.get("url4_2"));
    logger.info("Тестирование страницы "+config.get("url4_2"));
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div[1]/div[3]/div[1]")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div[1]/div[3]/div[1]")).getText(),
            "Данный раздел содержит реестр выданных и аннулированных уполномоченным федеральным органом квалифицированных сертификатов");
    List<WebElement> list1 = driver.findElements(By.xpath("html/body/div[1]/div[3]/table/tbody/tr[.]/td[2]"));
    Assert.assertTrue(list1.size()>1);
    driver.get(config.get("url4_3"));
    logger.info("Тестирование страницы "+config.get("url4_3"));
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div[1]/div[3]/div[1]/h3")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div[1]/div[3]/div[1]/h3")).getText(),
            "Сертификаты ЭП, выданные аккредитованными УЦ");
    WebElement webElement = driver.findElement(By.xpath("html/body/div[1]/div[3]/div[2]/form/fieldset"));
    List<WebElement> list2 = webElement.findElements(By.tagName("input"));
    Assert.assertEquals(list2.size(),9);
    driver.get(config.get("url4_4"));
    logger.info("Тестирование страницы "+config.get("url4_4"));
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div[1]/div[3]/div[1]/h3")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div[1]/div[3]/div[1]/h3")).getText(),
            "Переданные сертификаты");
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div[1]/div[3]/div[2]/form/fieldset/div[2]/table/tbody/tr/td/div/img")));

}
public void findVoskhod(){
    driver.findElement(By.xpath("//*[@id=\"FilterName\"]")).sendKeys("ВОСХОД");
    driver.findElement(By.xpath("/html/body/div/div[3]/div[2]/form/fieldset/table/tbody/tr[3]/td/input[1]")).click();
    driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
    Assert.assertTrue(driver.findElements(By.xpath("/html/body/div/div[3]/table/tbody/tr[.]")).size()>1);
}

    public boolean isElementPresent(By locator){
        List<WebElement> list = driver.findElements(locator);
        if (list.size()==0) {
            return false;
        }else return list.get(0).isDisplayed()&&list.get(0).isEnabled();
    }
@AfterClass
    public void closeDriver(){
    if (driver!=null){
        driver.close();
        driver.quit();
    }
}
}

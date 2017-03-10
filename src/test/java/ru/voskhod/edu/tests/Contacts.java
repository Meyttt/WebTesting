package ru.voskhod.edu.tests;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Александра on 20.04.2016.
 */
public class Contacts {
    private WebDriver driver;
    private Config config;
@BeforeClass
public void initDriver() throws IOException {
    System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
    config= new Config("config.properties");
    WebDriver driver= new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    this.driver=driver;
}
@Test
    public void contacts(){
    driver.get(config.get("url8"));
    ArrayList<String> test =new ArrayList<String>();
    test.add("^*Контактная информация");
    test.add("^*Справки по документам");
    test.add("^*Справочная");
    test.add("^*Факс");
    test.add("^*Адрес электронной почты");
    test.add("^*Адрес веб-сайта");
    test.add("^*Адрес");
    for (String str: test){
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(driver.findElement(By.xpath("html/body/div/div[3]")).getText());
        Assert.assertTrue(matcher.find());
    }

    //System.out.println(driver.findElement(By.xpath("html/body/div/div[3]")).getText());

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

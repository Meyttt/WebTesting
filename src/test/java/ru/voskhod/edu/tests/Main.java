package ru.voskhod.edu.tests;

import com.sun.javafx.binding.Logging;
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
import java.util.logging.LogManager;
//import java.util.logging.Logger;
import  org.apache.log4j.Logger;

public class Main {
    private WebDriver driver;
    private Config config;
    private Logger logger = Logger.getLogger(Main.class);


    @BeforeClass

    public void initDriver() throws IOException {
        System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
        config= new Config("config.properties");
        WebDriver driver= new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        this.driver=driver;
    }

    @Test

    public void test_Glavnaya(){
        driver.get(config.get("url"));
        logger.info("Тестирование страницы "+config.get("url"));
//        WebElement webElement = driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[1]/div"));
//        Assert.assertEquals(webElement.getText(),"Уполномоченный федеральный орган в области электронной подписи");
//        WebElement webElement1 = driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody"));
        //проверка меню справа на отображение-1
        Assert.assertEquals(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[1]/td[2]/a")),true);
        Assert.assertEquals(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td[2]/a")),true);
        Assert.assertEquals(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[5]/td[2]/a")),true);
//        Assert.assertEquals(isElementPresent(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[7]/td[2]/a")),true);
//        //проверка меню справа на отображение-2
//        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[1]/td[2]/a")).isDisplayed(),true);
//        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td[2]/a")).isDisplayed(),true);
//        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[5]/td[2]/a")).isDisplayed(),true);
//        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/table/tbody/tr/td[2]/div/table/tbody/tr[7]/td[2]/a")).isDisplayed(),true);
//        //проверка mainMenu
       // List<WebElement> list =
        //проверка числа и показа менюшек на главной
        List<WebElement> list = driver.findElements(By.xpath("//*[@id='mainMenu']/li[.]/a"));
        Assert.assertEquals(list.size(),8);

        for (int i=0; i<list.size();i++){
            Assert.assertEquals(list.get(i).isEnabled()&&list.get(i).isDisplayed(),true);
        }
        List<WebElement> list1 = driver.findElements(By.xpath("//*[@id='mainMenu']/li[2]/ul/li[.]/a"));
        Assert.assertEquals(list1.size(),3);
        List<WebElement> list2 = driver.findElements(By.xpath("//*[@id='mainMenu']/li[4]/ul/li[.]/a"));
        Assert.assertEquals(list2.size(),4);
        //driver.findElements(By.className("mainMenu"));
        //river.findElements(By.class("mainMenu"));
//        System.out.println(list.size()+" = list");
//        System.out.println(list1.size()+" = list1");
        //System.out.println(list.size());

    }

    public boolean isElementPresent (By locator){

        List<WebElement> list = driver.findElements(locator);
        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        if (list.size()==0){
            return  false;
        }else return (list.get(0).isDisplayed()&&list.get(0).isEnabled());

    }
    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}

package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import  java.util.regex.Matcher;
import  java.security.cert.X509Certificate;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Александра on 20.04.2016.
 */
public class Golovnoy_uc {
    public Golovnoy_uc(WebDriver driver) throws IOException {
        this.driver = driver;
        this.dir =(new File("data/docs")).getAbsolutePath();
        this.ext="cer";
        config = new Config("config.properties");
    }

    private WebDriver driver;
    private Config config;
    private String dir;
    private String ext;
    private Logger logger = Logger.getLogger(Golovnoy_uc.class);
@BeforeClass
    public void initDriver() throws IOException {


    System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
    config= new Config("config.properties");
    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    chromePrefs.put("profile.default_content_settings.popups", 0);
    chromePrefs.put("download.default_directory", dir);
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("prefs", chromePrefs);
    DesiredCapabilities cap = DesiredCapabilities.chrome();
    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    cap.setCapability(ChromeOptions.CAPABILITY, options);
    driver = new ChromeDriver(cap);
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

    }
@Test(description = "Скачивание и проверка сертификатов")
    public void certs() throws InterruptedException, IOException, CertificateException {
    driver.get(config.get("url3"));
    logger.info("Тестирование страницы "+config.get("url3"));
    driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
}
@Test
    public void test_uc(){
    driver.get(config.get("url3"));
    logger.info("Тестирование страницы "+config.get("url3") +" (разметка)");
    Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/div[1]")));
    Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]")).getText(),"Данный раздел содержит информацию о головном удостоверяющем центре");
    List<WebElement> list = driver.findElements(By.xpath("html/body/div/div[3]/div[2]/fieldset[.]/legend"));
    Pattern pattern = Pattern.compile("^ПАК?");
    int counter = 0;
    for (int i = 0; i<list.size(); i++){
        WebElement webElement = list.get(i);
        Matcher matcher = pattern.matcher(webElement.getText());
        if (matcher.find()) counter++;
    }

    Assert.assertEquals(counter,8);
}

    public boolean isElementPresent(By locator){
        List<WebElement> list= driver.findElements(locator);
        if (list.size()==0){
            return false;
        }else return (list.get(0).isEnabled()&&list.get(0).isDisplayed());
    }
@AfterClass
    public void closeDriver(){
    if (driver!=null){
        driver.close();;
        driver.quit();
    }
}

}


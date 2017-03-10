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
    private WebDriver driver;
    private Config config;
    private String dir;
    private String ext;
    private Logger logger = Logger.getLogger(Golovnoy_uc.class);
@BeforeClass
    public void initDriver() throws IOException {
    config = new Config("config.properties");
    this.dir =(new File("data/certs")).getAbsolutePath();
    this.ext="cer";
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
    logger.info("Тестирование страницы "+config.get("url3")+" (скачивание и проверка сертификатов)");
    driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
    List<WebElement> dates = driver.findElements(By.xpath("html/body/div/div[3]/div[2]/fieldset[.]/div/fieldset/fieldset/table/tbody/tr[4]/td[2]"));
    List<WebElement> certificatesElem = driver.findElements(By.xpath("html/body/div/div[3]/div[2]/fieldset[.]/div/fieldset/fieldset/table/tbody/tr[5]/td[2]/a"));
    List <WebElement> checkNumber = driver.findElements(By.xpath("html/body/div/div[3]/div[2]/fieldset[.]/div/fieldset/fieldset/table/tbody/tr[3]/td[2]"));
    File file = new File(dir);
    for (File myfile : file.listFiles()){
        if (myfile.getName().endsWith(ext)) myfile.delete();
    }
    for (int i=0; i<certificatesElem.size();i++){
        certificatesElem.get(i).click();
        Thread.sleep(3000);
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(ext);
            }
        });
        InputStream inStream = new FileInputStream(files[0]);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();

        Date dAfter = cert.getNotAfter();
        Date dBefore = cert.getNotBefore();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        Pattern pattern = Pattern.compile(format1.format(dBefore));
        Matcher matcher = pattern.matcher(dates.get(i).getText());
        Assert.assertTrue(matcher.find());
        pattern = Pattern.compile(format1.format(dAfter));
        matcher = pattern.matcher(dates.get(i).getText());
        Assert.assertTrue(matcher.find());
        BigInteger numberFact = cert.getSerialNumber();
        files[0].delete();
        BigInteger numberExp = new BigInteger(checkNumber.get(i).getText(),16);
        Assert.assertEquals(numberFact, numberExp );
    }
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
        System.out.println(webElement.getText());
        if (matcher.find()) counter++;
    }
//    Matcher matcher = pattern.matcher(driver.getPageSource());
//    //System.out.println(matcher.find());
//    while (matcher.find())counter++;
    Assert.assertEquals(counter,8);
    //System.out.println(list.size());
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


package ru.voskhod.edu.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.log4j.*;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Александра on 19.04.2016.
 */
public class Accred {
    ChromeDriver driver;
    private Config config;
    String dir;
    String ext;
    Logger logger;
    File file;

    @BeforeClass
    public void initDriver() throws IOException {
        SSLTool.disableCertificateValidation();
        logger = Logger.getLogger(Accred.class);
        config = new Config("config.properties");
        file = new File("data/docs");
        this.dir = file.getAbsolutePath();
        this.driver = driver;
        this.ext = "pdf";
        System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");
        config = new Config("config.properties");
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", dir);
        chromePrefs.put("plugins.plugins_disabled", new String[]{
                "Chrome PDF Viewer", "Adobe Flash Player", "PDF Viewer"
        });
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        cap.setCapability("enablePersistentHover", true);
        driver = new ChromeDriver(cap);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        File[] filelist = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(ext);
            }
        });
        for (int i = 0; i < filelist.length; i++) {
            filelist[i].delete();

        }
    }



    //@BeforeMethod
//public  void cleanDirectory() {
//    File[] filelist = file.listFiles(new FilenameFilter() {
//        public boolean accept(File dir, String name) {
//            return name.toLowerCase().endsWith(ext);
//        }
//    });
//    int lenght = filelist.length;
//    for (int i = 0; i < filelist.length; i++) {
//        filelist[i].delete();
//
//    }
//}
    @Test
    //порядок аккредитации
    public void test_accred() throws InterruptedException, IOException {
        driver.get(config.get("url2_1"));
        logger.info("Тестирование страницы " + config.get("url2_1"));
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]")).getText(), "Данный раздел содержит информацию об установленном порядке аккредитации удостоверяющих центров");
        //аккредитованные уц
        driver.get(config.get("url2_2"));
        logger.info("Тестирование страницы " + config.get("url2_2"));
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        Assert.assertEquals(isElementPresent(By.xpath("html/body/div/div[3]/div[1]/div[1]")), true);
        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]/div[1]")).getText(), "Данный раздел содержит перечень аккредитованных удостоверяющих центров");
        List<WebElement> list = driver.findElements(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/td[2]"));
        Assert.assertTrue(list.size() > 1);
        //проверки уц
        driver.get(config.get("url2_3"));
        logger.info("Тестирование страницы " + config.get("url2_3"));
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        isElementPresent(By.xpath("html/body/div/div[3]/div[1]"));
        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[1]")).getText(), "Данный раздел содержит информацию о порядке проверки соблюдения аккредитованными удостоверяющими центрами установленных требований");
        List<WebElement> targetList = driver.findElements(By.xpath("//@target/.." +
                ""));
        Assert.assertEquals(targetList.size(), 4);
        int pdf=0;
        for (WebElement currentElement : targetList) {
            String windowHandleBefore = driver.getWindowHandle();
            String href = currentElement.getAttribute("href");
            if (href.contains(".pdf")){
                if(pdf<1) {
                    downloadPdf(href);
                    pdf++;
                }else{continue;}

            }else {
                currentElement.click();
                Thread.sleep(2000);
                if (driver.getWindowHandles().size() > 1) {
                    Set<String> windows = driver.getWindowHandles();
                    windows.remove(windowHandleBefore);
                    for (String windowHandle : windows) {
                        driver.switchTo().window(windowHandle);
                        Thread.sleep(2000);
                        try {

                            WebElement findRes = driver.findElement(By.xpath("/html/body/header/div[1]/div/div/span[1]/span"));

                            logger.info("Переход на страницу " + href + " выполнен.");
                            driver.close();
                        } catch (NoSuchElementException e) {
                            logger.error("Не удалось выполнить переход на страницу " + href);
                        }
                        try {
                            driver.switchTo().window(windowHandleBefore);
                        } catch (NoSuchWindowException e) {
                            logger.error("Не удалось вернуться на предыдущую стрaницу.");
                        }

                    }
                }
            }
        }
        if (!file.exists()) logger.error("Директория не существует");
        Thread.sleep(3000);
        File[] filelist = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(ext);
            }
        });
        int lenght = filelist.length;
        for (int i = 0; i < filelist.length; i++) {
            filelist[i].delete();
        }
        Assert.assertEquals(lenght, 1);

//    driver.get(config.get("url3"));driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
//    List<WebElement> list1=driver.findElements(By.partialLinkText("Постановлением"));
//    Assert.assertEquals(list1.size(),1);
//    Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div[2]/fieldset[6]/legend")).getText(),"ПАК \"УЦ 2 ИС ГУЦ\"");
    }

    private boolean isElementPresent(By locator) {
        List<WebElement> list = driver.findElements(locator);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        if (list.size() == 0) {
            return false;
        } else return list.get(0).isDisplayed() && list.get(0).isEnabled();
    }

    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }

    }

    public void downloadPdf(String currentUrl) throws IOException {
        URL website = new URL(currentUrl);
        String name = "data/docs/"+currentUrl.split('/' + "")[currentUrl.split('/' + "").length - 1];
        FileUtils.copyURLToFile(website,new File(name));

    }
}
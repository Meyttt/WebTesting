package ru.voskhod.edu.tests;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpConnection;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
//import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.internal.ApacheHttpClient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sun.nio.cs.StreamDecoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Александра on 20.04.2016.
 */
public class Norm_Docs {
    private WebDriver driver;
    private Config config;
    String dir;
    String ext ;
    Logger logger = Logger.getLogger(Norm_Docs.class);
    File file;

    public Norm_Docs(WebDriver driver) throws IOException {
        this.driver = driver;
        config = new Config("config.properties");
        file = new File("data/docs");
        dir = file.getAbsolutePath();
        ext = "pdf";
    }

    @BeforeClass
    public void initDriver() throws IOException {
        SSLTool.disableCertificateValidation();
        System.setProperty("webdriver.chrome.driver", "data/chromedriver.exe");

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", dir);
        chromePrefs.put("plugins.plugins_disabled", new String[]{
                "Adobe Flash Player",
                "Chrome PDF Viewer"
        });
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(cap);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }





    @Test
    public void norm_docs() {
        driver.get(config.get("url7"));
        logger.info("Тестирование страницы " + config.get("url7") + " (разметка)");
        Assert.assertTrue(isElementPresent(By.xpath("html/body/div/div[3]/div")));
        Assert.assertEquals(driver.findElement(By.xpath("html/body/div/div[3]/div")).getText(),
                "Данный раздел содержит подборку нормативных документов в области использования электронной подписи и деятельности удостоверяющих центров");
        List<WebElement> list = driver.findElements(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/td[1]"));
        Assert.assertEquals(list.size(), 7);
        List<WebElement> list1 = driver.findElements(By.xpath("html/body/div/div[3]/table/tbody/tr[.]/td[2]/table/tbody/tr[.]/td[2]/a"));
        Assert.assertEquals(list1.size(), 31);
        for (int i = 0; i < list1.size(); i++) {
            Assert.assertTrue(list1.get(i).isDisplayed() && list1.get(i).isEnabled());
        }

    }


    public boolean isElementPresent(By locator) {
        List<WebElement> list = driver.findElements(locator);
        if (list.size() == 0) {
            return false;
        } else return list.get(0).isEnabled() && list.get(0).isDisplayed();
    }

    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

}

package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.util.Date;

/**
 * Created by a.chebotareva on 06.04.2017.
 */
public class Runner {
    public static void main(String[] args) throws IOException, InterruptedException, CertificateException, URISyntaxException {
        Logger logger = Logger.getLogger(Runner.class);
        boolean error = false;
        File log = new File(new File(".").getAbsolutePath()+"\\..\\"+"log.txt");
        FileWriter fileWriter = new FileWriter(log,true);
        logger.info("********Тестирование портала УФО от "+new Date()+"********");
        Accred accred=null;
        try {
            try {
                accred = new Accred();
                accred.initDriver();
                accred.test_accred();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + accred.driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + accred.driver.getCurrentUrl());
                error = true;
            }
            WebDriver driver = accred.driver;
            Accreded_uc accreded_uc = new Accreded_uc(driver);
            try {
                accreded_uc.test_reestry();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Contacts contacts = new Contacts(driver);
            try {

                contacts.contacts();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Golovnoy_uc golovnoy_uc = new Golovnoy_uc(driver);
            try {

                golovnoy_uc.certs();
                golovnoy_uc.test_uc();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getCause());
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Main main = new Main(driver);
            try {

                main.test_Glavnaya();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Monitoring monitoring = new Monitoring(driver);
            try {

                monitoring.monitoring();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Norm_Docs norm_docs = new Norm_Docs(driver);
            try {
                norm_docs.norm_docs();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            }
            Ob_ident ob_ident = new Ob_ident(driver);
            try {

                ob_ident.ob_ident();
            } catch (Exception e) {
                logger.error(stackTraceToString(e));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } catch (AssertionError e1) {
                logger.error(stackTraceToString(e1));
                logger.error("Ошибка на странице " + driver.getCurrentUrl());
                error = true;
            } finally {
                ob_ident.closeDriver();
            }
            if (driver != null) {
                driver.quit();
            }
            if (!error) {
                logger.info("Проверка прошла успешно.");
                fileWriter.append("Тестирование портала УФО прошло успешно\r\n");
                fileWriter.flush();
                System.exit(0);
            } else {
                logger.error("Проверка провалена.");
                fileWriter.append("Тестирование портала УФО провалено\r\n");
                fileWriter.flush();
                System.exit(1);
            }
        }catch (Exception e){
            logger.error("Произошла непредвиденная ошибка.");
            logger.error(stackTraceToString(e));
            logger.error("Проверка провалена.");
            fileWriter.append("Тестирование портала УФО провалено\r\n");
            fileWriter.flush();
            System.exit(1);
        }
    }
    public static String stackTraceToString(Throwable e){
        StringBuilder stringBuilder = new StringBuilder();
        for(StackTraceElement stackTraceElement:e.getStackTrace()){
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}

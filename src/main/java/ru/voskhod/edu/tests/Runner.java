package ru.voskhod.edu.tests;

import org.apache.log4j.Logger;

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
        logger.warn("Тестирование сайта ГУЦ от "+new Date());
        Accred accred = new Accred();
        try{
            accred.initDriver();
            accred.test_accred();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            accred.closeDriver();
        }
        Accreded_uc accreded_uc = new Accreded_uc();
        try {
            accreded_uc.initDriver();
            accreded_uc.test_reestry();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            accreded_uc.closeDriver();
        }
        Contacts contacts = new Contacts();
        try{
            contacts.initDriver();
            contacts.contacts();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            contacts.closeDriver();
        }
        Golovnoy_uc golovnoy_uc = new Golovnoy_uc();
        try{
            golovnoy_uc.initDriver();
            golovnoy_uc.certs();
            golovnoy_uc.test_uc();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            golovnoy_uc.closeDriver();
        }
        Main main = new Main();
        try{
            main.initDriver();
            main.test_Glavnaya();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            main.closeDriver();
        }
        Monitoring monitoring = new Monitoring();
        try{
            monitoring.initDriver();
            monitoring.monitoring();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            monitoring.closeDriver();
        }
        Norm_Docs norm_docs = new Norm_Docs();
        try{
            norm_docs.initDriver();
            norm_docs.testHttp();
            norm_docs.norm_docs();
            norm_docs.files();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            norm_docs.closeDriver();
        }
        Ob_ident ob_ident = new Ob_ident();
        try{
            ob_ident.initDriver();
            ob_ident.ob_ident();
        }catch (Exception e){
            logger.error(e.getCause());
            error=true;
        }finally {
            ob_ident.closeDriver();
        }
        if(!error){
            logger.warn("Проверка прошла успешно.");
        }else{
            logger.error("Проверка провалена.");
        }
    }
}

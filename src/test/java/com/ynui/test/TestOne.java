package com.ynui.test;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class TestOne {

    public static void delay(int seconds){
        try
        {
            Thread.sleep(seconds*1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public  static WebDriver drv;
    public  static int expectedCount;
    public  static List<String> results;
    public  static int resultsIndexForTitleCheck;


    @AfterClass
    public static void oneTimeTearDown() {
        drv.quit();
    }

    @BeforeClass
    public static void oneTimeSetUp() {
        expectedCount = 10;
        results = new ArrayList<String>();
        resultsIndexForTitleCheck = 0;

        System.setProperty("webdriver.gecko.driver", "/home/i/Downloads/geckodriver");

        drv = new FirefoxDriver();

        drv.manage().window().maximize();
        drv.get("https://yandex.ru");
        //drv.navigate().to("https://yandex.ru/");

        Assert.assertEquals("Title check failed!",
                "Яндекс", drv.getTitle());


        WebElement mkt = drv.findElement(By.linkText("Маркет"));
        mkt.click();

        WebElement cmp = drv.findElement(By.linkText("Компьютеры"));
        cmp.click();

        WebElement tbl = drv.findElement(By.xpath("html/body/div[1]/div[4]/div[1]/div/div[1]/div/a[1]"));
        tbl.click();


        drv.findElement(By.xpath("//*[@id=\"glf-pricefrom-var\"]")).sendKeys("20000");
        drv.findElement(By.xpath("//*[@id=\"glf-priceto-var\"]")).sendKeys("25000");

        drv.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[2]/div[2]/div/div[4]/div[2]/div/div[2]/button")).click();

        WebDriverWait wait = new WebDriverWait(drv, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1) > input:nth-child(1)")));
        drv.findElement(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1) > input:nth-child(1)")).sendKeys("Acer|Dell");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)")));
        drv.findElement(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > span:nth-child(1) > span:nth-child(1)")));
        drv.findElement(By.cssSelector("div.n-filter-block:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > span:nth-child(1) > span:nth-child(1)")).click();


        drv.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        drv.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[2]/div[2]/div/div[30]/div[1]/button")).click();

        drv.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);


        //List<WebElement> lst = drv.findElements(By.tagName("a"));
        //System.out.println(lst.size());

        //drv.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //int resultsTotal = 0;
        //for(int Counter = 0; Counter < lst.size(); Counter++){

        //    WebElement element = lst.get(Counter);

        //    try {

        //        String cls = element.getAttribute("href");
        //        String txt = element.getText();
        //        System.out.println(cls);
        //        System.out.println(txt);

        //if (txt.contains("Acer")) {

        //    System.out.println(txt);
        //    resultsTotal++;
        // }


        //  }
        //  catch(StaleElementReferenceException e){
        //      System.out.println(element.toString());


        //String cls = element.getAttribute("href");
        //String txt = element.getText();


        //      continue;}

        //}

        //System.out.println(resultsTotal);

        delay(5);

        JavascriptExecutor js = (JavascriptExecutor) drv;
        ArrayList<Map<String, Object>> link = (ArrayList<Map<String, Object>>) js.executeScript("var all = document.getElementsByTagName(\"a\");" +
                "var res = [];" +
                "for (i = 0; i < all.length; i++)" +
                "{" +
                "var item = {};" +
                "item.url = all[i].href; item.txt = all[i].text;" +
                "res[i] = item;" +
                "}" +
                "return res;");

        for (Map<String, Object> entry : link) {
            String resultName = entry.get("txt").toString();
            String resultUrl = entry.get("url").toString();

            if (resultUrl.contains("product") && (resultName.contains("Acer") || resultName.contains("Dell"))) {
                results.add(resultName);
                //System.out.println(resultName);
            }


        }
    }




    @Test
    public void resultsCount() {
        Assert.assertEquals("Unexpected results count!", expectedCount, results.size());
    }

    @Test
    public void productTitle(){
        String expectedTitle = results.get(resultsIndexForTitleCheck);
        drv.findElement(By.id("header-search")).sendKeys(expectedTitle);
        drv.findElement(By.cssSelector("button.button2")).click();

        delay(5);

        String foundProductTitle = drv.findElement(By.cssSelector("h1.title")).getText();

        System.out.println(expectedTitle);
        System.out.println(foundProductTitle);

        Assert.assertEquals("Title check failed!",
                expectedTitle, foundProductTitle);

    }

}

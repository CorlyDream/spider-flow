package org.spiderflow.selenium.io;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.io.SpiderResponse;
import org.spiderflow.model.CookieDto;

import java.util.*;

public class SeleniumResponse implements SpiderResponse {

    private static final Logger log = LoggerFactory.getLogger(SeleniumResponse.class);
    private WebDriver driver;

    private Actions actionsObj;

    public SeleniumResponse(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public int getStatusCode() {
        return 0;
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public byte[] getBytes() {
        return null;
    }

    @Override
    public String getHtml() {
        return driver.getPageSource();
    }

    @Override
    public Map<String, String> getCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        Map<String, String> cookieMap = new HashMap<>();
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
        return cookieMap;
    }

    @Override
    public List<CookieDto> getCookieList() {
        Set<Cookie> cookies = driver.manage().getCookies();
        List<CookieDto> cookieList = new ArrayList<>(cookies.size());
        for (Cookie cookie : cookies) {
            CookieDto cookieDto = new CookieDto();
            cookieDto.setName(cookie.getName());
            cookieDto.setValue(cookie.getValue());
            if (cookie.getDomain().startsWith(".")) {
                cookieDto.setDomain(cookie.getDomain());
            } else {
                // 浏览器会自动在 domain 前面加上 . ，这里手动加上
                cookieDto.setDomain("." + cookie.getDomain());
            }
            cookieDto.setPath(cookie.getPath());
            cookieDto.setExpiry(cookie.getExpiry());
            cookieList.add(cookieDto);
        }
        return cookieList;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public SeleniumResponse switchTo(int index) {
        driver.switchTo().frame(index);
        return this;
    }

    public SeleniumResponse switchTo(String name) {
        driver.switchTo().frame(name);
        return this;
    }

    public SeleniumResponse switchTo(WebElement element) {
        driver.switchTo().frame(element);
        return this;
    }

    public SeleniumResponse switchToDefault() {
        driver.switchTo().defaultContent();
        return this;
    }

    public void quit() {
        try {
            driver.quit();
        } catch (Exception ignored) {
            log.error("关闭浏览器出错，异常信息：{}", ignored);
        }
    }

    public Actions action() {
        if (actionsObj == null) {
            this.actionsObj = new Actions(this.driver);
        }
        return this.actionsObj;
    }

    public void clearAction() {
        this.actionsObj = null;
    }
}

package org.spiderflow.context;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.model.CookieDto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Cookie上下文
 */
public class CookieContext {
    private static final Logger log = LoggerFactory.getLogger(CookieContext.class);

    private Collection<CookieDto> cookies = new HashSet<>();

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    public void addCookie(CookieDto cookie) {
        this.cookies.add(cookie);
    }

    public void addCookies(Collection<CookieDto> cookies) {
        this.cookies.addAll(cookies);
    }

    /**
     * 手动配置的 cookie
     * @param name
     * @param value
     */
    public void addCookie(String name, String value, String domain) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(value)) {
            return;
        }
        CookieDto cookieDto = new CookieDto();
        cookieDto.setName(name);
        cookieDto.setValue(value);
        cookieDto.setDomain(domain);
        cookieDto.setPath("/");
        cookieDto.setExpiry(DateUtils.addMonths(new Date(), 1));
        if (cookies.contains(cookieDto)) {
            // 以最新的 cookie 为准
            cookies.remove(cookieDto);
        }
        cookies.add(cookieDto);
    }

    public Collection<CookieDto> getCookies() {
        return cookies;
    }


    public Collection<CookieDto> getUrlCookies(String url) {
        try {
            return getUrlCookies(new URL(url));
        } catch (MalformedURLException e) {
            log.error("url is not valid {} 获取配置的 cookie 失败", url, e);
        }
        return Collections.emptyList();
    }

    public Collection<CookieDto> getUrlCookies(URL url) {
        String domain = url.getHost();
        String path = url.getPath();
        return cookies.stream().filter(cookie -> {
            if (domainEqual(cookie.getDomain(), domain)) {
                if (StringUtils.isBlank(cookie.getPath()) || cookie.getPath().equals("/")) {
                    return true;
                }
                return path.startsWith(cookie.getPath());
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * cookie
     * @param cookieDomain
     * @param urlDomain
     * @return
     */
    private boolean domainEqual(String cookieDomain, String urlDomain) {
        if (cookieDomain.equalsIgnoreCase(urlDomain) || StringUtils.isBlank(cookieDomain)) {
            return true;
        }
        if (cookieDomain.startsWith(".")) {
            String subDomain = cookieDomain.substring(1);
            return urlDomain.endsWith(subDomain);
        }
        return false;
    }

    @Override
    public String toString() {
        return cookies.toString();
    }
}

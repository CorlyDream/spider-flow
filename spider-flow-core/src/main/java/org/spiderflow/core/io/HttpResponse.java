package org.spiderflow.core.io;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.spiderflow.io.SpiderResponse;
import org.spiderflow.model.CookieDto;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 响应对象包装类
 *
 * @author Administrator
 */
public class HttpResponse implements SpiderResponse {

    private Response response;

    private CookieStore cookieStore;

    private int statusCode;

    private String urlLink;

    private String htmlValue;

    private String titleName;

    private Object jsonValue;

    public HttpResponse(Response response, CookieStore cookieStore) {
        super();
        this.response = response;
        this.cookieStore = cookieStore;
        this.statusCode = response.statusCode();
        this.urlLink = response.url().toExternalForm();
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getTitle() {
        if (titleName == null) {
            synchronized (this) {
                titleName = Jsoup.parse(getHtml()).title();
            }
        }
        return titleName;
    }

    @Override
    public String getHtml() {
        if (htmlValue == null) {
            synchronized (this) {
                htmlValue = response.body();
            }
        }
        return htmlValue;
    }

    @Override
    public Object getJson() {
        if (jsonValue == null) {
            jsonValue = JSON.parse(getHtml());
        }
        return jsonValue;
    }

    @Override
    public Map<String, String> getCookies() {
        return response.cookies();
    }

    @Override
    public List<CookieDto> getCookieList() {
        List<HttpCookie> cookies = cookieStore.getCookies();
        List<CookieDto> cookieDtos = new ArrayList<>();
        for (HttpCookie cookie : cookies) {
            CookieDto cookieDto = new CookieDto();
            cookieDto.setName(cookie.getName());
            cookieDto.setValue(cookie.getValue());
            if (cookie.getDomain().startsWith(".")) {
                cookieDto.setDomain(cookie.getDomain());
            } else {
                cookieDto.setDomain("." + cookie.getDomain());
            }
            cookieDto.setPath(cookie.getPath());
            cookieDto.setExpiry(getCookieExpire(cookie));

            cookieDtos.add(cookieDto);
        }

        return cookieDtos;
    }

    public Date getCookieExpire(HttpCookie cookie) {
        if (cookie.getMaxAge() < 1) {
            return null;
        }
        Field field = ReflectionUtils.findField(HttpCookie.class, "whenCreated");
        if (field == null) {
            return new Date(cookie.getMaxAge() * 1000 + System.currentTimeMillis());
        }
        field.setAccessible(true);
		long whenCreated = (long) ReflectionUtils.getField(field, cookie);
		return new Date(whenCreated + cookie.getMaxAge() * 1000);
    }

    @Override
    public Map<String, String> getHeaders() {
        return response.headers();
    }

    @Override
    public byte[] getBytes() {
        return response.bodyAsBytes();
    }

    @Override
    public String getContentType() {
        return response.contentType();
    }

    @Override
    public void setCharset(String charset) {
        this.response.charset(charset);
    }

    @Override
    public String getUrl() {
        return urlLink;
    }

    @Override
    public InputStream getStream() {
        return response.bodyStream();
    }
}

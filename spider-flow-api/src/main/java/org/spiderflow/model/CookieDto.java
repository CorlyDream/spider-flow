package org.spiderflow.model;

import java.util.Date;
import java.util.Objects;

public class CookieDto {
    /**
     * cookie名
     */
    private String name;

    /**
     * cookie值
     */
    private String value;

    /**
     * cookie域名
     */
    private String domain;

    /**
     * cookie路径
     */
    private String path;

    /**
     * cookie过期时间
     */
    private Date expiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\",\"value\":\"" + value + "\",\"domain\":\"" + domain + "\",\"path\":\"" + path + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CookieDto cookieDto = (CookieDto) o;
        return Objects.equals(name, cookieDto.name) && Objects.equals(domain, cookieDto.domain) && Objects.equals(path, cookieDto.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, path);
    }
}

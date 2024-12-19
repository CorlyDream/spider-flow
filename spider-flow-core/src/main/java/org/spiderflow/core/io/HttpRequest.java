package org.spiderflow.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import org.htmlunit.jetty.util.HttpCookieStore;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 请求对象包装类
 * @author Administrator
 *
 */
public class HttpRequest {
	
	private Connection connection = null;
	
	public static HttpRequest create(){
		return new HttpRequest();
	}
	
	public HttpRequest url(String url){
		this.url(url, Method.GET);
		return this;
	}
	public HttpRequest url(String url, Method method){
		this.connection = Jsoup.connect(url);
		this.connection.method(method);
		this.connection.timeout(60000);
		return this;
	}
	
	public HttpRequest headers(Map<String,String> headers){
		this.connection.headers(headers);
		return this;
	}
	
	public HttpRequest header(String key,String value){
		this.connection.header(key, value);
		return this;
	}
	
	public HttpRequest header(String key,Object value){
		if(value != null){
			this.connection.header(key,value.toString());
		}
		return this;
	}

	public HttpRequest cookies(Map<String,String> cookies){
		this.connection.cookies(cookies);
		return this;
	}

	public HttpRequest cookie(String name, String value) {
		if (value != null) {
			this.connection.cookie(name, value);
		}
		return this;
	}
	
	public HttpRequest contentType(String contentType){
		this.connection.header("Content-Type", contentType);
		return this;
	}
	
	public HttpRequest data(String key,String value){
		this.connection.data(key, value);
		return this;
	}
	
	public HttpRequest data(String key,Object value){
		if(value != null){
			this.connection.data(key, value.toString());
		}
		return this;
	}
	
	public HttpRequest data(String key,String filename,InputStream is){
		this.connection.data(key, filename, is);
		return this;
	}
	
	public HttpRequest data(Object body){
		if(body != null){
			this.connection.requestBody(body.toString());	
		}
		return this;
	}
	
	public HttpRequest data(Map<String,String> data){
		this.connection.data(data);
		return this;
	}
	
	public HttpRequest method(String method){
		this.connection.method(Method.valueOf(method));
		return this;
	}
	
	public HttpRequest followRedirect(boolean followRedirects){
		this.connection.followRedirects(followRedirects);
		return this;
	}
	
	public HttpRequest timeout(int timeout){
		this.connection.timeout(timeout);
		return this;
	}
	
	public HttpRequest proxy(String host,int port){
		this.connection.proxy(host, port);
		return this;
	}
	public HttpRequest validateTLSCertificates(boolean value){
		if (!value) {
			this.connection.sslSocketFactory(socketFactory());
		}
		return this;
	}

	public HttpResponse execute() throws IOException{
		this.connection.ignoreContentType(true);
		this.connection.ignoreHttpErrors(true);
		this.connection.maxBodySize(0);
		CookieStore cookieStore = new HttpCookieStore();
		this.connection.cookieStore(cookieStore);
		Response response = connection.execute();
		return new HttpResponse(response, cookieStore);
	}

	static private SSLSocketFactory socketFactory() {
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}};

		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException("Failed to create a SSL socket factory", e);
		}
	}
}

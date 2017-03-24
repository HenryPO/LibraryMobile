package com.mobile.library.http.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.mobile.library.http.bean.HttpPair;
import com.mobile.library.utils.MLog;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.content.Context;

/**
 * 
 * @ClassName: HttpUtil
 * @Description: Http请求管理工具
 * @author lhy
 * @date 2014-10-9 上午11:16:46
 * 
 */
public class HttpUtil {

	private Context context;

	private OkHttpClient client;

	private static int CONNECTION_TIMEOUT = 6 * 1000;
	private static int SO_TIMEOUT = 6 * 1000;

	public HttpUtil(Context context) {
		this.context = context;
		client = new OkHttpClient();
		client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setReadTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);

	}

	/**
	 * 设置连接超时
	 * 
	 * @param time
	 */
	public void setConnectionTimeOut(int time) {
		CONNECTION_TIMEOUT = time;
	}

	/**
	 * 设置读取超时
	 * 
	 * @param time
	 */
	public void setSoTimeOut(int time) {
		SO_TIMEOUT = time;
	}

	/**
	 * 
	 * @Title: get @Description:get请求 @param @param context @param @param
	 *         url @param @param para @param @param cls @param @return
	 *         设定文件 @return T 返回类型 @throws
	 */
	public <T> T get(String url, ArrayList<HttpPair> para, Type cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(get(url, para), cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: get @Description: get请求 @param @param context @param @param
	 *         url @param @param para @param @return 设定文件 @return String
	 *         返回类型 @throws
	 */
	public String get(String url, ArrayList<HttpPair> para) throws IOException {
		Response response = getResponse(url, para);
		if (response == null) {
			return null;
		}
		int statusCode = response.code();
		MLog.i("HttpStatus=>" + statusCode);
		// 判断请求是否成功
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String body = null;
			body = response.body().string();
			MLog.i("请求服务器端成功=>\n" + body);
			return body;
		}
		return null;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: getResponse @Description: get请求 @param @param
	 *         context @param @param url @param @param para @param @return
	 *         设定文件 @return HttpResponse 返回类型 @throws
	 */
	public Response getResponse(String url, ArrayList<HttpPair> para) throws IOException {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url);
		if (para != null) {
			urlBuilder.append("?");
			for (int i = 0; i < para.size(); i++) {
				HttpPair pair = para.get(i);
				urlBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8")).append('=')
						.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
				urlBuilder.append('&');
			}
			urlBuilder.delete(urlBuilder.length() - 1, urlBuilder.length());
		}
		MLog.i("getRequest=>", urlBuilder.toString());

		Request request = new Request.Builder().url(urlBuilder.toString()).build();
		client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setReadTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		Response response = client.newCall(request).execute();
		return response;

	}

	/**
	 * 
	 * @Title: post @Description: post请求 @param @param context @param @param
	 *         url @param @param para @param @param cls @param @param
	 *         mStream @param @return 设定文件 @return T 返回类型 @throws
	 */
	public <T> T post(String url, ArrayList<HttpPair> para, Type cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(post(url, para), cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: post @Description: post请求 @param @param context @param @param
	 *         url_Str @param @param pairs @param @param mStream @param @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	public String post(String url_Str, ArrayList<HttpPair> pairs) throws IOException {
		Response httpResponse = postResponse(url_Str, pairs);
		if (httpResponse == null) {
			return null;
		}
		int statusCode = httpResponse.code();
		MLog.i("HttpStatus+>" + statusCode);
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String body = null;
			body = httpResponse.body().string();
			MLog.i("请求服务器端成功=>\n" + body);
			return body;
		}
		return null;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: postResponse @Description: Post请求 @param @param
	 *         context @param @param url_Str @param @param pairs @param @param
	 *         mStream @param @return 设定文件 @return HttpResponse 返回类型 @throws
	 */
	public Response postResponse(String url_Str, ArrayList<HttpPair> pairs) throws IOException {
		FormEncodingBuilder formBody = new FormEncodingBuilder();
		MLog.v("URL="+url_Str);
		for (HttpPair pair : pairs) {
			formBody.add(pair.getName(), pair.getValue());
			MLog.v("KEY="+pair.getName()+"=====VALUE="+ pair.getValue());
		}

		Request request = new Request.Builder().url(url_Str).post(formBody.build()).build();
		client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setReadTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		Response response = client.newCall(request).execute();

		return response;
	}

	/**
	 * 
	 * @Title: post @Description: post请求 @param @param context @param @param
	 *         url @param @param para @param @param cls @param @param
	 *         mStream @param @return 设定文件 @return T 返回类型 @throws
	 */
	public <T> T post(String url, String xmlData, Type cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(post(url, xmlData), cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 
	 * @throws IOException
	 * @Title: post @Description: post请求 @param @param context @param @param
	 *         url_Str @param @param pairs @param @param mStream @param @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	public String post(String url_Str, String xmlData) throws IOException {
		Response httpResponse = postResponse(url_Str, xmlData);
		if (httpResponse == null) {
			return null;
		}
		int statusCode = httpResponse.code();
		MLog.i("HttpStatus+>" + statusCode);
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String body = null;
			body = httpResponse.body().string();
			MLog.i("请求服务器端成功=>\n" + body);
			return body;
		}
		return null;
	}

	/**
	 * 
	 * @param url_Str
	 * @param xmlData
	 * @return
	 * @throws IOException
	 */
	public Response postResponse(String url_Str, String xmlData) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), xmlData);

		Request request = new Request.Builder().url(url_Str).post(body).build();
		client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setReadTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		Response response = client.newCall(request).execute();

		return response;
	}

	/**
	 * 
	 * @param url_Str
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String postFile(String url_Str,  String key, String filePath) throws IOException {
		Response httpResponse = postFileResponse(url_Str, key, filePath);
		if (httpResponse == null) {
			return null;
		}
		int statusCode = httpResponse.code();
		MLog.i("HttpStatus+>" + statusCode);
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String body = null;
			body = httpResponse.body().string();
			MLog.i("请求服务器端成功=>\n" + body);
			return body;
		}
		return null;
	}

	/**
	 * 
	 * @param filePath
	 * @param xmlData
	 * @return
	 * @throws IOException
	 */
	public Response postFileResponse(String url_Str, String key, String filePath)
			throws IOException {
		File file = new File(filePath);
		RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
		RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
				.addFormDataPart(key, file.getName(), fileBody).build();
		// RequestBody body =
		// RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),
		// new File(filePath));

		Request request = new Request.Builder().url(url_Str).post(requestBody).build();
		client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setWriteTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		client.setReadTimeout(SO_TIMEOUT, TimeUnit.MILLISECONDS);
		Response response = client.newCall(request).execute();

		return response;
	}

	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(path);
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		return contentTypeFor;
	}

}

package com.mobile.library.http.bean;



/**
 * 
 * Description 三个参数的键值对<br>
 * CreateDate 2014-5-13 <br>
 * 
 * @author qujl <br>
 */
public class HttpPair  {

	private String name;
	private String value;

	public HttpPair(String name, String value) {
		this.name = name;
		this.value = value;
	}



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

}

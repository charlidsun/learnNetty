package com.sun.heartclient.client;

import java.io.Serializable;

import lombok.Data;

/**
 * POJO,序列化
 * @author Administrator
 *
 */
@Data
public class CustomProtocol implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String content;
	
	
	public CustomProtocol(long id, String content) {
		super();
		this.id = id;
		this.content = content;
	}



	public CustomProtocol() {
		super();
	}
	
	
	
}

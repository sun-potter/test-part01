package com.self.util;

import java.util.UUID;

public class UUIDUtil {

	public static String create(){
		 String uuid = UUID.randomUUID().toString();
		 return uuid;
	}
	public static String createNotLink(){
		String uuid = create().replaceAll("-", "");
		 return uuid;
	}
	
	public static void main(String[] args) {
		System.out.println(createNotLink());
	}
}

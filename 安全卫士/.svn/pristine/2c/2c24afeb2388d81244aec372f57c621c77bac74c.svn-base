package com.example.reviewmobile.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
	public static String encode(String password) {
		//String password = "123456";
		
		try {
			byte[] digest = MessageDigest.getInstance("MD5").digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			
			for (byte b : digest) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				//System.out.println(hexString);

				if(hexString.length()<2){
					hexString="0"+hexString;
				}

				sb.append(hexString);
			}
			//System.out.println("MD5: "+sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}


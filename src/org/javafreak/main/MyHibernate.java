package org.javafreak.main;

import org.javafreak.service.MyService;

// This project covers from 1 to 18 of the vedios
public class MyHibernate {

	public static void main(String[] args) {
		MyService service=new MyService();
		
		System.out.println(service.saveUserDetails(service.createDummyObject(1)));
		System.out.println(service.getUserDetails());
	}

}

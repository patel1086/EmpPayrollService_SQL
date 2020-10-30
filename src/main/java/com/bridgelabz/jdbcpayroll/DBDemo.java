package com.bridgelabz.jdbcpayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {
	public static void main(String[] args) {
		String jdbcURL="jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
		String username="root";
		String password="1234";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cant finr the driver in the classpath",e);
		}
		listDrivers();
		try {
			System.out.println("connecting to database: "+jdbcURL);
			connection=DriverManager.getConnection(jdbcURL,username,password);
			System.out.println("Connection established successfully!!!!"+connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private static void listDrivers() {
		Enumeration<Driver>driverList=DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass=(Driver)driverList.nextElement();
			System.out.println(" "+driverClass.getClass().getName());
		}
	}

}

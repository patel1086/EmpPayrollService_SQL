package com.bridgelabz.jdbcpayroll;

import java.util.*;
import java.io.IOException;
import java.lang.NullPointerException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeePayrollService {
	LocalDate localDate = null;
	DateTimeFormatter formatter = null;

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	public ArrayList<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
	}

	public EmployeePayrollService(ArrayList<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();

	}

	public ArrayList<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = (ArrayList<EmployeePayrollData>) new EmployeePayrollDBService().readData();
		return this.employeePayrollList;

	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = new EmployeePayrollDBService().updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.salary = salary;
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.Name.equals(name)).findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		ArrayList<EmployeePayrollData> employeePayrollDataList = new ArrayList<EmployeePayrollData>();
		employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).Name
				.equals(EmployeePayrollDBService.getEmployeePayrollData(name).get(0).Name);
	}

}

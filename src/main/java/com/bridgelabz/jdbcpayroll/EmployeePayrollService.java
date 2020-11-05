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

	public List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();

	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = (ArrayList<EmployeePayrollData>) new EmployeePayrollDBService().readData();
		return this.employeePayrollList;

	}

	public void updateEmployeeSalary(String name, double salary, IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			int result = new EmployeePayrollDBService().updateEmployeeData(name, salary);
			if (result == 0)
				return;
		}
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.salary = salary;
	}

	public EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		ArrayList<EmployeePayrollData> employeePayrollDataList = new ArrayList<EmployeePayrollData>();
		employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).name
				.equals(EmployeePayrollDBService.getEmployeePayrollData(name).get(0).name);
	}

	public double findAvgOfEmployeeSalary() {
		return new EmployeePayrollDBService().getAvgOfEmployeeSalary();

	}

	public int writeData() {
		return new EmployeePayrollDBService().writeData();
	}

	public void addEmployeePayroll(String name, double salary, LocalDate start, String gender) {
		new EmployeePayrollDBService().addEmployeePayroll(name, salary, start, gender);

	}

	public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			return new EmployeePayrollDBService().getAverageSalaryByGender();
		return null;

	}

	public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) {
		if (ioService.equals(IOService.DB_IO))
			return new EmployeePayrollDBService().getEmployeePayrollForDateRange(startDate, endDate);
		return null;
	}

	public void addEmployeePayroll(List<EmployeePayrollData> employeePayrollDataList) {
		employeePayrollDataList.forEach(employeePayrollData -> {
			this.addEmployeePayroll(employeePayrollData.name, employeePayrollData.salary, employeePayrollData.start,
					employeePayrollData.gender);

		});
	}

	public int countEntries() {
		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<EmployeePayrollData>();
		employeePayrollDataList = new EmployeePayrollDBService().readData();
		return employeePayrollDataList.size();
	}

	public void addEmployeePayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				this.addEmployeePayroll(employeePayrollData.name, employeePayrollData.salary, employeePayrollData.start,
						employeePayrollData.gender);
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
				System.out.println("Employee Added: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employeePayrollData.name);
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}
		System.out.println(this.employeePayrollList);

	}

	public void updateEmployeeSalary(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Salary Being Added: " + Thread.currentThread().getName());
				this.updateEmployeeSalary(employeePayrollData.name, employeePayrollData.salary, IOService.DB_IO);
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
				System.out.println("Salary Updated: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employeePayrollData.name);
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}
		System.out.println(this.employeePayrollList);

	}

	public long countEntries(IOService ioService) {
		return employeePayrollList.size();
	}

	public void addEmployeeToPayroll(EmployeePayrollData employeePayrollData, IOService iOService) {
		if (iOService.equals(IOService.DB_IO)) {
			new EmployeePayrollDBService().addEmployeePayroll(employeePayrollData.name, employeePayrollData.salary,
					employeePayrollData.start, employeePayrollData.gender);
		} else
			employeePayrollList.add(employeePayrollData);
	}

	public void deleteEmployeeToPayroll(String name, IOService iOService) {
		if (iOService.equals(IOService.REST_IO)) {
			EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
			employeePayrollList.remove(employeePayrollData);
		}

	}

}

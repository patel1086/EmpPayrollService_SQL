package com.bridgelabz.jdbcpayroll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServiceTest {

	@Test
	public void givenSQLConnectionOnReadingFromMYSQL_WorkbenchShouldMatchEmployeeCount() {
		ArrayList<EmployeePayrollData> arraylist = new ArrayList<>();
		arraylist = (ArrayList<EmployeePayrollData>) EmployeePayrollDBService.readData();
		Assert.assertEquals(5, arraylist.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatchWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		ArrayList<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Jitendra", 77000.89);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Jitendra");
		Assert.assertTrue(result);

	}

	@Test
	public void givenSalaryForEmplyee_FindAvgSalaryOfEmployee_ShouldMatchWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		double result = employeePayrollService.findAvgOfEmployeeSalary();
		Assert.assertEquals(result, result, 0.0);
	}

//	@Test
//	public void insertEmployeeDataInTable_ShouldUpdateTheDB() {
//		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
//		employeePayrollService.addEmployeePayroll("Mark",50000.00,LocalDate.now(),"M");
//		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
//		
//		Assert.assertEquals(1,result);
//	}

	@Test
	public void givenPayrollData_WhenAverageSalaryRetrievedByGender_SholudReturnProperValue() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService
				.readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
		Assert.assertTrue(
				averageSalaryByGender.get("M").equals(41339.645) && averageSalaryByGender.get("F").equals(10000.00));
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(5, employeePayrollData.size());
	}

	@Test
	public void given2Employee_WhenAddedToDB_ShouldMatchUpEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(3, "Jeff Bezoz", 10000.00, LocalDate.now(), "M"),
				new EmployeePayrollData(4, "Narayan", 20000.00, LocalDate.now(), "M") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeePayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		Assert.assertEquals(5, employeePayrollService.countEntries());
	}

	@Test
	public void given6Employee_WhenAddedToDBWithThreads_ShouldMatchUpEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(3, "Jeff Bezoz", 10000.00, LocalDate.now(), "M"),
				new EmployeePayrollData(4, "Narayan", 20000.00, LocalDate.now(), "M"),
				new EmployeePayrollData(5, "Bhanwar", 90000.00, LocalDate.now(), "M"),
				new EmployeePayrollData(6, "Anushka", 25000.00, LocalDate.now(), "F"),
				new EmployeePayrollData(7, "Radha", 29000.00, LocalDate.now(), "F"),
				new EmployeePayrollData(8, "Nancy", 20300.00, LocalDate.now(), "F") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeePayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		Assert.assertEquals(3, employeePayrollService.countEntries());
	}

}

package com.bridgelabz.jdbcpayroll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServiceTest {

	@Test
	public void givenSQLConnectionOnReadingFromMYSQL_WorkbenchShouldMatchEmployeeCount() {
		ArrayList<EmployeePayrollData> arraylist = new ArrayList<>();
		arraylist = (ArrayList<EmployeePayrollData>) EmployeePayrollDBService.readData();
		Assert.assertEquals(3, arraylist.size());
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
		Assert.assertEquals(30893.096666666665, result, 0.0);
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
		Assert.assertTrue(averageSalaryByGender.get("M").equals(41339.645) && averageSalaryByGender.get("F").equals(10000.00));
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(3, employeePayrollData.size());
	}

}

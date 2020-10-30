package com.bridgelabz.jdbcpayroll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServiceTest {

	EmployeePayrollService employeePayrollService;

	@Before
	public void setBefore() {
		employeePayrollService = new EmployeePayrollService();
	}

	@Test
	public void givenSQLConnectionOnReadingFromMYSQL_WorkbenchShouldMatchEmployeeCount() {
		ArrayList<EmployeePayrollData> arraylist = new ArrayList<>();
		arraylist = (ArrayList<EmployeePayrollData>) EmployeePayrollDBService.readData();
		Assert.assertEquals(2, arraylist.size());
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
		ArrayList<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		double result = employeePayrollService.findAvgOfEmployeeSalary();
		Assert.assertEquals(41339.645, result, 0.0);
	}

}

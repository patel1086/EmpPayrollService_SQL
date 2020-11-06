package com.bridgelabz.jdbcpayroll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollServiceTest {

	public static boolean finalResult = true;

	@Test
	public void givenSQLConnectionOnReadingFromMYSQL_WorkbenchShouldMatchEmployeeCount() {
		ArrayList<EmployeePayrollData> arraylist = new ArrayList<>();
		arraylist = (ArrayList<EmployeePayrollData>) EmployeePayrollDBService.readData();
		Assert.assertEquals(5, arraylist.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatchWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Jitendra", 77888.89, EmployeePayrollService.IOService.DB_IO);
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

	@Test
	public void insertEmployeeDataInTable_ShouldUpdateTheDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		employeePayrollService.addEmployeePayroll("Mark", 50000.00, LocalDate.now(), "M");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");

		Assert.assertEquals(1, result);
	}

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
				new EmployeePayrollData(4, "Narayan", 25800.00, LocalDate.now(), "M"),
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
		Assert.assertEquals(13, employeePayrollService.countEntries());
	}

	@Test
	public void given6Employee_UpdateSalaryDetailsWithThreads_ShouldMatchUpWithDB() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData("Jeff Bezoz", 5987.00),
				new EmployeePayrollData("Narayan", 6789.00), new EmployeePayrollData("Bhanwar", 99876.00),
				new EmployeePayrollData("Anushka", 94463.00), new EmployeePayrollData("Radha", 54786.00),
				new EmployeePayrollData("Nancy", 56743.00) };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.updateEmployeeSalary(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		List<EmployeePayrollData> employeePayrollDataList = Arrays.asList(arrayOfEmps);
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB(employeePayrollData.name);
				finalResult = finalResult && result;
			};
			Thread thread = new Thread(task, employeePayrollData.name);
			thread.start();
		});
		Assert.assertTrue(finalResult);

	}

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		System.out.println("Employee Payroll Service " + response.asString());
		EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmps;
	}

	private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}

	@Test
	public void giveEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(
				new ArrayList<EmployeePayrollData>(Arrays.asList(arrayOfEmps)));
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
		Assert.assertEquals(5, entries);
	}

	@Test
	public void givenNewEmployee_WhenAdded_ShouldMatch201ResponseAndCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(
				new ArrayList<EmployeePayrollData>(Arrays.asList(arrayOfEmps)));
		EmployeePayrollData employeePayrollData = new EmployeePayrollData(6, "Laxman Patel", 35000, LocalDate.now(),
				"M");
		Response response = addEmployeeToJsonServer(employeePayrollData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.addEmployeeToPayroll(employeePayrollData, EmployeePayrollService.IOService.REST_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
		Assert.assertEquals(6, entries);
	}

	@Test
	public void givenMultipleEmployee_WhenAdded_ShouldMatch201ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(
				new ArrayList<EmployeePayrollData>(Arrays.asList(arrayOfEmps)));
		EmployeePayrollData[] arrayOfEmployees = { new EmployeePayrollData(12, "Hanuman", 30970, LocalDate.now(), "M"),
				new EmployeePayrollData(13, "ManjuLika", 34542, LocalDate.now(), "F"),
				new EmployeePayrollData(14, "Suman", 359876, LocalDate.now(), "F") };
		for (EmployeePayrollData employeePayrollData : arrayOfEmployees) {
			Response response = addEmployeeToJsonServer(employeePayrollData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
			employeePayrollService.addEmployeeToPayroll(employeePayrollData, EmployeePayrollService.IOService.REST_IO);
		}
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
		Assert.assertEquals(9, entries);
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch200Response() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(
				new ArrayList<EmployeePayrollData>(Arrays.asList(arrayOfEmps)));
		employeePayrollService.updateEmployeeSalary("David", 1111, EmployeePayrollService.IOService.REST_IO);
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("David");
		String empJSon = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJSon);
		Response response = request.put("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

	@Test
	public void givenEmployeeToDelete_WhenDeleted_ShouldMatch200ResponseAndCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(
				new ArrayList<EmployeePayrollData>(Arrays.asList(arrayOfEmps)));
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Pankaj");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		employeePayrollService.deleteEmployeeToPayroll(employeePayrollData.name,
				EmployeePayrollService.IOService.REST_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
		Assert.assertEquals(8, entries);
	}

}

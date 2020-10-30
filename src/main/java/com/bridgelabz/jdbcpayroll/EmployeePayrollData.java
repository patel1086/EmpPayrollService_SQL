package com.bridgelabz.jdbcpayroll;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int ID;
	public String Name;
	public double salary;
	public LocalDate start;

	public EmployeePayrollData(int ID, String Name, double salary,LocalDate start) {
		this.ID = ID;
		this.Name = Name;
		this.salary = salary;
		this.start=start;
	}

	public EmployeePayrollData() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "EmployeePayrollData [ID=" + ID + ", Name=" + Name + ", salary=" + salary + ", start=" + start + "]";
	}
}

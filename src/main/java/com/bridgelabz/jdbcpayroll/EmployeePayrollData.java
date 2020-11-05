package com.bridgelabz.jdbcpayroll;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double salary;
	public LocalDate start;
	public String gender;
	public double deduction;
	public double taxable_pay;
	public double tax;
	public double netpay;
	public int emp_id;

	public EmployeePayrollData(int ID, String Name, double salary, LocalDate start, String gender) {
		this.id = ID;
		this.name = Name;
		this.salary = salary;
		this.start = start;
		this.gender = gender;
	}

	public EmployeePayrollData(int emp_id, double salary, double deduction, double taxable_pay, double tax,
			double netpay) {
		this.deduction = deduction;
		this.emp_id = emp_id;
		this.taxable_pay = taxable_pay;
		this.tax = tax;
		this.netpay = netpay;
		this.salary = salary;
	}

	public EmployeePayrollData(String name, double salary) {
		this.name = name;
		this.salary = salary;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(deduction);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + emp_id;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		temp = Double.doubleToLongBits(netpay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		temp = Double.doubleToLongBits(tax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(taxable_pay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		if (Double.doubleToLongBits(deduction) != Double.doubleToLongBits(other.deduction))
			return false;
		if (emp_id != other.emp_id)
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(netpay) != Double.doubleToLongBits(other.netpay))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (Double.doubleToLongBits(tax) != Double.doubleToLongBits(other.tax))
			return false;
		if (Double.doubleToLongBits(taxable_pay) != Double.doubleToLongBits(other.taxable_pay))
			return false;
		return true;
	}

	public String toString() {
		return "EmployeePayrollData [ID=" + id + ", Name=" + name + ", salary=" + salary + ", start=" + start
				+ ",gender=" + gender + "]";
	}

}

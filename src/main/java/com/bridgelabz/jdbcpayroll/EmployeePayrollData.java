package com.bridgelabz.jdbcpayroll;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int ID;
	public String Name;
	public double salary;
	public LocalDate start;
	public String gender;

	public EmployeePayrollData(int ID, String Name, double salary,LocalDate start,String gender) {
		this.ID = ID;
		this.Name = Name;
		this.salary = salary;
		this.start=start;
		this.gender=gender;
	}

	public EmployeePayrollData() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "EmployeePayrollData [ID=" + ID + ", Name=" + Name + ", salary=" + salary + ", start=" + start + ",gender="+gender+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		long temp;
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		if (ID != other.ID)
			return false;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
	
}

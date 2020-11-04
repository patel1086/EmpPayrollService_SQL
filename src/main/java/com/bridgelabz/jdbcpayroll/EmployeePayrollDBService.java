package com.bridgelabz.jdbcpayroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.xdevapi.Statement;

public class EmployeePayrollDBService {
	private static int connectionCounter = 0;
	private static PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	EmployeePayrollDBService() {
	}

	private static Connection getConnection() throws SQLException {
		connectionCounter++;
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
		String username = "root";
		String password = "1234";
		Connection connection;
		System.out.println("Processing Thread: " + Thread.currentThread().getName() + " Connection to database with Id:"
				+ connectionCounter);
		System.out.println("connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, username, password);
		System.out.println("Processing Thread: " + Thread.currentThread().getName() + " Id:" + connectionCounter);
		System.out.println("Connection established successfully!!!!" + connection);
		return connection;
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public static List<EmployeePayrollData> readData() {
		String sql = "SELECT * FROM employee;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollList = getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public static int writeData() {
		String sql = "INSERT INTO employee(name,salary,start) values('Shri Ram',5600,'2020-06-01');";
		try (Connection connection = getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateEmployeeData(String name, double salary) {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	public static ArrayList<EmployeePayrollData> getEmployeePayrollData(String name) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		if (employeePayrollDataStatement == null)
			prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private static ArrayList<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				String gender = resultSet.getString("gender");
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate, gender));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private static void prepareStatementForEmployeeData() {
		try {
			Connection con = getConnection();
			String sql = "SELECT * FROM employee where name=?";
			employeePayrollDataStatement = con.prepareStatement(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public double getAvgOfEmployeeSalary() {
		String sql = "SELECT AVG(salary) FROM employee;";
		try (Connection connection = this.getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				return resultSet.getDouble("AVG(salary)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public EmployeePayrollData addEmployeePayroll(String name, double salary, LocalDate start, String gender) {
		int employeeId = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (java.sql.Statement statement = connection.createStatement();) {
			String sql = String.format("INSERT INTO employee (name,gender,salary,start) values('%s','%s','%s','%s')",
					name, gender, salary, Date.valueOf(start));
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
			// employeePayrollData = new EmployeePayrollData(employeeId, name, salary,
			// start, gender);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try (java.sql.Statement statement = connection.createStatement();) {
			double deduction = salary * 0.2;
			double taxable_pay = salary - deduction;
			double tax = taxable_pay * 0.1;
			double net_pay = salary - tax;
			String sqlpayroll = String.format(
					"INSERT INTO payroll (emp_id,basic_pay,deduction,taxable_pay,tax,net_pay) values('%s',%.2f,%.2f,%.2f,%.2f,%.2f)",
					employeeId, salary, deduction, taxable_pay, tax, net_pay);
			int rowAffected = statement.executeUpdate(sqlpayroll);
			if (rowAffected == 1) {
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary, start, gender);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return employeePayrollData;
	}
	
	private int updateEmployeeDataUsingStatement(String name, double salary) {
		Connection connection = null;
		int rowAffected=0;
		int employeeId = -1;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (java.sql.Statement statement = connection.createStatement();) {
			String sql = String.format("update employee set salary=%.2f where name='%s'", salary, name);
			rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return rowAffected;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try (java.sql.Statement statement = connection.createStatement();) {
			double deduction = salary * 0.2;
			double taxable_pay = salary - deduction;
			double tax = taxable_pay * 0.1;
			double net_pay = salary - tax;
			String sqlpayroll = String.format("update payroll set salary=%.2f,deduction=%.2f,taxable_pay=%.2f,tax=%.2f,net_pay=%.2f where emp_id='%s';",salary, deduction, taxable_pay, tax, net_pay,employeeId);
		
			rowAffected = statement.executeUpdate(sqlpayroll);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return rowAffected;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return rowAffected;
	}

	public Map<String, Double> getAverageSalaryByGender() {
		String sql = "SELECT gender,AVG(salary) as avg_salary from employee group by gender;";
		Map<String, Double> genderToAvgSalaryMap = new HashMap<>();
		try (Connection connection = this.getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				double avg_salary = resultSet.getDouble("avg_salary");
				genderToAvgSalaryMap.put(gender, avg_salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genderToAvgSalaryMap;

	}

	public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format("SELECT * FROM employee where start between '%s' and '%s';", Date.valueOf(startDate),
				Date.valueOf(endDate));
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
}

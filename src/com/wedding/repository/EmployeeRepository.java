package com.wedding.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.wedding.databaseconnection.MySqlConnection;
import com.wedding.models.Employee;

public class EmployeeRepository {
	private Employee employee;
	private Gson gson = new Gson();
	public EmployeeRepository() {
		employee = new Employee();
	}
	
	public Employee convertJSONToEmployee(String JSON) {
		return gson.fromJson(JSON, Employee.class);
	}
	
	public List<Employee> getAll() {
		Connection connection = MySqlConnection.getInstance().getConnection();
		String query = "{call getAllEmployee()}";
		List<Employee> listEmployee = new ArrayList<Employee>();
		
		try {
			CallableStatement statement = connection.prepareCall(query);
			ResultSet res = statement.executeQuery();
			while(res.next()) {
				Employee employee = new Employee();
				employee.setUserID(res.getInt("userID"));
				employee.setDOB(res.getString("DOB"));
				employee.setFullname(res.getString("fullname"));
				employee.setGender(res.getString("gender"));
				employee.setJoiningDate(res.getString("joiningDate"));
				employee.setSalary(res.getInt("salary"));
				employee.setUsername(res.getString("username"));
				listEmployee.add(employee);
			}
			connection.close();
			return listEmployee;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getAllUsername() {
		Connection connection = MySqlConnection.getInstance().getConnection();
		String query = "SELECT username FROM `USER` WHERE NOT isDeleted;";
		List<String> listUsername = new ArrayList<String>();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet res = statement.executeQuery();
			while(res.next()) {
				String username = "";
				username = res.getString("username");
				listUsername.add(username);
			}
			return listUsername;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void delete(int id) {
		String query = "UPDATE `USER` SET isDeleted = ? WHERE userID = ?";
		Connection connection = MySqlConnection.getInstance().getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setBoolean(1, true);
			statement.setInt(2, id);
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void resetpassword(int id) {
		String query = "UPDATE `USER` SET pswd = ? WHERE userID = ?";
		Connection connection = MySqlConnection.getInstance().getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, 1);
			statement.setInt(2, id);
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void add(Employee employee) {
		String query = "INSERT INTO USER(fullname, username, pswd, DOB, joiningDate, salary, gender) VALUES(?,?,?,?,?,?,?)";
		Connection connection = MySqlConnection.getInstance().getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, employee.getFullname());
			statement.setString(2, employee.getUsername());
			statement.setString(3, "1");
			statement.setString(4, employee.getDOB());
			statement.setString(5, employee.getJoiningDate());
			statement.setInt(6, employee.getSalary());
			statement.setString(7, employee.getGender());
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void update(Employee employee) {
		String query = "UPDATE `USER` SET fullname = ?, username = ?, salary = ? WHERE userID = ?";
		Connection connection = MySqlConnection.getInstance().getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, employee.getFullname());
			statement.setString(2, employee.getUsername());
			statement.setInt(3, employee.getSalary());
			statement.setInt(4, employee.getUserID());
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

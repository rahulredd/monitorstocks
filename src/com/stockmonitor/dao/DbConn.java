package com.stockmonitor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.sql.DatabaseMetaData;

public class DbConn {
	private static String url = "jdbc:mysql://localhost:3306/stockmonitor";
	private static String user = "root";
	private static String password = "root";

	public Connection getDbConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	public void updateStocks(String stocks) throws ClassNotFoundException, SQLException {
		String[] line = stocks.split(",");
		Company company = new Company();
		String companyName = line[0].replaceAll("^\"|\"$", "");
		double stockPrice = Double.parseDouble(line[1]);
		String date = line[2].replaceAll("^\"|\"$", "");
		String time = line[3].replaceAll("^\"|\"$", "");
		String timeStamp = date + "-" + time;
		company.setTimeStamp(timeStamp);
		company.setName(companyName);
		company.setPrice(stockPrice);
		boolean isCompanyExists = isCompanyExist(companyName);
		System.out.println("Checking for " + companyName);
		if (!isCompanyExists) {
			addCompany(companyName);
		} 
		updateStocks(company);
	}

	private void updateStocks(Company company) throws ClassNotFoundException, SQLException {
		System.out.println("Updating stocks  for company " + company.getName());
		Connection conn = getDbConn();
		String updateQuery = "INSERT INTO " + company.getName() + "(name, timeStamp, price)" + "values(?, ?, ?)";
		PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
		preparedStmt.setString(1, company.getName());
		preparedStmt.setString(2, company.getTimeStamp());
		preparedStmt.setDouble(3, company.getPrice());
		preparedStmt.execute();
		conn.close();
	}

	private boolean isCompanyExist(String companyName) throws ClassNotFoundException, SQLException {
		List<String> listOfCompanies = listCompanies();
		return listOfCompanies.contains(companyName) ? true :  false;
	}

	public void deleteCompany(String companyName) throws ClassNotFoundException, SQLException {
		boolean isCompanyExists = isCompanyExist(companyName);
		if (isCompanyExists) {
			Connection conn = getDbConn();
			Statement stmt = conn.createStatement();
			String query = "drop table " +  companyName;
			stmt.executeUpdate(query);
			System.out.println("Sucessfully Delete company " + companyName);
			conn.close();
		} else {
			System.out.println("The requested company " + companyName + " is not available");
		}
	}

	public List<String> getLatestStocks() throws ClassNotFoundException, SQLException {
		List<String> listOfCompanies = listCompanies();
		List<String> latestStocks = new ArrayList<String>(); 
		for (String company : listOfCompanies) {
			String companyProfile = getLatestStock(company);
			if (companyProfile != null) {
				latestStocks.add(companyProfile);
			}
		}
		System.out.println(latestStocks);
		return latestStocks;
	}

	public Set<String> getAllStocks(String company) throws SQLException, ClassNotFoundException {
		Set<String> stocks = new HashSet<String>();
		Connection conn = getDbConn();
		Statement stmt = conn.createStatement();
		String query = "SELECT * FROM "+ company;
		ResultSet resultSet = stmt.executeQuery(query);
		List<String> allCompanyStocks = getCompanyProfile(company, resultSet);
		for (String stock : allCompanyStocks) {
			stocks.add(stock);
		}
		return stocks;
	}

	private String getLatestStock(String company) throws SQLException, ClassNotFoundException {
		String result = null;
		Connection conn = getDbConn();
		Statement stmt = conn.createStatement();
		String query = "SELECT price,timeStamp FROM "+ company + " ORDER BY id DESC LIMIT 1";
		ResultSet resultSet = stmt.executeQuery(query);
		if (resultSet.next()) {
			resultSet.beforeFirst();
			result = getCompanyProfile(company, resultSet).get(0);
			conn.close();
		}
		return result;
	}

	private List<String> getCompanyProfile(String company, ResultSet resultSet) throws SQLException {
		List<String> list = new ArrayList<String>();
		String date = null;
		String time = null;
		double price = 0.0;
		while (resultSet.next()) {
			price = resultSet.getDouble("price");
			String timeStamp = resultSet.getString("timeStamp");
			String[] dateAndTime = timeStamp.split("-");
			if (dateAndTime.length == 2) {
				date = dateAndTime[0];
				time	= dateAndTime[1];
			}
			list.add(company + " "+ price + " " + date + " " + time);
		}
		return list;
	}

	public void addCompany(String companyName) throws SQLException, ClassNotFoundException {
		boolean isCompanyExists = isCompanyExist(companyName);
		if (!isCompanyExists) {
			System.out.println("Adding company " + companyName);
			Connection conn = getDbConn();
			Statement stmt = conn.createStatement();
			String sql = "CREATE TABLE " + companyName +
					"(id int(11) NOT NULL AUTO_INCREMENT, " +
					" name VARCHAR(255), " + 
					" timeStamp VARCHAR(255), " + 
					" price DOUBLE, " + 
					" PRIMARY KEY ( id ))"; 
			stmt.executeUpdate(sql);
			conn.close();
		}
	}

	public List<String> listCompanies() throws SQLException, ClassNotFoundException {
		List<String> listOfCompanies = new ArrayList<String>();
		Connection conn = getDbConn();
		DatabaseMetaData dataBaseMetaData = conn.getMetaData();
		ResultSet rs = dataBaseMetaData.getTables(null, null, "%", null);
		while (rs.next()) {
			String tableName = rs.getString(3);
			listOfCompanies.add(tableName);
		}
		conn.close();
		return listOfCompanies;
	}

	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		String company = scan.next();
		DbConn db = new DbConn();
		System.out.println(db.isCompanyExist(company));
	}
}

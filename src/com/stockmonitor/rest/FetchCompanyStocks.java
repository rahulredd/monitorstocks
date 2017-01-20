package com.stockmonitor.rest;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import com.stockmonitor.dao.DbConn;
import java.net.*;
import java.sql.SQLException;
import java.io.*;

public class FetchCompanyStocks {
	
	public void fetchAndSaveStocks(String company) {
		Timer timer = new Timer();
		StockCompany stock = new StockCompany(company);
		timer.schedule(stock, 0, 300000);
		try {
			Thread.sleep(1500000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.cancel();
	}
	
	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		String company = scan.next();
		FetchCompanyStocks fetch = new FetchCompanyStocks();
		fetch.fetchAndSaveStocks("FB");
	}
}

class StockCompany extends TimerTask {  
	private int count = 0;
	private static String company;
	
	public StockCompany(String company) {
		this.company = company;
	}
	
	private void addCompanyStocks() throws ClassNotFoundException, SQLException {
		String stocks = getStocks();
		DbConn db = new DbConn();
		db.updateStocks(stocks);
	}
	
	private String getStocks() {
		String inputLine = null;
		try {
			URL stockData = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + company + "&f=sl1d1t1");
			URLConnection conn = stockData.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			inputLine = in.readLine();
			System.out.println(inputLine);
			in.close();
		} catch (IOException ex) {
			System.out.println("Unable to retrieve stock " + ex.getMessage());
		}
		return inputLine;
	}
	
	public void run() {
		count++;
		if (count <= 5) {
			try {
				addCompanyStocks();
			} catch (ClassNotFoundException | SQLException ex) {
			  	ex.printStackTrace();
		  } 
		}
	}
}

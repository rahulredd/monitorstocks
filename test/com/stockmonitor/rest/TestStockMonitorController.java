package com.stockmonitor.rest;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.stockmonitor.dao.StringList;

public class TestStockMonitorController {

	private StockMonitorController stockMonitor;
	
	@Before
	public void setup() throws ClassNotFoundException, SQLException {
		stockMonitor =  new StockMonitorController();
	}
	
	@Test
	public void testAddCompany() throws ClassNotFoundException, SQLException {
		String company = "JNPR";
		stockMonitor.addCompany(company);
		StringList list = stockMonitor.listCompanies();
		boolean status = list.getData().contains(company);
		Assert.assertEquals(true, status);
	}
	
	@Test
	public void testListCompanies() throws ClassNotFoundException, SQLException {
		String company = "MSFT";
		stockMonitor.addCompany(company);
		StringList list = stockMonitor.listCompanies();
		boolean status = list.getData().contains(company);
		Assert.assertEquals(true, status);
	}
	
	@Test
	public void testDeleteCompany() throws ClassNotFoundException, SQLException {
		String company = "JNPR";
		stockMonitor.deleteCompany(company);
		boolean status = stockMonitor.listCompanies().getData().contains(company);
		Assert.assertEquals(false, status);
	}
}

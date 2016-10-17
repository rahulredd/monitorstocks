package com.stockmonitor.dao;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.stockmonitor.dao.DbConn;

public class TestDbConn {
	private DbConn db;
	
	@Before
	public void setup() {
		db = new DbConn();
	}
	
	@Test
	public void testDbConn() throws ClassNotFoundException, SQLException {
		boolean status = db.getDbConn().isClosed();
		Assert.assertEquals(false, status);
	}
	
	@Test
	public void testCompanyExists() throws ClassNotFoundException, SQLException {
		String company = "NKE";
		db.addCompany(company);
		boolean status = db.listCompanies().contains(company);
		Assert.assertEquals(true, status);
	}
	
	@Test
	public void testDeleteCompany() throws ClassNotFoundException, SQLException {
		String company = "BABA";
		db.addCompany(company);
		boolean status = db.listCompanies().contains(company);
		Assert.assertEquals(true, status);
		db.deleteCompany(company);
		status = db.listCompanies().contains(company);
		Assert.assertEquals(false, status);
	}
}

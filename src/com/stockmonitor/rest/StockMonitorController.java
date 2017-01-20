package com.stockmonitor.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.stockmonitor.dao.DbConn;
import com.stockmonitor.dao.StringList;

@Path("/controller")
public class StockMonitorController {

	@PUT
	@Path("return/{name}")
	public void updateStocks(@PathParam("name")String companyName) {
		FetchCompanyStocks fetchStocks = new FetchCompanyStocks();
		fetchStocks.fetchAndSaveStocks(companyName);
	}

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public void addCompany(String companyName) throws ClassNotFoundException, SQLException {
		DbConn db = new DbConn();
		db.addCompany(companyName);
	}

	@GET
	@Path("listPrices")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public StringList listLatestStocks() throws ClassNotFoundException, SQLException {
		DbConn db = new DbConn();
		List<String> list = new ArrayList<String>();
		List<String> stocks = db.getLatestStocks();
		for (String companyProfile : stocks) {
			list.add(companyProfile);
		}
		System.out.print(list);
		return new StringList(list);
	}

	@GET
	@Path("list")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public StringList listCompanies() throws ClassNotFoundException, SQLException {
		DbConn db = new DbConn();
		List<String> list = db.listCompanies();
		System.out.println(list);
		return new StringList(list);
	}
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void deleteCompany(String companyName) throws ClassNotFoundException, SQLException {
		DbConn db = new DbConn();
		db.deleteCompany(companyName);
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnection() {
		return "Hello Its Working";
	}
	
	@GET
	@Path("allStocks/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String listAllCompanyStocks(@PathParam("name") String companyName) throws ClassNotFoundException, SQLException {
		Double price = 0.0;
		String timeStamp = null;
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap<String, List<HashMap>> companyMap = new HashMap<String, List<HashMap>>();
		DbConn db = new DbConn();
		Set<String> set = db.getAllStocks(companyName);
		Gson gson = new Gson();
		for (String companyProfile : set) {
			HashMap map = new HashMap<>();
			String info[] = companyProfile.split(" ");
			price = Double.parseDouble(info[1]); 
			timeStamp = info[2] + "-" + info[3];
			map.put("price", price);
			map.put("timeStamp", timeStamp);
			list.add(map);
		}
		companyMap.put(companyName, list);
                String jsonString = gson.toJson(companyMap);
		System.out.println("json " + jsonString);
		return jsonString;
	}
}

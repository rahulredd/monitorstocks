package com.stockmonitor.dao;

import java.util.List;

public class Company {

	private int id;
	private String name;
	private double price;
	private String timeStamp;
	private List<String> list;
	
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String date) {
		this.timeStamp = date;
	}
	@Override
	public String toString() {
	return "Company " + name + ", date" + timeStamp + ", price" + price;
	}
}

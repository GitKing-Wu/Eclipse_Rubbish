package com.bean;

public class Address {
	private String x;
	private String y;
	public Address() {}
	public Address(String x, String y) {
		this.x = x;
		this.y = y;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	@Override
	public String toString() {
		return "Address [x=" + x + ", y=" + y + "]";
	}
	public void setY(String y) {
		this.y = y;
	}
	
}

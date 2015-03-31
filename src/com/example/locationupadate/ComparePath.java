package com.example.locationupadate;

public class ComparePath {
	
	private String name;
	double lat ,lng;
	int route_no;
	
	
	
	public ComparePath(String name)
	{
		this.name =name;
		
	}



	public double getLat() {
		return lat;
	}



	public void setLat(double lat) {
		this.lat = lat;
	}



	public double getLng() {
		return lng;
	}



	public void setLng(double lng) {
		this.lng = lng;
	}



	public int getRoute_no() {
		return route_no;
	}



	public void setRoute_no(int route_no) {
		this.route_no = route_no;
	}
	
	
	

}

package com.example.googlemapstest;

public class FlightThread extends Thread
{
	public int lat = 38944400;
	public int lon = -77455800;
	
	public void run()
	{
		while(lon > -117280000)
		{
		lat = lat - 323;
		lon = lon - 2214;
			try {
			Thread.sleep(2000);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}
	}
	
	public int getLat()
	{
		return this.lat;
	}
	
	public int getLong()
	{
		return this.lon;
	}
}

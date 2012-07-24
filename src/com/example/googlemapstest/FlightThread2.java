package com.example.googlemapstest;

public class FlightThread2 extends Thread
{
	public int lat = 40639700;
	public int lon = -73778900;
	
	public void run()
	{
		while(lon > -117280000)
		{
		lat = lat + 248*5;
		lon = lon - 2522*5;
			try {
			Thread.sleep(5000);
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

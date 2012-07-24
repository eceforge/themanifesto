package com.example.googlemapstest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {

	Button downPeriscope;
	Button capture;
	MapController mc;
	MapView mapView;
	
	FlightThread t;
	PeriscopeThread t2;
	int pressed = 1;
	boolean stopThread = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        mc = mapView.getController();
        
        t = new FlightThread();
        t.start();
        
        t2 = new PeriscopeThread();
        
        downPeriscope = (Button) findViewById(R.id.clickBtn);
        downPeriscope.setClickable(true);
        downPeriscope.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		{
        		stopThread = false;
        		if(!t2.isAlive())
        			{
        			t2.start();
        			downPeriscope.setText("Up Periscope!");
        			}
        		else
        			{
        			stopThread = true;
        			t2 = new PeriscopeThread();
        			downPeriscope.setText("Down Periscope!");
        			}
        		}
        	}
        });
        
       capture = (Button) findViewById(R.id.capture);
       capture.setOnClickListener(new View.OnClickListener(){
       	public void onClick(View view){
       		Bitmap captured = mapView.getDrawingCache();
       		//TODO do something with captured to share to facebook
       	}
       });
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private class PeriscopeThread extends Thread
	{
		public void run()
		{
			while(!stopThread)
			{
    		GeoPoint point = new GeoPoint(t.getLat(), t.getLong());
    		mc.animateTo(point);
    		mc.setZoom(19);
    		Log.d(""+t.getLong(), "String");

    		try {
				Thread.sleep(2000);
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
    		
			}
    		
		}
	}
	
}


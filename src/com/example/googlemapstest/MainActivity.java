package com.example.googlemapstest;

//import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {

	Button downPeriscope;
	Button capture;
	Button ping;
	Button scan;
	MapController mc;
	MapView mapView;

	FlightThread plane1;
	FlightThread2 plane2;
	PeriscopeThread t2;
	PeriscopeThread2 t3;
	int pressed = 1;
	boolean stopThread1 = false;
	boolean stopThread2 = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mc = mapView.getController();

		plane1 = new FlightThread();
		plane1.start();
		plane2 = new FlightThread2();
		plane2.start();
		

		t2 = new PeriscopeThread();
		t3 = new PeriscopeThread2();

		downPeriscope = (Button) findViewById(R.id.clickBtn);
		downPeriscope.setClickable(true);
		downPeriscope.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				{
					stopThread1 = false;
					if (!t2.isAlive()) {
						t2.start();
						downPeriscope.setText("Up Periscope!");
					} else {
						stopThread1 = true;
						t2 = new PeriscopeThread();
						downPeriscope.setText("Down Periscope!");
					}
				}
			}
		});

		capture = (Button) findViewById(R.id.capture);
		capture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// Bitmap captured = mapView.getDrawingCache();
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(R.layout.capture_popup,
						(ViewGroup) findViewById(R.id.popup_element));
				LayoutParams layoutParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				mapView.addView(popupView, layoutParams);

				Button btnDismiss = (Button) popupView
						.findViewById(R.id.dismiss);
				btnDismiss.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mapView.removeAllViews();
					}
				});

			}
			});
		
		ping = (Button) findViewById(R.id.ping);
		ping.setClickable(true);
		ping.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				{
					int randLat;
					int randLon;
					
					mc.setZoom(10);
					GeoPoint point = new GeoPoint(plane2.getLat(), plane2.getLong());
					mc.animateTo(point);
					randLat = (plane2.getLat() + (int) (Math.random() * (mapView.getLatitudeSpan())))-(int)(mapView.getLatitudeSpan()/2);
					randLon = (plane2.getLong() + (int) (Math.random() * (mapView.getLongitudeSpan())))-(int)(mapView.getLongitudeSpan()/2);
					point = new GeoPoint(randLat, randLon);
					mc.animateTo(point);
				}
			}
		});
		
		scan = (Button) findViewById(R.id.scan);
		scan.setClickable(true);
		scan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
					
					if(plane2.getLat() > (mapView.getMapCenter().getLatitudeE6() - mapView.getLatitudeSpan()/2)
							&& plane2.getLat() < (mapView.getMapCenter().getLatitudeE6() + mapView.getLatitudeSpan()/2) 
							&& plane2.getLong() > (mapView.getMapCenter().getLongitudeE6() - mapView.getLongitudeSpan()/2)
							&& plane2.getLong() < (mapView.getMapCenter().getLongitudeE6() + mapView.getLongitudeSpan()/2))
					{
						scan.setBackgroundColor(Color.GREEN);
						if(mapView.getLongitudeSpan() < 300000)
						{
							LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View hitView = layoutInflater.inflate(R.layout.hitscreen,
									(ViewGroup) findViewById(R.id.hitscreen));
							LayoutParams layoutParams = new LayoutParams(
									LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
							mapView.addView(hitView, layoutParams);

							Button btnDismiss = (Button) hitView
									.findViewById(R.id.back);
							btnDismiss.setOnClickListener(new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
									mapView.removeAllViews();
								}
							});
							
							Button downPeris = (Button) hitView
									.findViewById(R.id.periscope);
							downPeris.setOnClickListener(new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
									mapView.removeAllViews();
									stopThread2 = false;
									if (!t3.isAlive()) {
										t3.start();
										downPeriscope.setText("Up Periscope!");
									} else {
										stopThread2 = true;
										t3 = new PeriscopeThread2();
										downPeriscope.setText("Down Periscope!");
									}
								}
							});
						}
					}
					else
					{
						scan.setBackgroundColor(Color.RED);
					}
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

	private class PeriscopeThread extends Thread {
		public void run() {
			while (!stopThread1) {
				GeoPoint point = new GeoPoint(plane1.getLat(), plane1.getLong());
				mc.animateTo(point);
				mc.setZoom(19);
				Log.d("" + plane1.getLong(), "String");

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			
	}
	
	private class PeriscopeThread2 extends Thread {
		public void run() {
			while (!stopThread2) {
				GeoPoint point = new GeoPoint(plane2.getLat(), plane2.getLong());
				mc.animateTo(point);
				mc.setZoom(19);
				Log.d("" + plane2.getLong(), "String");

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}
}

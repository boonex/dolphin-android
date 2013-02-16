package com.boonex.oo.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.MapActivityBase;
import com.boonex.oo.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class LocationActivity extends MapActivityBase {
	private static final String TAG = "OO LocationActivity";
	public static final int RESULT_OK = RESULT_FIRST_USER + 1;
	public static final int ZOOM = 16;
	private Button m_buttonSubmit;
	private MapView m_viewMap;
	private LocationOverlay m_locationOverlay;
	private List<GeoPoint> m_mapLocations;
	private ProgressDialog m_dialogProgress;
	LocationListener m_locationListener;
	LocationManager m_locationManager;
	private String m_sUsername;
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);        
        
        setContentView(R.layout.location);
        setTitleCaption(R.string.title_location);
        
        m_buttonSubmit = (Button) findViewById(R.id.submit);
        m_viewMap = (MapView) findViewById(R.id.map_view);        
        
        m_viewMap.setEnabled(true);
		m_viewMap.setClickable(true);
		m_viewMap.setBuiltInZoomControls(true);
                       
        Intent i = getIntent();        
        m_sUsername = i.getStringExtra("username");
		               
        m_buttonSubmit.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		        	     
        		if (m_locationManager == null)
        			m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        		        		
        		String strProvider = null; 
        		
        		if (m_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        			strProvider = LocationManager.GPS_PROVIDER;
        		} else if (m_locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        			strProvider = LocationManager.NETWORK_PROVIDER;
        		} else {
        			// TODO: open gps enable dialog
        		}
        		
        		if (strProvider == null)
        			return;
        		
            	Log.d(TAG, "LOCATION PROVIDER: " + strProvider);
            		
            	if (m_locationListener != null)
            		m_locationManager.removeUpdates(m_locationListener);
            	
            	m_locationListener = new CurrentLocationListener();        		
            	m_locationManager.requestLocationUpdates(strProvider, 1000, 1, m_locationListener);
        		
            	startProgress ();
            	
        	}
        });
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
        m_buttonSubmit.setVisibility(m_sUsername.equalsIgnoreCase(o.getUsername()) ? Button.VISIBLE : Button.GONE);
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		m_sUsername        		
        };        
        o.execAsyncMethod("dolphin.getUserLocation", aParams, new Connector.Callback() {
			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getUserLocation result: " + result.toString());
				
				if (true == result.toString().equals("0") || true == result.toString().equals("-1")) {
				
                	AlertDialog dialog = new AlertDialog.Builder(m_actThis).create();
                	dialog.setMessage(getString(true == result.toString().equals("-1") ? R.string.access_denied : R.string.location_undefined ));
                	dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int whichButton) {
                			dialog.dismiss();
                			Connector o = Main.getConnector();
                			if (false == m_sUsername.equalsIgnoreCase(o.getUsername())) {
                				finish();
                			}
                		}
                	}); 
                	dialog.show();
					
				} else {
					
					Map<String, String> map = (Map<String, String>)result;					
					String sLat = map.get("lat");
					String sLng = map.get("lng");
					String sType = map.get("type");
					String sZoom = map.get("zoom");
				 
					int iLat = 0;
					int iLng = 0;
					int iZoom = 3;
					try {
						iLat = (int)(Double.parseDouble(sLat)*1000000);
						iLng = (int)(Double.parseDouble(sLng)*1000000);
						iZoom = Integer.parseInt(sZoom);
					} catch (NumberFormatException e) {
					
					}
				
					setMapLocation(iLat, iLng);
					m_viewMap.getController().setZoom(iZoom);
					if (iLat != 0 && iLng != 0)
						m_viewMap.getController().animateTo(new GeoPoint(iLat, iLng));				
					m_locationOverlay = new LocationOverlay((LocationActivity)m_actThis);
					m_viewMap.getOverlays().add(m_locationOverlay);
					if (sType.equals("satellite") || sType.equals("hybrid"))
						m_viewMap.setSatellite(true);
				
				}
				
			}
        }, this);    	
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// not implemented
		return false;
	}    

	public void setMapLocation(int lat, int lng) {
		if (0 == lat && 0 == lng) {
			Toast toast = Toast.makeText(this, getString(R.string.location_undefined), Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		
		if (m_mapLocations == null) {
			m_mapLocations = new ArrayList<GeoPoint>();			
		} else {
			m_mapLocations.clear();
		}
		m_mapLocations.add(new GeoPoint(lat, lng));			
	}
	
	public List<GeoPoint> getMapLocations() {
		return m_mapLocations;
	}
	
	public void setLocation(double fLat, double fLng) {
				
		int iLat = (int)(fLat*1000000);
		int iLng = (int)(fLng*1000000);
		
		m_viewMap.getController().setZoom(ZOOM);
		m_viewMap.getController().animateTo(new GeoPoint(iLat, iLng));
		
		Log.d(TAG, "lat2: " + iLat);
		Log.d(TAG, "lng2: " + iLng);
		
		m_locationOverlay = null;        			
		m_viewMap.getOverlays().clear();
		setMapLocation(iLat, iLng);
		m_locationOverlay = new LocationOverlay((LocationActivity)m_actThis);
		m_viewMap.getOverlays().add(m_locationOverlay);
		m_viewMap.invalidate();
		
		
        Connector o = Main.getConnector();
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		String.format("%.8f", fLat).replace(",", "."),
        		String.format("%.8f", fLng).replace(",", "."),
        		String.format("%d", ZOOM),
        		m_viewMap.isSatellite() ? "hybrid" : "normal"
        };        
        o.execAsyncMethod("dolphin.updateUserLocation", aParams, new Connector.Callback() {
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.updateUserLocation result: " + result.toString());				
			}
        }, this);		
	}
	
	public void startProgress () {
		if (m_dialogProgress != null)
			return;
		m_dialogProgress = ProgressDialog.show(this, "", getString(R.string.location_acquiring), false, true, new DialogInterface.OnCancelListener () {
			public void  onCancel  (DialogInterface dialog) {
				stopProgress();				
			}
		}); 	
	}
	
	public void stopProgress () {
		if (m_dialogProgress != null) {					
			m_dialogProgress.dismiss();
			m_dialogProgress = null;
		}
		
		if (m_locationListener != null && m_locationManager != null) {
			m_locationManager.removeUpdates(m_locationListener);
			m_locationListener = null;
		}
	}
	
	
	
	public class CurrentLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			if (argLocation != null) {
				Log.d(TAG, "Location is available");
				setLocation (argLocation.getLatitude(), argLocation.getLongitude());
				stopProgress ();			
			} else {
				Log.d(TAG, "Location is NULLLLLLLLLLL");
			}
		}
		
		public void onProviderDisabled(String provider) {
			Log.d(TAG, "Provider disabled: " + provider);
			stopProgress ();						
		}

		public void onProviderEnabled(String provider) {
			Log.d(TAG, "Provider enabled: " + provider);
		}

		public void onStatusChanged(String provider, int status, Bundle arg2) {
			Log.d(TAG, "Status changed(" + provider + "): " + status + " / " + arg2);
		}
	}
	
	
}

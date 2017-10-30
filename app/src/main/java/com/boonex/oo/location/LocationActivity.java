package com.boonex.oo.location;

import java.util.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.boonex.oo.Connector;
import com.boonex.oo.FragmentActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends FragmentActivityBase implements OnMapReadyCallback {
	private static final String TAG = "OO LocationActivity";
	public static final int RESULT_OK = RESULT_FIRST_USER + 1;
	public static final int ZOOM = 16;
	private SupportMapFragment m_frMap;
	private GoogleMap m_googleMap;
	private String m_sUsername;
	private LocationActivity m_actThis;
	private boolean m_bLocationAccess;
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        
        m_actThis = this;
        
        setContentView(R.layout.location);
        setTitle(R.string.title_location);
        
        Intent i = getIntent();
        m_sUsername = i.getStringExtra("username");

        m_frMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		m_frMap.getMapAsync(this);

		m_bLocationAccess = true;
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			m_bLocationAccess = false;
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, 0);
		}


    }

	@Override
	public void onMapReady(GoogleMap map) {
		m_googleMap = map;
		reloadRemoteData ();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 0) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				m_bLocationAccess = true;
			}
		}
	}
    
    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
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
				 
					double fLat = 0;
					double fLng = 0;
					float fZoom = 3;
					try {
						fLat = Double.parseDouble(sLat);
						fLng = Double.parseDouble(sLng);
						fZoom = Float.parseFloat(sZoom);
					} catch (NumberFormatException e) {
						Log.e(TAG, e.toString());
					}
				
					setMapLocation(fLat, fLng, fZoom, sType);				
				}
				
			}
        }, this);    	
    }
    
	public void setMapLocation(double lat, double lng, float fZoom, String sType) {
		if (0 == lat && 0 == lng) {
			Toast toast = Toast.makeText(this, getString(R.string.location_undefined), Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		
		LatLng latLng = new LatLng(lat, lng);

		if (m_googleMap != null) {
			m_googleMap.clear();
			m_googleMap.addMarker(new MarkerOptions().position(latLng).title(m_sUsername));
			m_googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, fZoom));

			if (sType.equals("satellite") || sType.equals("hybrid"))
				m_googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		}

		Log.i(TAG, "setMapLocation - lat:" + lat + " / lng:" + lng);
	}
		
	public void setLocation(double fLat, double fLng) {

		setMapLocation(fLat, fLng, ZOOM, "");

		if (m_googleMap == null)
			return;

        Connector o = Main.getConnector();
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		String.format("%.8f", fLat).replace(",", "."),
        		String.format("%.8f", fLng).replace(",", "."),
        		String.format("%d", ZOOM),
				m_googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE || m_googleMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID ? "hybrid" : "normal"
        };        
        o.execAsyncMethod("dolphin.updateUserLocation", aParams, new Connector.Callback() {
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.updateUserLocation result: " + result.toString());				
			}
        }, this);		
	}
	
	public void startProgress () {
		getActionBarHelper().setRefreshActionItemState(true); 	
	}
	
	public void stopProgress () {
		getActionBarHelper().setRefreshActionItemState(false);
	}
		
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Connector o = Main.getConnector();
    	if (m_sUsername.equalsIgnoreCase(o.getUsername())) {
    		MenuInflater inflater = getMenuInflater();
    		inflater.inflate(R.menu.location, menu);    	
    		return true;
    	} 
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.location_update:
			if (!m_bLocationAccess) {
				Toast toast = Toast.makeText(this, getString(R.string.location_not_available), Toast.LENGTH_LONG);
				toast.show();
				break;
			}
    		LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult(){
    		    @Override
    		    public void gotLocation(Location location){
    		        stopProgress();
    		        if (null != location) {
    		        	Log.i(TAG, "Got Location: " + location);
    		        	setLocation (location.getLatitude(), location.getLongitude());
    		        } else {
    		        	Toast.makeText(m_actThis, R.string.location_not_available, Toast.LENGTH_LONG).show();
    		        }
    		    }
    		};
    		LocationHelper myLocation = new LocationHelper();
    		if (myLocation.getLocation(m_actThis, locationResult))
    			startProgress();
    		else
    			myLocation.openLocationEnableDialog();
            break;
        }
        return super.onOptionsItemSelected(item);
    }    
	
}

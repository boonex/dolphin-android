package com.boonex.oo.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.location.LocationActivity;

public class SearchResultsNearMeActivity extends SearchResultsBaseActivity {
	private static final int ACTIVITY_SEARCH_RESULTS=0;	
	private static final String TAG = "SearchResultsNearMeActivity";
	
    protected Boolean m_isOnlineOnly;
    protected Boolean m_isWithPhotosOnly;
    protected Integer m_iStart; 
	
    protected LocationActivity m_locationActivity;
	protected ProgressDialog m_dialogProgress;
	protected LocationListener m_locationListener;
	protected LocationManager m_locationManager;

	protected double m_fLat;
	protected double m_fLng;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        Intent i = getIntent();                	
        m_isOnlineOnly = i.getBooleanExtra("online_only", false);
        m_isWithPhotosOnly = i.getBooleanExtra("with_photos_only", false);
        m_iStart = i.getIntExtra("start", 0);        
        
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
    
	public void onNext () {		
		Intent i = new Intent(this, SearchResultsNearMeActivity.class);
		i.putExtra("online_only", m_isOnlineOnly);
		i.putExtra("with_photos_only", m_isWithPhotosOnly);
		i.putExtra("start", m_iStart + m_iPerPage);
		startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);		
	}    

	public void setLocation(double fLat, double fLng) {
		
		m_fLat = fLat;
		m_fLng = fLng;
		
		int iLat = (int)(fLat*1000000);
		int iLng = (int)(fLng*1000000);
		
		Log.d(TAG, "lat2: " + iLat);
		Log.d(TAG, "lng2: " + iLng);
				
		reloadRemoteData ();
	}	

    protected void reloadRemoteData () {
        Connector oConnector = Main.getConnector();
        
        Object[] aParams = {
        		oConnector.getUsername(), 
        		oConnector.getPassword(),
        		Main.getLang(),
        		String.format("%.8f", m_fLat),
        		String.format("%.8f", m_fLng),        		        		
        		m_isOnlineOnly ? "1" : "0",
        		m_isWithPhotosOnly ? "1" : "0",
        		String.format("%d", m_iStart),
        		String.format("%d", m_iPerPage)
        };                                    
        
        oConnector.execAsyncMethod("dolphin.getSearchResultsNearMe", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getSearchResultsNearMe result: " + result.toString());
				
				m_aProfiles = (Object [])result;
				
				Log.d(TAG, "dolphin.getSearchResultsNearMe num: " + m_aProfiles.length); 
								
				adapterSearchResults = new SearchResultsAdapter(m_actThis, m_aProfiles, m_aProfiles.length == m_iPerPage);
		        setListAdapter(adapterSearchResults);
			}
        }, this);    	
    }
    
	public void startProgress () {
		if (m_dialogProgress != null)
			return;
		m_dialogProgress = ProgressDialog.show(this, "", getString(R.string.location_acquiring_current), false, true, new DialogInterface.OnCancelListener () {
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
				setLocation (argLocation.getLatitude(), argLocation.getLongitude());
				stopProgress ();			
			} else {
				Log.d(TAG, "Location is NULLLLLLLLLLL");
			}
		}
		
		public void onProviderDisabled(String provider) {			
			stopProgress ();						
		}

		public void onProviderEnabled(String provider) {			
		}

		public void onStatusChanged(String provider, int status, Bundle arg2) {			
		}
	}
	
	
}

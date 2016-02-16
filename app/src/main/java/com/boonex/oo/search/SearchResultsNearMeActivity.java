package com.boonex.oo.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.location.LocationActivity;

public class SearchResultsNearMeActivity extends SearchResultsBaseActivity {
	private static final int ACTIVITY_SEARCH_RESULTS=0;	
	private static final String TAG = "SearchResultsNearMeActivity";
	
    protected Boolean m_isOnlineOnly;
    protected Boolean m_isWithPhotosOnly;
    protected Integer m_iStart; 
	
    protected LocationActivity m_locationActivity;
	protected ProgressDialog m_dialogProgress;

	protected double m_fLat;
	protected double m_fLng;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        Intent i = getIntent();                	
        m_isOnlineOnly = i.getBooleanExtra("online_only", false);
        m_isWithPhotosOnly = i.getBooleanExtra("with_photos_only", false);
        m_iStart = i.getIntExtra("start", 0);
        m_fLat = i.getDoubleExtra("lat", 0);
        m_fLng = i.getDoubleExtra("lng", 0);
        
        reloadRemoteData();
    }
    
	public void onNext () {		
		Intent i = new Intent(this, SearchResultsNearMeActivity.class);
		i.putExtra("online_only", m_isOnlineOnly);
		i.putExtra("with_photos_only", m_isWithPhotosOnly);
		i.putExtra("start", m_iStart + m_iPerPage);
		i.putExtra("lat", m_fLat);
		i.putExtra("lng", m_fLng);
		startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
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
								
				checkNextButton(m_aProfiles.length == m_iPerPage);
				
				adapterSearchResults = new SearchResultsAdapter(m_actThis, m_aProfiles, m_aProfiles.length == m_iPerPage);
		        setListAdapter(adapterSearchResults);
			}
        }, this);    	
    }

}

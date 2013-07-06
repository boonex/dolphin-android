package com.boonex.oo.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;

public class SearchResultsKeywordActivity extends SearchResultsBaseActivity {
	private static final int ACTIVITY_SEARCH_RESULTS=0;	
	private static final String TAG = "SearchResultsKeywordActivity";
    protected String m_sKeyword;        	
    protected Boolean m_isOnlineOnly;
    protected Boolean m_isWithPhotosOnly;
    protected Integer m_iStart; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = getIntent();        
        m_sKeyword = i.getStringExtra("keyword");        	
        m_isOnlineOnly = i.getBooleanExtra("online_only", false);
        m_isWithPhotosOnly = i.getBooleanExtra("with_photos_only", false);
        m_iStart = i.getIntExtra("start", 0);
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
        Connector oConnector = Main.getConnector();
        
        Object[] aParams = {
        		oConnector.getUsername(), 
        		oConnector.getPassword(),
        		Main.getLang(),
        		m_sKeyword,
        		m_isOnlineOnly ? "1" : "0",
        		m_isWithPhotosOnly ? "1" : "0",
        		String.format("%d", m_iStart),
        		String.format("%d", m_iPerPage)
        };                                    
        
        oConnector.execAsyncMethod("dolphin.getSearchResultsKeyword", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getSearchResultsKeyword result: " + result.toString());
				
				m_aProfiles = (Object [])result;
				
				Log.d(TAG, "dolphin.getSearchResultsKeyword num: " + m_aProfiles.length); 
				
				checkNextButton(m_aProfiles.length == m_iPerPage);
				
				adapterSearchResults = new SearchResultsAdapter(m_actThis, m_aProfiles, m_aProfiles.length == m_iPerPage);
		        setListAdapter(adapterSearchResults);
			}
        }, this);    	
    }
    
	public void onNext () {		
		Intent i = new Intent(this, SearchResultsKeywordActivity.class);    			                         	
		i.putExtra("keyword", m_sKeyword);
		i.putExtra("online_only", m_isOnlineOnly);
		i.putExtra("with_photos_only", m_isWithPhotosOnly);
		i.putExtra("start", m_iStart + m_iPerPage);
		startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
		
	}    
    
}

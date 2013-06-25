package com.boonex.oo.search;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.R;
import com.boonex.oo.location.LocationHelper;

public class SearchNearMeActivity extends ActivityBase {
	
	private static final int ACTIVITY_SEARCH_RESULTS=0;
	
	protected Button m_buttonSubmit;	
	protected CheckBox m_cbOnlineOnly;
	protected CheckBox m_cbWithPhotosOnly;
	protected ProgressDialog pd;

    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);
        
        setContentView(R.layout.search_near_me);
        setTitleCaption (R.string.title_search_near_me);
                               
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);
        m_buttonSubmit = (Button) findViewById(R.id.submit);
                
        MySetContextInterface listener = new MySetContextInterface() {
        	public SearchNearMeActivity context;
            public void onClick(View view) {            	
    			
        		LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult(){
        		    @Override
        		    public void gotLocation(Location location){
        		        stopProgress();
        		        if (null != location) {
        	    			Intent i = new Intent(context, SearchResultsNearMeActivity.class);
        	    			i.putExtra("online_only", m_cbOnlineOnly.isChecked());
        	    			i.putExtra("with_photos_only", m_cbWithPhotosOnly.isChecked());
        	    			i.putExtra("start", 0);
        	    			i.putExtra("lat", location.getLatitude());
        	    			i.putExtra("lng", location.getLongitude());
        	    			startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
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

            }          
            public void setContext(SearchNearMeActivity context) {
            	this.context = context;
            }
        };        
        listener.setContext(this);
        m_buttonSubmit.setOnClickListener(listener);
        
    }
    
    interface MySetContextInterface extends View.OnClickListener {
    	public void setContext(SearchNearMeActivity context);    
    }
    
    @Override
	protected void onPause() {		
		super.onPause();
	}

	@Override
	protected void onResume() {		
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		  
	}
	    
	public void startProgress () {
		getActionBarHelper().setRefreshActionItemState(true); 	
	}
	
	public void stopProgress () {
		getActionBarHelper().setRefreshActionItemState(false);
	}

}

package com.boonex.oo.search;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.boonex.oo.R;
import com.boonex.oo.location.LocationHelper;

public class SearchNearMeActivity extends SearchBaseActivity {
		
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
        
        checkSearchWithPhotos(m_cbWithPhotosOnly, findViewById(R.id.with_photos_only_title));
    }
    
    @Override
    protected void actionSearchSubmit() {
		
		LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		        stopProgress();
		        if (null != location) {
	    			Intent i = new Intent(m_actThis, SearchResultsNearMeActivity.class);
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
    	    
	public void startProgress () {
		getActionBarHelper().setRefreshActionItemState(true); 	
	}
	
	public void stopProgress () {
		getActionBarHelper().setRefreshActionItemState(false);
	}

}

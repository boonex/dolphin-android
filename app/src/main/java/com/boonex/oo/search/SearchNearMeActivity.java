package com.boonex.oo.search;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.Toast;

import com.boonex.oo.R;
import com.boonex.oo.location.LocationHelper;

public class SearchNearMeActivity extends SearchBaseActivity {
		
	protected CheckBox m_cbOnlineOnly;
	protected CheckBox m_cbWithPhotosOnly;
	protected ProgressDialog pd;
	private boolean m_bLocationAccess;

    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);
        
        setContentView(R.layout.search_near_me);
        setTitleCaption (R.string.title_search_near_me);
                               
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);

		m_bLocationAccess = true;
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			m_bLocationAccess = false;
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, 0);
		}

        checkSearchWithPhotos(m_cbWithPhotosOnly, findViewById(R.id.with_photos_only_title));
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

    @Override
    protected void actionSearchSubmit() {

		if (!m_bLocationAccess) {
			Toast toast = Toast.makeText(this, getString(R.string.location_not_available), Toast.LENGTH_LONG);
			toast.show();
			return;
		}

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

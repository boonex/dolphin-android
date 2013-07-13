package com.boonex.oo.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class SearchBaseActivity extends ActivityBase {
	
	protected static final int ACTIVITY_SEARCH_RESULTS=0;
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);
    }
        
    protected void actionSearchSubmit() {
    	// Overridden in child classes
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.search_form, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.search_submit:
        	actionSearchSubmit();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void checkSearchWithPhotos(View v1, View v2) {
		Connector o = Main.getConnector();
		if (!o.getSearchWithPhotos()) {
			v1.setVisibility(View.GONE);
			v2.setVisibility(View.GONE);
		}
    }
}

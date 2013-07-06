package com.boonex.oo.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.boonex.oo.ActivityBase;
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
}

package com.boonex.oo.search;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.R;

public class SearchNearMeActivity extends ActivityBase {
	
	private static final int ACTIVITY_SEARCH_RESULTS=0;
	
	protected Button m_buttonSubmit;	
	protected CheckBox m_cbOnlineOnly;
	protected CheckBox m_cbWithPhotosOnly;
	protected ProgressDialog pd;

    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false);
        
        setContentView(R.layout.search_near_me);
        setTitleCaption (R.string.title_search_near_me);
                               
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);
        m_buttonSubmit = (Button) findViewById(R.id.submit);
                
        MySetContextInterface listener = new MySetContextInterface() {
        	public SearchNearMeActivity context;
            public void onClick(View view) {            	
    			    			
    			Intent i = new Intent(context, SearchResultsNearMeActivity.class);
    			i.putExtra("online_only", m_cbOnlineOnly.isChecked());
    			i.putExtra("with_photos_only", m_cbWithPhotosOnly.isChecked());
    			i.putExtra("start", 0);
    			startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
    			                
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
	    
    
}

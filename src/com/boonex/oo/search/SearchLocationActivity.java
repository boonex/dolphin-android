package com.boonex.oo.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.CountryPickerActivity;
import com.boonex.oo.R;

public class SearchLocationActivity extends ActivityBase {
	
	private static final int ACTIVITY_SEARCH_RESULTS=0;
	private static final int ACTIVITY_COUNTRY_PICKER=1;
	
	private Button m_buttonSelectCountry;
	private Button m_buttonSubmit;
	private EditText m_editCountry;
	private String m_sCountryCode;
	private EditText m_editCity;
    private CheckBox m_cbOnlineOnly;
    private CheckBox m_cbWithPhotosOnly;
    private SearchLocationActivity m_activitySearchLocation;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false);
        
        setContentView(R.layout.search_location);
        setTitleCaption (R.string.title_search_location);
        
        m_editCountry = (EditText) findViewById(R.id.country);
        m_buttonSelectCountry = (Button) findViewById(R.id.search_select_country);
        m_editCity = (EditText) findViewById(R.id.city);        
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);
        m_buttonSubmit = (Button) findViewById(R.id.submit);
        m_activitySearchLocation = this;
        
        m_buttonSelectCountry.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
    			Intent i = new Intent(m_activitySearchLocation, CountryPickerActivity.class);
    			startActivityForResult(i, ACTIVITY_COUNTRY_PICKER);               
            }
        });
        
        MySetContextInterface listener = new MySetContextInterface() {
        	public SearchLocationActivity context;
            public void onClick(View view) {            	
    			    			
    			Intent i = new Intent(context, SearchResultsLocationActivity.class);
    			i.putExtra("country", m_sCountryCode);
    			i.putExtra("city", m_editCity.getText().toString());
    			i.putExtra("online_only", m_cbOnlineOnly.isChecked());
    			i.putExtra("with_photos_only", m_cbWithPhotosOnly.isChecked());
    			i.putExtra("start", 0);
    			startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
    			
            }          
            public void setContext(SearchLocationActivity context) {
            	this.context = context;
            }
        };        
        listener.setContext(this);
        m_buttonSubmit.setOnClickListener(listener);
        
    }
    
    interface MySetContextInterface extends View.OnClickListener {
    	public void setContext(SearchLocationActivity context);    
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (null == i) 
        	return;
                
        Bundle b = i.getExtras();
        m_editCountry.setText(b.getString("name"));
        m_sCountryCode = b.getString("code");
    }	
}

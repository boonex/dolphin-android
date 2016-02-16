package com.boonex.oo;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CountryPickerActivity extends ListActivityBase {	
	private static final String TAG = "UserPickerActivity";
	
	protected LinearLayout m_viewListContainer;
	protected Object m_aCountries[];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        
        setContentView(R.layout.list_picker);
        setTitleCaption (R.string.title_picker_country);        
               
        m_viewListContainer = (LinearLayout)findViewById(R.id.list_container);
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
    	
        Connector o = Main.getConnector();
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		Main.getLang()
        };                                            
        
        o.execAsyncMethod("dolphin.getCountries", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getCountries result: " + result.toString());

				m_aCountries = (Object [])result;
				
				Log.d(TAG, "dolphin.getCountries num: " + m_aCountries.length); 
								
				CountryPickerAdapter adapter = new CountryPickerAdapter(m_actThis, m_aCountries);         
		        setListAdapter(adapter);
		        m_viewListContainer.setVisibility(View.VISIBLE);
                
			}
        }, this);    	
    }

 
    @SuppressWarnings("unchecked")
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
                
        Map<String, String> map = (Map<String, String>)m_aCountries[position];
                    
    	Bundle b = new Bundle();    	
    	b.putString("name", map.get("Name"));
    	b.putString("code", map.get("Code"));
    	Log.d(TAG, "Selected country code: " + map.get("Code"));
    	Intent i = new Intent();
    	i.putExtras(b);        	
        setResult(0, i);
        finish();        
    }    
}

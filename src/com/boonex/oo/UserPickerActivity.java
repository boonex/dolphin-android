package com.boonex.oo;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class UserPickerActivity extends ListActivityBase {	
	private static final String TAG = "UserPickerActivity";
	
	protected LinearLayout m_viewListContainer;
	protected Object m_aUsers[];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list_picker);
        setTitleCaption (R.string.title_picker_user);                
                
        m_viewListContainer = (LinearLayout)findViewById(R.id.list_container);
        
        reloadRemoteData ();
    }

    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword()		
        };                                            
        
        o.execAsyncMethod("dolphin.getContacts", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getContacts result: " + result.toString());

				m_aUsers = (Object [])result;
				
				Log.d(TAG, "dolphin.getContacts num: " + m_aUsers.length); 
								
				UserPickerAdapter adapter = new UserPickerAdapter(m_actThis, m_aUsers);         
		        setListAdapter(adapter);
		        m_viewListContainer.setVisibility(View.VISIBLE);
                
			}
        }, this);    	
    }
 
    @SuppressWarnings("unchecked")
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
                
        Map<String, String> map = (Map<String, String>)m_aUsers[position];
                    
    	Bundle b = new Bundle();
    	try {
    		b.putInt("id", new Integer(map.get("ID")));
    		b.putString("name", map.get("Nick"));
    		b.putString("user_title", Main.getConnector().getProtocolVer() > 2 ? map.get("UserTitle") : map.get("Nick"));
    	} catch (NumberFormatException e) {
    		b.putInt("id", 0);
    		b.putString("name", "");
    		b.putString("user_title", "");
    	}
    	Intent i = new Intent();
    	i.putExtras(b);        	
        setResult(0, i);
        finish();
    }    
}

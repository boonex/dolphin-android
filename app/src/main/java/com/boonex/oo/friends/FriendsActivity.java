package com.boonex.oo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.profile.ProfileActivity;

public class FriendsActivity extends ListActivityBase {	
	private static final String TAG = "FriendsActivity";	
	FriendsAdapter m_adpFriends;
	String m_sUsername;
	Object m_aFriends[];
	Connector m_oConnector;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        setTitleCaption (R.string.title_friends);
        
        Intent i = getIntent();        
        m_sUsername = i.getStringExtra("username");        	
        
        m_oConnector = Main.getConnector();
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
        Object[] aParams = {
        		m_oConnector.getUsername(), 
        		m_oConnector.getPassword(),
        		m_sUsername,
        		Main.getLang()
        };                                    
        
        Log.d(TAG, "starting dolphin.getFriends");
        
        m_oConnector.execAsyncMethod("dolphin.getFriends", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getFriends result: " + result.toString());
				
				m_aFriends = (Object [])result;
				
				Log.d(TAG, "dolphin.getFriends num: " + m_aFriends.length); 
								
				m_adpFriends = new FriendsAdapter(m_actThis, m_aFriends, m_sUsername.equalsIgnoreCase(m_oConnector.getUsername()));         
		        setListAdapter(m_adpFriends);
			}
        }, this);    	
    }
    
    public void onRemoveFriend (String s) {
    	        
        Connector o = Main.getConnector();
                    	               
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		s        	
        };                                    
        
        MyFriendActionCallback listener = new MyFriendActionCallback() {

			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.removeFriend result: " + result.toString());
				if (result.toString().equals("ok")) {
					((FriendsActivity)m_actThis).reloadRemoteData();
				}
			}
        };                
        
        o.execAsyncMethod("dolphin.removeFriend", aParams, listener, this);
        
    }

    static class MyFriendActionCallback extends Connector.Callback {    	
    	public void setRemovedUsername(String s) {}
    }    
    

    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
           
        String sUsername = m_adpFriends.getUsername(position);
        if (null == sUsername)
        	return;
        
    	Intent i = new Intent(this, ProfileActivity.class);    			                         	
    	i.putExtra("username", sUsername);
    	startActivityForResult(i, 0);
    }
    
}

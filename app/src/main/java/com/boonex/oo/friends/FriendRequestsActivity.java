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
import com.boonex.oo.friends.FriendsActivity.MyFriendActionCallback;
import com.boonex.oo.profile.ProfileActivity;

public class FriendRequestsActivity extends ListActivityBase {

	private static final String TAG = "FriendRequestsActivity";	
	FriendRequestsAdapter m_adpFriendsRequests;
	Object m_aFriendRequests[];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);        	
        setTitleCaption (R.string.title_friend_requests);
        
        reloadRemoteData ();
    }

    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),        		
        		Main.getLang()
        };                                            
        
        Log.d(TAG, "starting dolphin.getFriendRequests");
        
        o.execAsyncMethod("dolphin.getFriendRequests", aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getFriendRequests result: " + result.toString());
				
				m_aFriendRequests = (Object [])result;
				
				Log.d(TAG, "dolphin.getFriendRequests num: " + m_aFriendRequests.length); 
								
				m_adpFriendsRequests = new FriendRequestsAdapter(m_actThis, m_aFriendRequests);         
		        setListAdapter(m_adpFriendsRequests);
			}
        }, this);    	
    }
    
    public void onAcceptFriend (String s) {
    	        
        Connector o = Main.getConnector();
                    	               
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		s        	
        };                                    
        
        MyFriendActionCallback listener = new MyFriendActionCallback() {
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.acceptFriendRequest result: " + result.toString());
				if (result.toString().equals("ok")) {
					((FriendRequestsActivity)m_actThis).reloadRemoteData();
				}
			}
        };        
        
        o.execAsyncMethod("dolphin.acceptFriendRequest", aParams, listener, this);
    }
    
    public void onRejectFriend (String s) {
        
        Connector o = Main.getConnector();
                    	               
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		s        	
        };                                    
        
        MyFriendActionCallback listener = new MyFriendActionCallback() {
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.declineFriendRequest result: " + result.toString());
				if (result.toString().equals("ok")) {					
					((FriendRequestsActivity)m_actThis).reloadRemoteData();
				}
			}
        };        
        
        o.execAsyncMethod("dolphin.declineFriendRequest", aParams, listener, this);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {    	
        super.onListItemClick(l, v, position, id);
        
        Log.d(TAG, "onListItemClick - " + m_adpFriendsRequests.toString());
        
        String sUsername = m_adpFriendsRequests.getUsername(position);
        if (null == sUsername)
        	return;
        
    	Intent i = new Intent(this, ProfileActivity.class);    			                         	
    	i.putExtra("username", sUsername);
    	startActivityForResult(i, 0);    	
    }    
    
}

package com.boonex.oo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class FriendsHomeActivity extends ListActivityBase {

	private static final int ACTIVITY_FRINDS_LIST=0;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true, false);
        
        setContentView(R.layout.list);
        setTitleCaption (R.string.title_friends_home);
    
        FriendsHomeAdapter adapter = new FriendsHomeAdapter(this);         
		setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
        case 0:
    		{
    			Connector o = Main.getConnector();
    			Intent i = new Intent(this, FriendsActivity.class);    			                         	
    			i.putExtra("username", o.getUsername());
    			startActivityForResult(i, ACTIVITY_FRINDS_LIST);
    		}    	        
        	break;
        case 1:
        	{    			
    			Intent i = new Intent(this, FriendRequestsActivity.class);    			                         	
    			startActivityForResult(i, ACTIVITY_FRINDS_LIST);    			
        	}
        	break;        	
        }
    }
    
}

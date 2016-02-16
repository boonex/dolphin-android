package com.boonex.oo.friends;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.ViewText;

public class FriendsHomeAdapter extends BaseAdapter {

	private Context m_context;	
	
	public FriendsHomeAdapter(Context context) {
		this.m_context = context;
	}
	
	public int getCount() {
		return 2;
	}

	public Object getItem(int arg0) {
		// not implemented
		return null; 
	}

	public long getItemId(int arg0) {		
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
			
		String s = "";
		Connector o = Main.getConnector();
		
		switch (arg0) {		
		case 0:			
			s = m_context.getString(R.string.friends_menu);			
			break;
		case 1:
			if (o.getFriendRequestsNum() > 0) {
				s = String.format(m_context.getString(R.string.friends_requests_menu_num), o.getFriendRequestsNum());
			} else {				
				s = m_context.getString(R.string.friends_requests_menu);
			}
			break;
		}
			
		return new ViewText (this.m_context, s);
	}

}

package com.boonex.oo.friends;

import java.util.Map;

import android.content.Context;
import android.view.View;

public class FriendRequestsAdapter extends FriendsBaseAdapter {
	
	public FriendRequestsAdapter(Context context, Object[] aProfiles) {
		super(context, aProfiles);
		initViews();
	}
	
	public View getViewReal(int arg0) {
		Map<String, Object> map = m_listProfiles.get(arg0);		
		return new ThumbViewActionApproveReject (m_context, map, (String)map.get("Nick"));
	}
	
}

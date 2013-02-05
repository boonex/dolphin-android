package com.boonex.oo.search;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.boonex.oo.ThumbView;
import com.boonex.oo.friends.FriendsBaseAdapter;

public class SearchResultsAdapter extends FriendsBaseAdapter {

	protected Boolean m_isFullPage = true;
	
	public SearchResultsAdapter(Context context, Object[] aProfiles, Boolean isFullPage) {
		super(context, aProfiles);
		m_isFullPage = isFullPage;
		initViews();
	}

	public int getCount() {		
		return super.getCount()+ (m_isFullPage ? 1 : 0);
	}
	
	public View getView(int i, View oldView, ViewGroup arg2) {
		View view = super.getView(i, oldView, arg2);
		if (view != null)
			return view;
		if (i >= super.getCount()) {
			return new NextPrevView(m_context); 
		} else {
			Map<String, Object> map = m_listProfiles.get(i);
			return new ThumbView (m_context, map, (String)map.get("Nick"));
		}
	}	
}

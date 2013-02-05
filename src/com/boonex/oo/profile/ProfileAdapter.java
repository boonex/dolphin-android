package com.boonex.oo.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.ViewText;

public class ProfileAdapter extends BaseAdapter {
	private Context m_context;
	protected Map<String, Object> m_map;
	protected Object[] m_aMenu;
	protected String m_sUsername;
	protected ProfileView m_actProfileView;
	protected List<View> m_listViews;
	
	public ProfileAdapter(Context context, Map<String, Object> mapProfileInfo, Object[] aMenu, String username) {
		this.m_context = context;
		this.m_map = mapProfileInfo;
		this.m_aMenu = aMenu;
		this.m_sUsername = username;
		initViews();
	}
	
	protected boolean isDisplayAddToFriendButton() {
		if (Main.getConnector().getProtocolVer() > 2 && null != m_map.get("user_friend") && m_map.get("user_friend").equals("1"))
			return false;
		return true;
	}
	
	protected void initViews() {
		m_listViews = new ArrayList<View>();
		for (int i=0 ; i < getCount() ; ++i)			
			m_listViews.add(i, getView(i, null, null));		
	}
	
	public int getCount() {		 
		return m_aMenu.length + (isDisplayAddToFriendButton() ? 2 : 1);
	}

	public Object getItem(int position) {		
		return "not implemented";
	}

	public long getItemId(int position) {		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (position >= 0 && position < m_listViews.size())
			return m_listViews.get(position);		
		
		if (0 == position) { // first view is always profile info
			m_actProfileView = new ProfileView(m_context, m_map, m_sUsername);
			return m_actProfileView;
		} else if (position == (m_aMenu.length + 1)) { // last view is always add to friends button
			if (!isDisplayAddToFriendButton())
				return new View(m_context); // actually it should never happen
			Button btn = new Button(m_context);
			btn.setText(m_context.getString(R.string.friends_add_friend));
			btn.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View v) {
	            	((ProfileActivity)m_context).onAddFriend();
	            }
			});			
			return btn;
		} else { // all other items are customizable
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)m_aMenu[position-1];
            String sTitle = map.get("title");
            String sBubble = map.get("bubble");
            String s;
            if (!sBubble.equals("") && !sBubble.equals("0"))
            	s = String.format(m_context.getString(R.string.menu_item_format), sTitle, sBubble);
            else
            	s = sTitle;            
            return new ViewText (m_context, s);
		}
	}

	public Map<String, Object> getMap() {
		return m_map;
	}
	
	public Object[] getMenu() {
		return m_aMenu;
	}
	
	public ProfileView getProileView() {
		return m_actProfileView;
	}
}

package com.boonex.oo.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FriendsBaseAdapter extends BaseAdapter {
	private static final String TAG = "FriendsBaseAdapter";
	protected Context m_context;	
	protected List<Map<String, Object>> m_listProfiles;
	protected List<View> m_listViews;
	
	@SuppressWarnings("unchecked")
	public FriendsBaseAdapter(Context context, Object[] aProfiles) {
		this.m_context = context;
		m_listProfiles = new ArrayList<Map<String, Object>>();		
		for (int i=0 ; i < aProfiles.length ; ++i) {			
			m_listProfiles.add((Map<String, Object>)aProfiles[i]);
		}		
	}
	
	protected void initViews() {
		m_listViews = new ArrayList<View>();		
	}
	
	public int getCount() {		
		return m_listProfiles.size();
	}

	public String getUsername(int i) {
		Log.d(TAG, "getUsername at  " + (new Integer(i)).toString());
		if (i < 0 || i >= m_listProfiles.size())			
			return null;		
		Map<String, Object> map = (Map<String, Object>)m_listProfiles.get(i);
		Log.d(TAG, "getUsername returns  " + (String)map.get("Nick"));
		return (String)map.get("Nick");
	}
	
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int i, View arg1, ViewGroup arg2) {		
		int iMin = m_listViews.size();
		int iMax = i;
		for (int j=iMin ; j < iMax+1 ; ++j)			
			m_listViews.add(j, getViewReal(j));
				
		if (i >= 0 && i < m_listViews.size())
			return m_listViews.get(i);
		return null;
	}

	public View getViewReal(int arg0) {
		return null;
	}
	
	public boolean remove(String s) {
		for (int i=0 ; i < m_listProfiles.size() ; ++i) {
			Map<String, Object> map = (Map<String, Object>)m_listProfiles.get(i); 
			if (s.equalsIgnoreCase((String)map.get("Nick"))) {
				try {
					m_listProfiles.remove(i);
				} catch (IndexOutOfBoundsException e) {
					return false;	
				}
				return true;
			}
		}	
		return false;
	}	
}

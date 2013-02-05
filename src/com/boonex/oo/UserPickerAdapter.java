package com.boonex.oo;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserPickerAdapter extends BaseAdapter {

	protected Context m_context;
	protected Object[] m_aUsers;
	
	public UserPickerAdapter(Context context, Object[] aUsers) {
		this.m_context = context;
		this.m_aUsers = aUsers;
	}
	
	public int getCount() {		
		return this.m_aUsers.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	
	public View getView(int arg0, View arg1, ViewGroup arg2) {              
        ViewTextSimple control = new ViewTextSimple(m_context, getUserTitle(arg0));
        control.setPadding(5, 5, 5, 5);
        return control;
	}

	@SuppressWarnings("unchecked")
	protected String getUserTitle (int i) {
		Map<String, String> map = (Map<String, String>)m_aUsers[i];
		return Main.getConnector().getProtocolVer() > 2 ? map.get("UserTitle") : map.get("Nick");
	}
}

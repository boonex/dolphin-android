package com.boonex.oo;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CountryPickerAdapter extends BaseAdapter {

	protected Context m_context;
	protected Object[] m_aCountries;
	
	public CountryPickerAdapter(Context context, Object[] aUsers) {
		this.m_context = context;
		this.m_aCountries = aUsers;
	}
	
	public int getCount() {		
		return this.m_aCountries.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("unchecked")
	public View getView(int arg0, View arg1, ViewGroup arg2) {
        Map<String, String> map = (Map<String, String>)m_aCountries[arg0];
        ViewTextSimple control = new ViewTextSimple(m_context, map.get("Name"));                
        control.setPadding(5, 5, 5, 5);
        return control;
	}

}

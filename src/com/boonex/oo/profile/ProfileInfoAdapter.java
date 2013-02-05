package com.boonex.oo.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.boonex.oo.R;
import com.boonex.oo.ViewTextArea;
import com.boonex.oo.ViewTextSimple;

public class ProfileInfoAdapter extends BaseAdapter {

	protected Context m_context;	
	protected Object m_aInfoBlocks[];
	protected String m_sUsername;
	protected List<View> m_listViews;
	
	public ProfileInfoAdapter(Context context, Object[] aInfoBlocks, String sUsername) {
		m_context = context;
		m_sUsername = sUsername;
		m_aInfoBlocks = aInfoBlocks;
		initViews();
	}

	protected void initViews() {
		m_listViews = new ArrayList<View>();
		for (int i=0 ; i < m_aInfoBlocks.length ; ++i)
			m_listViews.add(i, getView(i, null, null));		
	}
	
	public int getCount() {
		return m_aInfoBlocks.length;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getItem(int i) {
		return (Map<String, Object>)m_aInfoBlocks[i];
	}
	
	@SuppressWarnings("unchecked")
	public String getBlockTitle(int i) {
		
		Map<String, Object> mapBlock = (Map<String, Object>)m_aInfoBlocks[i];
		return mapBlock.get("Title").toString();
	}
	@SuppressWarnings("unchecked")
	public Object[] getBlockInfo(int i) {
		Map<String, Object> mapBlock = (Map<String, Object>)m_aInfoBlocks[i];
		return (Object [])mapBlock.get ("Info");
	}
	
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("unchecked")
	public View getView(int i, View arg1, ViewGroup arg2) {
		if (i >= 0 && i < m_listViews.size())
			return m_listViews.get(i);
		
		LinearLayout l = (LinearLayout)LayoutInflater.from(m_context).inflate(R.layout.view_profile_info, null, false);
		l.setOrientation(LinearLayout.VERTICAL);		
		
		TextView viewCaption = (TextView)l.findViewById(R.id.profile_info_title);
		viewCaption.setText(getBlockTitle(i));
		
		Object aInfo[] = getBlockInfo(i);
		TableLayout t = (TableLayout)l.findViewById(R.id.profile_info_data);		
		if (0 == aInfo.length)
			l.setVisibility(View.GONE);
		
		for (int j=0 ; j < aInfo.length ; ++j) {
			Map<String, String> mapField = (Map<String, String>)aInfo[j];
			
			if (mapField.get("Type").equals("area") || mapField.get("Type").equals("html_area")) {
				ViewTextSimple viewFieldCaption = new ViewTextSimple (m_context, mapField.get("Caption") + ": ");
			
				ViewTextArea viewField1 = new ViewTextArea (m_context, mapField.get("Value1") + (null != mapField.get("Value2") && mapField.get("Value2").length() > 0 ? "\n / \n" + mapField.get("Value2") : ""));			
						
				LinearLayout ll = new LinearLayout(m_context);
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.addView(viewFieldCaption);												
				ll.addView(viewField1);								
				t.addView(ll);
			} else {
				ViewTextSimple viewFieldCaption = new ViewTextSimple (m_context, mapField.get("Caption") + ": ");
				viewFieldCaption.setPadding(0, 0, 5, 0);				
			
				ViewTextSimple viewField1 = new ViewTextSimple (m_context, mapField.get("Value1") + (null != mapField.get("Value2") && mapField.get("Value2").length() > 0 ? " / " + mapField.get("Value2") : ""));			
						
				LinearLayout ll = new LinearLayout(m_context);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.addView(viewFieldCaption);												
				ll.addView(viewField1);								
				t.addView(ll);				
			}
		}		
		
		return l;
	}


}

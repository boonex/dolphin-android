package com.boonex.oo.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boonex.oo.R;
import com.boonex.oo.ViewText;


public class SearchHomeAdapter extends BaseAdapter {

	private Context m_context;
	protected Object[] m_aMenu;
	protected List<View> m_listViews;
	
	public SearchHomeAdapter(Context context, Object[] aMenu) {
		m_context = context;
		m_aMenu = aMenu;
		initViews();
	}

	protected void initViews() {
		m_listViews = new ArrayList<View>();
		for (int i=0 ; i < getCount() ; ++i)			
			m_listViews.add(i, getView(i, null, null));		
	}


	public int getCount() {
		return m_aMenu.length;
	}

	public Object getItem(int position) {
		// not implemented
		return null; 
	}

	public long getItemId(int position) {		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
			
		if (position >= 0 && position < m_listViews.size())
			return m_listViews.get(position);		
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)m_aMenu[position];
		String sTitle = map.get("title");
		String sBubble = map.get("bubble");
		String s;
		if (!sBubble.equals("") && !sBubble.equals("0"))
			s = String.format(m_context.getString(R.string.menu_item_format), sTitle, sBubble);
		else
			s = sTitle;            
		return new ViewText (m_context, s);
	}
	
	public Object[] getMenu() {
		return m_aMenu;
	}	
}

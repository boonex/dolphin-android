package com.boonex.oo.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MailMessagesAdapter extends BaseAdapter {

	protected Context m_context;
	protected Object[] m_aMessages;
	protected Boolean m_isInbox;
	protected List<View> m_listViews;
	
	public MailMessagesAdapter(Context context, Object[] aMessages, Boolean isInbox) {
		this.m_context = context;
		this.m_aMessages = aMessages;
		this.m_isInbox = isInbox;
		initViews();
	}
	
	protected void initViews() {
		m_listViews = new ArrayList<View>();
	}	
	
	public int getCount() {		
		return this.m_aMessages.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	
	public View getView(int arg0, View arg1, ViewGroup arg2) {		
		int iMin = m_listViews.size();
		int iMax = arg0;
		for (int j=iMin ; j < iMax+1 ; ++j)			
			m_listViews.add(j, getViewReal(j));
		
		if (arg0 >= 0 && arg0 < m_listViews.size())
			return m_listViews.get(arg0);		
		return null;
	}

	@SuppressWarnings("unchecked")
	public View getViewReal(int arg0) {		
		return new MailMessageView (m_context, (Map<String, Object>)m_aMessages[arg0], m_isInbox);
	}

}

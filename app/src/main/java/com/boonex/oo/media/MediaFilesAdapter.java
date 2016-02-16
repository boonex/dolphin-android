package com.boonex.oo.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class MediaFilesAdapter extends BaseAdapter {
	protected Context m_context;	
	protected List<Map<String, Object>> m_listFiles;
	protected String m_sUsername;
	protected List<View> m_listViews;
	
	@SuppressWarnings("unchecked")
	public MediaFilesAdapter(Context context, Object[] aFiles, String sUsername) {
		m_context = context;
		m_sUsername = sUsername;
		m_listFiles = new ArrayList<Map<String, Object>>();
		for (int i=0 ; i < aFiles.length ; ++i) {
			m_listFiles.add((Map<String, Object>)aFiles[i]);
		}
		initViews();
	}

	protected void initViews() {
		m_listViews = new ArrayList<View>();		
	}
	
	public int getCount() {
		return m_listFiles.size();
	}

	public Map<String, Object> getItem(int i) {
		if (i >= 0 && i < m_listFiles.size())			
			return (Map<String, Object>)m_listFiles.get(i);
		return null;
	}

	public int getPositionByFileId(String sId) {
		for (int i=0 ; i < m_listFiles.size() ; ++i) {
			Map<String, Object> map = (Map<String, Object>)m_listFiles.get(i);
			if (sId.equals(map.get("id")))
				return i;
		}
		return -1;
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

	public View getViewReal(int i) {
		if (i >= 0 && i < m_listViews.size())
			return m_listViews.get(i);
		Map<String, Object> map = (Map<String, Object>)m_listFiles.get(i);
		if (isDeleteAllowed())
			return new ThumbViewMediaEdit(m_context, map, m_sUsername);
		else
			return new ThumbViewMedia(m_context, map, m_sUsername);
	}	
	
	public List<Map<String, Object>> getListStorage () {
		return m_listFiles;
	}
	
	protected boolean isDeleteAllowed () {
		Connector o = Main.getConnector();
		// if not owner, don't allow file deletion
		if (!m_sUsername.equalsIgnoreCase(o.getUsername()))
			return false;		
		// allow deletion for API >= 5 only (except Images, see ImagesFilesAdapter)
		return o.getProtocolVer() >= 5 ? true : false;
	}

}

package com.boonex.oo.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boonex.oo.R;
import com.boonex.oo.ViewText;

public class MediaAlbumsAdapter extends BaseAdapter {

	protected Context m_context;	
	protected List<Map<String, String>> m_listAlbums;
	protected List<View> m_listViews;
	
	@SuppressWarnings("unchecked")
	public MediaAlbumsAdapter(Context context, Object[] aAlbums) {
		this.m_context = context;
		m_listAlbums = new ArrayList<Map<String, String>>();
		for (int i=0 ; i < aAlbums.length ; ++i) {			
			m_listAlbums.add((Map<String, String>)aAlbums[i]);
		}
		initViews();
	}
	
	protected void initViews() {
		m_listViews = new ArrayList<View>();
		for (int i=0 ; i < m_listAlbums.size() ; ++i)			
			m_listViews.add(i, getView(i, null, null));		
	}
	
	public int getCount() {
		return m_listAlbums.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg0 >= 0 && arg0 < m_listViews.size())
			return m_listViews.get(arg0);
		
		return new ViewText (m_context, getAlbumName(arg0));
	}
	
	public String getAlbumName(int i) {		
		if (i < 0 || i >= m_listAlbums.size())			
			return "Out of range";		
		Map<String, String> map = (Map<String, String>)m_listAlbums.get(i);		
		Integer iNum = Integer.valueOf(map.get("Num"));
		if (iNum > 0)
			return String.format(m_context.getString(R.string.media_album_name_num), map.get("Title"), iNum);
		else
			return map.get("Title");
	}	

	public String getAlbumId(int i) {
		Map<String, String> map = (Map<String, String>)m_listAlbums.get(i);
		return map.get("Id");
	}
	
	public String getAlbumNameRaw(int i) {
		Map<String, String> map = (Map<String, String>)m_listAlbums.get(i);
		return map.get("Title");
	}
	
	public boolean isAlbumDefault(int i) {
		Map<String, String> map = (Map<String, String>)m_listAlbums.get(i);
		String s = map.get("DefaultAlbum");		
		return null == s || s.equals("1") ? true : false;
	}	
	
}

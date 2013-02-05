package com.boonex.oo.media;

import java.util.Map;

import android.content.Context;
import android.view.View;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;

public class ImagesFilesAdapter extends MediaFilesAdapter {

	public ImagesFilesAdapter(Context context, Object[] aFiles, String sUsername) {
		super(context, aFiles, sUsername);
	}
	
	public View getViewReal(int i) {
		if (i >= 0 && i < m_listViews.size())
			return m_listViews.get(i);		
		Map<String, Object> map = (Map<String, Object>)m_listFiles.get(i);
		Connector o = Main.getConnector();
		if (m_sUsername.equalsIgnoreCase(o.getUsername()))
			return new ThumbViewMediaEdit(m_context, map, m_sUsername);
		else
			return new ThumbViewMedia(m_context, map, m_sUsername);
	}	
}

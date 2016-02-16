package com.boonex.oo.media;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;

import android.content.Context;

public class ImagesFilesAdapter extends MediaFilesAdapter {

	public ImagesFilesAdapter(Context context, Object[] aFiles, String sUsername) {
		super(context, aFiles, sUsername);
	}
	
	protected boolean isDeleteAllowed () {
		Connector o = Main.getConnector();
		return m_sUsername.equalsIgnoreCase(o.getUsername());
	}
}

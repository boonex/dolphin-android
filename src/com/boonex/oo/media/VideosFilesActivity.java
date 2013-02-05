package com.boonex.oo.media;

import android.content.Context;
import android.os.Bundle;

import com.boonex.oo.R;

public class VideosFilesActivity extends MediaFilesActivity {

	public VideosFilesActivity () {
		super();
		m_sMethodXMLRPC = "dolphin.getVideoInAlbum";
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_video_files);
    }

    protected MediaFilesAdapter getAdapterInstance (Context context, Object[] aFiles, String sUsername) {    	    	
		return (MediaFilesAdapter)new VideosFilesAdapter(context, aFiles, sUsername);              	
    }
    
}

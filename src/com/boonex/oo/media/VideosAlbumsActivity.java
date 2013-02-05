package com.boonex.oo.media;

import android.content.Context;
import android.os.Bundle;

import com.boonex.oo.R;

public class VideosAlbumsActivity extends MediaAlbumsActivity {
	
	public VideosAlbumsActivity () {
		super();
		m_sMethodXMLRPC = "dolphin.getVideoAlbums";
		m_classFilesActivity = VideosFilesActivity.class;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_video_albums);
    }
	
    protected MediaAlbumsAdapter getAdapterInstance (Context context, Object[] aAlbums) {    	
    	return (MediaAlbumsAdapter)new VideosAlbumsAdapter (context, aAlbums);
    }
    
}

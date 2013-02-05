package com.boonex.oo.media;

import android.content.Context;
import android.os.Bundle;

import com.boonex.oo.R;

public class SoundsAlbumsActivity extends MediaAlbumsActivity {
	
	public SoundsAlbumsActivity () {
		super();
		m_sMethodXMLRPC = "dolphin.getAudioAlbums";
		m_classFilesActivity = SoundsFilesActivity.class;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_sound_albums);
    }
    
    protected MediaAlbumsAdapter getAdapterInstance (Context context, Object[] aAlbums) {    	
    	return (MediaAlbumsAdapter)new SoundsAlbumsAdapter (context, aAlbums);
    }
    
}

package com.boonex.oo.media;

import android.content.Context;
import android.os.Bundle;

import com.boonex.oo.R;

public class SoundsFilesActivity extends MediaFilesActivity {

	public SoundsFilesActivity () {
		super();
		m_sMethodXMLRPC = "dolphin.getAudioInAlbum";
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_sound_files);
    }

    protected MediaFilesAdapter getAdapterInstance (Context context, Object[] aFiles, String sUsername) {    	    	
		return (MediaFilesAdapter)new SoundsFilesAdapter(context, aFiles, sUsername);
    }
    
}

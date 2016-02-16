package com.boonex.oo.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.boonex.oo.R;

public class ImagesAlbumsActivity extends MediaAlbumsActivity {
    
	public ImagesAlbumsActivity () {
		super();		
		m_sMethodXMLRPC = "dolphin.getImageAlbums";
		m_classFilesActivity = ImagesFilesActivity.class;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_image_albums);
    }

    protected MediaAlbumsAdapter getAdapterInstance (Context context, Object[] aAlbums) {    	
    	return (MediaAlbumsAdapter)new ImagesAlbumsAdapter (context, aAlbums);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {		
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (m_oConnector.getAlbumsReloadRequired()) {
			reloadRemoteData();
			m_oConnector.setAlbumsReloadRequired(false);
		}
	}    
}

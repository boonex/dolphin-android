package com.boonex.oo.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.boonex.oo.R;

public class VideosFilesActivity extends MediaFilesActivity {

	public VideosFilesActivity () {
		super();
		m_sMethodXMLRPC = "dolphin.getVideoInAlbum";
		m_sMethodRemove = "dolphin.removeVideo";
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_video_files);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.media_add:
        	Intent i = new Intent(this, AddVideoActivity.class);
        	i.putExtra("album_name", m_sAlbumName);
        	startActivityForResult(i, 0);
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected MediaFilesAdapter getAdapterInstance (Context context, Object[] aFiles, String sUsername) {    	    	
		return (MediaFilesAdapter)new VideosFilesAdapter(context, aFiles, sUsername);              	
    }
    
	protected boolean isAddAllowed () {
		if (!super.isAddAllowed ())
			return false;
		return m_oConnector.getProtocolVer() >= 5 ? true : false;
	}
}

package com.boonex.oo.media;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.R;

public class ImagesFilesActivity extends MediaFilesActivity {
	
	public ImagesFilesActivity () {
		super();		
		m_sMethodXMLRPC = "dolphin.getImagesInAlbum";
		m_sMethodRemove = "dolphin.removeImage";
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleCaption (R.string.title_image_files);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.media_add:
        	Intent i = new Intent(this, AddImageActivity.class);    	
        	i.putExtra("album_name", m_sAlbumName);
        	startActivityForResult(i, 0);
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected MediaFilesAdapter getAdapterInstance (Context context, Object[] aFiles, String sUsername) {    	    	
		return (MediaFilesAdapter)new ImagesFilesAdapter(context, aFiles, sUsername);              	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
                
        Map<String, Object> map = filesAdapter.getItem(position);
        if (null == map)
        	return;
        
        List<Map<String, Object>> list = filesAdapter.getListStorage ();
        
    	Intent i = new Intent(this, ImagesGallery.class);
    	i.putExtra("username", m_sUsername);
    	i.putExtra("index", position);
    	i.putExtra("list", (Serializable)list);
    	i.putExtra("album_default", m_isAlbumDefault);
    	startActivityForResult(i, 0);
    	        
    }        


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {		
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (m_oConnector.getImagesReloadRequired()) {
			reloadRemoteData();
			m_oConnector.setImagesReloadRequired(false);
		}
	}	
	
    public void onViewFile (String sId) {
    	int i = filesAdapter.getPositionByFileId (sId);
    	if (i >= 0)
    		this.onListItemClick(null, null, i, 0);	
    }    

}

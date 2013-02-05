package com.boonex.oo.media;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class MediaAlbumsActivity extends ListActivityBase {


	private static final String TAG = "ImagesAlbumsActivity";
	protected String m_sMethodXMLRPC;	
	protected Class<?> m_classFilesActivity;	
	MediaAlbumsAdapter albumsAdapter;
	String m_sUsername;
	Object m_aAlbums[];
	Connector m_oConnector;
	
	public MediaAlbumsActivity () {
		super();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        
        Intent i = getIntent();        
        m_sUsername = i.getStringExtra("username");        	
        
        m_oConnector = Main.getConnector();        
        
        reloadRemoteData();
    }

    protected void reloadRemoteData() {
    	
        Object[] aParams = {
        		m_oConnector.getUsername(), 
        		m_oConnector.getPassword(),
        		m_sUsername
        };                                    
        
        m_oConnector.execAsyncMethod(m_sMethodXMLRPC, aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, m_sMethodXMLRPC + " result: " + result.toString());
				
				m_aAlbums = null;
				m_aAlbums = (Object [])result;
				
				Log.d(TAG, m_sMethodXMLRPC + " num: " + m_aAlbums.length); 
				
				albumsAdapter = null;
				albumsAdapter = getAdapterInstance(m_actThis, m_aAlbums);
				setListAdapter(albumsAdapter);
		        
			}
        }, this);    
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        String sAlbumId = albumsAdapter.getAlbumId(position);
        if (null == sAlbumId)
        	return;
        
        Log.d(TAG, "class: " + m_classFilesActivity);
        
    	Intent i = new Intent(this, m_classFilesActivity);    			                         	
    	i.putExtra("username", m_sUsername);
    	i.putExtra("album_id", sAlbumId);
    	i.putExtra("album_name", albumsAdapter.getAlbumNameRaw(position));
    	startActivityForResult(i, 0);
    }

    protected MediaAlbumsAdapter getAdapterInstance (Context context, Object[] aAlbums) {    	
    	return null;
    }
    
}

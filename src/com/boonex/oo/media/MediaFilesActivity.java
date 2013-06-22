package com.boonex.oo.media;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class MediaFilesActivity extends ListActivityBase {
	
	private static final String TAG = "MediaFilesActivity";
	
	protected String m_sMethodXMLRPC;	
	protected MediaFilesAdapter filesAdapter;
	protected String m_sUsername;
	protected String m_sMediaId;
	protected String m_sAlbumId;
	protected String m_sAlbumName;
	protected boolean m_isAlbumDefault;
	protected Object m_aFiles[];
	protected Connector m_oConnector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        Intent i = getIntent();        
        m_sUsername = i.getStringExtra("username");        	
        m_sAlbumId = i.getStringExtra("album_id");
        m_sAlbumName = i.getStringExtra("album_name");
        m_sMediaId = i.getStringExtra("media_id");
        m_isAlbumDefault = i.getBooleanExtra("album_default", true);        
        if (null != m_sMediaId) {
        	TextView m_editSite = (TextView) findViewById(android.R.id.empty);
        	m_editSite.setVisibility(View.GONE);
        }
        
        m_oConnector = Main.getConnector();
        
        reloadRemoteData();
    }
    
    protected void reloadRemoteData() {
               
        Object[] aParams = {
        		m_oConnector.getUsername(), 
        		m_oConnector.getPassword(),
        		m_sUsername,
        		m_sAlbumId
        };                                    
                               
        m_oConnector.execAsyncMethod(m_sMethodXMLRPC, aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {
				Log.d(TAG, m_sMethodXMLRPC + " result: " + result.toString());
				
				m_aFiles = null;
				m_aFiles = (Object [])result;
				
				Log.d(TAG, m_sMethodXMLRPC + " num: " + m_aFiles.length);
				
				filesAdapter = null;
				filesAdapter = getAdapterInstance(m_actThis, m_aFiles, m_sUsername);		        
		        
		        if (null == m_sMediaId) {
		        	setListAdapter(filesAdapter);
		        } else {
		        	if (0 == m_aFiles.length) {
		        		finish();
		        	} else {
		        		int iPos = filesAdapter.getPositionByFileId(m_sMediaId);
		        		if (iPos < 0)
		        			iPos = 0;
		        		onListItemClick(null, null, iPos, 0);
		        	}
		        } 
			}
        }, this);    	
    }
    
    protected MediaFilesAdapter getAdapterInstance (Context context, Object[] aFiles, String sUsername) {
    	// override this func
		return null;              	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
                
        Log.d(TAG, "onActivityResult | requestCode:" + requestCode + " | resultCode:" + resultCode + " | i:" + i);
        
        if (null != m_sMediaId)
        	finish();        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        
        Map<String, Object> map = filesAdapter.getItem(position);
        if (null == map)
        	return;
        
        String sUrl = (String)map.get("file");
        if (!sUrl.startsWith("http://") && !sUrl.startsWith("https://")) {
        	sUrl = "http://www.youtube.com/watch?v=" + sUrl;
        	startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl)), 2);
        } else {
        	Intent i = new Intent(Intent.ACTION_VIEW);        
        	i.setDataAndType(Uri.parse(sUrl), "video/*");
        	startActivityForResult(i, 1);
        }
              
    }
    
    public void onRemoveFile (String sId) {
    	
    }
    
    public void onViewFile (String sId) {
    	
    }    
    
}

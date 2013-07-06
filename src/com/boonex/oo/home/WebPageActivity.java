package com.boonex.oo.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.friends.FriendsActivity;
import com.boonex.oo.friends.FriendsHomeActivity;
import com.boonex.oo.location.LocationActivity;
import com.boonex.oo.mail.MailComposeActivity;
import com.boonex.oo.mail.MailHomeActivity;
import com.boonex.oo.media.AddImageActivity;
import com.boonex.oo.media.ImagesAlbumsActivity;
import com.boonex.oo.media.ImagesGallery;
import com.boonex.oo.media.SoundsAlbumsActivity;
import com.boonex.oo.media.SoundsFilesActivity;
import com.boonex.oo.media.VideosAlbumsActivity;
import com.boonex.oo.media.VideosFilesActivity;
import com.boonex.oo.profile.ProfileActivity;
import com.boonex.oo.search.SearchHomeActivity;

public class WebPageActivity extends ActivityBase {
	private static final String TAG = "OO WebPageActivity";
	
	protected WebView m_viewWeb;
	protected ProgressBar m_progressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = getIntent();        
        String sTitle = i.getStringExtra("title");
        String sUrl = i.getStringExtra("url");
        	
        setContentView(R.layout.web_page);
        setTitleCaption(sTitle);
        
        m_viewWeb = (WebView) findViewById(R.id.web_view);                		
        m_viewWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);        
        m_viewWeb.setBackgroundColor(0);
        m_viewWeb.getSettings().setJavaScriptEnabled(true);
        m_viewWeb.loadUrl(sUrl, Main.getHeadersForLoggedInUser());
        m_viewWeb.setWebViewClient(new WebPageViewClient(this));
        m_viewWeb.setWebChromeClient(new WebPageChromeClient(this));
        
    }
    
    @Override
    public void setContentView (int iLayoutResID) {    	
    	super.setContentView(iLayoutResID);
    }
    
    protected void reloadRemoteData () {    	
    	m_viewWeb.clearCache(true);
    	m_viewWeb.reload();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the BACK key and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && m_viewWeb.canGoBack()) {
        	m_viewWeb.goBack();
            return true;
        }
        // If it wasn't the BACK key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    
    private class WebPageChromeClient extends WebChromeClient {
    	    	
    	public WebPageChromeClient(WebPageActivity act) {
    		super();    		
    	}
    	
    	public void onProgressChanged(WebView view, int progress) {    		
    		getActionBarHelper().setRefreshActionItemState(progress >= 100 ? false : true);
    	}    	
    }
    private class WebPageViewClient extends WebViewClient {
    	protected WebPageActivity m_actWebPage;
    	
    	public WebPageViewClient(WebPageActivity act) {
    		super();
    		m_actWebPage = act;
    	}
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	String sScheme = Uri.parse(url).getScheme();   
        	
        	Log.d(TAG, "URL scheme: " + Uri.parse(url).getScheme());
        	Log.d(TAG, "URL host: " + Uri.parse(url).getHost());
        	Log.d(TAG, "URL user: " + Uri.parse(url).getUserInfo());
        	Log.d(TAG, "URL last path segment: " + Uri.parse(url).getLastPathSegment());        	
        	Log.d(TAG, "URL fragment: " + Uri.parse(url).getFragment());
        	
        	if (sScheme.equals("bxprofile")) {
        		
        		String sUsername = url.substring("bxprofile:".length());
            	Intent i = new Intent(m_actWebPage, ProfileActivity.class);    			                         	
            	i.putExtra("username", sUsername);
            	startActivityForResult(i, 0);        		
        		return true;
        		
        	} else if (sScheme.equals("bxcontact")) {
        		
        		String sUsername = Uri.parse(url).getUserInfo();
        		String sUserTitle = Uri.parse(url).getHost();
        		
        		if (null == sUsername || 0 == sUsername.length()) {
        			sUsername = url.substring("bxcontact:".length()); 
        		}
        		
            	Intent i = new Intent(m_actWebPage, MailComposeActivity.class);    			                         	
            	i.putExtra("recipient", sUsername);
            	i.putExtra("recipient_title", null == sUserTitle || 0 == sUserTitle.length() ? sUsername : sUserTitle);
            	startActivityForResult(i, 0);        		
        		return true;
        		
        	} else if (sScheme.equals("bxgoto")) {
        		
        		String sSection = url.substring("bxgoto:".length());
        		if (sSection.equals("home")) {
        			m_oActivityHelper.gotoHome();
        		} else if (sSection.equals("friends")) {
        			Intent i = new Intent(m_actWebPage, FriendsHomeActivity.class);
    				startActivityForResult(i, 0);
        		} else if (sSection.equals("messages")) {
        			Intent i = new Intent(m_actWebPage, MailHomeActivity.class);
        			startActivityForResult(i, 0);
        		} else if (sSection.equals("search")) {
        			Intent i = new Intent(m_actWebPage, SearchHomeActivity.class);
        			startActivityForResult(i, 0);        		
        		}
        		return true;
        	
        	} else if (sScheme.equals("bxphoto")) { // bxphoto://USERNAME@ABUM_ID/IMAGE_ID
        		
        		String sUsername = Uri.parse(url).getUserInfo();
        		String sAlbumId = Uri.parse(url).getHost();
        		String sPhotoId = Uri.parse(url).getLastPathSegment();
        		
            	Intent i = new Intent(m_actWebPage, ImagesGallery.class);  
            	i.putExtra("username", sUsername);
            	i.putExtra("album_id", sAlbumId);
            	i.putExtra("photo_id", sPhotoId);
            	startActivityForResult(i, 0);        		
        		return true;
        		
        	} else if (sScheme.equals("bxvideo")) { // bxvideo://USERNAME@ABUM_ID/VIDEO_ID
        		
        		String sUsername = Uri.parse(url).getUserInfo();
        		String sAlbumId = Uri.parse(url).getHost();
        		String sMediaId = Uri.parse(url).getLastPathSegment();
        		
            	Intent i = new Intent(m_actWebPage, VideosFilesActivity.class);  
            	i.putExtra("username", sUsername);
            	i.putExtra("album_id", sAlbumId);
            	i.putExtra("media_id", sMediaId);
            	startActivityForResult(i, 0);
        		return true;
        		
        	} else if (sScheme.equals("bxaudio")) { // bxaudio://USERNAME@ABUM_ID/SOUND_ID
        		
        		String sUsername = Uri.parse(url).getUserInfo();
        		String sAlbumId = Uri.parse(url).getHost();
        		String sMediaId = Uri.parse(url).getLastPathSegment();
        		
            	Intent i = new Intent(m_actWebPage, SoundsFilesActivity.class);  
            	i.putExtra("username", sUsername);
            	i.putExtra("album_id", sAlbumId);
            	i.putExtra("media_id", sMediaId);
            	startActivityForResult(i, 0);
        		return true;
        		
        	} else if (sScheme.equals("bxphotoupload")) { // bxphotoupload:ALBUM_NAME

        		String sAlbumName = Uri.decode(url.substring("bxphotoupload:".length()));        		
            	Intent i = new Intent(m_actWebPage, AddImageActivity.class);    	
            	i.putExtra("album_name", sAlbumName);
            	startActivityForResult(i, 0);        		
        		return true;
        		
        	} else if (sScheme.equals("bxlocation")) { //  bxlocation:USERNAME
        		
        		String sUsername = Uri.decode(url.substring("bxlocation:".length()));
        		Intent i = new Intent(m_actWebPage, LocationActivity.class);
        		i.putExtra("username", sUsername);
        		startActivityForResult(i, 0);        	
        		return true;
        		        		
        	} else if (sScheme.equals("bxprofilefriends")) { //  bxprofilefriends:USERNAME
        		
        		String sUsername = Uri.decode(url.substring("bxprofilefriends:".length()));
    			Intent i = new Intent(m_actWebPage, FriendsActivity.class);    			                         	
    			i.putExtra("username", sUsername);
    			startActivityForResult(i, 0);
        		return true;
        		
        	} else if (sScheme.equals("bxphotoalbums")) { // bxphotoalbums:USERNAME
        		
        		String sUsername = Uri.decode(url.substring("bxphotoalbums:".length()));
    			Intent i = new Intent(m_actWebPage, ImagesAlbumsActivity.class);
    			i.putExtra("username", sUsername);				
    			startActivityForResult(i, 0);
        		return true;
        		
        	} else if (sScheme.equals("bxvideoalbums")) { // bxvideoalbums:USERNAME
        		
        		String sUsername = Uri.decode(url.substring("bxvideoalbums:".length()));
    			Intent i = new Intent(m_actWebPage, VideosAlbumsActivity.class);
    			i.putExtra("username", sUsername);				
    			startActivityForResult(i, 0);
        		return true;
        		
        	} else if (sScheme.equals("bxaudioalbums")) { // bxaudioalbums:USERNAME
        		
        		String sUsername = Uri.decode(url.substring("bxaudioalbums:".length()));
    			Intent i = new Intent(m_actWebPage, SoundsAlbumsActivity.class);
    			i.putExtra("username", sUsername);				
    			startActivityForResult(i, 0);
        		return true;

        	} else if (sScheme.equals("bxpagetitle")) { // bxaudioalbums:USERNAME
        		
        		String sTitle = Uri.decode(url.substring("bxpagetitle:".length()));
        		setTitleCaption(sTitle);
        		return true;
        		
        	} else {
        	        	
        		if (null != Uri.parse(url).getFragment() && Uri.parse(url).getFragment().equals("blank")) {
        			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("#blank", "")));
        			startActivity(intent);
        			return true;
        		}
        		return false;
        	}
        }
        
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(m_actWebPage, description + " (" + failingUrl + ")", Toast.LENGTH_SHORT).show();
        }        
    }    
}

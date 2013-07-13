package com.boonex.oo.home;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.friends.FriendsHomeActivity;
import com.boonex.oo.location.LocationActivity;
import com.boonex.oo.mail.MailHomeActivity;
import com.boonex.oo.media.ImagesAlbumsActivity;
import com.boonex.oo.media.SoundsAlbumsActivity;
import com.boonex.oo.media.VideosAlbumsActivity;
import com.boonex.oo.profile.ProfileInfoActivity;
import com.boonex.oo.search.SearchHomeActivity;
import com.boonex.oo.status.StatusMessageActivity;

public class HomeActivity extends ActivityBase {
	
	private static final String TAG = "OO HomeActivity";
	
	public static final int RESULT_LOGOUT = RESULT_FIRST_USER + 1;
	
	private static final int ACTIVITY_PROFILE_INFO=0;
	private static final int ACTIVITY_STATUS_MESSAGE=1;
	private static final int ACTIVITY_LOCATION=2;
	private static final int ACTIVITY_MAIL_HOME=3;
	private static final int ACTIVITY_FRIENDS_HOME=4;
	private static final int ACTIVITY_IMAGES_ALBUMS=5;
	private static final int ACTIVITY_VIDEOS_ALBUMS=6;
	private static final int ACTIVITY_SOUNDS_ALBUMS=7;
	private static final int ACTIVITY_SEARCH_HOME=8;
	private static final int ACTIVITY_WEB_PAGE=9;
    
    protected HomeActivity m_actHome;
    protected String m_sThumb;
    protected String m_sInfo;
    protected String m_sStatus;
    protected int m_iMemberId;
    protected String m_sUsername;
    protected String m_sUserTitle;
    protected String m_sPasswd;
    protected String m_sSite;
    protected int m_iSiteIndex;
    protected int m_iProtocolVer;
    protected Map<String, Object> m_map;

    protected HomeBtn m_btnMessages;
    protected HomeBtn m_btnFriends;
    
    protected TableLayout m_table;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.home);
        setTitleCaption (R.string.title_home);
        
        m_actHome = this;
        
        m_table = (TableLayout)findViewById(R.id.home_table);

        Intent i = getIntent();
        m_sSite = i.getStringExtra("site");
        m_iSiteIndex = i.getIntExtra ("index", 0);
        m_iMemberId = i.getIntExtra("member_id", 0);
    	m_sUsername = i.getStringExtra("username");
    	m_sPasswd = i.getStringExtra("password");
    	m_iProtocolVer = i.getIntExtra("protocol", 1);
    	
    	Log.d(TAG, "m_sSite: " + m_sSite);
    	Log.d(TAG, "m_sUsername: " + m_sUsername);    	

    	reloadRemoteData();
    }
    
    protected void reloadButtons (String sUnreasLetters, String sFriendRequests) {
        Connector o = Main.getConnector();		        		        		        
        o.setFriendRequestsNum(Integer.parseInt(sFriendRequests));
        o.setUnreadLettersNum(Integer.parseInt(sUnreasLetters));
    	
    	Map<String, String> mapStatus = new HashMap<String, String>();		
    	mapStatus.put("title", getString(R.string.status_message_menu));
    	mapStatus.put("action", "1");
    	mapStatus.put("bubble", "");
    	mapStatus.put("icon", "home_status.png");
    
    	Map<String, String> mapLocation = new HashMap<String, String>();
    	mapLocation.put("title", getString(R.string.location_menu));
    	mapLocation.put("action", "2");
    	mapLocation.put("bubble", "");
    	mapLocation.put("icon", "home_location.png");
    	
    	Map<String, String> mapMessages = new HashMap<String, String>();
    	mapMessages.put("title", getString(R.string.mail_menu));
    	mapMessages.put("action", "3");
    	mapMessages.put("bubble", sUnreasLetters);
    	mapMessages.put("icon", "home_messages.png");
    	
    	Map<String, String> mapFriends = new HashMap<String, String>();
    	mapFriends.put("title", getString(R.string.friends_menu));
    	mapFriends.put("action", "4");
    	mapFriends.put("bubble", sFriendRequests);
    	mapFriends.put("icon", "home_friends.png");
    	
    	Map<String, String> mapInfo = new HashMap<String, String>();
    	mapInfo.put("title", getString(R.string.profile_info_menu));
    	mapInfo.put("action", "5");
    	mapInfo.put("bubble", "");
    	mapInfo.put("icon", "home_info.png");
    	
    	Map<String, String> mapSearch = new HashMap<String, String>();
    	mapSearch.put("title", getString(R.string.search_menu));
    	mapSearch.put("action", "6");
    	mapSearch.put("bubble", "");
    	mapSearch.put("icon", "home_search.png");
    	
    	Map<String, String> mapImages = new HashMap<String, String>();
    	mapImages.put("title", getString(R.string.images_menu));
    	mapImages.put("action", "7");
    	mapImages.put("bubble", "");
    	mapImages.put("icon", "home_images.png");
    	    	
    	Map<String, String> mapSounds = new HashMap<String, String>();
    	mapSounds.put("title", getString(R.string.sounds_menu));
    	mapSounds.put("action", "9");
    	mapSounds.put("bubble", "");
    	mapSounds.put("icon", "home_sounds.png");

    	Map<String, String> mapVideos = new HashMap<String, String>();
    	mapVideos.put("title", getString(R.string.videos_menu));
    	mapVideos.put("action", "8");
    	mapVideos.put("bubble", "");
    	mapVideos.put("icon", "home_videos.png");
    	
    	Object[] aMenu = {
    			mapStatus, mapLocation, mapMessages,
    			mapFriends, mapInfo, mapSearch,
    			mapImages, mapSounds, mapVideos};
    	
    	reloadButtons(aMenu);
    }
    
    protected void reloadButtons (Object[] aMenu) {
    	
    	Connector o = Main.getConnector();
    	
    	Map<String, Integer> mapIcons = new HashMap<String, Integer>();
    	mapIcons.put("home_status.png", R.drawable.ic_home_status);     
    	mapIcons.put("home_location.png", R.drawable.ic_home_location);
    	mapIcons.put("home_messages.png", R.drawable.ic_home_messages);
        mapIcons.put("home_friends.png", R.drawable.ic_home_friends);
        mapIcons.put("home_info.png", R.drawable.ic_home_info);
        mapIcons.put("home_search.png", R.drawable.ic_home_search);
        mapIcons.put("home_images.png", R.drawable.ic_home_photos);
        mapIcons.put("home_sounds.png", R.drawable.ic_home_sounds);
        mapIcons.put("home_videos.png", R.drawable.ic_home_videos);
        
        Map<Integer, View.OnClickListener> mapActions = new HashMap<Integer, View.OnClickListener>();
        mapActions.put(1, new OnClickListener() {
            public void onClick(View v) {                	                	
            	LaunchActivityStatus();
            }
        });
        mapActions.put(2, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityLocation();
            }
        });    	    	    	
        mapActions.put(3, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityMail();
            }
        });    	
        mapActions.put(4, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityFriends();
            }
        });    	
        mapActions.put(5, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityInfo();
            }
        });    	
        mapActions.put(6, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivitySearch();
            }
        });    	
        mapActions.put(7, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityPhotos();
            }
        });    	
        mapActions.put(8, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivityVideos();
            }
        });    	
        mapActions.put(9, new OnClickListener() {
            public void onClick(View v) {              	
            	LaunchActivitySounds();
            }
        });

        m_table.removeAllViews();
        
        TableRow r = null;
    	int iCount = aMenu.length;
    	for (int i=0; i<iCount ; ++i) {
    		@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)aMenu[i];
    		HomeBtn oBtn;
            String sTitle = map.get("title");            
            int iAction = Integer.valueOf(map.get("action"));
            String sActionData = map.get("action_data");
            String sBubble = map.get("bubble");
    		String sIcon = map.get("icon");
    		int iIcon = 0;
    		if (mapIcons.containsKey(sIcon))
    			iIcon = mapIcons.get(sIcon);
    		
            Log.d(TAG, "INDEX: " + i);
            Log.d(TAG, "  TITLE: " + sTitle);
            Log.d(TAG, "  ICON: " + sIcon);
            Log.d(TAG, "  ACTION: " + iAction);
            Log.d(TAG, "  ACTION DATA: " + sActionData);
            Log.d(TAG, "  BUBBLE: " + sBubble);
            
            if (100 == iAction || 101 == iAction) {
            	oBtn = new HomeBtn3rdParty(this, sTitle, sBubble, sIcon);
    			ThirdPartyOnClickListener listener = new ThirdPartyOnClickListener() {
    				protected String m_sUrl;
    				protected String m_sTitle;
    				protected int m_iAction;
    		    	public void setUrl(String s) {
    		    		m_sUrl = s;
    		    	}
    		    	public void setTitle(String s) {
    		    		m_sTitle = s;
    		    	}
    		    	public void setAction(int i) {
    		    		m_iAction = i;
    		    	}    		    	
    	        	public void onClick(View view) {
    	        		if (101 == m_iAction)
    	        			LaunchActivityBrowser(m_sTitle, m_sUrl);
    	        		else
    	        			LaunchActivityWebPage(m_sTitle, m_sUrl);
    	        	}
    	        }; 
    	        listener.setTitle(sTitle);
    	        listener.setUrl(sActionData);
    	        listener.setAction(iAction);
            	oBtn.getBtn().setOnClickListener(listener);
            } else {
            	oBtn = new HomeBtn(this, sTitle, sBubble, iIcon);            
            	if (mapActions.containsKey(iAction))
            		oBtn.getBtn().setOnClickListener(mapActions.get(iAction));    	
            }
            
            if (0 == (i % 3)) {
                r = new TableRow(this);                
                m_table.addView(r);
            }
            if (r != null)
            	r.addView(oBtn);
            
            if (3 == iAction) {
            	m_btnMessages = oBtn;
            	o.setUnreadLettersNum(Integer.parseInt(sBubble));
            } else if (4 == iAction) {
            	m_btnFriends = oBtn; 
            	o.setFriendRequestsNum(Integer.parseInt(sBubble));
    		}
    	}
    }
    
    protected void reloadRemoteData () {
    	Object[] aParams;
    	String sMethod;
        Connector o = new Connector (m_sSite, m_sUsername, m_sPasswd, m_iMemberId);        
        
        o.setPassword (32 == m_sPasswd.length() || 40 == m_sPasswd.length() ? m_sPasswd : o.md5(m_sPasswd));
        o.setProtocolVer(m_iProtocolVer);
        
        Main.setConnector(o);
        Connector.saveConnector(this, o);        
        
        if (m_iProtocolVer > 1) {
        	Object[] aParamsLocal = {
        		o.getUsername(), 
        		o.getPassword(),
        		Main.getLang()
        	};
        	aParams = aParamsLocal;
        	sMethod = "dolphin.getHomepageInfo2";
        } else {
        	Object[] aParamsLocal = {
        		o.getUsername(), 
        		o.getPassword()
        	};
        	aParams = aParamsLocal;
        	sMethod = "dolphin.getHomepageInfo";
        }
        
        o.execAsyncMethod(sMethod, aParams, new Connector.Callback() {
			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getHomepageInfo("+m_iProtocolVer+") result: " + result.toString());
				m_map = (Map<String, Object>)result;
	
	    		Connector o = Main.getConnector();
	    		o.setSearchWithPhotos(true);
				
		    	m_sThumb = (String)m_map.get("thumb");
		    	m_sStatus = (String)m_map.get("status");
		    	if (m_iProtocolVer > 2) {
		    		m_sInfo = (String)m_map.get("user_info");
		    		m_sUserTitle = (String)m_map.get("user_title");
		    		if (null != m_map.get("search_with_photos"))
		    			o.setSearchWithPhotos(((String)m_map.get("search_with_photos")).equals("1") ? true : false);
		    	} else {
			    	m_sInfo = Main.formatUserInfo(m_map, m_actHome);
			    	m_sUserTitle = m_sUsername;
		    	}
					
				if (m_iProtocolVer > 1) {					
					reloadButtons((Object [])m_map.get("menu"));
				} else {				
					reloadButtons((String)m_map.get("unreadLetters"), (String)m_map.get("friendRequests"));
				}
			}

        }, this);        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.home, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.home_logout:
			Bundle b = new Bundle();
			b.putInt("index", m_iSiteIndex);
			Intent i = new Intent();
			i.putExtras(b);
			
			setResult(RESULT_LOGOUT, i);
    	
			finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (null == i) 
        	return;
                
        switch(requestCode) {        
        case ACTIVITY_STATUS_MESSAGE:        	
        	switch (resultCode) {
        	case StatusMessageActivity.RESULT_OK:
        		{
        			Bundle b = i.getExtras();
        			updateStatus (b.getString("status_message"));
        		}
        		break;        		
        	}
        	break;        	
        }        
    }    
    
    
    protected void LaunchActivityStatus () {
		Intent i = new Intent(m_actHome, StatusMessageActivity.class);
		i.putExtra("status_message", (String)m_map.get("status"));
		startActivityForResult(i, ACTIVITY_STATUS_MESSAGE);
    }
    
    protected void LaunchActivityInfo ()  {
		Intent i = new Intent(this, ProfileInfoActivity.class);                	
		i.putExtra("username", m_sUsername);
		i.putExtra("user_title", m_sUserTitle);
		i.putExtra("thumb", m_sThumb);
		i.putExtra("info", m_sStatus);		
		startActivityForResult(i, ACTIVITY_PROFILE_INFO);
	}


    protected void LaunchActivityLocation ()  {
		Connector o = Main.getConnector();
		Intent i = new Intent(this, LocationActivity.class);
		i.putExtra("username", o.getUsername());
		startActivityForResult(i, ACTIVITY_LOCATION);
	}

    protected void LaunchActivityMail () {	
		Intent i = new Intent(this, MailHomeActivity.class);
		startActivityForResult(i, ACTIVITY_MAIL_HOME);
	}

    protected void LaunchActivityFriends () {
		Intent i = new Intent(this, FriendsHomeActivity.class);
		startActivityForResult(i, ACTIVITY_FRIENDS_HOME);
	}    	

    protected void LaunchActivityPhotos () {
		Connector o = Main.getConnector();
		Intent i = new Intent(this, ImagesAlbumsActivity.class);
		i.putExtra("username", o.getUsername());				
		startActivityForResult(i, ACTIVITY_IMAGES_ALBUMS);
	}    	

    protected void LaunchActivityVideos () {
		Connector o = Main.getConnector();
		Intent i = new Intent(this, VideosAlbumsActivity.class);
		i.putExtra("username", o.getUsername());				
		startActivityForResult(i, ACTIVITY_VIDEOS_ALBUMS);
	}    	

    protected void LaunchActivitySounds () {
		Connector o = Main.getConnector();
		Intent i = new Intent(this, SoundsAlbumsActivity.class);
		i.putExtra("username", o.getUsername());				
		startActivityForResult(i, ACTIVITY_SOUNDS_ALBUMS);
	}    	

    protected void LaunchActivitySearch () {
		Intent i = new Intent(this, SearchHomeActivity.class);
		startActivityForResult(i, ACTIVITY_SEARCH_HOME);        		        	
	}    

    protected void LaunchActivityWebPage (String sTitle, String sUrl) {
		Intent i = new Intent(this, WebPageActivity.class);
		i.putExtra("title", sTitle);
		i.putExtra("url", sUrl);
		startActivityForResult(i, ACTIVITY_WEB_PAGE);        		        	
	}        
    
    protected void LaunchActivityBrowser (String sTitle, String sUrl) {
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
    	startActivity(browserIntent);
    }

    protected void updateStatus (String s) {
    	if (null == s)
    		return;
    	if (null == m_map)
    		return;
    	m_map.put("status", s);
    	m_sStatus = s;
    }

    interface ThirdPartyOnClickListener extends View.OnClickListener {
    	public void setUrl(String s);
    	public void setAction(int iAction);
		public void setTitle(String s);
    }
}

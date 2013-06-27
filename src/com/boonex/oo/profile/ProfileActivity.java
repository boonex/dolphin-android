package com.boonex.oo.profile;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.friends.FriendsActivity;
import com.boonex.oo.home.WebPageActivity;
import com.boonex.oo.location.LocationActivity;
import com.boonex.oo.mail.MailComposeActivity;
import com.boonex.oo.media.ImagesAlbumsActivity;
import com.boonex.oo.media.SoundsAlbumsActivity;
import com.boonex.oo.media.VideosAlbumsActivity;

public class ProfileActivity extends ListActivityBase {
	
	private static final String TAG = "ProfileActivity";
	
	private static final int ACTIVITY_PROFILE_INFO=0;
	private static final int ACTIVITY_FRINDS_LIST=1;
	private static final int ACTIVITY_LOCATION=2;
	private static final int ACTIVITY_IMAGES_ALBUMS=3;
	private static final int ACTIVITY_VIDEOS_ALBUMS=4;
	private static final int ACTIVITY_SOUNDS_ALBUMS=5;
	private static final int ACTIVITY_CONTACT=6;
	private static final int ACTIVITY_WEB_PAGE=7;

	protected ProfileActivity m_actProfile;
	protected boolean m_isFinishActivityAfterFriendRquest = false;
	
	protected String m_sUsername;
	protected String m_sUserTitle;
	protected String m_sThumb;
	protected String m_sInfo;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);

        m_actProfile = this;
        
        Intent i = getIntent();
        m_sUsername = i.getStringExtra("username");
    	
        setTitleCaption (m_sUsername);
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
    	Connector o = Main.getConnector();
        String sMethod;
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		m_sUsername,
        		Main.getLang()
        };                    
        
        if (o.getProtocolVer() > 1) {
        	sMethod = "dolphin.getUserInfo2";
        } else {
        	sMethod = "dolphin.getUserInfo";
        }
        
        o.execAsyncMethod(sMethod, aParams, new Connector.Callback() {
			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getUserInfo result: " + result.toString());
				
				if (result instanceof String) {
					
					String s = result.toString();
					if (s.equals("-1")) {
						dialogMsgAddFriend(m_actThis.getString(R.string.access_denied));
					} else {
						dialogMsg(0 == s.length() ? m_actThis.getString(R.string.access_denied) : s, true);
					}

				} else {
				
					Map<String, Object> mapProfileInfo;
					Object[] aMenu;
					if (Main.getConnector().getProtocolVer() > 1) {
						Map<String, Object> map = (Map<String, Object>)result;
						mapProfileInfo = (Map<String, Object>)map.get("info");
						aMenu = (Object[])map.get("menu");
					} else {
						mapProfileInfo = (Map<String, Object>)result;
					
			    		Map<String, String> mapInfo = new HashMap<String, String>();
			    		mapInfo.put("title", getString(R.string.profile_info_menu));
			    		mapInfo.put("action", "5");
			    		mapInfo.put("bubble", "");
			    
			    		Map<String, String> mapContact = new HashMap<String, String>();
			    		mapContact.put("title", getString(R.string.profile_info_contact));
			    		mapContact.put("action", "3");
			    		mapContact.put("bubble", "");
			    	
			    		Map<String, String> mapLocation = new HashMap<String, String>();
			    		mapLocation.put("title", getString(R.string.location_menu));
			    		mapLocation.put("action", "2");
			    		mapLocation.put("bubble", "");			    
			    	
			    		Map<String, String> mapFriends = new HashMap<String, String>();
			    		mapFriends.put("title", getString(R.string.friends_menu));
			    		mapFriends.put("action", "4");
			    		mapFriends.put("bubble", null == mapProfileInfo.get("countFriends") ? "" : (String)mapProfileInfo.get("countFriends"));	
			    				    	
			    		Map<String, String> mapImages = new HashMap<String, String>();
			    		mapImages.put("title", getString(R.string.images_menu));
			    		mapImages.put("action", "7");
			    		mapImages.put("bubble", null == mapProfileInfo.get("countPhotos") ? "" : (String)mapProfileInfo.get("countPhotos"));
			    	    	
			    		Map<String, String> mapSounds = new HashMap<String, String>();
			    		mapSounds.put("title", getString(R.string.sounds_menu));
			    		mapSounds.put("action", "9");
			    		mapSounds.put("bubble", null == mapProfileInfo.get("countSounds") ? "" : (String)mapProfileInfo.get("countSounds"));

			    		Map<String, String> mapVideos = new HashMap<String, String>();
			    		mapVideos.put("title", getString(R.string.videos_menu));
			    		mapVideos.put("action", "8");
			    		mapVideos.put("bubble", null == mapProfileInfo.get("countVideos") ? "" : (String)mapProfileInfo.get("countVideos"));
			    	
						Object[] aMenuDefault = {mapInfo, mapContact, mapLocation, mapFriends, mapImages, mapSounds, mapVideos};
						aMenu = aMenuDefault;
					}
				
			    	if (Main.getConnector().getProtocolVer() > 2) {
			    		m_sInfo = (String)mapProfileInfo.get("user_info");
			    		m_sUserTitle = (String)mapProfileInfo.get("user_title");
			    	} else {
				    	m_sInfo = Main.formatUserInfo(mapProfileInfo, m_actProfile);
				    	m_sUserTitle = m_sUsername;
			    	}
			    	setTitleCaption (m_sUserTitle);
			    	m_sThumb = (String)mapProfileInfo.get("thumb");			    	
					ProfileAdapter adapter = new ProfileAdapter (m_actThis, mapProfileInfo, aMenu, m_sUsername);
					setListAdapter(adapter);
				}
			}
        }, this);    	
    }
    	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {    	
        super.onListItemClick(l, v, position, id);
                
        Object[] aMenu = ((ProfileAdapter)getListAdapter()).getMenu();        
        if (position < 1 || position > (aMenu.length+1))
        	return;
        
        @SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)aMenu[position-1];         
        String sAction = map.get("action");
        int iAction = Integer.parseInt(sAction);        
                
        switch (iAction) {        
        	case 2:
        	{
        		Intent i = new Intent(this, LocationActivity.class);
        		i.putExtra("username", m_sUsername);
        		startActivityForResult(i, ACTIVITY_LOCATION);        	
        	}
        	break;
        	case 3:
        	{
        		Intent i = new Intent(this, MailComposeActivity.class);    			                         	
        		i.putExtra("recipient", m_sUsername);
        		i.putExtra("recipient_title", m_sUserTitle);
        		startActivityForResult(i, ACTIVITY_CONTACT);
        	}
        	break;        	
        	case 4:
    		{
    			Intent i = new Intent(this, FriendsActivity.class);    			                         	
    			i.putExtra("username", m_sUsername);
    			startActivityForResult(i, ACTIVITY_FRINDS_LIST);
    		}    	        
        	break;
        	case 5:
        	{
        		Intent i = new Intent(this, ProfileInfoActivity.class);
        		i.putExtra("username", m_sUsername);
        		i.putExtra("user_title", m_sUserTitle);
        		i.putExtra("thumb", m_sThumb);
        		i.putExtra("info", m_sInfo);			
        		startActivityForResult(i, ACTIVITY_PROFILE_INFO);        	
        	}
        	break;		
        	case 7:
        	{        	
        		Intent i = new Intent(this, ImagesAlbumsActivity.class);
        		i.putExtra("username", m_sUsername);				
        		startActivityForResult(i, ACTIVITY_IMAGES_ALBUMS);
        	}    	        
        	break;		
        	case 8:
        	{        	
        		Intent i = new Intent(this, VideosAlbumsActivity.class);
        		i.putExtra("username", m_sUsername);				
        		startActivityForResult(i, ACTIVITY_VIDEOS_ALBUMS);
        	}    	        
        	break;
        	case 9:
        	{
        		Intent i = new Intent(this, SoundsAlbumsActivity.class);
        		i.putExtra("username", m_sUsername);				
        		startActivityForResult(i, ACTIVITY_SOUNDS_ALBUMS); 
        	}		
        	break;
        	case 100:
        	{
        		String sUrl = map.get("action_data");
        		String sTitle = map.get("title");
        		Intent i = new Intent(this, WebPageActivity.class);
        		i.putExtra("title", sTitle);
        		i.putExtra("url", sUrl);
        		startActivityForResult(i, ACTIVITY_WEB_PAGE);
        	}		
        	break;
        	case 101:
        	{
        		String sUrl = map.get("action_data");
        		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
        		startActivity(browserIntent);
        	}
        	break;
        }
    }
        
    protected void onAddFriend() {

    	Connector o = Main.getConnector();
    	
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		m_sUsername,
        		Main.getLang()
        };                    
                        
        o.execAsyncMethod("dolphin.addFriend", aParams, new Connector.Callback() {
			public void callFinished(Object result) {
				dialogMsg(result.toString(), m_isFinishActivityAfterFriendRquest);
			}
        }, this);
        
        
    }

    protected void dialogMsgAddFriend(String sMsg) {
        AlertDialog dialog = new AlertDialog.Builder(m_actThis).create();
        dialog.setMessage(sMsg);
        
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.friends_add_friend), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	m_isFinishActivityAfterFriendRquest = true;
            	onAddFriend();
            }
        });
        
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int whichButton) {
    			dialog.dismiss();
        		Connector o = Main.getConnector();
        		if (!m_sUsername.equalsIgnoreCase(o.getUsername()))
        			finish();
    		}
        }); 
        dialog.show();    	
    }
    
    protected void dialogMsg (String sMsg, boolean isFinishOnClose) {
        AlertDialog dialog = new AlertDialog.Builder(m_actThis).create();
        dialog.setMessage(sMsg);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new CustomDialogOnClickListener(isFinishOnClose)); 
        dialog.show();
    }
    
    protected class CustomDialogOnClickListener implements DialogInterface.OnClickListener {
    	protected boolean m_isFinishOnClose;
    	
    	public CustomDialogOnClickListener (boolean isFinishOnClose) {
    		m_isFinishOnClose = isFinishOnClose;
    	}
		@Override
		public void onClick(DialogInterface dialog, int whichButton) {			
			dialog.dismiss();
			if (m_isFinishOnClose) {
    			Connector o = Main.getConnector();
    			if (!m_sUsername.equalsIgnoreCase(o.getUsername()))
    				finish();
			}
		}
    
    }
}

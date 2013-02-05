package com.boonex.oo.mail;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.profile.ProfileActivity;

public class MailMessageActivity extends ListActivityBase {

	private static final String TAG = "MailMessageActivity";	
	Boolean m_isInbox;
	Integer m_iMsgId;
	String m_sRecipient = "";
	String m_sRecipientTitle = "";
	    
    // activity ads
    private static final int ACTIVITY_MAIL_COMPOSE=2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        setTitleCaption(R.string.title_mail_message);

        m_btnAction.setVisibility(View.VISIBLE);
        m_btnAction.setImageResource(R.drawable.ic_toolbar_reply);
        
        Intent i = getIntent();        
        m_isInbox = i.getBooleanExtra("inbox", true);
        m_iMsgId = i.getIntExtra("msg_id", 0);        
        
        reloadRemoteData ();
    }
    
    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		String.valueOf(m_iMsgId)		
        };                                    
        
        o.execAsyncMethod("dolphin." + (m_isInbox ? "getMessageInbox" : "getMessageSent"), aParams, new Connector.Callback() {
			
			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getMessagesInbox result: " + result.toString());				
				Map<String, Object> mapMessage = (Map<String, Object>)result;
				m_sRecipient = (String)mapMessage.get("Nick");				
				m_sRecipientTitle = (String)mapMessage.get(Main.getConnector().getProtocolVer() > 2 ? "UserTitleInterlocutor" : "Nick");
				MailMessageAdapter adapter = new MailMessageAdapter(m_actThis, mapMessage, m_isInbox);         
		        setListAdapter(adapter);
			}
        }, this);    	
    }
    
    protected void customAction () {
		Intent i = new Intent(this, MailComposeActivity.class);
		i.putExtra("recipient", m_sRecipient);
		i.putExtra("recipient_title", m_sRecipientTitle);
		startActivityForResult(i, ACTIVITY_MAIL_COMPOSE);    	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {    	
        super.onListItemClick(l, v, position, id);
        
        if (0 == position) {        
    		Intent i = new Intent(this, ProfileActivity.class);    			                         	
    		i.putExtra("username", m_sRecipient);
    		i.putExtra("user_title", m_sRecipientTitle);
    		startActivityForResult(i, 0);
        }
    }    
}

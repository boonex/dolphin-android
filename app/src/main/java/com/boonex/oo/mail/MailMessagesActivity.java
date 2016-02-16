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

public class MailMessagesActivity extends ListActivityBase {
	private static final int ACTIVITY_MAIL_MESSAGE=0;
	private static final String TAG = "MailMessagesActivity";
	
	protected Boolean m_isInbox;
	protected Object m_aMessages[];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        setTitleCaption(R.string.title_mail_messages);
        
        Intent i = getIntent();        
        m_isInbox = i.getBooleanExtra("inbox", true);        	
        
        reloadRemoteData ();
    }

    protected void reloadRemoteData () {
        Connector o = Main.getConnector();
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword()		
        };                                    
        
        o.execAsyncMethod("dolphin." + (m_isInbox ? "getMessagesInbox" : "getMessagesSent"), aParams, new Connector.Callback() {
			
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getMessagesInbox result: " + result.toString());
				
				m_aMessages = (Object [])result;
				
				Log.d(TAG, "dolphin.getMessagesInbox num: " + m_aMessages.length); 
								
				MailMessagesAdapter adapter = new MailMessagesAdapter(m_actThis, m_aMessages, m_isInbox);         
		        setListAdapter(adapter);
			}
        }, this);    	
    }
 
    @SuppressWarnings("unchecked")
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
                
        Map<String, String> mapMessage = (Map<String, String>)m_aMessages[position];
                    
        Intent i = new Intent(this, MailMessageActivity.class);    			                         	
        i.putExtra("inbox", m_isInbox);
        i.putExtra("msg_id", new Integer(mapMessage.get("ID")));
        startActivityForResult(i, ACTIVITY_MAIL_MESSAGE);        
    }    
}

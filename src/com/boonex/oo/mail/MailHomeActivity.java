
package com.boonex.oo.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.ListActivityBase;
import com.boonex.oo.R;

public class MailHomeActivity extends ListActivityBase {

	private static final int ACTIVITY_MAIL_MESSAGES_INBOX=0;
	private static final int ACTIVITY_MAIL_MESSAGES_OUTBOX=1;
	private static final int ACTIVITY_MAIL_COMPOSE=2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true, false);
        
        setContentView(R.layout.list);
        setTitleCaption(R.string.title_mail_home);
        
        MailHomeAdapter adapter = new MailHomeAdapter(this);         
		setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
        case 0:
    		{
    			Intent i = new Intent(this, MailMessagesActivity.class);    			                         	
    			i.putExtra("inbox", true);
    			startActivityForResult(i, ACTIVITY_MAIL_MESSAGES_INBOX);
    		}    	        
        	break;
        case 1:
        	{
    			Intent i = new Intent(this, MailMessagesActivity.class);    			                         	
    			i.putExtra("inbox", false);
    			startActivityForResult(i, ACTIVITY_MAIL_MESSAGES_OUTBOX);
        	}
        	break;
        case 2:
        	{        		
    			Intent i = new Intent(this, MailComposeActivity.class);    			                         	    			
    			startActivityForResult(i, ACTIVITY_MAIL_COMPOSE);    			
        	}
        	break;        	
        }
    }    
}

package com.boonex.oo.status;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class StatusMessageActivity extends ActivityBase {
	private static final String TAG = "StatusMessageActivity";
	public static final int RESULT_OK = RESULT_FIRST_USER + 1;
	private EditText m_editStatus;
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);        
        
        setContentView(R.layout.status_message);
        setTitleCaption (R.string.title_status_message);
        
        m_editStatus = (EditText) findViewById(R.id.status_message);
        
        Intent i = getIntent();        
        if (null != i.getStringExtra("status_message")) {
        	String s = i.getStringExtra("status_message");
        	m_editStatus.setText(s);
        }
                
    }
    
    protected void actionUpdateMessage() {
        Connector o = Main.getConnector();

        Log.d(TAG, o.getPassword());
        
        Object[] aParams = {
        		o.getUsername(),
        		o.getPassword(),
        		m_editStatus.getText().toString()
        };                    
        
        o.execAsyncMethod("dolphin.updateStatusMessage", aParams, new Connector.Callback() {
			public void callFinished(Object result) {
				Log.d(TAG, "dolphin.updateStatusMessage result: " + result.toString());        				
				Bundle b = new Bundle();
    			b.putString("status_message", m_editStatus.getText().toString());
    			Intent i = new Intent();
    			i.putExtras(b);
    			setResult(RESULT_OK, i);
				finish(); 
			}
        }, m_actThis);    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.status_message, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.status_message_update:
        	actionUpdateMessage();
            break;
        }
        return super.onOptionsItemSelected(item);
    }    
}

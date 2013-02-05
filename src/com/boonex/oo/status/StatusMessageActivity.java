package com.boonex.oo.status;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class StatusMessageActivity extends ActivityBase {
	private static final String TAG = "StatusMessageActivity";
	public static final int RESULT_OK = RESULT_FIRST_USER + 1;
	private Button m_buttonSubmit;
	private EditText m_editStatus;
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);        
        
        setContentView(R.layout.status_message);
        setTitleCaption (R.string.title_status_message);
        
        m_buttonSubmit = (Button) findViewById(R.id.submit);
        m_editStatus = (EditText) findViewById(R.id.status_message);
        
        Intent i = getIntent();        
        if (null != i.getStringExtra("status_message")) {
        	String s = i.getStringExtra("status_message");
        	m_editStatus.setText(s);
        }
        
        MySetContextInterface listener = new MySetContextInterface() {
        	public StatusMessageActivity context;
            public void onClick(View view) {            	
    			
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
                }, this.context);
                
            }          
            public void setContext(StatusMessageActivity context) {
            	this.context = context;            
            }
        };        
        listener.setContext(this);
        m_buttonSubmit.setOnClickListener(listener);        
    }
    
    interface MySetContextInterface extends View.OnClickListener {
    	public void setContext(StatusMessageActivity context);    
    }    
}

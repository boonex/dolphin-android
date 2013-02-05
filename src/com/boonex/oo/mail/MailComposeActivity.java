package com.boonex.oo.mail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.UserPickerActivity;

public class MailComposeActivity extends ActivityBase {
	private static final int ACTIVITY_USER_PICKER=0;
	private static final String TAG = "MailComposeActivity";
	protected Button m_buttonSubmit;
	protected Button m_buttonSelectUser;
	protected EditText m_editRecipient;
	protected EditText m_editSubject;
	protected EditText m_editText;
	protected RadioButton m_radioOptionsMe;
	protected RadioButton m_radioOptionsRecipient;
	protected RadioButton m_radioOptionsBoth;
	protected String m_sRecipient;
	protected String m_sRecipientTitle;
	MailComposeActivity m_actMailCompose;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false);
        
        setContentView(R.layout.mail_compose);
        setTitleCaption(R.string.title_mail_compose);
        
        m_editRecipient = (EditText) findViewById(R.id.mail_compose_recipient);
        m_editSubject = (EditText) findViewById(R.id.mail_compose_subject);
        m_editText = (EditText) findViewById(R.id.mail_compose_text);        
        m_buttonSubmit = (Button) findViewById(R.id.mail_compose_send);
        m_buttonSelectUser = (Button) findViewById(R.id.mail_compose_select_user);
        m_radioOptionsMe = (RadioButton) findViewById(R.id.mail_compose_options_me);
        m_radioOptionsRecipient = (RadioButton) findViewById(R.id.mail_compose_options_recipient);
        m_radioOptionsBoth = (RadioButton) findViewById(R.id.mail_compose_options_both);        
        m_actMailCompose = this;
        
        Intent i = getIntent();  
        m_sRecipient = i.getStringExtra("recipient");
        m_sRecipientTitle = i.getStringExtra("recipient_title");
        if (null != m_sRecipientTitle)
        	m_editRecipient.setText(m_sRecipientTitle);
        	
        m_buttonSelectUser.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
    			Intent i = new Intent(m_actMailCompose, UserPickerActivity.class);
    			startActivityForResult(i, ACTIVITY_USER_PICKER);               
            }
        });         
        
        m_buttonSubmit.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
                Connector o = Main.getConnector();                
                
                if (0 == m_editRecipient.getText().length() || 
                	0 == m_editSubject.getText().length() || 
                	0 == m_editText.getText().length()) {
                	AlertDialog dialog = new AlertDialog.Builder(m_actMailCompose).create();
                	dialog.setTitle(getString(R.string.mail_error));
                	dialog.setMessage(getString(R.string.mail_form_error));
                	dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int whichButton) {
                			dialog.dismiss();
                		}
                	}); 
                	dialog.show();
                	return;
                }
                
                
                String sOptions = "";
                if (m_radioOptionsMe.isChecked())
                	sOptions = "me";
                else if (m_radioOptionsRecipient.isChecked())
                	sOptions = "recipient";
                else if (m_radioOptionsBoth.isChecked())
                	sOptions = "both";                
                
                Log.d(TAG, "recipient: " + m_sRecipient);
                
                Object[] aParams = {
                		o.getUsername(), 
                		o.getPassword(),
                		m_sRecipient,
                		m_editSubject.getText().toString(),
                		m_editText.getText().toString(),
                		sOptions
                };                    
                
                o.execAsyncMethod("dolphin.sendMessage", aParams, new Connector.Callback() {
        			public void callFinished(Object result) {				 
        				
        				Log.d(TAG, "dolphin.sendMessage result: " + result.toString());
        				Integer iResult = new Integer (result.toString());
        				String sErrorMsg = "";
        				String sTitle = getString(R.string.mail_error);
        				switch (iResult) {
        				case 1:
        					sErrorMsg = getString(R.string.mail_msg_send_failed);
        					break;
        				case 3:
        					sErrorMsg = getString(R.string.mail_wait_before_sendin_another_msg);
        					break;
        				case 5:
        					sErrorMsg = getString(R.string.mail_you_are_blocked);
        					break;
        				case 10:
        					sErrorMsg = getString(R.string.mail_recipient_is_inactive);
        					break;
        				case 1000:
        					sErrorMsg = getString(R.string.mail_unknown_recipient);
        					break;
        				case 1001:
        					sErrorMsg = getString(R.string.mail_membership_dont_allow);
        					break;
        					
        				default:
        					sTitle = getString(R.string.mail_success);
        					sErrorMsg = getString(R.string.mail_msg_successfully_sent);        					
        				}
        				        				
                        AlertDialog dialog = new AlertDialog.Builder(m_actMailCompose).create();
                        dialog.setTitle(sTitle);
                        dialog.setMessage(sErrorMsg);
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
                        	public void onClick(DialogInterface dialog, int whichButton) {
                        		dialog.dismiss();
                        		finish();
                        	}
                        }); 
                        dialog.show();        					
        				        				 
        			}
                }, m_actMailCompose);               
            }
        });
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (null == i) 
        	return;
                
        Bundle b = i.getExtras();
        m_sRecipient = b.getString("name");
        m_sRecipientTitle = b.getString("user_title");
        m_editRecipient.setText(m_sRecipientTitle);
    }    
}

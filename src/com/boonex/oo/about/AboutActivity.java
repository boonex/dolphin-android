package com.boonex.oo.about;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.R;

public class AboutActivity extends ActivityBase {
	private static final String TAG = "AboutActivity";	
	
	protected TextView m_textAbout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, false, false, false);
        
        setContentView(R.layout.about);
        setTitleCaption(R.string.title_about);    
        
        m_textAbout = (TextView) findViewById(R.id.about_text);        
        m_textAbout.setText(Html.fromHtml(this.getString(R.string.about_text, getAppVer())));
        m_textAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    protected String getAppVer() {
    	String sVer = "1";
        try {
            sVer = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e) {
            Log.w(TAG, e.getMessage());
        }   
        return sVer;
    }

}

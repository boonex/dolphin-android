package com.boonex.oo.media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.R;

public class AddMediaActivity extends ActivityBase {
	private static final String TAG = "AddMediaActivity";
	protected static final int CAMERA_ACTIVITY = 0;
	protected static final int PICKER_ACTIVITY = 1;
	
	protected String sGalleryFilesType;	
	protected Button m_buttonFromCamera;
	protected Button m_buttonFromGallery;
	protected EditText m_editTitle;
	protected EditText m_editTags;
	protected EditText m_editDesc;
	protected ImageView m_viewFileThumb;	
	protected String m_sAlbumName;
	protected AddMediaActivity m_actAddMedia;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false); 
                
        m_actAddMedia = this;
        
        setContentView(R.layout.media_add);
        setTitleCaption (R.string.title_media_add);
                
        m_buttonFromCamera = (Button) findViewById(R.id.media_btn_from_camera);
        m_buttonFromGallery = (Button) findViewById(R.id.media_btn_from_gallery);
        m_editTitle = (EditText) findViewById(R.id.media_title);
        m_editTags = (EditText) findViewById(R.id.media_tags);
        m_editDesc = (EditText) findViewById(R.id.media_desc);        
        m_viewFileThumb = (ImageView) findViewById(R.id.media_file_preview);
        
        Intent i = getIntent();  
        m_sAlbumName = i.getStringExtra("album_name");
        
        m_buttonFromGallery.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {
    	    	Intent filePickerIntent = new Intent(Intent.ACTION_PICK);
    	    	filePickerIntent.setType(sGalleryFilesType);
            	startActivityForResult(filePickerIntent, PICKER_ACTIVITY);
            }
        });

    }
 
    protected void actionSubmitFile() {
    	// overridded in child classes 
    }
    
	protected void showToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}
	protected void showToast(int i) {
		showToast(getString(i));
	}
	
	protected String getRealPathFromURI(Uri contentUri) {
		if (contentUri.toString().startsWith("file://"))
			return contentUri.toString().replace("file://", "");
		return getDataFromURI(MediaStore.Images.Media.DATA, contentUri);
	}
	
	protected String getDataFromURI(String sData, Uri contentUri) {		
	    String[] proj = { sData };
	    CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();	    
	    int column_index = cursor.getColumnIndexOrThrow(sData);
	    if (!cursor.moveToFirst())
	    	return null;
	    return cursor.getString(column_index);
	}
	
    protected static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    protected static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    protected void showErrorDialog(int iErrorMsg, boolean isFinishOnClose) {
    	showErrorDialog(getString(iErrorMsg), isFinishOnClose);
    }
    protected void showErrorDialog(String sErrorMsg, boolean isFinishOnClose) {
		AlertDialog dialog = new AlertDialog.Builder(m_actAddMedia).create();
		dialog.setTitle(R.string.media_popup_error_title);
		dialog.setMessage(sErrorMsg);
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new CustomOnClickListener(isFinishOnClose)); 
		dialog.show();    	
    }

    protected class CustomOnClickListener implements DialogInterface.OnClickListener {
		protected boolean m_isFinishOnClose;    	
    	public CustomOnClickListener (boolean isFinishOnClose) {
    		m_isFinishOnClose = isFinishOnClose;
    	}
		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
			dialog.dismiss();
			if (m_isFinishOnClose)
				finish();
		}		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.media_add, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.media_submit:
        	actionSubmitFile();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected boolean isFileTooBig (double fileSize, boolean isDisplayError) {    	
    	double max = Runtime.getRuntime().maxMemory(); //the maximum memory the app can use
    	double heapSize = Runtime.getRuntime().totalMemory(); //current heap size
    	double heapRemaining = Runtime.getRuntime().freeMemory(); //amount available in heap
    	double nativeUsage = Debug.getNativeHeapAllocatedSize(); //is this right? I only want to account for native memory that my app is being "charged" for.  Is this the proper way to account for that?    	
    	double remaining = max - (heapSize - heapRemaining + nativeUsage); //heapSize - heapRemaining = heapUsed + nativeUsage = totalUsage

    	double potentialMemoryRequired = 2.5*1024*1024 + fileSize + (fileSize * 1.37) * 4; // potential required memory is 2.5Mb(service methids usage) + binary data + two strings of 64base encoded data (1 char is 2 bytes in java)  
    	
    	Log.i(TAG, "----------------------------------------");
    	Log.i(TAG, String.format("File size: %.2f Mb", fileSize/1024/1024));
    	Log.i(TAG, String.format("Memory - max:%.2fMb   heapSize:%.2fMb   heapRemaining:%.2fMb   nativeUsage:%.2fMb", max/1024/1024, heapSize/1024/1024, heapRemaining/1024/1024, nativeUsage/1024/1024));
    	Log.i(TAG, String.format("Memory remaining: %.2f Mb", remaining/1024/1024));
    	Log.i(TAG, String.format("Memory requred: %.2f Mb", potentialMemoryRequired/1024/1024));
    	Log.i(TAG, "----------------------------------------");

    	if (remaining > potentialMemoryRequired)
    		return false;

    	if (isDisplayError)
    		showErrorDialog(R.string.media_error_file_too_big, false);
    	
    	return true;
    }
}

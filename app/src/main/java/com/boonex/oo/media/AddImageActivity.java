package com.boonex.oo.media;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class AddImageActivity extends AddMediaActivity {
	private static final String TMP_FILE = "tmp_oo.jpg";
	private static final int MAX_WIDTH = 1280;
	private static final int MAX_HEIGHT = 1280;
	private static final String TAG = "AddImageActivity";
	
	protected Bitmap m_bmpImage;	
    
	public AddImageActivity () {
		super();
		sGalleryFilesType = "image/*";
	}
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);  
                        
        Object data = getLastNonConfigurationInstance();
        if (data != null) 
        	m_bmpImage = (Bitmap)data;         
        
        if (null != m_bmpImage)
        	m_viewFileThumb.setImageBitmap(m_bmpImage); 
       
        m_buttonFromCamera.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
                           	
            	File file = new File(Environment.getExternalStorageDirectory(), TMP_FILE);
            	
        		Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        		mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        		startActivityForResult(mIntent, CAMERA_ACTIVITY);
            }
        });
         
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        return m_bmpImage;
    }
    
    @Override
    protected void actionSubmitFile() {
        Connector o = Main.getConnector();                
        
        if (0 == m_editTitle.getText().length() || null == m_bmpImage) {
        	showErrorDialog(R.string.media_form_error, false);
        	return;
        }
        
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        m_bmpImage.compress(Bitmap.CompressFormat.JPEG, 75, bao);
        byte [] ba = bao.toByteArray();
        
        if (isFileTooBig(ba.length, true))
        	return;
        
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		m_sAlbumName,
        		ba,
        		Integer.valueOf(ba.length).toString(),
        		m_editTitle.getText().toString(),
        		m_editTags.getText().toString(),
        		m_editDesc.getText().toString()
        };                    
                        
        o.execAsyncMethod("dolphin.uploadImage", aParams, new Connector.Callback() {
			public void callFinished(Object result) {				         				
				
				
				Log.d(TAG, "dolphin.uploadImage result: " + result.toString());
				
				if (!result.toString().equals("ok")) {
					showErrorDialog(R.string.media_upload_failed, true);
				} else {
					Connector o = Main.getConnector();
					o.setImagesReloadRequired(true);
					o.setAlbumsReloadRequired(true);
					finish();
				}
			}
        }, m_actAddMedia); 
    }
    
	/**
	 * Retrieves the returned image from the Intent, inserts it into the MediaStore, which
	 *  automatically saves a thumbnail. Then assigns the thumbnail to the ImageView.
	 *  @param requestCode is the sub-activity code
	 *  @param resultCode specifies whether the activity was cancelled or not
	 *  @param intent is the data packet passed back from the sub-activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {		
		super.onActivityResult(requestCode, resultCode, intent);
				
		if (resultCode == RESULT_CANCELED) {
			showToast(getString(R.string.media_activity_canceled));
			return;
		}
				
		if (requestCode == CAMERA_ACTIVITY || requestCode == PICKER_ACTIVITY) {
			
			Bitmap tmpImage = null;				
			Bundle b;
			String sMsg = null;
			
			if (requestCode == CAMERA_ACTIVITY) { 		
				
				if (null != intent && null != (b = intent.getExtras()) && null != (m_bmpImage = (Bitmap) b.get("data"))) {
								
					tmpImage = (Bitmap) b.get("data");
					
				} else {
					
					File file = new File(Environment.getExternalStorageDirectory(), TMP_FILE);					
					tmpImage = BitmapFactory.decodeFile(file.getAbsolutePath());
					
					try {
						
						ExifInterface exif = new ExifInterface(file.getAbsolutePath());
						String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
						Matrix matrix = new Matrix();
						if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_90+""))
							matrix.postRotate(90);
						else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_180+""))
							matrix.postRotate(180);
						else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_270+""))
							matrix.postRotate(270);						
						tmpImage = Bitmap.createBitmap(tmpImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), matrix, true);
						
						
					} catch (IOException e) {
						sMsg = e.toString();
					} catch (IllegalArgumentException e) {
						sMsg = e.toString();
					}
					
					
				}
			} else { // media library
				
			    Uri imageUri = intent.getData();
			    Log.i(TAG, "Image URI: " + imageUri);
			    
			    try {
                    tmpImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
			    } catch (FileNotFoundException e) {
			    	sMsg = e.toString();
			    } catch (IOException e) {
			    	sMsg = e.toString();
			    }

			}
			
			if (null == sMsg) {
				
				float scaleFactor;
				int w = tmpImage.getWidth();
				int h = tmpImage.getHeight();
				if (w > h)
					scaleFactor = ((float) MAX_WIDTH) / w;
				else
					scaleFactor = ((float) MAX_HEIGHT) / h;				       
				
				Matrix matrix = new Matrix();				
				matrix.postScale(scaleFactor, scaleFactor);
		        
				// recreate final bitmap
				if (m_bmpImage != null) {
					m_viewFileThumb.setImageBitmap(null);
					m_bmpImage = null;
				}
				m_bmpImage = Bitmap.createBitmap(tmpImage, 0, 0, w, h, matrix, true); 

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
					tmpImage.recycle();
		    					
				m_viewFileThumb.setImageBitmap(m_bmpImage); // Display image in the View				
			
				sMsg = getString(R.string.media_file_selected);
					
			}
			
			showToast(sMsg);
		}			

	}
    
}

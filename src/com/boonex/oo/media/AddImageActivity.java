package com.boonex.oo.media;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;

public class AddImageActivity extends ActivityBase {
	private static final String TMP_FILE = "tmp_oo.jpg";
	private static final int MAX_WIDTH = 1280;
	private static final int MAX_HEIGHT = 1280;
	private static final int CAMERA_ACTIVITY = 0;
	private static final int PICKER_ACTIVITY = 1;
	private static final String TAG = "AddImageActivity";
	protected Button m_buttonSubmit;
	protected Button m_buttonFromCamera;
	protected Button m_buttonFromGallery;
	protected EditText m_editTitle;
	protected EditText m_editTags;
	protected EditText m_editDesc;
	protected ImageView m_viewImage;
	protected Bitmap m_bmpImage;	
	protected String m_sAlbumName;
	protected AddImageActivity m_actAddImage;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false);
        
        setContentView(R.layout.media_images_add);
        setTitleCaption (R.string.title_image_add);
        
        m_buttonSubmit = (Button) findViewById(R.id.media_images_submit);
        m_buttonFromCamera = (Button) findViewById(R.id.media_images_btn_from_camera);
        m_buttonFromGallery = (Button) findViewById(R.id.media_images_btn_from_gallery);
        m_editTitle = (EditText) findViewById(R.id.media_images_title);
        m_editTags = (EditText) findViewById(R.id.media_images_tags);
        m_editDesc = (EditText) findViewById(R.id.media_images_desc);        
        m_viewImage = (ImageView) findViewById(R.id.media_images_image);
        if (null != m_bmpImage)
        	m_viewImage.setImageBitmap(m_bmpImage);
        
        m_actAddImage = this;
        
        Intent i = getIntent();  
        m_sAlbumName = i.getStringExtra("album_name");
        
        m_buttonFromCamera.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
                           	
            	File file = new File(Environment.getExternalStorageDirectory(), TMP_FILE);
            	
        		Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	
        			//mIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
        			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        			startActivityForResult(mIntent, CAMERA_ACTIVITY);
        		
            }
        });         

        m_buttonFromGallery.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
    	    	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            	photoPickerIntent.setType("image/*");            
            	startActivityForResult(photoPickerIntent, PICKER_ACTIVITY);
            }
        });         

        
        m_buttonSubmit.setOnClickListener(new View.OnClickListener(){            
            public void onClick(View view) {               
                Connector o = Main.getConnector();                
                
                if (0 == m_editTitle.getText().length() || 
                	null == m_bmpImage) {
                	AlertDialog dialog = new AlertDialog.Builder(m_actAddImage).create();
                	dialog.setTitle(getString(R.string.media_image_add_popup_error_title));
                	dialog.setMessage(getString(R.string.media_image_add_form_error)); 
                	dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int whichButton) {
                			dialog.dismiss();
                		}
                	}); 
                	dialog.show();
                	return;
                }
                
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                m_bmpImage.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte [] ba = bao.toByteArray();
                
                
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
        				        				
        					String sTitle = getString(R.string.media_image_add_popup_error_title);
        					String sErrorMsg = getString(R.string.media_image_upload_failed);
        				        				
        					AlertDialog dialog = new AlertDialog.Builder(m_actAddImage).create();
        					dialog.setTitle(sTitle);
        					dialog.setMessage(sErrorMsg);
        					dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.close), new DialogInterface.OnClickListener() {
        						public void onClick(DialogInterface dialog, int whichButton) {
                        			dialog.dismiss();
                        			finish();
        						}
        					}); 
        					dialog.show();
        				} else {
        					Connector o = Main.getConnector();
        					o.setImagesReloadRequired(true);
        					o.setAlbumsReloadRequired(true);
        					finish();
        				}
        			}
                }, m_actAddImage);
                               
            }
        });
 
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
		
		Log.i(TAG, "Result code = " + resultCode);				
		Log.i(TAG, "Request code = " + requestCode);				
		
		if (resultCode == RESULT_CANCELED) {
			showToast(getString(R.string.media_images_add_activity_canceled));
			return;
		}
		
		Log.i(TAG, "intent = " + intent);
		
		if (requestCode == CAMERA_ACTIVITY || requestCode == PICKER_ACTIVITY) {
			
			Bitmap tmpImage = null;				
			Bundle b;
			
			if (requestCode == CAMERA_ACTIVITY) { 				
				if (null != intent && null != (b = intent.getExtras()) && null != (m_bmpImage = (Bitmap) b.get("data"))) {
					Log.i(TAG, "Bundle = " + b);				
					tmpImage = (Bitmap) b.get("data");
				} else {
					Log.i(TAG, "Bundle is NULL");
					File file = new File(Environment.getExternalStorageDirectory(), TMP_FILE);
					Log.i(TAG, "Path = " + Environment.getExternalStorageDirectory());
					Log.i(TAG, "AbsolutePath = " + file.getAbsolutePath());
					tmpImage = BitmapFactory.decodeFile(file.getAbsolutePath());
				}
			} else { // media library
				
			    Uri imageUri = intent.getData();
			    String imgPath = imageUri.toString();
			    
			    if (imageUri.toString().contains("content:")){ //file is in media library
			    	 
				 	String[] projection = new String[] {
					       		    Images.Media._ID,
					       		    Images.Media.DATA,
					       		    Images.Media.MIME_TYPE,
					       		    Images.Media.ORIENTATION
					       		};

				 	  Cursor cur = getContentResolver().query(imageUri, projection, null, null, null);
				 	 
				 	  if (cur.moveToFirst()) {				 		  
				 		  
				 		  int dataColumn = cur.getColumnIndex(Images.Media.DATA);
				 		  int mimeTypeColumn = cur.getColumnIndex(Images.Media.MIME_TYPE);
				 		  int orientationColumn = cur.getColumnIndex(Images.Media.ORIENTATION);
		              	            				       
				 		  Log.i(TAG, "orientation = " + cur.getString(orientationColumn));
				 		  Log.i(TAG, "data = " + cur.getString(dataColumn));
				 		  Log.i(TAG, "mime/type = " + cur.getString(mimeTypeColumn));

				 		  tmpImage = BitmapFactory.decodeFile(cur.getString(dataColumn));				 		 
				 	  } 
				 	  
			    } else {			 		  
			    	tmpImage = BitmapFactory.decodeFile(imgPath.replace("file://", ""));
			    }
					
			}
			
			float scaleFactor;
			int w = tmpImage.getWidth();
			int h = tmpImage.getHeight();
			if (w > h)
				scaleFactor = ((float) MAX_WIDTH) / w;
			else
				scaleFactor = ((float) MAX_HEIGHT) / h;				       
		
		    // create matrix for the manipulation		
		    Matrix matrix = new Matrix();		
		    // resize the bit map		
		    matrix.postScale(scaleFactor, scaleFactor);
		        
		    // recreate the new Bitmap
		    m_bmpImage = Bitmap.createBitmap(tmpImage, 0, 0, w, h, matrix, true); 
		
		    tmpImage.recycle();
		        
			m_viewImage.setImageBitmap(m_bmpImage); // Display image in the View				
				
			showToast(getString(R.string.media_images_add_image_selected));
		}			

	}

	/**
	 * Utility method for displaying a Toast.
	 * @param mContext
	 * @param text
	 */
	private void showToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}    
}

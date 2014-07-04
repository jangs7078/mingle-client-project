package com.example.mingle;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.view.View;

import com.example.mingle.HttpHelper;

import android.widget.*;

import com.example.mingle.MingleApplication;
/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
*/
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {//implements ConnectionCallbacks, OnConnectionFailedListener {
    //public static int REQUEST_CODE = 1;
    private Bitmap taken_photo_bitmap;		//Bitmap to save photo just taken
    private ArrayList<Bitmap> photo_list;	//List of user's photos
    private String sex_option;				//Sex identity of user
    private String comment_option;			//Comment written by user
    private int num_option;					//Number of people with user
    private float latitude;					//GPS latitude of user
    private float longitude;				//GPS longitude of user
    
    //Server Address
    private static final String server_url = "http://ec2-54-178-214-176.ap-northeast-1.compute.amazonaws.com:8080";
    
    //private GoogleApiClient mGoogleApiClient;
    
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //InputStream stream = null;
    	
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            	
            	Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);
                ImageView imageView = (ImageView) findViewById(R.id.photoView1);
                ContentResolver cr = getContentResolver();
                
                try {
                     taken_photo_bitmap = android.provider.MediaStore.Images.Media
                     .getBitmap(cr, selectedImage);
                    imageView.setImageBitmap(taken_photo_bitmap);
                    Toast.makeText(this, selectedImage.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Camera " + e.toString());
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_RESOLVE_ERROR) {     
                mResolvingError = false;
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
      /*              if (!mGoogleApiClient.isConnecting() &&
                            !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }*/
                }
            }
   
    }
    

   
    
    @Override
    protected void onStop() {
//        mGoogleApiClient.disconnect();
        super.onStop();
    }
    
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //Initialize HttpHelper that supports HTTP GET/POST requests and socket connection
        ((MingleApplication) this.getApplication()).connectHelper = new HttpHelper(server_url, this);
        //Initialize MingleUser object that stores current user's info
        ((MingleApplication) this.getApplication()).currUser = new MingleUser();
        //Initialize DatabaseHelper
        ((MingleApplication) this.getApplication()).dbHelper = new DatabaseHelper(this);
        //Initialize Google API
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .addConnectionCallbacks(this)
//        .addOnConnectionFailedListener(this)
//        .build();
        
        if (!mResolvingError) {  // more about this later
//            mGoogleApiClient.connect();
        }
        
        /*
        sex_option = "M";
        Switch mySwitch = (Switch)findViewById(R.id.sex_switch);
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)sex_option = "F";
                else sex_option = "M";
            }
        });
        
        */
        
        sex_option = "M";
        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        // get selected radio button from radioGroup
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group,
                    int checkedId) {
            	if(checkedId == R.id.radioFemale) sex_option="F";
            	else sex_option="M";
            }
        });
        //this.takePicture();
    }

    //On user creation request, get user's info and send request to server
    public void userCreateButtonPressed(View view) {
        //hard coded. need to be replaced later on
        comment_option = "hi";
        num_option = 300;
        latitude = (float)4.11;
        longitude = (float)4.11;
        
        //Check validity of user input and send user creation request to server
        //if (new_user.isValid()) {
        ((MingleApplication) this.getApplication()).connectHelper.userCreateRequest(photo_list, comment_option, sex_option, num_option, longitude, latitude);
       //} else {
       //    System.out.println("The user is not valid.");
       //}
    }

    //Update MingleUser info and join Mingle Market
    //Called when user creation request confirmation returns from server
    public void joinMingle(JSONObject userData) {
        try {
            System.out.println(userData.toString());
            ((MingleApplication) this.getApplication()).currUser.addPhoto(taken_photo_bitmap);
            ((MingleApplication) this.getApplication()).currUser.setAttributes(userData.getString("UID"), userData.getString("SEX"), userData.getInt("NUM"), userData.getString("COMM"),
                                                                                        (float)userData.getDouble("LOC_LAT"), (float)userData.getDouble("LOC_LONG"), userData.getInt("DIST_LIM"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        
        //Start activity for Mingle Market
        Intent i = new Intent(this, HuntActivity.class);
        startActivity(i);
    }

    private byte[] compressPicture(Bitmap pic) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;

    /* Takes a picture with the camera */
    private void takePicture() {
    	 Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	  File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT,
    	            Uri.fromFile(photo));
    	 imageUri = Uri.fromFile(photo);
    	 startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings )
            return true;

        return super.onOptionsItemSelected(item);
    }




    
    
    /* Google Connection Client Connection callback functions */
    
	//@Override
/*	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
        	//showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
	}




*/
/*	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		System.out.println("unconnected!!");
		
	}





	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	*/
    /*
    // Creates a dialog for an error message 
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        //dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    // Called from ErrorDialogFragment when the dialog is dismissed. 
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    // A fragment to display an error dialog 
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity)getActivity()).onDialogDismissed();
        }
    }
    
    */
}



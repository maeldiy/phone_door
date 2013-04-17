/*
 * Bidirectional Android -> Arduino TCP Client
 * 
 * 28.04.2012
 * by Laurid Meyer
 * 
 * http://www.lauridmeyer.com
 * 
 */
package com.maelreymond.phone_door;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BidirectionalAndroidUDPClientActivity extends Activity {
	
	TextView textlog;//Log for outputs
	
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar
	ProgressBar progressbar;//progressbar showing the poti rotation
	
	Boolean connected=false;//stores the connectionstatus
	
 //   NetworkTask networktask;//networktask is the included class to handle the socketconnection
    
    // ajout variable mael
	boolean mIsBound;
	Intent intent;
	String etat_service;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    if (CheckIfServiceIsRunning()) {
			etat_service = "1";
	
			Toast.makeText(BidirectionalAndroidUDPClientActivity.this, etat_service + "continuation", Toast.LENGTH_SHORT).show();
		} else {
			etat_service = "2";

			startService(new Intent(this, MyService.class));
			Toast.makeText(BidirectionalAndroidUDPClientActivity.this, etat_service + " demarrage", Toast.LENGTH_SHORT).show();
			}
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK," TAG");
	    wl.acquire(); //ACQUIRE_CAUSES_WAKEUP
    }
    
  
  
    //  mael add : voids for knowing if service is running
    //----------------------- THE service check TASK - end ----------------------------
    private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	//	Toast.makeText(BidirectionalAndroidUDPClientActivity.this, "is my service running ??", Toast.LENGTH_SHORT).show(); // debug
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			Toast.makeText(BidirectionalAndroidUDPClientActivity.this, "name of service " + service.service.getClassName(), Toast.LENGTH_LONG);
			 Log.i("myservice is running",service.service.getClassName());
			if ("com.lauridmeyer.tests.MyService".equals(service.service.getClassName())){
				Toast.makeText(BidirectionalAndroidUDPClientActivity.this, "YES the service is running", Toast.LENGTH_SHORT).show(); // debug
				return true;
			}
			
		}
		Toast.makeText(BidirectionalAndroidUDPClientActivity.this, "CHECK IF the service is running irhgi^rzîhgni^zrhengi^znrîhngir^nhgrngoirnbgoiznboinhir^ngîrnogr", Toast.LENGTH_LONG);
		return false;
	}

	private boolean CheckIfServiceIsRunning() {
		// If the service is running when the activity starts, we want to
		// automatically bind to it.
		if (isMyServiceRunning()) {
			// doBindService();
//			Toast.makeText(ServiceDemoActivity.this, "CHECK IF the service is running irhgi^rzîhgni^zrhengi^znrîhngir^nhgrngoirnbgoiznboinhir^ngîrnogr", Toast.LENGTH_LONG);
			return true;
		}
		Toast.makeText(BidirectionalAndroidUDPClientActivity.this, "CLULU", Toast.LENGTH_LONG);
		return false;
		
	}
	
}
   
 
    

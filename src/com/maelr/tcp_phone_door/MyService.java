package com.maelr.tcp_phone_door;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyService extends Service  {
	
	
	byte[] buf = ("Hello from Client").getBytes();
	byte[] bufreply = ("OKFROMTAB1").getBytes();
	Intent intent;
	int counter = 0;
	public Context context;
	boolean allow_request = true;
//	protected PowerManager.WakeLock mWakeLock;
//	protected PowerManager.WakeLock mPartialWakeLock;

    Socket nsocket; //Network Socket
    InputStream nis; //Network Input Stream
    OutputStream nos; //Network Output Stream
    BufferedReader inFromServer;//Buffered reader to store the incoming bytes
    
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar
	TextView textlog;//Log for outputs
	// MAel add
	Boolean connected=false;//stores the connection status
	

	@Override
	public void onCreate() {
		// Called on service created

		Log.i("myservice", "networktask tried to be launched in on create");
		Log.i("myservice", "networktask launched in on create");
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK," TAG");
	    wl.acquire(); //ACQUIRE_CAUSES_WAKEUP

	}
;
	@Override
	public void onDestroy() {
		Toast.makeText(getApplicationContext(), "service destroyed",Toast.LENGTH_LONG ).show();
		
     super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {		
		Thread t = new Thread(new Runnable() { //one thread to handle all
			
				public void run() { 
		            try { 
			            final String SERVERIP = "192.168.0.177"; // the ip of the phone "192.168.0.177"
			            final int SERVERPORT = 8888;
		            	//create a new socket instance
		                SocketAddress sockaddr = new InetSocketAddress(SERVERIP, SERVERPORT);  // the ip of the phone "192.168.0.177"
		                nsocket = new Socket();
		                nsocket.connect(sockaddr, 5000);//connect and set a 10 second connection timeout
		                if (nsocket.isConnected()) {//when connected
		                    nis = nsocket.getInputStream();//get input
		                    nos = nsocket.getOutputStream();//and output stream from the socket
		                    inFromServer = new BufferedReader(new InputStreamReader(nis));//"attach the inputstreamreader"
		                    while(true){//while connected
		                    	String msgFromServer = inFromServer.readLine();//read the lines coming from the socket
		                    	byte[] theByteArray = msgFromServer.getBytes();//store the bytes in an array
		                    	

		                    	String donnes_recues = new String(theByteArray);
				     // management of request for launching app 
				 				 if ((donnes_recues.startsWith("request") | donnes_recues.equals("request") | donnes_recues.contains("request")) && allow_request == true ) {
				 					allow_request = false;    //disable reading
				 		// TODO : throwing an intent or calling something to set the timer below to clean up the code
				 					
				 					new Timer().schedule((new TimerTask() {   // for 2 minutes
				 			            
				 			            @Override
				 			            public void run() {
				 			                // TODO Auto-generated method stub
				 			            	allow_request = true;
				 			            }
				 			        }), 120000); 	    
				 					 
				 					 
				 					PowerManager pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);
				 				    PowerManager.WakeLock wl1 = pm1.newWakeLock(PowerManager.FULL_WAKE_LOCK    //full wake up of CPU
				 				    		| PowerManager.ON_AFTER_RELEASE   // to allow a long screen on 
				 				    		| PowerManager.ACQUIRE_CAUSES_WAKEUP," TAG");  // forcing the screen on 
				 				    wl1.acquire(); 
				 				    KeyguardManager  kg = (KeyguardManager ) getSystemService(Context.KEYGUARD_SERVICE); 
				 				    KeyguardLock mKeyguardLock;
				 						                  mKeyguardLock = kg.newKeyguardLock( "My Tag" ); 
				 						                  mKeyguardLock.disableKeyguard(); 
				 //					DatagramSocket socketout = new DatagramSocket();	                  	
				 		            SendDataToNetwork( "OKFROMTAB1");
				 	//				 Toast.makeText(getApplicationContext(), "ack sent !!!!!", Toast.LENGTH_LONG).show();
				 					MediaPlayer mp = MediaPlayer.create(getBaseContext(),R.raw.ultra);
				 					mp.start();
				 					mp.setOnCompletionListener(new OnCompletionListener() {
				 						 @Override
				 						 public void onCompletion(MediaPlayer mp) {
				 							 mp.release();
				 						 }
				 					});
				 		 
				 					Intent c = new Intent( Intent.ACTION_MAIN );
				 					c.addCategory( Intent.CATEGORY_LAUNCHER );         
				 					c.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
				          
				 					ComponentName cn = new ComponentName( "com.rcreations.ipcamviewer", "com.rcreations.ipcamviewer.WebCamViewerActivity" );
				   //         ComponentName cn = new ComponentName( "com.rcreations.ipcamviewerFree", "com.rcreations.ipcamviewer.WebCamViewerActivity" );
				 					c.setComponent( cn );
				         //   wl1.release();
				 					c.putExtra( "selectView", "MATRIX_VIEW" );
				 					startActivity(c);
			             // this add to  put an automatic back to homescreen, this because ipcamviewer app has a full wake lock when called
					 			// source code for homescreen return 	http://stackoverflow.com/questions/9679677/can-not-launch-home-from-android-4-0?lq=1
					 			// source code for timer	http://androidrox.wordpress.com/2011/05/12/hello-world/

					 					new Timer().schedule((new TimerTask() {
					 			            
					 			            @Override
					 			            public void run() {
					 			                // TODO Auto-generated method stub
							 					Intent mHomeIntent = new Intent(Intent.ACTION_MAIN, null);
							 					mHomeIntent.addCategory(Intent.CATEGORY_HOME); 
							 					mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							 					startActivity(mHomeIntent);
					 			            }
					 			        }), 60000); 	                
				            wl1.release();    	          		
				 				 } 		
				 				 
				 			  // keep alive management 
		 	 				if (donnes_recues.contains("keep_alive_message")) {	
		 	 			//	    	    DatagramSocket socketout = new DatagramSocket();
					 		            SendDataToNetwork( "OK KEEP ALIVE");
								 }
				 		  }
					 }
		            }
		                
	/*	            catch (IOException e) {
				
		            	Toast.makeText(getApplicationContext(), "couille IO" + e,Toast.LENGTH_LONG ).show();

		            }
		*/            
		            catch (Exception e) {
						
		            	Toast.makeText(getApplicationContext(), "couille" + e,Toast.LENGTH_LONG ).show();

		            }
		    }; 
		}); 
		
	t.start();
		
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	
public void SendDataToNetwork(String cmd) { //You run this from the main thread.
    try {
        if (nsocket.isConnected()) {
            nos.write(cmd.getBytes());
        } else {
        	Toast.makeText(getApplicationContext(), "SendDataToNetwork: Cannot send message. Socket is closed",Toast.LENGTH_LONG ).show();

        }
    } catch (Exception e) {
 //   	outputText("SendDataToNetwork: Message send failed. Caught an exception");
    	Toast.makeText(getApplicationContext(), "plantage dans senddatanetwork" + e,Toast.LENGTH_LONG ).show();
    }
}

}



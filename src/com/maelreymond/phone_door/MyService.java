package com.maelreymond.phone_door;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
//	protected PowerManager.WakeLock mWakeLock;
//	protected PowerManager.WakeLock mPartialWakeLock;

	    
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar
	
	// MAel add
	Boolean connected=false;//stores the connection status
	
   // NetworkTask networktask;//networktask is the included class to handle the socket connection

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
//	}

	@Override
	public void onStart(Intent intent, int startid) {

	    //	    UDPpacket.run();
		
		Thread t = new Thread(new Runnable() {
			
				public void run() 
		         { 
		            try { 
		            final String SERVERIP = "192.168.0.177"; // the ip of the phone "192.168.0.177"
		            final int SERVERPORT = 8888;
		            String donnes_recues;
		         	// mael add for udp purpose 
		         	
		 			/* Retrieve the ServerName */
		 			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

		 			/* Create new UDP-Socket */
		 			DatagramSocket socketin = null;
		 			socketin = new DatagramSocket(5650);
		 			socketin.setBroadcast(false);
		 			byte[] buf = new byte[18];	    			
		 			DatagramPacket packet = new DatagramPacket(buf, buf.length);
		 			 while(true){
		 				 socketin.receive(packet);
		 				 donnes_recues = new String(packet.getData());
		 				 DatagramSocket socketout = new DatagramSocket();
		 				   // keep alive management 
 	 				     if (donnes_recues.startsWith("keep_alive_message")) {		 					    
			 		            SendDataToNetwork( "OK KEEP ALIVE",socketout,serverAddr, SERVERPORT);
						 }
 	 				     // management of request for launching app 
		 				 if (donnes_recues.startsWith("request") | donnes_recues.equals("request") | donnes_recues.contains("request") ) {
		 					PowerManager pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 				    PowerManager.WakeLock wl1 = pm1.newWakeLock(PowerManager.FULL_WAKE_LOCK    //full wake up of CPU
		 				    		| PowerManager.ON_AFTER_RELEASE   // to allow a long screen on 
		 				    		| PowerManager.ACQUIRE_CAUSES_WAKEUP," TAG");  // forcing the screen on 
		 				    wl1.acquire(); 
		 				    KeyguardManager  kg = (KeyguardManager ) getSystemService(Context.KEYGUARD_SERVICE); 
		 				    KeyguardLock mKeyguardLock;
		 						                  mKeyguardLock = kg.newKeyguardLock( "My Tag" ); 
		 						                  mKeyguardLock.disableKeyguard(); 
     
		 		            SendDataToNetwork( "OKFROMTAB1",socketout,serverAddr, SERVERPORT);
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
		 		}
			} catch (Exception e) {
				
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
	
	 public void SendDataToNetwork(String cmd,DatagramSocket chaussette,InetAddress Addrserver,int SERVERPORT) { //You run this from the main thread. chaussette = socket in french
		 /*       	final String SERVERIP = "192.168.0.177"; // the ip of the phone "192.168.0.177"  // a enlever pour le debug de l'adresse locale    
		   	 int SERVERPORT2 = 8888;  */   // a enlever pour le debug de l'adresse locale      

		       try {
		 			/* Create new UDP-Socket */
		        		/* Create UDP-packet with 
		 			 * data & destination(url+port) */
		 			byte[] c = cmd.getBytes();
		 			DatagramPacket packetcmd = new DatagramPacket(c, c.length,	Addrserver, SERVERPORT);
		 			chaussette.send(packetcmd);
		       } catch (Exception e) {
		  			Log.e("send to net excepetion ", "plantage", e);
		       }
		   }
	}
	




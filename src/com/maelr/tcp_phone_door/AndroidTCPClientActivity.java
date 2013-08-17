/*
 * AndroidTCPClientActivity -> arduino client
 * 
 * 5.01.2013
 * by Mael reymond
 * see readme and maeldiy.wordpress.com for more information and inspiration
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * 
 *             DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE

 Copyright (C) 2012 Mael REYMOND <maeldiy@gmail.com>

 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document, and changing it is allowed as long
 as the name is changed.

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

  0. You just DO WHAT THE FUCK YOU WANT TO.

 */
package com.maelr.tcp_phone_door;

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
import com.maelr.tcp_phone_door.R;

public class AndroidTCPClientActivity extends Activity {
	
	TextView textlog;//Log for outputs
	
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar
	ProgressBar progressbar;//progressbar showing the poti rotation
	
	Boolean connected=false;//stores the connection status
	
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
	
			Toast.makeText(AndroidTCPClientActivity.this, etat_service + "continuation", Toast.LENGTH_SHORT).show();
		} else {
			etat_service = "2";

			startService(new Intent(this, MyService.class));
			Toast.makeText(AndroidTCPClientActivity.this, etat_service + " demarrage", Toast.LENGTH_SHORT).show();
			}
	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK," TAG");
	    wl.acquire(); //ACQUIRE_CAUSES_WAKEUP
    }
    
  
  
    //  mael add : voids for knowing if service is running
    //----------------------- THE service check TASK - end ----------------------------
    private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	//	Toast.makeText(AndroidTCPClientActivity.this, "is my service running ??", Toast.LENGTH_SHORT).show(); // debug
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			Toast.makeText(AndroidTCPClientActivity.this, "name of service " + service.service.getClassName(), Toast.LENGTH_LONG);
			 Log.i("myservice is running",service.service.getClassName());
			if ("com.maelr.tcp_phone_door.MyService".equals(service.service.getClassName())){
				Toast.makeText(AndroidTCPClientActivity.this, "YES the service is running", Toast.LENGTH_SHORT).show(); // debug
				return true;
			}
			
		}
		Toast.makeText(AndroidTCPClientActivity.this, "CHECK IF the service is running irhgi^rzîhgni^zrhengi^znrîhngir^nhgrngoirnbgoiznboinhir^ngîrnogr", Toast.LENGTH_LONG).show();
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
		Toast.makeText(AndroidTCPClientActivity.this, "CLULU", Toast.LENGTH_LONG).show();
		return false;
		
	}
	
}
   
 
    

/*
 * 
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



 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document, and changing it is allowed as long
 as the name is changed.

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

  0. You just DO WHAT THE FUCK YOU WANT TO.

Phonedoorv1_0.pde:
This sketch send udp frame to defined IP adress when a discrete input is activated

 created 10 Mar 2013 by mael reymond
 */

#include <Bounce.h>
#include <SPI.h>         // needed for Arduino versions later than 0018
#include <Ethernet.h>
#include <EthernetUdp.h>         // UDP library from: bjoern@cs.stanford.edu 12/30/2008
#include <Timer.h>

const int buttonPin = 8;     // the number of the pushbutton pin
const int debug_enable = 1; //debug = 1, not debug =0
// Variables will change:
int ledState = 1;  //reading = 1;        // the current state of the output pin
int buttonState=0;             // the current reading from the input pin
int lastButtonState = LOW;   // the previous reading from the input pin
int reading = 1; // reading allowed or not
Timer t,MESSAGE_interval;                     // the timer which perform the keepalive every keep_alive_intervall milliseconds

// Instantiate a Bounce object with a 200 millisecond debounce time
Bounce bouncer = Bounce( buttonPin,500 ); 

// the following variables are long's because the time, measured in miliseconds,
// will quickly become a bigger number than can be stored in an int.
long lastDebounceTime = 0;  // the last time the output pin was toggled
long debounceDelay = 350;    // the debounce time; increase if the output flickers
unsigned int intervalle, is_MESSAGE_sent =0;
long keep_alive_intervall = 3000, MESSAGE_sending_interval_time = 3000,last_MESSAGE_Time;

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {  
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192, 168, 0, 177);

IPAddress remoteIPadress0(192, 168, 0, 10);
IPAddress remoteIPadress1(192, 168, 0, 11);  
IPAddress remoteIPadress2(192, 168, 0, 12);  
IPAddress remoteIPadress3(192, 168, 0, 13);  
IPAddress remoteIPadress4(192, 168, 0, 14);  
IPAddress remoteIPadress5(192, 168, 0, 15);  
IPAddress remoteIPadress6(192, 168, 0, 16);  
IPAddress remoteIPadress7(192, 168, 0, 17);  
IPAddress remoteIPadress8(192, 168, 0, 18);  
IPAddress remoteIPadress9(192, 168, 0, 19);  
IPAddress remoteIPadress10(192, 168, 0, 20); 
IPAddress remoteIPadress40(192, 168, 0, 50);  

unsigned int localPort = 8888;      // local port to listen on
unsigned int destinationPort = 5650;      // port distant a connecter si necessaire
unsigned int timeout = 20; 
unsigned int ack = 0;  //acknowledge
unsigned int nb_envoi;

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet, 
char  MESSAGE[] = "MESSAGE";  
char keep_alive_message[] = "keep_alive_message";
int tick; 
EthernetUDP Udp; // An EthernetUDP instance to let us send and receive packets over UDP
long debug_value;
     
void setup() {
  // start the Ethernet and UDP:
  pinMode(buttonPin, INPUT);
  Ethernet.begin(mac,ip);
  Udp.begin(localPort);
  Serial.begin(9600);
  int tick = t.every(25000, keepaliv);

}

void loop() {
  bouncer.update ( );
  t.update(); // send keepalive every keep_alive_intervall milliseconds
  int reading = digitalRead(buttonPin);
  // check to see if you just pressed the button 
  // (i.e. the input went from LOW to HIGH), 
 
   int j = 1;  
   if (debug_enable == 1) {
      Serial.print("    bouncer ");
       Serial.println(bouncer.read());
       
       Serial.print("            bouncer rising edge ");
       Serial.println(bouncer.risingEdge());
  }
 //LANCER LA SEQUENCE D'ENVOIE RECEPTION
//   if (buttonState == HIGH && is_MESSAGE_sent == 0) {
     if (bouncer.risingEdge() == true) {  
/*     is_MESSAGE_sent = 1;
     lastDebounceTime =0;
     buttonState = LOW;
  */  if (debug_enable == 1) {
       Serial.print("sending MESSAGE ");
       for (int l =0; l < 2000; l++)  {         } //little wait before printing out data, visual effect
       Serial.print("ack avant while ack = :");  
       Serial.println(ack);  
     } // end debug
     udpsend(remoteIPadress0,destinationPort,MESSAGE);  // broadcast not always supported by android in sleep mode
     udpsend(remoteIPadress1,destinationPort,MESSAGE);    // the whole LAN is not scanned because keep alive might interfere 
     udpsend(remoteIPadress2,destinationPort,MESSAGE);
     udpsend(remoteIPadress3,destinationPort,MESSAGE);
     udpsend(remoteIPadress4,destinationPort,MESSAGE);
     udpsend(remoteIPadress5,destinationPort,MESSAGE);
     udpsend(remoteIPadress6,destinationPort,MESSAGE);    
     udpsend(remoteIPadress7,destinationPort,MESSAGE);
     udpsend(remoteIPadress8,destinationPort,MESSAGE);
     udpsend(remoteIPadress9,destinationPort,MESSAGE);
     udpsend(remoteIPadress10,destinationPort,MESSAGE);    
     udpsend(remoteIPadress40,destinationPort,MESSAGE);    // add my phone 
    }

     int packetSize = Udp.parsePacket();
     if(packetSize)
        {
          // debug : identifiant expediteur
          Serial.print("Received packet of size ");     Serial.println(packetSize);    Serial.print("From ");
          IPAddress remote = Udp.remoteIP(); 
          for (int i =0; i < 4; i++)
          {
            Serial.print(remote[i], DEC);
            if (i < 3)
            {
              Serial.print(".");
            }
          }   // end debug identifier A METTRE EN COMMENTAIRE POUR FINAL RELEASE
          Udp.read(packetBuffer,UDP_TX_PACKET_MAX_SIZE);  // read the packet into packetBufffer
          Serial.println("Contents:");  // debug
          Serial.print(packetBuffer); // DEBUG
          Serial.println("+"); // DEBUG
          if (debug_enable == 1) { 
             Serial.print("ack :");  // debug
             Serial.println(ack);  // debug
             int sLen = packetSize +1; // to be sure of what is the next caracter ?
             for(int s=0; s<sLen; s++)
              {
               Serial.print("theCommand[");
               Serial.print(s);
               Serial.print("] is {");
               Serial.print(packetBuffer[s]);
               Serial.print("} which has an ascii value of ");
               Serial.println(packetBuffer[s], DEC);
             }
             for (int k =0; k < 5000; k++) //this produce a little pause to provide time to acknoledge on serial debug screen
            {          }
           }
     }
  }

void keepaliv(){
    if (debug_enable == 1) { 
      Serial.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!   SENDING KEEP ALIVE !!!!!!!!!!!!!!!!!!!!!!!!!!");  // debug
    }
   for (int z =0; z < 5; z++)  {          
     udpsend(remoteIPadress0,destinationPort,keep_alive_message);  // broadcast not always supported by android in sleep mode
     udpsend(remoteIPadress1,destinationPort,keep_alive_message);    // the whole LAN is not scanned because keep alive might interfere 
     udpsend(remoteIPadress2,destinationPort,keep_alive_message);
     udpsend(remoteIPadress3,destinationPort,keep_alive_message);
     udpsend(remoteIPadress4,destinationPort,keep_alive_message);
     udpsend(remoteIPadress5,destinationPort,keep_alive_message);
     udpsend(remoteIPadress6,destinationPort,keep_alive_message);    
     udpsend(remoteIPadress7,destinationPort,keep_alive_message);
     udpsend(remoteIPadress8,destinationPort,keep_alive_message);
     udpsend(remoteIPadress9,destinationPort,keep_alive_message);
     udpsend(remoteIPadress10,destinationPort,keep_alive_message);    
     udpsend(remoteIPadress40,destinationPort,keep_alive_message);
     }  
   }
   
void udpsend(IPAddress ipdestinataire, unsigned int port_destinataire, char message[]) {
  
    Udp.beginPacket(ipdestinataire, port_destinataire);
    Udp.write(message);
    Udp.endPacket();
}
void doAfter()
{
  Serial.println("    RESTART reading");
  reading = 1;
}




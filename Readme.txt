this project implement a video door gate using an android device (android 2.1 minimum) and an Ethernet video camera.
See video door gate architecture.jpg and http://maeldiy.wordpress.com/ for more details

The repositery contains Arduino_Phone_door directory for Arduino part. 

By receiving an Ethernet message from the arduino device, the android application do the following :

1 Acknowledge the message by answer to the Ethernet device
2 Wake up the screen
3 Unlock the android device (only if no code to unlock)
4 Play a sound
5 Launch the application “IP cam viewer”, to view a predefined camera
6 After 1 minute of inaction, the application goes to the main menu to allow the device to put the screen in sleep mode
7 What the application need ? :

_ “IP cam viewer lite” app must be installed : https://play.google.com/store/apps/details?id=com.rcreations.ipcamviewer&hl=fr

_ “WiFi keep alive” app must be installed, running with all workarounds. https://play.google.com/store/apps/details?id=com.shantz.wifikeepalive&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5zaGFudHoud2lmaWtlZXBhbGl2ZSJd

Beware ! :  The application is intended to run on a device which is permanently connected to a power supply. As the Wifi never stop, it will drain the battery.

What the application should do more :

1 Provide a push button to send action request on the local network (arduino for example)
2 Allow the possibility to choose a song to play
3 Allow the possibility to define the volume of the song
4 Define the time out value to return to main screen
5 Run in DHCP mode
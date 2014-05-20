package com.augmate;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class glassvideo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen is always on for the video
		//String path="https://apprtc.appspot.com/?r=21794903"; // P2P video WebRTC
		
		String path = "http://";
	    Uri uri=Uri.parse(path);

	    VideoView video=(VideoView)findViewById(R.id.myvideoview);
	    MediaController mc = new MediaController(this);
	    video.setMediaController(mc);
	    video.setVideoPath("/sdcard/sap.3gp"); //local video
	    video.requestFocus();
	    //video.setVideoURI(uri); web video
	    
	    video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	    	public void onCompletion(MediaPlayer vmp){
	    		setResult(RESULT_OK);
	    		finish();
	    	}	    	
	    });
	    
	    video.start();
	    
	    //video.stopPlayback(); // stop the video -- voice command	    	
		//Intent intent = new Intent(this, AugmateWarehouseUserLogin.class);		
		//startActivity(intent);	   	    
	}
		

}

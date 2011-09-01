package org.miloss;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

public class TalkActivity extends Activity {
  
    /**
     * Layout items
     */
    Button mTalkButton;
    Button mGuysButton;
    
    /**
     * Media player and recorder
     */
    AudioRecord mRecorder;
    ByteBuffer  mAudioBuffer;
    
    public static final String TAG = "TalkActivity";
    
    /**
     * OnTouch listener handles starting and stopping the buzzer sound
     */
    private OnTouchListener mTalkTouch = new OnTouchListener() {

      public boolean onTouch(View yourButton, MotionEvent motion) {
      
        switch (motion.getAction()) {
        case MotionEvent.ACTION_DOWN:
          Log.d(TAG, "Talk Down");
          try {
            startRecording();
          } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          break;
        case MotionEvent.ACTION_UP:
          Log.d(TAG, "Talk Up");
          try {
            stopRecording();
          } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          break;
        }
        
        return true;
      }
    };
    
    /**
     * Onclick for the guys button
     */
    private OnClickListener mGuysClick = new OnClickListener() {
      public void onClick(View v) {
        Log.d(TAG, "Guys Click");
      }
    };
    
    private void startRecording() throws IllegalStateException, IOException {
     MediaRecorder recorder = new MediaRecorder();
     recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
     recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
     recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
     FileOutputStream file = new FileOutputStream("/sdcard/output.sound");
     recorder.setOutputFile(file.getFD());
     recorder.prepare();
     recorder.start(); 
  }

  private void stopRecording() throws IllegalArgumentException, IllegalStateException, IOException {
      mRecorder.stop();
      mRecorder.release();
      mRecorder = null;
      MediaPlayer mediaPlayer = new MediaPlayer();
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.setDataSource("/sdcard/output.sound");
      mediaPlayer.prepare(); // might take long! (for buffering, etc)
      mediaPlayer.start();
  }
    
    /** Get the refs to the ui elements on the page */
    public void setupUIReferences() {
      mTalkButton = (Button)this.findViewById(R.id.talkButton);
      mGuysButton = (Button)this.findViewById(R.id.guysButton);
      }
    
    /** Setup my listeners */
    public void setupEventListeners()
    {
      mTalkButton.setOnTouchListener(mTalkTouch);
      mGuysButton.setOnClickListener(mGuysClick);
    }
  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        setupUIReferences();
        setupEventListeners();
    }
}
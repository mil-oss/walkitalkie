/* Copyright 2011 Patrick Reynolds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.miloss;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
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
    MediaRecorder mRecorder;
    MediaPlayer mPlayer;
    
    /**
     * Socket stuff
     */
    String mFileName;
    FileOutputStream mFileOut;
    Socket mSocket;
    ParcelFileDescriptor mPFD;
    FileDescriptor mFD;
    
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
	  // Use a filedescriptor instead of direct file
	  // This will enable easy transition to sockets later
	  mFileOut = new FileOutputStream(mFileName);
	  mFD = mFileOut.getFD();
	  
      mRecorder = new MediaRecorder();
      mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      mRecorder.setOutputFile(mFD);
      mRecorder.prepare();
      mRecorder.start(); 
  }

  private void stopRecording() throws IllegalArgumentException, IllegalStateException, IOException {
      mRecorder.stop();
      mRecorder.release();
      mRecorder = null;
      mFileOut.close();
      mFileOut = null;
      
	  // Use a filedescriptor instead of direct file
	  // This will enable easy transition to sockets later
	  FileInputStream fileIn = new FileInputStream(mFileName);
	  mFD = fileIn.getFD();
	  
      mPlayer = new MediaPlayer();
      mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mPlayer.setDataSource(mFD);
      mPlayer.prepare(); // might take long! (for buffering, etc)
      mPlayer.start();
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
        
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        
        setContentView(R.layout.main);
        setupUIReferences();
        setupEventListeners();
    }
}
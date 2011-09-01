package org.miloss;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class TalkActivity extends Activity {
  
    /**
     * Layout items
     */
    Button mTalkButton;
    Button mGuysButton;
    
    public static final String TAG = "TalkActivity";
    
    /**
     * OnTouch listener handles starting and stopping the buzzer sound
     */
    private OnTouchListener mTalkTouch = new OnTouchListener() {

      public boolean onTouch(View yourButton, MotionEvent motion) {
      
        switch (motion.getAction()) {
        case MotionEvent.ACTION_DOWN:
          Log.d(TAG, "Talk Down");
          break;
        case MotionEvent.ACTION_UP:
          Log.d(TAG, "Talk Up");
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
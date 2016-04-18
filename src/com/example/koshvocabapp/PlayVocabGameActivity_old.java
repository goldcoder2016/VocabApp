package com.example.koshvocabapp;

import android.media.AudioManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.view.Menu;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.res.*;
import java.io.*;


public class PlayVocabGameActivity_old extends Activity implements TextToSpeech.OnInitListener {

    /** Called when the activity is first created. */
	 
    private TextToSpeech tts;
    private Button btnSpeak;
    private Button btnRepeat;
    private Button btnRestart;
    private Button btnChkSplng;
    private Button btnAdd;
    private EditText txtSplng;
    private TextView txtResult;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String currentLine = null;
    private String[] wordTokens = null;
    private int wordIndex;
	private int tempIndex = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_vocab_game);
 
        tts = new TextToSpeech(this, this);
 
        btnSpeak = (Button) findViewById(R.id.btnSayword);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        btnChkSplng = (Button) findViewById(R.id.btnChkSplng);
        txtSplng = (EditText) findViewById(R.id.txtSplng);
        txtSplng.setHint("Enter spelling here");
        txtSplng.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtResult = (TextView) findViewById(R.id.txtResult);

        // temporarily disable the button for Kosha to play.
        btnRestart.setEnabled(false);        
        /*
         * load the file so that new word can be read each time the button is clicked
         */
		try{
			reader = new BufferedReader(new InputStreamReader(getAssets().open("dict.txt")));
		}catch (Exception e){
				System.out.println("Error opening the file - " + e.toString());
		}

        // speak button on click event
        btnSpeak.setOnClickListener(new View.OnClickListener() 
        {
			@Override
            public void onClick(View arg0) 
			{
				txtSplng.setText("");
				try{
					if ((currentLine = reader.readLine()) != null) {
						tempIndex++;
						wordTokens = currentLine.split("=");
						speakOut(wordTokens[0]);
						// clear previous meaning from the results label before speaking new word
	 					txtResult.setText("");
						btnSpeak.setEnabled(false);
					}else
					{
						// eof reached. Show the message
	 					txtResult.setText("No more words left Kosha !");
					}
				}catch (Exception e){
						System.out.println("Error - " + e.toString());
				}
            }
 
        });


        // repeat button on click event
        btnRepeat.setOnClickListener(new View.OnClickListener() 
        {
			@Override
            public void onClick(View arg0) 
			{
				if(wordTokens != null)
					speakOut(wordTokens[0]);
            }
 
        });

        // restart button on click event
        btnRestart.setOnClickListener(new View.OnClickListener() 
        {
			@Override
            public void onClick(View arg0) 
			{
		        // reset the file to start reading from top
				try{
					reader.close();
					reader = new BufferedReader(new InputStreamReader(getAssets().open("dict.txt")));
					tempIndex=0;
					btnSpeak.setEnabled(true);
				}catch (Exception e){
						System.out.println("Error opening the file - " + e.toString());
				}
            }
 
        });

        // check spelling button on click event
        btnChkSplng.setOnClickListener(new View.OnClickListener() 
        {
 			@Override
            public void onClick(View arg0) 
			{
 				String kidWord = txtSplng.getText().toString();
 				// clear the previously typed word
 				if(kidWord == "")
 				{
 					txtResult.setText("Enter the spelling first..");
 				}else if(wordTokens == null)
 				{
 					txtResult.setText("Get the new word first..");
 				}else if(kidWord.equalsIgnoreCase(wordTokens[0]))
 				{
 					txtResult.setText("Correct Kosha !" + "\n\n" + "Meaning:" + "\n" + wordTokens[1]);
					btnSpeak.setEnabled(true);
 				}else{
 					txtResult.setText("Wrong Kosha !");
 				}
			}
        });

//        // Add button on click event
//        btnAdd.setOnClickListener(new View.OnClickListener() 
//        {
//			@Override
//            public void onClick(View arg0) 
//			{
//		        // reset the file to start reading from top
//				try{
//					
//					reader = new BufferedReader(new InputStreamReader(getAssets().open("dict.txt")));
//					btnSpeak.setEnabled(true);
//				}catch (Exception e){
//						System.out.println("Error opening the file - " + e.toString());
//				}
//            }
// 
//        });
	}
    

	@Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
            
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
            	AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            	mAudioManager.setSpeakerphoneOn(true);
            	int loudmax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
            	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,loudmax, AudioManager.FLAG_PLAY_SOUND);
                btnSpeak.setEnabled(true);
                /*
                 * get the first word based on the index
                 * open the dictionary file and read the first word 
                 */
        		try{
        			// 1st step
        			reader = new BufferedReader(new InputStreamReader(getAssets().open("index.txt")));
        			if ((currentLine = reader.readLine()) != null) {
        				wordIndex = Integer.parseInt(currentLine);
        				reader.close();
        			}
        			// 2nd step
        			reader = new BufferedReader(new InputStreamReader(getAssets().open("dict.txt")));
					while(tempIndex < wordIndex)
					{
						if((currentLine = reader.readLine()) != null)
						{
							tempIndex++;
						}
						wordTokens = currentLine.split("=");
						speakOut(wordTokens[0]);
						// clear previous meaning from the results label before speaking new word
	 					txtResult.setText("");
						btnSpeak.setEnabled(false);
					}
        		}catch (Exception e){
        				System.out.println("Error opening the file - " + e.toString());
        		}

            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }

    @Override
    public void onDestroy() {
    	// write back to the file
//		writer = new BufferedWriter(new OutputStreamWriter(getAssets().open("index.txt")));
    	
    	// Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speakOut(String word) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }
}

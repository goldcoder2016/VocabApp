package com.example.koshvocabapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditVocabFileActivity extends Activity {
	
    private Button btnAddWord;
    private EditText txtWord;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_vocab_file);
        btnAddWord = (Button) findViewById(R.id.btnAddWord);
        txtWord = (EditText) findViewById(R.id.txtWord);
        AppScope appContext = (AppScope)getApplicationContext();

//		final View controlsView = findViewById(R.id.btnAddWord);
//		final View contentView = findViewById(R.id.btnAddWord);

		};


}

package com.example.koshvocabapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

	private Button btnFileEdit;
	private Button btnGame;
	private Intent navigate; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	
        btnFileEdit = (Button) findViewById(R.id.btnEditFile);
        btnGame = (Button) findViewById(R.id.btnGame);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void startGame(View view)
	{
		navigate = new Intent(this, PlayVocabGameActivity.class);
		startActivity(navigate);
	}

	public void editFile(View view)
	{
		navigate = new Intent(this, EditVocabFileActivity.class);
		startActivity(navigate);
	}

}

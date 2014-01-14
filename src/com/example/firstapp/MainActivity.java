package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private Game game;
	TextView guessLeft;
	TextView tv;
	TextView guessed;
	EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//savePreferences("Letters_Value",Integer.toString(4));
		//savePreferences("Wrongs_Value",Integer.toString(8));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	    
		this.game = new Game(this);
		tv = (TextView) findViewById(R.id.word);
		tv.setText(game.getString());
		
		this.guessed = (TextView) findViewById(R.id.guessed);
		this.guessed.setText(this.game.guessed.toString());
		guessLeft = (TextView) findViewById(R.id.allowed_misguesses);
		guessLeft.setText(String.valueOf(game.nW));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void guess(View v){
		EditText editText = (EditText) findViewById(R.id.input);
		String str = editText.getText().toString();
		switch(this.game.guessLetter(str)) {
			case 1:
				Toast.makeText(getApplicationContext(), "Please put an alphabetical character.", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "You Lost, the word was: "+this.game.curWord, Toast.LENGTH_LONG).show();
				newGame();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "You Win!", Toast.LENGTH_LONG).show();
				// Save score
				newGame();
				break;
			case 4:	
				Toast.makeText(getApplicationContext(), "This letter has already been guessed.", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
				newGame();
				default:
				break;
		}
		editText.setText("");
		updateFields();
	}
	
	private void updateFields() {
		this.guessLeft.setText(String.valueOf(this.game.nW));
		this.guessed.setText(this.game.guessed.toString());
		this.tv.setText(this.game.getString());
		
	}

	/** Called when the user clicks the Send button */
	/*public void sendMessage(View view) {
	    Intent intent = new Intent(this, DisplayMessageActivity.class);
	    EditText editText = (EditText) findViewById(R.id.input);
	    String message = editText.getText().toString();
	    intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new:
	            newGame();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void newGame() {
		this.game = new Game(this);
		finish();
		startActivity(getIntent());
	}

	public void openSettings() {
	    Intent intent = new Intent(this, SettingsActivity.class);
	    startActivity(intent);
	}
}

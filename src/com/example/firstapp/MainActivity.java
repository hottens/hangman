package com.example.firstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String EXTRA_GAME = "com.example.myfirstapp.MESSAGE";
	private Game game;
	TextView guessLeft;
	TextView tv;
	TextView guessed;
	EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	 
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		tv = (TextView) findViewById(R.id.word);
		this.game = new Game(this);
		if(this.game.evil){
			EvilGame evil = new EvilGame(getApplicationContext());
			this.game = evil;
		}
		
		tv.setText(game.getString());
		correctWidth(tv);
		
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
		if(this.game.nW<0 || !this.game.getString().contains("_")) return;
		int event = this.game.guessLetter(str);
		updateFields();
		switch(event) {
			case 1:
				Toast.makeText(getApplicationContext(), "Please put an alphabetical character.", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "You Lost, the word was: "+this.game.curWord, Toast.LENGTH_LONG).show();
				gameOver("You Lose!",game.curWord);
				//newGame();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "You Win!", Toast.LENGTH_LONG).show();
				// Save score
				this.game.setHighScore();
				gameOver("You Win!",game.curWord);
				//newGame();
				break;
			case 4:	
				Toast.makeText(getApplicationContext(), "This letter has already been guessed.", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "Enter a character.", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
		editText.setText("");
	}
	
	private void updateFields() {
		this.guessLeft.setText(String.valueOf(this.game.nW));
		this.guessed.setText(this.game.guessed.toString());
		this.tv.setText(this.game.getString());
		
	}


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
	        case R.id.action_highscores:
	            openHighscores();
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
	public void gameOver(String game, String word) {
	    Intent intent = new Intent(this, WinActivity.class);
	    String[] game_word = {game,word};
	    intent.putExtra(EXTRA_GAME, game_word);
	    startActivity(intent);
	}
	
	public void openHighscores() {
	    Intent intent = new Intent(this, HighScoresActivity.class);
	    startActivity(intent);
	}
	
	@SuppressLint("NewApi")
	public void correctWidth(TextView textView)
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int desiredWidth = size.x;
		
	    Paint paint = new Paint();
	    Rect bounds = new Rect();

	    paint.setTypeface(textView.getTypeface());
	    float textSize = textView.getTextSize();
	    paint.setTextSize(textSize);
	    String text = textView.getText().toString();
	    paint.getTextBounds(text, 0, text.length(), bounds);

	    //Log.v("pre while widths","bounds: "+String.valueOf(bounds.width())+ " desiredWidth: " + String.valueOf(desiredWidth));
	    while (2*bounds.width() < desiredWidth-100)
	    {
	        textSize++;
	        paint.setTextSize(textSize);
	        paint.getTextBounds(text, 0, text.length(), bounds);
	    }

	    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	}
}

package com.example.firstapp;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.preference.PreferenceManager;

public class SettingsActivity extends Activity {
	SeekBar seekbarL;
	SeekBar seekbarW;
	TextView letters;
	TextView wrongs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
		
		letters = (TextView) findViewById(R.id.nL);
		wrongs = (TextView) findViewById(R.id.nW);
		seekbarW = (SeekBar) findViewById(R.id.numberOfWrongLetters);
		seekbarL = (SeekBar) findViewById(R.id.numberOfLetters);
		seekbarL.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
			{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				// TODO Auto-generated method stub
			    letters.setText(Integer.toString(progress));
			}
	
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
		seekbarW.setOnSeekBarChangeListener( new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				// TODO Auto-generated method stub
			    wrongs.setText(Integer.toString(progress));
			}
	
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
		loadSavedPreferences();
		
	}
	
	private void savePreferences(String key, String value) {
		        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		        Editor editor = sharedPreferences.edit();
		        editor.putString(key, value);
		        editor.commit();
	}
	
	public void saveSettings(View v){
		savePreferences("Letters_Value",Integer.toString(seekbarL.getProgress()));
		savePreferences("Wrongs_Value",Integer.toString(seekbarW.getProgress()));
		finish();
	}

	
	private void loadSavedPreferences() {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			int wrongsValue = Integer.parseInt(sharedPreferences.getString("Wrongs_Value", Integer.toString(R.string.default_settings_value)));
		    int lettersValue = Integer.parseInt(sharedPreferences.getString("Letters_Value", Integer.toString(R.string.default_settings_value)));
		    int maxLetters = Integer.parseInt(sharedPreferences.getString("Max_Letters", Integer.toString(15)));
		    seekbarL.setProgress(lettersValue);
		    seekbarW.setProgress(wrongsValue);
		    seekbarL.setMax(maxLetters);
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

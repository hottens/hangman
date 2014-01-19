package com.example.firstapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Game {
	WordsDataSource datasource;
	Context c;
	int nW,nL,W;
	ArrayList<Character> guessed;
	String str;
	Random randomGenerator;
	String curWord;
	boolean evil;
	
	public Game(Context context){
		this.datasource = new WordsDataSource(context);
		datasource.open();
		this.randomGenerator = new Random();
		this.curWord="";
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		this.c = context;
		this.str = "";
		this.guessed = new ArrayList<Character>(); 
		this.nW = Integer.parseInt(sharedPref.getString("Wrongs_Value", Integer.toString(8)));
		this.nL = Integer.parseInt(sharedPref.getString("Letters_Value", Integer.toString(4)));
		this.evil = Boolean.parseBoolean(sharedPref.getString("Evil_Toggle", Boolean.TRUE.toString()));
		Log.v("evil game",String.valueOf(sharedPref.getString("Evil_Toggle", Boolean.TRUE.toString())));
		this.W = this.nW;
		for (int i = 0; i < this.nL; i++){
			this.str +="_ ";
		}
		if (this.str.length() > 0) {
		    this.str = this.str.substring(0, this.str.length()-1);
		}
		if (!pickWordFromDictionary()){
			this.str = "Could not find a word";
		}
		savePreferences("Max_Letters",String.valueOf(datasource.getMaxLength()));
	}
	
	private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.c);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
	}
	
	private boolean pickWordFromDictionary() {
		/*// TODO Auto-generated method stub
		//String[] dic = this.c.getResources().getStringArray(R.array.words_small);
		String s = "A";
		for (int i = 0; i < 10; i++){
			s+= (char) (s.charAt(i)+1);
			datasource.createWord(s);
		}*/
		//pick random from possibleWords
		List<String> values = datasource.getWordsWithLength(this.nL);
		if(values.size()==0) return false;
		int rand = randomGenerator.nextInt(values.size());
		this.curWord = values.get(rand);
		Log.v("Game","Current word: "+this.curWord);
		return true;
	}
	String getString(){
		return this.str;
	}
	
	public int guessLetter(String str){
		if(str.length()==1){
			char ch = str.charAt(0);
			ch = Character.toUpperCase(ch);
			if (!Character.isLetter(ch)){//No alphabetical input
				return 1;
			} else if(!this.guessed.contains(ch)){//Not yet guessed
				if (!this.addGuessed(ch)){//Lose
					return 2;
				} else {
					if(this.validate()){//Win
						return 3;
					}
				}
			} else {//Already guessed
				return 4;	
			}
		} else {//Something went wrong
			return 5;
		}
		return 0;
	}
	
	public boolean addGuessed(char ch) {
		this.guessed.add(ch);
		if (!this.curWord.contains(String.valueOf(ch))){
			this.nW--;
		} else {
			setString();
		}
		if (this.nW < 0){
			return false;
		}
		//Log.v("Game","number of misguesses left: "+this.nW);
		return true;
	}
	public void setString() {
		for (int i = 0; i < this.curWord.length();i++){
			if(this.guessed.contains(this.curWord.charAt(i))){
				char[] strChars = this.str.toCharArray();
				strChars[2*i]=this.curWord.charAt(i);
				this.str = String.valueOf(strChars);
			}
		}
		
	}
	public boolean validate() {
		for (int i = 0; i < this.curWord.length();i++){
			if(!this.guessed.contains(this.curWord.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public void setHighScore(){
		Log.v("Score",String.valueOf(this.W-this.nW));
		this.datasource.addHighScore(this.curWord, this.W-this.nW,this.evil);
	}
	
}

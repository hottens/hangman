package com.example.firstapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class EvilGame extends Game{
	Cursor cursor;

	public EvilGame(Context context) {
		super(context);
		
		// TODO Auto-generated constructor stub
	}
	
	/*private ArrayList<Character> getCursor() {
		Cursor cursor = datasource.getCursorForWordsWithLength(this.nL);
		if(cursor.getCount()==0) return null;
		
		return cursor;
	}*/
	
	public boolean addGuessed(char ch) {
		this.guessed.add(ch);
		if (!addCharacter(ch)){
			this.nW--;
		} else {
			setString();
		}
		curWord = possibleWords.get(0);
		if (this.nW < 0){
			return false;
		}
		//Log.v("Game","number of misguesses left: "+this.nW);
		return true;
	}
	
	public boolean addCharacter(char ch) {
		// TODO Auto-generated method stub
		if(majorityWordsHave(ch)) return true;
		return false;
	}
	
	public boolean majorityWordsHave(char ch) {
		//Iterator<String> it = possibleWords.iterator();
		//Log.v("PossWords",String.valueOf(possibleWords));
		int size = possibleWords.size();
		List<String> copy = new ArrayList<String>();
		for(String word : possibleWords){
			copy.add(word);
		}
		//Store indeces of char with word
		Map<String, String> m = new HashMap<String,String>();
		for(int i=0;i<size;i++){
			String word = copy.get(i);
			String indeces =indecesOfChInWord(ch,word);
			if(!indeces.equals("")){//if(word.contains(String.valueOf(ch))){
				//Log.v("word with ch",word);
				//String indeces = indecesOfChInWord(ch,word);
				m.put(word, indeces);
				//Log.v("Word-indeces",word+"\t" + indeces);
				possibleWords.remove(word);
			}
		}
		Log.v("size",String.valueOf(m.size()));
		copy.clear();
		int nWordsWithCh = m.size();
		Log.v("return","w/out ch " +String.valueOf(size-nWordsWithCh)+ " : w/ ch" + String.valueOf(nWordsWithCh));
		if(size-nWordsWithCh>nWordsWithCh)
			return false;
					
		Log.v("nCh>",String.valueOf(nWordsWithCh));
		Map<String, Integer> maxOcc = new TreeMap<String, Integer>();
		for(Map.Entry<String, String> entry : m.entrySet()){
			String indeces = entry.getValue();
			int count = 0;
			if (maxOcc.get(indeces)!=null)
				count = maxOcc.get(indeces);
			
			maxOcc.put(indeces, count+1);
		}
		
		String indeces = "";
		int max = Collections.max(maxOcc.values());
		Log.v("max indeces/",String.valueOf(max));
		if(max<=size-nWordsWithCh) return false;
		for(Map.Entry<String, Integer> entry : maxOcc.entrySet()){
			if(entry.getValue()==max) indeces = entry.getKey(); 
		}
		Log.v("max indeces",indeces);
		this.possibleWords.clear();
		for(Map.Entry<String, String> entry : m.entrySet()){
			if(entry.getValue().equals(indeces)){
				Log.v("Word w/ indeces",entry.getKey());
				this.possibleWords.add(entry.getKey());
			}
		}
		this.curWord=possibleWords.get(0);
		Log.v("Adding ch",String.valueOf(ch));
		return true;		
	}

	private String indecesOfChInWord(char ch, String word) {
		// TODO Auto-generated method stub
		String str = "";
		for(int i=0; i<word.length();i++){
			if(word.charAt(i)==ch){
				str+="-"+i;
			}
		}
		return str.replaceFirst("-", "");
	}

	public boolean validate() {
		if (this.str.contains("_")) return false;
		return true;
	}
}

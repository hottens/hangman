package com.example.firstapp;


public class Score {
	  private long id;
	  private String word;
	  private int score;
	  private String date;
	  private boolean evil;
	  
	  public boolean getEvil(){
		  return evil;
	  }
	  
	  public void setEvil(boolean evil){
		  this.evil = evil;
	  }

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getWord() {
	    return word;
	  }

	  public void setWord(String word) {
	    this.word = word;
	  }
	  
	  public void setScore(int score) {
		    this.score = score;
		  }
	  
	  public int getScore() {
		    return score;
	  }

	  public String getDate() {
		    return this.date.toString();
	  }
	  
	  public void setDate(String date) {
		    this.date = date;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return word + " | " + score + " | " + date;
	  }
	} 

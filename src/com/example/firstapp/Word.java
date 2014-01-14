package com.example.firstapp;

public class Word {
	  private long id;
	  private String word;
	  private int length;

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
	  
	  public int getLength() {
		    return length;
	  }

	  public void setLength(int length) {
		    this.length = length;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return word + ": " + length;
	  }
	} 

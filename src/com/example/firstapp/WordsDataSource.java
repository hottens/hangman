package com.example.firstapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class WordsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_WORD, MySQLiteHelper.COLUMN_LENGTH };
  private String[] allScoreColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_WORD, MySQLiteHelper.COLUMN_SCORE,
	      MySQLiteHelper.COLUMN_DATE,MySQLiteHelper.COLUMN_EVIL};

  public WordsDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
    //dbHelper.onUpgrade(db, oldVersion, newVersion)
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Word createWord(String word) {
    ContentValues values = new ContentValues();
       
    values.put(MySQLiteHelper.COLUMN_WORD, word);
    values.put(MySQLiteHelper.COLUMN_LENGTH, word.length());
    
    Log.v("Values",values.toString());
    Log.v("Database",database.toString());
    long insertId = database.insert(MySQLiteHelper.TABLE_DICT, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_DICT,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Word newWord = cursorToWord(cursor);
    cursor.close();
    return newWord;
  }
  
  public void addHighScore(String word,int score,boolean evil){
	  Date date = (new java.sql.Date(System.currentTimeMillis()));
	  ContentValues values = new ContentValues(); 
	  values.put(MySQLiteHelper.COLUMN_WORD, word);
	  values.put(MySQLiteHelper.COLUMN_SCORE, score);
	  values.put(MySQLiteHelper.COLUMN_DATE, date.toString());
	  values.put(MySQLiteHelper.COLUMN_EVIL, Boolean.toString(evil));
	  database.insert(MySQLiteHelper.TABLE_HS , null, values); 
  }

  public void deleteWord(Word word) {
    long id = word.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_DICT, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Word> getAllWords() {
    List<Word> words = new ArrayList<Word>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_DICT,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Word word = cursorToWord(cursor);
      words.add(word);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return words;
  }
  
  public List<Score> getAllScores() {
	    List<Score> scores = new ArrayList<Score>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_HS,
	        allScoreColumns, null, null, null, null, "score");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Score score = cursorToScore(cursor);
	      scores.add(score);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return scores;
	  }
  
  public int getMaxLength() {
	    final SQLiteStatement stmt = database.compileStatement("SELECT MAX(length) FROM " +MySQLiteHelper.TABLE_DICT);

	    return (int) stmt.simpleQueryForLong();
  }
  
  public List<String> getWordsWithLength(int length) {
	    List<String> words = new ArrayList<String>();

	    Cursor cursor = database.query(
		    MySQLiteHelper.TABLE_DICT /* table */,
		    new String[] { "*" } /* columns */,
		    "length= ?" /* where or selection */,
		    new String[] { String.valueOf(length) }/* selectionArgs i.e. value to replace ? */,
		    null /* groupBy */,
		    null /* having */,
		    null /* orderBy */
	    );
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Word word = cursorToWord(cursor);
	      words.add(word.getWord());
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return words;
  }

  private Word cursorToWord(Cursor cursor) {
    Word word = new Word();
    word.setId(cursor.getLong(0));
    word.setWord(cursor.getString(1));
    word.setLength(cursor.getInt(2));
    return word;
  }
  
  private Score cursorToScore(Cursor cursor) {
	    Score score = new Score();
	    score.setId(cursor.getLong(0));
	    score.setWord(cursor.getString(1));
	    score.setScore(cursor.getInt(2));	    
	    score.setDate(cursor.getString(3));
	    score.setEvil(Boolean.parseBoolean(cursor.getString(4)));
	    return score;
	  }
} 
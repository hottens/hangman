package com.example.firstapp;

import java.util.ArrayList;
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
    long insertId = database.insert(MySQLiteHelper.TABLE_WORDS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Word newWord = cursorToWord(cursor);
    cursor.close();
    return newWord;
  }

  public void deleteWord(Word word) {
    long id = word.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_WORDS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Word> getAllWords() {
    List<Word> words = new ArrayList<Word>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS,
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
  
  public int getMaxLength() {
	    //Cursor cursor = database.query(MySQLiteHelper.TABLE_WORDS, new String [] {"MAX(length)"}, null, null, null, null, null);
	    /*int length = 0;
	    String qry = "SELECT MAX(length) FROM " + MySQLiteHelper.TABLE_WORDS;
	    Cursor cursor = database.rawQuery(qry,null);
	    Log.v("WordsDataSource","getMax " + cursor.getColumnName(0) );
	    if (cursor.moveToFirst()) {
	    	length = cursor.getInt(0);
	    	Log.v("WordsDataSource","getMax " + length );
	    }
	    cursor.close();
	    
	    return length;*/
	    final SQLiteStatement stmt = database.compileStatement("SELECT MAX(length) FROM " +MySQLiteHelper.TABLE_WORDS);

	    return (int) stmt.simpleQueryForLong();
  }
  
  public List<Word> getWordsWithLength(int length) {
	    List<Word> words = new ArrayList<Word>();

	    Cursor cursor = database.query(
		    MySQLiteHelper.TABLE_WORDS /* table */,
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
	      words.add(word);
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
    /*Log.v("Version",String.valueOf(database.getVersion()));
    for(int i=0;i<cursor.getColumnNames().length;i++){
    	Log.v("Cursor to Word",cursor.getColumnNames()[i]);
    }*/
    
    word.setLength(cursor.getInt(2));
    return word;
  }
} 
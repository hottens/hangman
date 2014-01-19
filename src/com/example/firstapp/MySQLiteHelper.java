package com.example.firstapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	Context c;

  public static final String TABLE_DICT = "dictionary";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_WORD = "word";
  public static final String COLUMN_LENGTH = "length";

  private static final String DATABASE_NAME = "hangman.db";
  private static final int DATABASE_VERSION = 15;

  // Database creation sql statement
  private static final String DICT_CREATE = "create table "
      + TABLE_DICT 
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_WORD + " TEXT not null, "
      + COLUMN_LENGTH + " INT NOT NULL "
      + ");";
  
  public static final String TABLE_HS = "highscores";
  public static final String COLUMN_SCORE = "score";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_EVIL = "evil";
  
  private static final String HS_CREATE = "create table "
	      + TABLE_HS 
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_WORD + " TEXT not null, "
	      + COLUMN_SCORE+ " INT NOT NULL, "
	      + COLUMN_DATE+ " DATE NOT NULL, "
	      + COLUMN_EVIL+ " TEXT NOT NULL"
	      + ");";

  public MySQLiteHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  this.c = context;
	  Log.v("MySQLiteHelper","init");
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  Log.v("onCreave SQLite helper","done");
	  //Log.v("MySQLiteHelper","oncreate");
    database.execSQL(DICT_CREATE);
    try {
		parseXMLtoDB(database);
	} catch (XmlPullParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Log.v("Create HighScores",HS_CREATE);
    database.execSQL(HS_CREATE);
  }
  
  public void parseXMLtoDB(SQLiteDatabase database) throws XmlPullParserException, IOException {
	 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	 factory.setNamespaceAware(true);
	 XmlPullParser xpp = factory.newPullParser();
	 InputStream raw = this.c.getResources().openRawResource(R.raw.words);
	 xpp.setInput(new InputStreamReader(raw));
	 int eventType = xpp.getEventType();
	 while (eventType != XmlPullParser.END_DOCUMENT) {
		 if(eventType == XmlPullParser.START_TAG) {
			 if(xpp.next()== XmlPullParser.TEXT) {
				 //Log.v("parse",xpp.getText());
				 String word = xpp.getText(); 
				 ContentValues values = new ContentValues(); 
				 values.put(MySQLiteHelper.COLUMN_WORD, word);
				 values.put(MySQLiteHelper.COLUMN_LENGTH, word.length());
				 database.insert(TABLE_DICT , null, values); 
				 //database.close();
			 }
		 }
		 eventType = xpp.next(); //
	  }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICT);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_HS);
    onCreate(db);
  }

} 
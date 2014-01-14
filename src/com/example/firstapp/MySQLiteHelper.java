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

  public static final String TABLE_WORDS = "words";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_WORD = "word";
  public static final String COLUMN_LENGTH = "length";

  private static final String DATABASE_NAME = "dictionary.db";
  private static final int DATABASE_VERSION = 10;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_WORDS 
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_WORD + " TEXT not null, "
      + COLUMN_LENGTH + " INT NOT NULL "
      + ");";

  public MySQLiteHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  this.c = context;
	  Log.v("MySQLiteHelper","init");
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  //Log.v("MySQLiteHelper","oncreate");
    database.execSQL(DATABASE_CREATE);
    try {
		parse(database);
	} catch (XmlPullParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void parse(SQLiteDatabase database) throws XmlPullParserException, IOException {
	 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	 factory.setNamespaceAware(true);
	 XmlPullParser xpp = factory.newPullParser();
	 InputStream raw = this.c.getResources().openRawResource(R.raw.words);//this.c.getAssets().open("small.xml");
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
				 database.insert(TABLE_WORDS , null, values); 
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
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
    onCreate(db);
  }

} 
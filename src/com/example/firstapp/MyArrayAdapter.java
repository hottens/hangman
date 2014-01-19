package com.example.firstapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<Score> {
  private final Context context;
  private final List<Score> values;

  public MyArrayAdapter(Context context, List<Score> values) {
    super(context, R.layout.highscore_list_item,values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.highscore_list_item, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.secondLine);
    TextView scoreView = (TextView) rowView.findViewById(R.id.score);
    TextView mainText = (TextView) rowView.findViewById(R.id.firstLine);
    scoreView.setText(String.valueOf(values.get(position).getScore()));
    if(values.get(position).getEvil()) scoreView.setTextColor(Color.RED);
    textView.setText(values.get(position).getDate());
    mainText.setText(values.get(position).getWord());
    

    return rowView;
  }
} 
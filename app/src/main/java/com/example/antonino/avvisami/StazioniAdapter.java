package com.example.antonino.avvisami;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.antonino.avvisami.modello.Stazione;
import com.example.antonino.avvisami.modello.Stazioni;

import java.util.ArrayList;

/**
 * Created by Antonino on 11/01/2018.
 */

public class StazioniAdapter extends ArrayAdapter<Stazione> {

     static class ViewHolder {

        TextView textView;
        private ViewHolder(View rootView) {
            textView = (TextView) rootView.findViewById(android.R.id.text1);
        }
    }

    public StazioniAdapter(Context context, int textViewResourceId, ArrayList<Stazione> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Stazione item = getItem(position);
        if (item!= null) {
            viewHolder.textView.setText(item.getNome() + " " + item.getDistance() + "  " + item.isPassato());
            if(item.isPassato() == true){
                Log.d("REDDDDD", "" + item.getNome() + " " + item.isPassato());
                viewHolder.textView.setBackgroundColor(Color.RED);
            }
            else
                viewHolder.textView.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }

    public void drawText(TextView textView, Stazione item) {
    }
}
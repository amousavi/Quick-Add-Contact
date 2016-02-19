package com.gifsoundroid.moosetown.quickaddcontact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecentsAdapter extends ArrayAdapter<RecentContact> {
    private Activity activity;
    private ArrayList<RecentContact> contacts;
    private static LayoutInflater inflater = null;

    public RecentsAdapter (Activity activity, int textViewResourceId,ArrayList<RecentContact> list) {
        super(activity, textViewResourceId, list);
        try {
            this.activity = activity;
            this.contacts = list;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return contacts.size();
    }



    public static class ViewHolder {
        public TextView mainText;
        public TextView subText;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();

                holder.mainText = (TextView) vi.findViewById(android.R.id.text1);
                holder.subText = (TextView) vi.findViewById(android.R.id.text2);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.mainText.setText(contacts.get(position).mainText());
            holder.subText.setText(contacts.get(position).subText());


        } catch (Exception e) {


        }
        return vi;
    }
}
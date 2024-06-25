package com.example.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageArrayAdapter extends ArrayAdapter<String> {
    private int[] imageResourceID;

    public ImageArrayAdapter(Context context, int spinner_with_image, String[] objects, int[] imageResourceID) {
        super(context, spinner_with_image, objects);
        this.imageResourceID = imageResourceID;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.spinner_with_image, parent, false);
        ImageView images = (ImageView) view.findViewById(R.id.image);
        TextView txt = (TextView) view.findViewById(R.id.text);

        images.setImageResource(imageResourceID[position]);
        txt.setText(getItem(position));

        return view;
    }
}

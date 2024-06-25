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

    // 添加方法用于找到特定选项的位置
    public int getPosition(String spinnerOption) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).equals(spinnerOption)) {
                return i; // 找到该选项在适配器中的位置并返回
            }
        }
        return -1; // 如果找不到对应选项，返回-1
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

package com.example.codescannergenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<EncodeData> {
    private static final String TAG = "ListAdapter";
    public ListAdapter(Context context, ArrayList<EncodeData>arrayList) {
        super(context, R.layout.layout_list_item,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EncodeData encodeData = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item, parent, false);
        }

        TextView text = convertView.findViewById(R.id.textView);

        text.setText(encodeData.text);

        return convertView;
    }
}

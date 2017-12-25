package com.example.asherif.task.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.asherif.task.R;
import com.example.asherif.task.model.Company;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterCompany extends ArrayAdapter<Company> {
    Activity c;
    List<Company> a;

    public AdapterCompany(Activity c, List<Company> b) {
        super(c, R.layout.list_row, b);
        // TODO Auto-generated constructor stub
        this.c = c;
        a = b;
    }

    @Override
    public View getView(int pos, View View, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = c.getLayoutInflater();
        View = inflater.inflate(R.layout.list_row, parent, false);

        TextView name = (TextView) View.findViewById(R.id.name_list);
        ImageView img = (ImageView) View.findViewById(R.id.image_list);
        Picasso.with(getContext()).load(a.get(pos).getLogo()).into(img);
        name.setText(a.get(pos).getName());
        name.setText(a.get(pos).getDomain());
        return View;

    }

}


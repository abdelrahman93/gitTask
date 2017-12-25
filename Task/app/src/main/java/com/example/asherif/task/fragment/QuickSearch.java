package com.example.asherif.task.fragment;

import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ListView;

import com.example.asherif.task.MainActivity;
import com.example.asherif.task.R;
import com.example.asherif.task.model.Company;
import com.example.asherif.task.restHttp.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * This fragment of the Quick Search Tab
 */

public class QuickSearch extends Fragment {

    TextView companyName;
    TextView companyDomain;
    ImageView comapnyLogo;
    List<Company> companyList;
    SimpleCursorAdapter mAdapter;
    private static String url = "https://autocomplete.clearbit.com/v1/companies/suggest?query=name";

    public QuickSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //Call method to get Companies data from Http
        new GetCompanies().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quick_search, container, false);
        //Initialization
        companyName = (TextView) rootView.findViewById(R.id.company_name);
        companyDomain = (TextView) rootView.findViewById(R.id.company_domain);
        comapnyLogo = (ImageView) rootView.findViewById(R.id.company_logo);
        //search filter with suggestions

        SearchView searchView = (SearchView) rootView.findViewById(R.id.search);


        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                companyName.setText(companyList.get(i).getName());
                companyDomain.setText(companyList.get(i).getDomain());
                String imageUri = companyList.get(i).getLogo();
                Picasso.with(getActivity().getApplicationContext()).load(imageUri).into(comapnyLogo);

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

        //Set TextView name custom font Montserrat Bold
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Montserrat-Bold.ttf");
        companyName.setTypeface(custom_font);
        companyList = new ArrayList<Company>();
        return rootView;
    }

    // get the list for search
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for (int i = 0; i < companyList.size(); i++) {
            if (companyList.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, companyList.get(i).getName()});
        }
        mAdapter.changeCursor(c);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetCompanies extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);
                    Log.e(TAG, "Jason array size : " + contacts.length());

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject obj = contacts.getJSONObject(i);

                        String name = obj.getString("name");
                        String domain = obj.getString("domain");
                        String logo = obj.getString("logo");

                        Company c = new Company(name, domain, logo);

                        // adding contact to contact list
                        companyList.add(c);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

    }


    }


package com.example.asherif.task;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.asherif.task.adapter.AdapterCompany;
import com.example.asherif.task.adapter.FontsOverride;
import com.example.asherif.task.fragment.ListCompanies;
import com.example.asherif.task.adapter.ViewPagerAdapter;
import com.example.asherif.task.fragment.QuickSearch;
import com.example.asherif.task.model.Company;
import com.example.asherif.task.restHttp.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * This MainActivity that control the fragments of the Tab Layouts.   .
 */

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView comapnyLogo;
    ArrayList<Company> companyList = new ArrayList<Company>();

    private static String url = "https://autocomplete.clearbit.com/v1/companies/suggest?query=name";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Call method to get Companies data from Http
        new GetCompanies().execute();
        // Set the Montserrat font to the application
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Fonts/Montserrat-Medium.ttf");
        // Initialization

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Adding Tabs icons
        tabLayout.getTabAt(0).setIcon(R.drawable.search_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.list_icon);


    }

    // This method to set the fragments to the Tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuickSearch(), "Quick Search");
        adapter.addFragment(new ListCompanies(), "List View");
        viewPager.setAdapter(adapter);
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
                   // loadImg();

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

}


package com.example.asherif.task.fragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.asherif.task.R;
import com.example.asherif.task.adapter.AdapterCompany;
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
 * This fragment of the List View Tab displaying the users fetched from the API.
 */
public class ListCompanies extends Fragment {
    List<Company> companyList;
    TextView name;
    ListView lv;
    SimpleCursorAdapter mAdapter;
    private static String url = "https://autocomplete.clearbit.com/v1/companies/suggest?query=name";

    public ListCompanies() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        companyList = new ArrayList<>();
        new GetCompanies().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        //Initialization
        name = (TextView) rootView.findViewById(R.id.name);
        lv = (ListView) rootView.findViewById(R.id.list_view);
        return rootView;
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //to inflate the listview to fragment
            AdapterCompany ad = new AdapterCompany(getActivity(), companyList);
            lv.setAdapter(ad);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    view = factory.inflate(R.layout.pop_up, null);
                    //to make pop up alert dialog
                    final TextView pop_name = (TextView) view.findViewById(R.id.pop_company_name);
                    TextView pop_domain = (TextView) view.findViewById(R.id.pop_company_name);
                    ImageView pop_logo = (ImageView) view.findViewById(R.id.pop_company_logo);
                    Button pop_button = (Button) view.findViewById(R.id.pop_btn);
                    pop_name.setText(companyList.get(i).getName());
                    pop_domain.setText(companyList.get(i).getDomain());
                    Picasso.with(getContext()).load(companyList.get(i).getLogo()).into(pop_logo);

                    alertDialog.setView(view);
                    final AlertDialog ad = alertDialog.show();
                    pop_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            name.setText(pop_name.getText().toString());
                            ad.dismiss();
                        }
                    });
                }
            });

        }

    }
}

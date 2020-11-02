package com.example.boxaim;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boxaim.Fragments.ProfileFragement;

public class FiltersActivity extends AppCompatActivity {

    private static String TAG="FilterActivity";
    private ImageView backArrow;
    private Button mSave;
    private EditText mCity, mStateProvince, mCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        handleIntent(getIntent());
        backArrow=findViewById(R.id.backArrow);
        mSave = (Button) findViewById(R.id.btnSave);
        mCity = (EditText) findViewById(R.id.input_city);
        mStateProvince = (EditText) findViewById(R.id.input_state_province);
        mCountry = (EditText) findViewById(R.id.input_country);

        init();

    }

    private void init() {
        getFilterPreferences();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Saving Filter Preferences");
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(FiltersActivity.this);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(getString(R.string.preferences_city),mCity.getText().toString());
                editor.commit();
                editor.putString(getString(R.string.preferences_state_province),mStateProvince.getText().toString());
                editor.commit();
                editor.putString(getString(R.string.preferences_country),mCountry.getText().toString());
                editor.commit();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFilterPreferences() {
        Log.d(TAG,"Retrieving filter preferences from SharedPreferences");
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String country=preferences.getString(getString(R.string.preferences_country),"");
        String state_province=preferences.getString(getString(R.string.preferences_state_province),"");
        String city=preferences.getString(getString(R.string.preferences_city),"");

        mCountry.setText(country);
        mCity.setText(city);
        mStateProvince.setText(state_province);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

}

package com.example.location;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationDetails extends AppCompatActivity {
    DBManagemet dbManagemet ;
    EditText AddressBefore;
    TextView latBefore;
    TextView lonBefore;
    Button saveBTN;
    MyAdapter myAdapter;
    int idLocation;
    List<Location> locationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_details);

        AddressBefore = findViewById(R.id.addressEditText);
        latBefore = findViewById(R.id.latitudeTextView);
        lonBefore = findViewById(R.id.longitudeTextView);
        saveBTN = findViewById(R.id.update);

        dbManagemet = new DBManagemet(this);;

        Intent intent = getIntent();
        String oldAddress = intent.getStringExtra("address");
        String oldLat = intent.getStringExtra("latitude");
        String oldLon = intent.getStringExtra("longitude");
        idLocation = intent.getIntExtra("locationID", -1);


        //updating the lat and lon directly while the user is typing in real-time
        AddressBefore.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCoordinates(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        saveBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText updatedAdress = findViewById(R.id.addressEditText);
                TextView newLat = findViewById(R.id.latitudeTextView);
                TextView newLon = findViewById(R.id.longitudeTextView);
                String newAddressToString = updatedAdress.getText().toString();

                //update the new address to the db
               if( dbManagemet.UpdateLocation(idLocation, newAddressToString,newLat.getText().toString(),newLon.getText().toString()))
               {
                   Log.d("upup","val "+newAddressToString);
                   myAdapter.notifyDataSetChanged();
                   Toast.makeText(getApplicationContext(), "Location saved", Toast.LENGTH_SHORT).show();

                   finish();
               }

            }
        });

        //setting the old address data
        AddressBefore.setText(oldAddress);
        latBefore.setText(oldLat);
        lonBefore.setText(oldLon);
    }

    //Applying Geocoding to get the lat and lon for the updated address
    private List<String> updateCoordinates(String address) {
        List<String> formattedCoordinates = new ArrayList<>();
        List<Double> latAndLon = new ArrayList<>();
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> ls= geocoder.getFromLocationName(address,1);
                for (Address addr: ls) {
                    double latitude = addr.getLatitude();
                    double longitude = addr.getLongitude();

                    String lat = formatCoordinates(latitude,5);
                    String lon = formatCoordinates(longitude,5);
//                    dbManagemet.UpdateLocation(id, address, String.valueOf(latitude),String.valueOf(longitude));
                    latBefore.setText(lat);
                    lonBefore.setText(lon);
                    formattedCoordinates.add(lat);
                    formattedCoordinates.add(lon);


                }
            } catch (IOException e) { e.printStackTrace();}
        }


        return formattedCoordinates;

    }

    //formatCoordinates to get to format the latitude and longitude values to have a specific number of decimal places
    private String formatCoordinates(Double coordinates, int decimalPlaces) {

        String formatPattern = "%." + decimalPlaces + "f";


            String formattedCoordinate = String.format(Locale.US, formatPattern, coordinates);



        return formattedCoordinate;
    }
}

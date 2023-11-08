package com.example.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;

import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    DBManagemet dbManagemet ;
    boolean dataExists;
    List<Location> locationList;

    TextView locationAnnouncement;
    TextView locationListLink;
    EditText serachAddress;
    Button mapBTN;
    Button addBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting ids from xml
        locationAnnouncement = findViewById(R.id.locationscount);
        locationListLink = findViewById(R.id.locationslist);
        serachAddress = findViewById(R.id.searchBar);
        mapBTN = findViewById(R.id.gotomap);
        addBTN = findViewById(R.id.addLocation);


        dbManagemet = new DBManagemet(this);
        ArrayList<String> geocodedAddresses = new ArrayList<>();
        dataExists = dbManagemet.checkIfDataExistsInDatabase();

//        checking if entered the 50 locations
        if (!dataExists) {
            try {
                //reed the asset input file
                InputStream inputStream = getAssets().open("latitude_longitude.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = br.readLine()) != null) {

                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        double latitude = Double.parseDouble(parts[0]);
                        double longitude = Double.parseDouble(parts[1]);

                        //apply reverse geocoding to get the addresses for the given lat and lon
                        if (Geocoder.isPresent()) {
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                for (Address addr : addresses) {

                                    String name = addr.getFeatureName();
                                    String address = addr.getAddressLine(0);
                                    String city = addr.getLocality();
                                    String county = addr.getSubAdminArea();
                                    String prov = addr.getAdminArea();
                                    String country = addr.getCountryName();
                                    String postalCode = addr.getPostalCode();
                                    String phone = addr.getPhone();
                                    String url = addr.getUrl();
                                    String formattedAddress = "Name: " + name + "\n" +
                                            "Address: " + address + "\n" +
                                            "City: " + city + "\n" +
                                            "County: " + county + "\n" +
                                            "Province: " + prov + "\n" +
                                            "Country: " + country + "\n" +
                                            "Postal Code: " + postalCode + "\n";

                                    geocodedAddresses.add(address);


                                    // add the locations to the database
                                    dbManagemet.AddLocation(address, String.valueOf(latitude), String.valueOf(longitude));


                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Handle the geocoding error
                            }
                        }

                    }

                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        locationList = dbManagemet.getAllLocations();

        //getting the number of locations stored in the database
        locationAnnouncement.setText("We currently have over "+ locationList.size() +" locations!");

        //to view the locations list
        locationListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewLocations.class);
                startActivity(intent);
            }
        });

        // to show the specific address on the map
       mapBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String searchAddressToString = serachAddress.getText().toString();
               Log.d("sear", "val "+ searchAddressToString);
               if(checkingEmpty(searchAddressToString))
               {
               Intent i = new Intent(MainActivity.this,MapsActivity.class);
               i.putExtra("address", searchAddressToString);

               startActivity(i);}

           }
       });
       //to add a new location
       addBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent in = new Intent(MainActivity.this, AddLocation.class);
               startActivity(in);
           }
       });



    }


    boolean checkingEmpty(String text)

    {
        if(text.isEmpty())
            return false;
        else
            return true;
    }


}
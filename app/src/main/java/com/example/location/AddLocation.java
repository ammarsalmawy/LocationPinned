package com.example.location;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddLocation extends AppCompatActivity {

    EditText newAddress;
    Button addingBTN;

    DBManagemet dbManagemet;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_location);

        newAddress = findViewById(R.id.addressInput);
        addingBTN =findViewById(R.id.add_to_database);


        dbManagemet = new DBManagemet(this);




        addingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAddressToString = newAddress.getText().toString();
                if(checkingEmpty(newAddressToString))
                {

                    //Aplying Geocoding to get the lat and lon for new address
                    if (Geocoder.isPresent()) {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        try {
                            List<Address> ls= geocoder.getFromLocationName(newAddressToString,1);
                            for (Address addr: ls) {
                                double latitude = addr.getLatitude();
                                double longitude = addr.getLongitude();
                                String lat = formatCoordinates(latitude,5);
                                String lon = formatCoordinates(longitude,5);
                                //adding the new location to the db
                                dbManagemet.AddLocation(newAddressToString,lat,lon);
                                Toast.makeText(getApplicationContext(), "Location saved", Toast.LENGTH_SHORT).show();

                                myAdapter.notifyDataSetChanged();
                                finish();
                            }
                        } catch (IOException e) { e.printStackTrace();}
                    }


                }
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

    //formatCoordinates to get to format the latitude and longitude values to have a specific number of decimal places
    private String formatCoordinates(Double coordinates, int decimalPlaces) {

        String formatPattern = "%." + decimalPlaces + "f";


        String formattedCoordinate = String.format(Locale.US, formatPattern, coordinates);



        return formattedCoordinate;
    }

}

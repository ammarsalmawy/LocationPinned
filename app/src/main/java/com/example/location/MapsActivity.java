package com.example.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MapsActivity extends AppCompatActivity {
    WebView webView;
    List<Location> locationList;
    DBManagemet dbManagemet;
    private final int initialZoomLevel = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        dbManagemet = new DBManagemet(this);
        Intent intent = getIntent();
        //getting the entered address so look for on the map
        String getAddress= intent.getStringExtra("address");

        webView = findViewById(R.id.mapWebView);
        webView.getSettings().setJavaScriptEnabled(true);


        //finding the location in the database
        Location location = dbManagemet.searchByAddress(getAddress);
        if (location != null)
        {
            double lat = Double.parseDouble(location.getLat());
            double lon = Double.parseDouble(location.getLon());
            String mapUrl = "https://www.google.com/maps?q=" + lat + "," + lon;

            webView.getSettings().setBuiltInZoomControls(true);

            webView.loadUrl(mapUrl.toString());
        }
        else{
            String mapUrl = "https://www.google.com/maps" ;

            webView.getSettings().setBuiltInZoomControls(true);

            webView.loadUrl(mapUrl.toString());

        }





    }
}

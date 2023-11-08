package com.example.location;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewLocations extends AppCompatActivity {


    DBManagemet dbManagemet;
    Button deleteLocation;
    MyAdapter adapter;
    List<Location> locationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);

        deleteLocation = findViewById(R.id.deleteAddress);
        dbManagemet = new DBManagemet(this);


       locationList = dbManagemet.getAllLocations();

       //to view the whole locations in recycler view
         adapter = new MyAdapter(getApplicationContext(), locationList);
        RecyclerView recyclerView = findViewById(R.id.adressesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                // Handle the item click here, e.g., start the NoteDetailActivity
                Location location = locationList.get(position);
                Intent intent = new Intent(ViewLocations.this, LocationDetails.class);
                intent.putExtra("address", location.getAddrss());
                intent.putExtra("latitude", location.getLat());
                intent.putExtra("longitude", location.getLon());
                intent.putExtra("locationID", location.getId());
                startActivity(intent);
            }
        });



        adapter.setOnItemtoDeleteClickListener(new MyAdapter.OnItemtoDeleteClickListener() {
            @Override
            public void onItemtoDeleteClick(int position) {
                showDeleteConfirmationDialog(position);
                adapter.notifyDataSetChanged();
            }
        });


    }

    //handling the deletion
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deleting");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the delete action
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteItem(int position) {
        Location location = locationList.get(position);
        boolean isDeleted = dbManagemet.DeleteLocation(location.getId());

        if (isDeleted) {
            locationList.remove(position);
            adapter.notifyItemRemoved(position);
        } else {

            Toast.makeText(this, "Failed to delete the note.", Toast.LENGTH_SHORT).show();
        }
    }
}

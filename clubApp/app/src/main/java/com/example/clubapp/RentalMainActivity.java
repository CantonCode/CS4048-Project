package com.example.clubapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class RentalMainActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("equipment");
    private EquipmentAdapter equipmentAdapter;
    private EquipmentAdapter equipmentAdapter1;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Dialog sortByDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_main);

        findViewById(R.id.goBack).setOnClickListener(this);
        findViewById(R.id.sortBy).setOnClickListener(this);

        setUpRecyclerView();
        setUpDialog();
    }

    public void setUpRecyclerView(){
        Query query = notebookRef.orderBy("rented", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Equipment> options = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(query, Equipment.class)
                .build();

        equipmentAdapter = new EquipmentAdapter(options);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView = findViewById(R.id.recyclerEquipment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(equipmentAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        equipmentAdapter.startListening();
    }
    protected void onStop(){
        super.onStop();
        equipmentAdapter.stopListening();
    }

    private void goBack(){
        Intent intent = new Intent(this, homeActivity.class);
        startActivity(intent);
    }

    private void setUpDialog() {
        sortByDialog = new Dialog(this);
        sortByDialog.setContentView(R.layout.sort_by_equipment);
        sortByDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sortByDialog.findViewById(R.id.availablitiy).setOnClickListener(this);
    }

    private void changeView(){
        Query query = notebookRef.whereEqualTo("rented",false);
        FirestoreRecyclerOptions<Equipment> options1 = new FirestoreRecyclerOptions.Builder<Equipment>()
                .setQuery(query, Equipment.class)
                .build();

        equipmentAdapter1 = new EquipmentAdapter(options1);
        recyclerView.setAdapter(equipmentAdapter1);
        equipmentAdapter1.startListening();
        sortByDialog.dismiss();
//        equipmentAdapter1.stopListening();
    }

    private void popUp(){
        sortByDialog.show();
    }

    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.goBack) {
            goBack();
        }
        if (i == R.id.sortBy) {
            popUp();
        }

        if (i == R.id.availablitiy) {
            changeView();
            Toast.makeText(this, "Showing only available boards", Toast.LENGTH_LONG).show();
        }

    }
}
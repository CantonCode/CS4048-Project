package com.Rental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetRentalsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String currentUserId;
    List<List<String>> allRentals = new ArrayList<>();
    List<List<String>> RentalsWithName = new ArrayList<>();
    ArrayList<String> rentals = new ArrayList<>();
    ArrayList number = new ArrayList<>();
    ArrayList lab= new ArrayList<>();
    List<Integer> nums = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    List<String> names = new ArrayList<>();
    TextView display;
    String equipmentName = "";
    HashMap<String, String> equipment=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_rentals);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        CollectionReference docRef = db.collection("equipment");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        equipment.put(document.getId(), document.getString("equipmentName"));
                    }

                } else {
                    Log.d("Chart", "Error getting documents: ", task.getException());
                }
                Log.d("Chart", equipment.toString());
                getUserRentals();
            }
        });

        display= findViewById(R.id.textView4);
    }

    private void getUserRentals(){
        CollectionReference colRef = db.collection("rented").document(currentUserId).collection("equipment");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        rentals = (ArrayList<String>) document.get("dateOfRental");
                        ids.add(document.getId());
                        String name = getEquipmentName(document.getId(), rentals);
                        Log.d("Check", name);
                        allRentals.add(rentals);
                    }

                    barChart();
                } else {
                    Log.d("Check", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private String getEquipmentName(String id, final ArrayList<String> dates){
        equipmentName= "";
        nums.add(dates.size());
        for (Map.Entry<String,String> entry : equipment.entrySet()) {
            String key = entry.getKey();
            String name = entry.getValue();
            //Log.d("Chart", key + " => " + name);
            if(key.equals(id)){
                Log.d("Chart", key + " => " + name);
                display(name, dates);
                lab.add(name);
            }
        }
        return equipmentName;
    }

    private void display(String name, ArrayList<String> dates){
        boolean upcomingDate = false;
        boolean nameDisplayed = false;
        names.add(name);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        for(String date : dates) {
            try {
                Date now = new Date();
                Date d=dateFormat.parse(date);
                System.out.println("DATE"+d);
                System.out.println("Formated"+dateFormat.format(d));
                if(now.before(d)) {
                    upcomingDate=true;
                    if(upcomingDate && !nameDisplayed){
                        //display.setTextColor(Color.parseColor("#2144F3"));
                        display.append("\n" + name + ": ");
                        //display.setTextColor(Color.parseColor("F6050000"));
                        display.append(date + "\n   ");
                        nameDisplayed=true;
                    }
                    else {
                        display.append(date + "\n   " + "   ");
                    }
                }
            }
            catch(Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep"+e);
            }
        }
    }

    private void barChart() {
        BarChart chart = findViewById(R.id.barchart);

        Log.d("Chart", nums.toString());
        Log.d("Chart", lab.toString());

        for(int i =0; i < nums.size(); i++){
            int numberOfDates = nums.get(i);
            float num = (float) numberOfDates;
            number.add((new BarEntry(num, i)));
        }

        Log.d("Chart", number.toString());
        BarDataSet bardataset = new BarDataSet(number ,"Cells");

        BarData data = new BarData(names, bardataset);
        chart.setData(data);
        chart.setDescription("Board Rental Summary");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.animateY(5000);

    }

    private void previousRentals(){

    }
}
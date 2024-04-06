package com.ved.Vedu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ved.mysafety.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    CardView c1;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ImageView l1,l2,l3;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        c1 = findViewById(R.id.v1);
      //  c2 = findViewById(R.id.v2);
        l1 =findViewById(R.id.b1);
        l2 =findViewById(R.id.b2);
        l3 =findViewById(R.id.b3);

        c1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Complaint.class);
            startActivity(intent);
        });

//        c2.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this,SoS.class);
//            startActivity(intent);
//        });

        l1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,number.class);
            startActivity(intent);
        });

        l2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Learn.class);
            startActivity(intent);
        });

        l3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,SoS.class);
            startActivity(intent);
        });
        mDatabase.child("org").child("Notice").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String data=String.valueOf(task.getResult().getValue());
                    String[] items = data.split(",");
                    ArrayList<String> listItems = new ArrayList<>();

                    for (String item : items) {
                        String[] idTitle = item.split("=");
                        String date = dateConverter(idTitle[0].replace("{", ""));
                        String notice = idTitle[1].replace("}", "");
                        listItems.add(date + " - " + notice);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listItems);

                    ListView listView = findViewById(R.id.listviewnotice);
                    listView.setAdapter(adapter);

                }
            }
        });

    }
    public String dateConverter(String timestamp) {
        String trimmedTimestamp = timestamp.trim(); // Remove any leading or trailing whitespace
        long unixTimestamp = Long.parseLong(trimmedTimestamp);
        Date date = new Date(unixTimestamp * 1000L); // Convert to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }
    public void signout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
package com.example.spendingtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spendingtracker.Model.friend;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addFriend extends AppCompatActivity implements View.OnClickListener {

    EditText etFEmail;
    EditText etFname;
    Button btnACancle;
    Button btnFAdd;
    FirebaseDatabase database;
    DatabaseReference users;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etFEmail = findViewById(R.id.etFEmail);
        etFname = findViewById(R.id.etFname);
        btnACancle = findViewById(R.id.btnACancle);
        btnFAdd = findViewById(R.id.btnFAdd);
        btnFAdd.setOnClickListener(addFriend.this);
        btnACancle.setOnClickListener(addFriend.this);
        SharedPreferences sp=getSharedPreferences("uName",MODE_PRIVATE);
        name = sp.getString("uname", "Not");
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnACancle:
                startActivity(new Intent(addFriend.this, Home.class));
                this.finish();
                break;
            case R.id.btnFAdd:
                add();
                break;
        }
    }

    private void add() {
        final friend  f = new friend(etFname.getText().toString(),etFEmail.getText().toString());
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(name).child("friends") .child(f.getFemail()).exists()) {
                    Toast.makeText(addFriend.this, "already", Toast.LENGTH_SHORT).show();
                }
                else {
                    users.child(name).child("friends").child(f.getFemail()).setValue(f);
                    Toast.makeText(addFriend.this, "Insert Successfully", Toast.LENGTH_SHORT).show();
                    addFriend.this.finish();
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

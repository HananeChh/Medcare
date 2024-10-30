package com.moncefadj.medcare.Medicaments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.Patient.PatientHome;
import com.moncefadj.medcare.R;

import java.util.ArrayList;

public class liste_mesure extends AppCompatActivity {
    ArrayList<mesureData> mesureList;
    private TextView middle_text;
    private RecyclerView malist;
    private mesureAdapter mesAdapter;
    DatabaseReference dbreference ;
    FirebaseUser uPatient;
    String uidPatient;
    FirebaseDatabase database;
    private Button goTOmed;
    private DatabaseReference  mesureref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesure_list);
        middle_text = (TextView) findViewById(R.id.text_middle);
        mesureList = new ArrayList<mesureData>();
        mesAdapter = new mesureAdapter(this,mesureList);
        malist = (RecyclerView) findViewById(R.id.malistmes);
        malist.setHasFixedSize(true);
        malist.setLayoutManager(new LinearLayoutManager(this));
        malist.setAdapter(mesAdapter);
        uPatient = FirebaseAuth.getInstance().getCurrentUser();
        uidPatient = uPatient.getUid();
        database = FirebaseDatabase.getInstance();
        dbreference = database.getReference().child("Users").child("Patients").child(uidPatient);
        mesureref = dbreference.child("Mesures");
        goTOmed = (Button) findViewById(R.id.goTOmeds);
        retreivemesureData();
        goTOmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),liste_medicaments.class);
                startActivity(intent);
            }
        });

    }

    public boolean mesexist(mesureData mesure){
        boolean exist = false;
        int i = 0;
        while ( ( i< mesureList.size() )&& (!exist)){
            if((mesureList.get(i).getTime() == mesure.getTime()) ){
                exist = true;
            }
            else {
                i++;
            }
        }
        return exist;

    }

    private void retreivemesureData() {
        mesureref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        mesureData mes = dataSnapshot.getValue(mesureData.class);
                        if (!mesexist(mes)) {
                            mesureList.add(mes);
                            Log.i("test ", mes.getNomMesure());
                        }

                    }
                    if (mesureList.size() != 0) {
                        middle_text.setText("");
                    } else if (mesureList.size() == 0) {
                        middle_text.setText("Ajouter vos mesures");
                    }
                    mesAdapter.notifyDataSetChanged();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    }
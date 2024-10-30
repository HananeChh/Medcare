package com.moncefadj.medcare.Medicaments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ajouterMesure extends AppCompatActivity {
    FirebaseDatabase data_base;
    DatabaseReference mesuresReference;
    DatabaseReference mesReference;
    private EditText nomMesure , valeur_mes ;
    ArrayList<mesureData> mesureList;
    private Button addMesure ;
    FirebaseUser uPatient;
    RecyclerView ma_list;
    String uidPatient;
    DatabaseReference dbreference ;
    private mesureAdapter mesAdapter;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_mesure);
        nomMesure = (EditText) findViewById(R.id.nomMesure);
        valeur_mes = (EditText) findViewById(R.id.valeur);
        addMesure = (Button) findViewById(R.id.ajouterAuMesureList);
        mesureList = new ArrayList<mesureData>();
        mesAdapter = new mesureAdapter(this,mesureList);
        back = (ImageView) findViewById(R.id.back_to_liste);

        uPatient = FirebaseAuth.getInstance().getCurrentUser();
        uidPatient = uPatient.getUid();
        data_base = FirebaseDatabase.getInstance();
        //-----------------adding---methods---to--buttons
        addMesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomMesure.getText().toString();
                String valeur = valeur_mes.getText().toString();
                Calendar now = Calendar.getInstance();
                String time = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE);
                if (nom.isEmpty()){
                    nomMesure.setError("Le nom est obligatoire");
                }
                if (valeur.isEmpty()){
                    valeur_mes.setError("La valeur est obligatoire ");
                    return;
                }

                mesuresReference = data_base.getReference().child("Users").child("Patients").child(uidPatient).child("Mesures");
                mesReference =mesuresReference.child(nom);
                mesReference.setValue( new mesureData(nom,time,valeur) );
                Toast.makeText(getApplicationContext(), "Ajout réussi" , Toast.LENGTH_LONG ).show();
                Intent intent = new Intent(ajouterMesure.this, liste_mesure.class);
                startActivity(intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),liste_medicaments.class);
                startActivity(intent);
            }
        });
    }



    }

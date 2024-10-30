package com.moncefadj.medcare.Medicaments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.Medicaments.medData;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.Patient.PatientHome;
import com.moncefadj.medcare.PatientSearch.Search;
import com.moncefadj.medcare.ProfilePatient.PatientProfile;
import com.moncefadj.medcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class liste_medicaments extends AppCompatActivity {
    private static final int ADD_MED_ACTIVITY = 0;

    private FloatingActionButton ajouter_med;
    ArrayList<medDataDb> list;
    ArrayList<mesureData> mesureList;
    private TextView  title_liste;
    private RecyclerView malist;
    private medAdapter adapter;
    private mesureAdapter mesAdapter;
    DatabaseReference dbreference ;
    FirebaseUser uPatient;
    String uidPatient;
    Dialog dialog;
    ImageView back ;
    private Button goTOmesure;
    private DatabaseReference medreference , mesureref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_medicaments);
        // ** Dialog ** to ask patient if he want to add a med or mesure
        dialog = new Dialog(liste_medicaments.this);
        dialog.setContentView(R.layout.custom_med_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable draw = getResources().getDrawable(R.drawable.rounded_corner_white);
            dialog.getWindow().setBackgroundDrawable(draw);
        }
        dialog.setCancelable(false);
        // we can make animation for Dialog by mentioned it in Style
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView text = dialog.findViewById(R.id.check_user_txt);
        Button med_button = dialog.findViewById(R.id.add_med);
        Button mesure_button = dialog.findViewById(R.id.add_mesure);
        ImageView cancelDialogBtn = dialog.findViewById(R.id.cancel_dialog_btn);
        //--------------------------------------------------------------------------------------------------------------



        //---------------------------------------------------------------------------------------------------------------
        uPatient = FirebaseAuth.getInstance().getCurrentUser();
        uidPatient = uPatient.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbreference = database.getReference().child("Users").child("Patients").child(uidPatient);
        medreference = dbreference.child("Medicaments");
        mesureref = dbreference.child("Mesures");
        list = new ArrayList<medDataDb>();
        mesureList = new ArrayList<mesureData>();
        retreiveData();
        //--------------------------------list-------------------------------------------------------
        mesAdapter = new mesureAdapter(this,mesureList);
        adapter = new medAdapter(this,list );
        malist = (RecyclerView) findViewById(R.id.malist);
        malist.setHasFixedSize(true);
        malist.setLayoutManager(new LinearLayoutManager(this));
        malist.setAdapter(adapter);
        //----------------------------declaration-----------------------------------
        ajouter_med = (FloatingActionButton) findViewById(R.id.add_med);
        title_liste = (TextView) findViewById(R.id.titie_liste);
        goTOmesure = (Button) findViewById(R.id.goTOmesure);
        back  =(ImageView) findViewById(R.id.back_to_acceuil);
        //-------------------------buttons onclick-------------------------------
        goTOmesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , liste_mesure.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PatientHome.class);
                startActivity(intent);
            }
        });




        //-------------------------------------------------------------------------------------------------------------------*/
        ajouter_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeIn = AnimationUtils.loadAnimation(liste_medicaments.this, R.anim.fade_in);
                dialog.show();
            }
        });
        med_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openajouterMed();
                dialog.dismiss();
            }
        });
        mesure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openajouterMesure();
                dialog.dismiss();

            }
        });
        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    //--------------------------------------mesures-------------------------------------------------------
    private void affichermesmesure() {

        retreivemesureData();
        malist.setHasFixedSize(true);
        malist.setLayoutManager(new LinearLayoutManager(this));
        malist.setAdapter(mesAdapter);
    }
    public boolean mesexist(mesureData mesure){
        boolean exist = false;
        int i = 0;
        while ( ( i< mesureList.size() )&& (!exist)){
            if((mesureList.get(i).getTime() == mesure.getTime()) && (mesureList.get(i).getValeur() == mesure.getValeur())){
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
                        mesureData mes= dataSnapshot.getValue(mesureData.class);
                        if (!mesexist(mes)) {
                            mesureList.add(mes);
                            Log.i("test ", mes.getNomMesure());
                        }

                    }
                    if (mesureList.size() != 0 ){
                        title_liste.setText("");
                    }
                    else if (mesureList.size()==0){
                        title_liste.setText("Ajouter vos mesures");
                    }
                    adapter.notifyDataSetChanged();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //-------------------------------------------------------------------------------

    private void openajouterMesure() {
        Intent intent = new Intent(this , ajouterMesure.class);
        startActivity(intent);
    }

    // get data from database ------------------------------------------

    public boolean medexist(medDataDb medicament){
        boolean exist = false;
        int i = 0;
        while ( ( i< list.size() )&& (!exist)){
            if(list.get(i).getNomMed() == medicament.getNomMed()){
                exist = true;
            }
            else {
                i++;
            }
        }
        return exist;

    }
    public void retreiveData() {
        medreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        medDataDb med = dataSnapshot.getValue(medDataDb.class);
                        if (!medexist(med)) {
                            list.add(med);
                            Log.i("test ", med.getNomMed());
                        }

                    }
                    if (list.size() != 0 ){
                        title_liste.setText("");
                    }
                    else if (list.size()==0){
                        title_liste.setText("ajouter vos medicament");
                    }
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    private void openajouterMed() {
        Intent intent = new Intent(this, ajouterMed.class);
        startActivityForResult(intent, ADD_MED_ACTIVITY);
    }



}
package com.moncefadj.medcare.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.R;

import java.util.HashMap;
import java.util.Map;

public class DoctorProfileFromPatientHome extends AppCompatActivity {

    ImageButton backy;
    TextView namey,specy, adressy, descy, phoney, idy;
    String ELNAME, ELSPECIALITY, ELADRESS, ELDESC, ELPHONE, ELID;

    FirebaseDatabase database;
    DatabaseReference doctorRef;
    String uidPatient;
    DatabaseReference patientRef;

    AutoCompleteTextView autoCompleteTextView;
    String[] days;
    TextInputLayout daysInput;
    ArrayAdapter arrayAdapter;

    RelativeLayout relativeLayoutRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_from_patient_home);

        //setting doctor's information
        namey = findViewById(R.id.doctor_name);
        specy = findViewById(R.id.doctor_speciality);
        adressy = findViewById(R.id.doctor_adress);
        phoney = findViewById(R.id.doctor_phone_text);
        descy = findViewById(R.id.doctor_dsc);
        idy = findViewById(R.id.profile_id_medecin);

        ELNAME = getIntent().getStringExtra("Name");
        ELSPECIALITY = getIntent().getStringExtra("Spec");
        ELADRESS = getIntent().getStringExtra("Adr");
        ELPHONE = getIntent().getStringExtra("Phn");
        ELDESC = getIntent().getStringExtra("Dsc");
        ELID = getIntent().getStringExtra("Id");

        namey.setText(ELNAME);
        specy.setText(ELSPECIALITY);
        adressy.setText(ELADRESS);
        phoney.setText(ELPHONE);
        descy.setText((ELDESC));
        idy.setText(ELID);

        //back button to the patient's home page
        backy = (ImageButton) findViewById(R.id.back_to_patient_home);
        backy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intentLoadNewActivity = new Intent(DoctorProfileFromPatientHome.this, PatientHome.class);
                startActivity(intentLoadNewActivity);

            }

        });

        relativeLayoutRating = findViewById(R.id.rating_button);
        relativeLayoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //current patient path
        uidPatient = FirebaseAuth.getInstance().getCurrentUser().getUid();
        patientRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients").child(uidPatient);

        //current doctor path
        database = FirebaseDatabase.getInstance();
        doctorRef = database.getReference("Users").child("Doctors").child(ELID);

        //setting days
        daysAutoCompleteTxt();

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String day = daysInput.getEditText().getText().toString();
                daysInput.setError(null);
                removeFlexBoxViews();
                showAllDayButtons(day);

            }

            @Override
            public void afterTextChanged(Editable s) { }

        });

    }

    private void daysAutoCompleteTxt() {

        daysInput = findViewById(R.id.doctor_days_input);
        autoCompleteTextView = findViewById(R.id.doctor_autoCompleteText);
        days = getResources().getStringArray(R.array.days);
        arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_item, days);
        autoCompleteTextView.setAdapter(arrayAdapter);

    }

    private void showAllDayButtons(String day) {

        //déplacement vers la jour séléctionné dans la base de données
        DatabaseReference dayRef = doctorRef.child("rdvTimes").child(day);
        dayRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    removeFlexBoxViews();
                    for (DataSnapshot dayChosen : snapshot.getChildren()) {

                        //récupération du temps choisi et de la disponibilité
                        String time = dayChosen.child("time").getValue(String.class);
                        String available = dayChosen.child("available").getValue(String.class);

                        //affichages des horraires
                        showButton(time, available, dayRef, day);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });

    }

    private void showButton(String time, String available, DatabaseReference dayRef, String day) {

        FlexboxLayout flexboxLayout = findViewById(R.id.doctor_flex_box);

        Button button = new Button(DoctorProfileFromPatientHome.this);
        button.setText(time);
        button.setTextColor(getResources().getColor(R.color.white));

        if (available.equals("yes")) {

            //rdv non pris (disponible) en couleur bleue
            button.setBackground(getResources().getDrawable(R.drawable.circle_button));
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //boite de dialogue de confirmation de la prise du rdv

                    createAlertDialog(time, dayRef, day);

                }

            });

        } else {

            //rdv déja pris (non disponible) affiché en gris
            button.setBackground(getResources().getDrawable(R.drawable.circle_button_grey));
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //message informant que le rdv est déjà pris
                    Toast.makeText(getApplicationContext(),"RDV déjà pris.", Toast.LENGTH_SHORT).show();

                }

            });

        }

        //convertion des dp en pixels
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int heightPixel = (int) (60 * scale + 0.5f);
        int widthPixel = (int) (65 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixel,heightPixel);
        params.setMargins(15,0,0,15);

        button.setLayoutParams(params);
        flexboxLayout.addView(button);

    }

    private void createAlertDialog(String time, DatabaseReference dayRef, String day) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Voulez vous vraiment allez chez Dr. " + ELNAME + " vers " + time + "?");
        builder.setMessage("Si vous cliquez sur Oui, vous aller prendre un RDV qui sera affiché chez Dr. " + ELNAME + ".");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //prise de rdv apres la confirmation
                // la chaine "no" sert à rendre l'horraire non disponible
                removeFlexBoxViews();
                prendreRdv("no", time, dayRef, day);

            }

        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }

        });

        //affichage de la boite de dialogue
        builder.show();

    }

    private void prendreRdv(String YN, String time, DatabaseReference dayRef, String day) {

        //mise à jour de la disponibilité (dispo => non dispo)
        Map< String, Object> map = new HashMap<>();
        map.put("available", YN);
        dayRef.child(time).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //message confirmant la prise du rdv choisi
                Toast.makeText(getApplicationContext(), "RDV pris.", Toast.LENGTH_SHORT).show();

            }

        });

        //récupération de nom du patient
        getPatientName(time, day);

    }

    private void getPatientName(String time, String day) {

        patientRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nom = snapshot.child("name").getValue(String.class);
                //ajout du patient à la liste des patients avec rdv (PatientsWithRdv)
                addPatient(time, nom, day);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });

    }

    private void addPatient(String time, String nom, String jour) {

        HashMap<String, Object> mapRdvDudes = new HashMap<>();
        mapRdvDudes.put("name", nom);
        mapRdvDudes.put("time", time);
        mapRdvDudes.put("id",uidPatient);
        doctorRef.child("PatientsWithRdv").child(jour).child(uidPatient).updateChildren(mapRdvDudes);

    }

    public void removeFlexBoxViews() {
        FlexboxLayout flexboxLayout = findViewById(R.id.doctor_flex_box);
        flexboxLayout.removeAllViews();
    }

}
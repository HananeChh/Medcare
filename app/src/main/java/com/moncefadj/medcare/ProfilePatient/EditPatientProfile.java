package com.moncefadj.medcare.ProfilePatient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moncefadj.medcare.DataClasses.PatientData;
import com.moncefadj.medcare.Doctor.EditDoctorProfile;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditPatientProfile extends AppCompatActivity {
    // public static final String EXTRA_MESSAGE ="com.example.test.MESSAGE";

    private DatabaseReference ref, reff;
    private String userrID;
    private FirebaseUser userr;

    private EditText mnameinput, mmotdepassinput, mdayinput, mmonthinput, myearinput, maddressseinput, mnuminput, memailinput;
    private TextInputLayout enameinput, emotdepassinput, edayinput, emonthinput, eyearinput, eaddressseinput, enuminput, eemailinput;

    private Button mplaybutton;
    private ImageView back,add;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_patient_2);


        mnameinput = (EditText) findViewById(R.id.inom);
        mplaybutton = (Button) findViewById(R.id.button_confirm);
        mdayinput = (EditText) findViewById(R.id.day);
        mmonthinput = (EditText) findViewById(R.id.month);
        myearinput = (EditText) findViewById(R.id.year);
        mnuminput = (EditText) findViewById(R.id.inumero);
        memailinput = (EditText) findViewById(R.id.iemail);
        mmotdepassinput = (EditText) findViewById(R.id.imotdepass);

        edayinput=(TextInputLayout) findViewById(R.id.k2);
        emonthinput=(TextInputLayout) findViewById(R.id.k3);
        eyearinput=(TextInputLayout) findViewById(R.id.k4);
        enameinput=(TextInputLayout) findViewById(R.id.k);
        emotdepassinput=(TextInputLayout) findViewById(R.id.k1);
        enuminput=(TextInputLayout) findViewById(R.id.k5);
        eemailinput=(TextInputLayout) findViewById(R.id.k6);


        back = (ImageView) findViewById(R.id.iback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add=(ImageView)findViewById(R.id.a);

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients");
        userr = FirebaseAuth.getInstance().getCurrentUser();
        userrID = userr.getUid();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients").child(userrID).child("BirthDay");

        ref.child(userrID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PatientData userprofile = snapshot.getValue(PatientData.class);
                if (userprofile !=null) {
                    String motdepasse = userprofile.getPassword();
                    String email= userprofile.getEmail();
                    String img =userprofile.getProfile();


                    mmotdepassinput.setText(motdepasse);
                    memailinput.setText(email);
                    Picasso.get().load(img).into(add);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edayinput.setError(null);
        emonthinput.setError(null);
        eyearinput.setError(null);

        mplaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = mnameinput.getText().toString();
                String email = memailinput.getText().toString();
                String numero = mnuminput.getText().toString();
                String motdepasse = mmotdepassinput.getText().toString();
                String day = mdayinput.getText().toString();
                String month = mmonthinput.getText().toString();
                String year = myearinput.getText().toString();

                int dayInt = Integer.parseInt(day);
                int monthInt = Integer.parseInt(month);
                int yearInt = Integer.parseInt(year);



                if (!Name.isEmpty() && !email.isEmpty() && !numero.isEmpty() && !motdepasse.isEmpty()
                        && !day.isEmpty() && !month.isEmpty() && !year.isEmpty()) {
                    if (dayInt >= 1 && dayInt <= 31 && monthInt >= 1 && monthInt <= 12 && yearInt <= 2002){
                        HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", Name);
                    hashMap.put("email", email);
                    hashMap.put("phone", numero);
                    hashMap.put("password", motdepasse);


                    HashMap<String, Object> hashMapp = new HashMap<>();

                    hashMapp.put("day", day);
                    hashMapp.put("month", month);
                    hashMapp.put("year", year);

                    ref.child(userrID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(EditPatientProfile.this, "Informations modifiées", Toast.LENGTH_LONG).show();
                        }
                    });
                    reff.updateChildren(hashMapp);
                }else {
                        edayinput.setError("Vous avez dépasser la limite");
                        emonthinput.setError("Vous avez dépasser la limite");
                        eyearinput.setError("Vous avez dépasser la limite");
                    }
                    }

                }
        });

    }
}

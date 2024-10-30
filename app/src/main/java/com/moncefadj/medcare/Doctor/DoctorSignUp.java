package com.moncefadj.medcare.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.Patient.PatientSignUp;
import com.moncefadj.medcare.R;

import java.util.HashMap;

public class DoctorSignUp extends AppCompatActivity {


    String uidDoctor;
    DatabaseReference doctorRef;

    String[] specialities;
    ArrayAdapter arrayAdapter;
    AutoCompleteTextView autoCompleteTextView;

    Button signUpBtn;
    TextInputLayout nameInput,emailInput,passwordInput,keyInput,phoneInput,specialityInput;
    String name,email,password,key,phone,speciality;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        // Adapter for specialities of the doctors
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        specialities = getResources().getStringArray(R.array.specialities);
        arrayAdapter = new ArrayAdapter(this,R.layout.drop_down_item, specialities);

        autoCompleteTextView.setAdapter(arrayAdapter);

        // SignUp a Doctor
        signUpBtn = findViewById(R.id.signUp_btn_doctor);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signUpDoctor(); }
        });

        // Back button : back to LoginActivity
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorSignUp.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUpDoctor() {

        getEditTexts();

        clearErrors();

        if (name.isEmpty()) {
            nameInput.setError("le nom est obligatoire");
            return;
        }
        if (email.isEmpty()) {
            emailInput.setError("l'Email' est obligatoire");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("le Mot de passe est obligatoire");
            return;
        }
        if (key.isEmpty()) {
            keyInput.setError("la Clé est obligatoire");
            return;
        }
        if (phone.isEmpty()) {
            phoneInput.setError("le Numéro de Téléphone est obligatoire");
            return;
        }
        if (speciality.isEmpty()) {
            specialityInput.setError("la spécialité est obligatoire");
            return;
        }

        // create a new Doctor (signUp Doctor)
        fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                addDoctorToDB();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(DoctorSignUp.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addDoctorToDB() {
        uidDoctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(uidDoctor);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", uidDoctor);
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("key", key);
        map.put("specialty", speciality);
        map.put("fullSpecialty", speciality);
        map.put("address", "Votre Adresse");
        map.put("rate", "4.6");
        map.put("desc", "");
        map.put("phone", phone);
        map.put("profileImg", "");


        doctorRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(DoctorSignUp.this, DoctorDashboard.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {

        nameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        keyInput.setError(null);
        phoneInput.setError(null);
        specialityInput.setError(null);

    }

    private void getEditTexts() {

        nameInput = findViewById(R.id.nom);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        keyInput = findViewById(R.id.key);
        phoneInput = findViewById(R.id.phone_number);
        specialityInput = findViewById(R.id.speciality);

        name = nameInput.getEditText().getText().toString();
        email = emailInput.getEditText().getText().toString();
        password = passwordInput.getEditText().getText().toString();
        key = keyInput.getEditText().getText().toString();
        phone = phoneInput.getEditText().getText().toString();
        speciality = specialityInput.getEditText().getText().toString();


    }

    // because maybe some times the adapter will not show all the drop items
    @Override
    protected void onResume() {
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        specialities = getResources().getStringArray(R.array.specialities);
        arrayAdapter = new ArrayAdapter(this,R.layout.drop_down_item, specialities);

        autoCompleteTextView.setAdapter(arrayAdapter);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoctorSignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
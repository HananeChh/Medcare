package com.moncefadj.medcare.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.Doctor.DoctorDashboard;
import com.moncefadj.medcare.Doctor.DoctorSignUp;
import com.moncefadj.medcare.Patient.PatientHome;
import com.moncefadj.medcare.Patient.PatientSignUp;
import com.moncefadj.medcare.R;
import com.moncefadj.medcare.Underbar.underbar;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button signUpBtn;
    LinearLayout signBtnsLayout;
    Dialog dialog;
    FirebaseAuth fAuth;
    TextInputLayout email,password;
    String emailTxt,passwordTxt;
    Button forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginBtn = findViewById(R.id.connexion);
        signUpBtn = findViewById(R.id.creer_compte);
        signBtnsLayout = findViewById(R.id.sign_btns_layout);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgetPassword = findViewById(R.id.forget_password);



        // animation for button and tabLayout
        loginBtn.setTranslationX(300);
        signUpBtn.setTranslationX(300);
        signBtnsLayout.setTranslationX(300);


        loginBtn.setAlpha(0);
        signUpBtn.setAlpha(0);
        signBtnsLayout.setAlpha(0);


        loginBtn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400);
        signUpBtn.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400);
        signBtnsLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400);


        // ** Dialog ** to ask user if he is a doctor or patient when he want to signUp
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_white));
        }
        dialog.setCancelable(false);
        // we can make animation for Dialog by mentioned it in Style
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button patientBtn = dialog.findViewById(R.id.check_user_btn_patient);
        Button doctorBtn = dialog.findViewById(R.id.check_user_btn_doctor);
        ImageView cancelDialogBtn = dialog.findViewById(R.id.cancel_dialog_btn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
                dialog.show();
            }
        });
        patientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PatientSignUp.class);
                startActivity(intent);
                finish();
            }
        });
        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DoctorSignUp.class);
                startActivity(intent);
                finish();
            }
        });
        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void signInUser() {

        emailTxt = email.getEditText().getText().toString();
        passwordTxt = password.getEditText().getText().toString();

        clearErrors();   // this fun will clear the SetError (if it was appearing before) of all EditTexts

        Log.i("Connexion msg", emailTxt);
        Log.i("Connexion msg", passwordTxt);

        if (emailTxt.isEmpty()) {
            email.setError("l'Email est Obligatoire");
            return;  // we will leave the onClick fun
        }
        if (passwordTxt.isEmpty()) {
            password.setError("Mot de passe Obligatoire");
            return;
        }

        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                // search if current user is patient or doctor
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUid = user.getUid();
                DatabaseReference doctors = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors");
                doctors.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(currentUid)) {  // user is a patient
                            Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, PatientHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        email.setError(null);
        password.setError(null);
    }
}
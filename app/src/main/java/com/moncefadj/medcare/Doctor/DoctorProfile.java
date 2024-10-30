package com.moncefadj.medcare.Doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.DataClasses.DoctorData;
import com.moncefadj.medcare.ProfilePatient.PatientProfile;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class DoctorProfile extends AppCompatActivity {

    private Uri imageUri;
    private static final int IMAGE_REQUEST =2;


    AutoCompleteTextView autoCompleteTextView;
    String[] days;
    TextInputLayout daysInput;
    ArrayAdapter arrayAdapter;

    String uidDoctor;
    DatabaseReference doctorRef;
    DatabaseReference doctorTimeRef;

    String currentDay;
    String selectedDay;

    // Doctor profile Data
    TextView name,fullSpecialty,address,rate,phone,desc;
    ImageView profileImg;

    Dialog dialog;
    Switch aSwitch;
    TextInputLayout hourLayout, minuteLayout;
    Button confirmBtn,deleteBtn;
    ImageView cancelBtn;
    String hour,minute,available;
    ImageButton backImgBtn;

    FlexboxLayout flexboxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // profile data Hooks
        name = findViewById(R.id.profile_doctor_name);
        fullSpecialty = findViewById(R.id.profile_doctor_specialty);
        address = findViewById(R.id.profile_doctor_address);
        desc = findViewById(R.id.profile_doc_desc);
        rate = findViewById(R.id.rate_txt);
        phone = findViewById(R.id.profile_phone_txt);
        profileImg = findViewById(R.id.profile_doc_image);




        uidDoctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(uidDoctor);




        setProfileData();

        ImageView settingsBtn = findViewById(R.id.profile_doc_settings_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(DoctorProfile.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.doctor_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_item:
                                Intent intentLoadNewActivity = new Intent(DoctorProfile.this, EditDoctorProfile.class);
                                startActivity(intentLoadNewActivity);
                                return true;
                            case R.id.logout_item:
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(DoctorProfile.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        setProfileData();

        daysAutoCompleteTxt();

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String d = daysInput.getEditText().getText().toString();
                daysInput.setError(null);
                removeFlexBoxViews();
                showAllDayButtons(d);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageButton addBtn = findViewById(R.id.add_rdv_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                daysInput.setError(null);

                selectedDay = daysInput.getEditText().getText().toString();

                if (selectedDay.isEmpty()) {
                    daysInput.setError("Choisissez d'abord le jour");
                } else {
                    // activating input type when the doctor add new button
                    //hourLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_DATETIME);
                    //minuteLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_DATETIME);
                    hourLayout.getEditText().setEnabled(true);
                    minuteLayout.getEditText().setEnabled(true);

                    hourLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    minuteLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);

                    dialog.show();

                }
            }
        });

        // ** Dialog ** to ask user if he is a doctor or patient when he want to signUp
        dialog = new Dialog(DoctorProfile.this);
        dialog.setContentView(R.layout.custom_time_picker_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_white));
        }
        dialog.setCancelable(false);

        confirmBtn = dialog.findViewById(R.id.confirm_time_btn);
        deleteBtn = dialog.findViewById(R.id.delete_time_btn);
        cancelBtn = dialog.findViewById(R.id.cancel_dialog);
        hourLayout = dialog.findViewById(R.id.dialog_hour);
        minuteLayout = dialog.findViewById(R.id.dialog_minute);
        aSwitch = dialog.findViewById(R.id.available_switch);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hourLayout.setError(null);
                minuteLayout.setError(null);

                hour = hourLayout.getEditText().getText().toString();
                minute = minuteLayout.getEditText().getText().toString();



                if (aSwitch.isChecked()) {
                    available = "yes";
                } else available = "no";

                if ((!hour.isEmpty()) && (!minute.isEmpty())) {

                    int hourInt = Integer.parseInt(hour);
                    int minuteInt = Integer.parseInt(minute);

                    if (hourInt >= 0 && hourInt <= 23 && minuteInt >= 0 && minuteInt <= 60) {

                        String selectedDay = daysInput.getEditText().getText().toString();

                        addButtonToDB(selectedDay, hour + ":" + minute, available);
                        // like that it will appear the buttons without using showAllDayButtons and duplicate circles(buttons)
                        removeFlexBoxViews();
                        dialog.dismiss();
                    } else {
                        hourLayout.setError("Veuillez pas dépasser la limite");
                        minuteLayout.setError("Veuillez pas dépasser la limite");
                    }

                } else {
                    hourLayout.setError("Champ vide");
                    minuteLayout.setError("Champ vide");
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedDay = daysInput.getEditText().getText().toString();
                String hour = hourLayout.getEditText().getText().toString();
                String minute = minuteLayout.getEditText().getText().toString();

                doctorRef.child("rdvTimes").child(selectedDay).child(hour + ":" + minute).removeValue();
                removeFlexBoxViews();
                dialog.dismiss();
            }
        });


        backImgBtn = (ImageButton) findViewById(R.id.profile_doc_back_btn);
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(DoctorProfile.this, DoctorDashboard.class);
                startActivity(intentLoadNewActivity);
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


    }




    private void showAllDayButtons(String day) {

        DatabaseReference dayRef = doctorRef.child("rdvTimes").child(day);
        dayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot day : snapshot.getChildren()) {

                        String time = day.child("time").getValue(String.class);
                        String available = day.child("available").getValue(String.class);

                        showButton(time, available);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDay = "Samedi";

        switch (day) {
            case Calendar.SATURDAY:
                currentDay = "Samedi";
                break;
            case Calendar.SUNDAY:
                currentDay = "Dimanche";
                break;
            case Calendar.MONDAY:
                currentDay = "Lundi";
                break;
            case Calendar.TUESDAY:
                currentDay = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Mercredie";
                break;
            case Calendar.THURSDAY:
                currentDay = "Jeudi";
                break;
            case Calendar.FRIDAY:
                currentDay = "Vendredi";
                break;
        }
        return currentDay;
    }

    private void addButtonToDB(String day, String time, String isAvailable) {
        // add time to DB
        doctorTimeRef = doctorRef.child("rdvTimes").child(day).child(time);
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("available", isAvailable);

        doctorTimeRef.updateChildren(map);
    }

    private void showButton(String time, String isAvailable) {
        Button button = new Button(DoctorProfile.this);
        FlexboxLayout flexboxLayout = findViewById(R.id.flex_box);

        button.setText(time);
        button.setTextColor(getResources().getColor(R.color.white));
        if (isAvailable.equals("yes")) {
            button.setBackground(getResources().getDrawable(R.drawable.circle_button));
        } else button.setBackground(getResources().getDrawable(R.drawable.circle_button_grey));

        // we want to convert dp to pixel cause setWith and setHeight use only pixels
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int heightPixel = (int) (60 * scale + 0.5f);
        int widthPixel = (int) (65 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixel,heightPixel);
        params.setMargins(15,0,0,15);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // get hour and minute from time
                int indexOfPoints = time.indexOf(":");
                String hour = time.substring(0, indexOfPoints);
                String minute = time.substring(indexOfPoints + 1, time.length());

                hourLayout.getEditText().setText(hour);
                minuteLayout.getEditText().setText(minute);

                // he can't modify the hour and minute (he can delete it and add another one)
                hourLayout.getEditText().setEnabled(false);
                minuteLayout.getEditText().setEnabled(false);

                hourLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                minuteLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);

                if (isAvailable.equals("yes")) {
                    aSwitch.setChecked(true);
                    aSwitch.setTextOn("Oui");
                } else {
                    aSwitch.setChecked(false);
                    aSwitch.setTextOn("Non");
                }
            }
        });

        button.setLayoutParams(params);
        flexboxLayout.addView(button);

    }


    private void setProfileData() {

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    DoctorData doctorData = snapshot.getValue(DoctorData.class);

                    String img =doctorData.getProfileImg();
                    Picasso.get().load(img).into(profileImg);

                    name.setText(doctorData.getName());
                    fullSpecialty.setText(doctorData.getFullSpecialty());
                    address.setText(doctorData.getAddress());
                    desc.setText(doctorData.getDesc());
                    rate.setText(doctorData.getRate());
                    phone.setText(doctorData.getPhone());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void daysAutoCompleteTxt() {

        daysInput = findViewById(R.id.days_input);

        autoCompleteTextView = findViewById(R.id.autoCompleteText);
        days = getResources().getStringArray(R.array.days);
        arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_item, days);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.clearFocus();

    }

    public void removeFlexBoxViews() {
        FlexboxLayout flexboxLayout = findViewById(R.id.flex_box);
        flexboxLayout.removeAllViews();
    }







    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();

            uploadImage();
        }
    }

    private String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("image is uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Log.d("DownloadUrl" , url);
                            pd.dismiss();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("profileImg", url);
                            doctorRef.updateChildren(hashMap);


                            Toast.makeText(DoctorProfile.this, "Image upload successfull", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


}
package com.moncefadj.medcare.ProfilePatient;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.DataClasses.PatientData;
import com.moncefadj.medcare.Medicaments.liste_medicaments;
import com.moncefadj.medcare.Patient.PatientHome;
import com.moncefadj.medcare.Patient.PatientSignUp;
import com.moncefadj.medcare.PatientSearch.Search;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

public class PatientProfile extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    Toast toast;
    private Button play;

    private FirebaseUser user;
    private DatabaseReference reference,referencee;
    private String userID;

    ImageView back;
    private ImageView add,logout,photo;
    private Uri imageUri;
    private static final int IMAGE_REQUEST =2;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_patient_1);


        play= (Button) findViewById(R.id.play);
        logout=(ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(PatientProfile.this,"logout",Toast.LENGTH_LONG).show();
                startActivity(new Intent(PatientProfile.this, LoginActivity.class));
                finish();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent otherAct =new Intent(getApplicationContext(), EditPatientProfile.class);
                startActivity(otherAct);

            }
        });




       /* Intent intent= getIntent();
        String message =intent.getStringExtra(EditPatientProfile.EXTRA_MESSAGE);
        EditText editText=findViewById(R.id.n);
        editText.setText(message);*/


        //underbar
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_med));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_search_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_profil));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override

            public void onShowItem(MeowBottomNavigation.Model item) {
                Intent intent = null;
                switch (item.getId()) {
                    case 1:
                        intent = new Intent(getApplicationContext(), PatientHome.class);
                        startActivity(intent);
                        break;
                    case 2: intent = new Intent(getApplicationContext(), liste_medicaments.class);
                        startActivity(intent);
                        break;
                    case 3: intent = new Intent(getApplicationContext(), Search.class);
                        startActivity(intent);
                        break;
                    default:break;


                    //  case 4: fragment=new ProfilFragment();
                    //  break;*/

                }
            }

        });
        boolean enableAnimation;
        //set home fragment initialy selected
        bottomNavigation.show(4, enableAnimation = true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //display toast

            }

            ;
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //display toast


            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients");
        userID =user.getUid();
        referencee = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients").child(userID).child("BirthDay");



        final TextView fullnametextview =(TextView) findViewById(R.id.b);
        final TextView emailtextview =(TextView) findViewById(R.id.pemail);
        final TextView numerotextview =(TextView) findViewById(R.id.n);
        final TextView datetextview =(TextView) findViewById(R.id.e);

        add=(ImageView)findViewById(R.id.a);
        photo=(ImageView)findViewById(R.id.aa);

        getInfo(userID, fullnametextview, emailtextview, numerotextview, datetextview);
        swipeRefreshLayout = findViewById(R.id.swiper_linear);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getInfo(userID, fullnametextview, emailtextview, numerotextview, datetextview);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getInfo(String userID, TextView fullnametextview, TextView emailtextview, TextView numerotextview, TextView datetextview) {

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PatientData userprofile = snapshot.getValue(PatientData.class);
                if (userprofile !=null) {
                    String fullname = userprofile.getName();
                    String email= userprofile.getEmail();
                    String numero=userprofile.getPhone();
                    String img =userprofile.getProfile();

                    fullnametextview.setText(fullname);
                    emailtextview.setText(email);
                    numerotextview.setText(numero);
                    Picasso.get().load(img).into(add);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referencee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PatientData userprofile = snapshot.getValue(PatientData.class);
                if (userprofile !=null) {
                    String date=userprofile.getDay();
                    String month=userprofile.getMonth();
                    String year=userprofile.getYear();

                    datetextview.setText(date+" / "+month+" / "+year);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add=(ImageView)findViewById(R.id.a);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

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
                            Toast.makeText(PatientProfile.this, "Image upload successfull", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
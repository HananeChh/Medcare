package com.moncefadj.medcare.HelperClasses;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.DataClasses.DoctorData;
import com.moncefadj.medcare.DataClasses.DoctorDataForHomePatient;
import com.moncefadj.medcare.Patient.DoctorProfileFromPatientHome;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class doctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    ArrayList<DoctorDataForHomePatient> DoctorsList = new ArrayList<>();

    public doctorsAdapter(Context context) {
        this.context = context;
    }

    public void setItems (ArrayList <DoctorDataForHomePatient> doctors){
        DoctorsList.addAll(doctors);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_doctors_adapter, parent, false);
        return new doctorsViewHolder(view);
    }
    public void filterlist(ArrayList<DoctorDataForHomePatient> filterlist) {
        DoctorsList = filterlist;
        notifyDataSetChanged();

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        doctorsViewHolder viewHolder = (doctorsViewHolder) holder;

        DoctorDataForHomePatient doctors = DoctorsList.get(position);
        viewHolder.name.setText(doctors.getName());
        viewHolder.spec.setText(doctors.getFullSpecialty());
        viewHolder.adr.setText(doctors.getAddress());
        viewHolder.phone.setText(doctors.getPhone());
        viewHolder.id.setText(doctors.getId());


//pour recherche
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorProfileFromPatientHome.class);
            intent.putExtra("Name",doctors.getName());
            intent.putExtra("Spec",doctors.getFullSpecialty());
            intent.putExtra("Adr",doctors.getAddress());
            intent.putExtra("Phn",doctors.getPhone());
            intent.putExtra("Dsc",doctors.getDesc());
            intent.putExtra("Id",doctors.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return DoctorsList.size();
    }

}
package com.moncefadj.medcare.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.DataClasses.DoctorDataForHomePatient;
import com.moncefadj.medcare.Doctor.DoctorProfile;
import com.moncefadj.medcare.Patient.DoctorProfileFromPatientHome;
import com.moncefadj.medcare.PatientSearch.Search;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<DoctorDataForHomePatient> Doctors = new ArrayList<>();
    private ArrayList<DoctorDataForHomePatient> full = new ArrayList<>();
    Context context;


    public adapter(Context context) {
        this.context = context;
    }


    public void setItems(ArrayList<DoctorDataForHomePatient> doctors) {
        Doctors.clear();
        Doctors.addAll(doctors);
        full.addAll(doctors);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.verow, parent, false);
        return new row(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        row viewHolder = (row) holder;
        DoctorDataForHomePatient doctors = Doctors.get(position);
        viewHolder.namedoc.setText(doctors.getName());
        viewHolder.specdoc.setText(doctors
                .getSpecialty());
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
        return Doctors.size();
    }


    @Override
    public Filter getFilter() {
        return search_Filter;
    }

    private Filter search_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DoctorDataForHomePatient> filteredlist = new ArrayList<>();
            if (charSequence == null | charSequence.length() == 0) {
                filteredlist.addAll(full);
            } else {
                String nom = charSequence.toString().toLowerCase().trim();
                for (DoctorDataForHomePatient item : full) {
                    if (item.getName().toLowerCase().contains(nom)) {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Doctors.clear();
            Doctors.addAll((Collection<? extends DoctorDataForHomePatient>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public ArrayList<DoctorDataForHomePatient> getDoctors() {
        return Doctors;
    }
}






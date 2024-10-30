package com.moncefadj.medcare.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.R;

public class doctorsViewHolder extends RecyclerView.ViewHolder {

        public TextView name, spec, phone, descry, adr, id;
        public ImageView img;


    public doctorsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nom_medecin);
        spec = itemView.findViewById(R.id.speciality1);
        adr = itemView.findViewById(R.id.adresse_medecin);
        phone = itemView.findViewById(R.id.numero_medecin);
        descry = itemView.findViewById(R.id.description_medecin);
        id = itemView.findViewById(R.id.id_medecin);
        img = itemView.findViewById(R.id.image_medecin);
    }
}
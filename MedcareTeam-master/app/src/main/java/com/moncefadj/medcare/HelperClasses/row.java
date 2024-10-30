package com.moncefadj.medcare.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.R;

public class row extends RecyclerView.ViewHolder {
   public TextView namedoc,specdoc;
   public ImageView imageView12;
    public row( View itemView) {
        super(itemView);
        namedoc=itemView.findViewById(R.id.medcin);
        imageView12=itemView.findViewById(R.id.icon);
        specdoc=itemView.findViewById(R.id.space);

    }
}

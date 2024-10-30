package com.moncefadj.medcare.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.moncefadj.medcare.R;

public class SlidesAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlidesAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.onboarding_b_img,
            R.drawable.onboarding_img,
            R.drawable.onboarding_c_img

    };

    int titles[] = {
            R.string.gagner_temps_title,
            R.string.trouver_med_title,
            R.string.prendre_med_title
    };

    int descriptions[] = {
            R.string.gagner_temps_desc,
            R.string.trouver_med_desc,
            R.string.prendre_med_desc
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides, container, false);

        ImageView imageView = view.findViewById(R.id.slide_img);
        TextView title = view.findViewById(R.id.slide_title);
        TextView desc = view.findViewById(R.id.slide_desc);

        imageView.setImageResource(images[position]);
        title.setText(titles[position]);
        desc.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

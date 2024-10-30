package com.moncefadj.medcare.Medicaments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.moncefadj.medcare.R;

import java.util.Calendar;

public class TimePickerFragment  extends DialogFragment {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //return super.onCreateDialog(savedInstanceState);
        //< start with actual Time >
        Calendar cal = Calendar.getInstance();
        int intHour = cal.get(Calendar.HOUR);
        int intMinute = cal.get(Calendar.MINUTE);

        //</ start with actual Time >
        TimePickerDialog mytimepicker = new TimePickerDialog(getActivity(), R.style.AppCompatDialogStyle, (TimePickerDialog.OnTimeSetListener) getActivity(), intHour, intMinute, true);
        return mytimepicker;

    }
}

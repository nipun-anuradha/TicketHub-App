package com.javainstitute.tickethub;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    private OnDateSetListener onDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(year, month, day);
        }
    }

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        this.onDateSetListener = listener;
    }
}
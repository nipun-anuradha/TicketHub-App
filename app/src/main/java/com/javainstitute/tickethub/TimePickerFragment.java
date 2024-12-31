package com.javainstitute.tickethub;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    private OnTimeSetListener onTimeSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker.
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it.
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public interface OnTimeSetListener {
        void onTimeSet(int hourOfDay, int minute);
    }


    public void setOnTimeSetListener(OnTimeSetListener listener) {
        this.onTimeSetListener = listener;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (onTimeSetListener != null) {
            onTimeSetListener.onTimeSet(hourOfDay, minute);
        }
    }

}

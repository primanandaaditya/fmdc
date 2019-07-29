package id.co.cp.mdc.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import id.co.cp.mdc.R;


/**
 * Created by jahid on 12/10/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public DatePickerFragment() {
    }

    private String txtDate;

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        int bln = view.getMonth()+1;
        TextView tv4= (TextView) getActivity().findViewById(R.id.zTxtDate4);
        TextView tv1= (TextView) getActivity().findViewById(R.id.zTxtDate1);
        TextView tv2= (TextView) getActivity().findViewById(R.id.zTxtDate);
        String bulan = (bln < 10 ? "0" : "") + bln;
        String tanggal = (view.getDayOfMonth() < 10 ? "0" : "") + view.getDayOfMonth();
        if (tv1!=null) {
            tv1.setText(view.getYear() + "-" + bulan + "-" + tanggal);
        }
        if (tv2!=null) {
            tv2.setText(view.getYear() + "-" + bulan + "-" + tanggal);
        }
        if (tv4!=null) {
            tv4.setText(view.getYear() + "-" + bulan + "-" + tanggal);
        }
        if(!txtDate.equals("")){
            TextView tv = (TextView) getActivity().findViewById(getResources().getIdentifier(txtDate, "id", getActivity().getPackageName()));
            tv.setText(view.getYear() + "-" + bulan + "-" + tanggal);
        }
    }
}
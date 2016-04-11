package works.tonny.mobile.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by tonny on 2015/7/19.
 */
public class DateDialogFragment extends DialogFragment {

    private int year;
    private int month;
    private int day;


    private DatePickerDialog.OnDateSetListener listener;

    public DateDialogFragment() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (year == 0) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
}

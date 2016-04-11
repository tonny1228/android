package works.tonny.mobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import works.tonny.mobile.R;

/**
 * Created by tonny on 2015/7/8.
 */
public class SimpleDialog extends AlertDialog {
    private String title;

    public SimpleDialog(Context context, int theme) {
        super(context, theme);
    }

    public SimpleDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_dialog);
        LinearLayout view = (LinearLayout) findViewById(R.id.dialog_parent);
        TextView text = new TextView(getContext(), null, R.style.simple_dialog_text);
        text.setText(title);
        view.addView(text);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = (String) title;
    }
}

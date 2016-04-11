package works.tonny.mobile.autobackup;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by tonny on 2016/2/23.
 */
public class ProcessDialog extends AlertDialog {

    private TextView view;

    public ProcessDialog(Context context, int theme) {
        super(context, theme);
    }

    public ProcessDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_dialog);
        view = (TextView) findViewById(R.id.message);
    }

    public void show(String text) {
        view.setText(text);
    }
}

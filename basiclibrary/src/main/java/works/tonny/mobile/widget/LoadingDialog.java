package works.tonny.mobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import works.tonny.mobile.R;

/**
 * Created by tonny on 2015/7/23.
 */
public class LoadingDialog extends AlertDialog {

    public static LoadingDialog newInstance(Context context) {
        return new LoadingDialog(context, R.style.dialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }

}

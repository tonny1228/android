package works.tonny.mobile.demo6.match;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import works.tonny.mobile.demo6.R;

/**
 * Created by tonny on 2016/3/21.
 */
public class ApplySearchDialog extends AlertDialog {

    public ApplySearchDialog(Context context, int theme) {
        super(context, theme);
    }

    public ApplySearchDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_applu_search_dialog);
    }
}

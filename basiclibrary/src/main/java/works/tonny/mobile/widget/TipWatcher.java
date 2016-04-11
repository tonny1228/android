package works.tonny.mobile.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by tonny on 2015/12/22.
 */
public class TipWatcher implements TextWatcher {

    View tip;

    public TipWatcher(View tip) {
        this.tip = tip;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            tip.setVisibility(View.GONE);
        else
            tip.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

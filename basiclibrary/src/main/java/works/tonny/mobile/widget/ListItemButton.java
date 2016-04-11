package works.tonny.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by tonny on 2015/7/6.
 */
public class ListItemButton extends Button {
    private int index = -1;


    public ListItemButton(Context context) {
        super(context);
    }

    public ListItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



}
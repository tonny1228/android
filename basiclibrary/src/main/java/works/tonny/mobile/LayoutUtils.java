package works.tonny.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import works.tonny.mobile.widget.ScrollView;
import works.tonny.mobile.widget.SimpleDialog;


/**
 * Created by tonny on 2015/7/4.
 */
public class LayoutUtils {

    private static float scale;

    static void init(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
    }

    /**
     * dip to px
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取滚动的位置，支持scrollview 和listview
     *
     * @param view scrollview或listview
     * @return
     */
    public static int getScrollY(View view) {
        if (view instanceof ScrollView) {
            return ((ScrollView) view).getPositionY();
        } else if (view instanceof AbsListView) {
            View c = ((AbsListView) view).getChildAt(0);
            if (c == null) {
                return 0;
            }
            int firstVisiblePosition = ((AbsListView) view).getFirstVisiblePosition();
            int top = c.getTop();
            return -top + firstVisiblePosition * c.getHeight();
        } else {
            return 0;
        }
    }


    public static AlertDialog alert(Context context, String t) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, works.tonny.mobile.R.style.dialog);
//        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.simple_dialog, null);
//        final ViewGroup parent = (ViewGroup) view.findViewById(R.id.dialog_parent);
//        TextView text = new TextView(context);
//        text.setText(t);
//        text.setTextColor(Color.parseColor("#ffffff"));
//        parent.addView(text);
//        AlertDialog dialog = builder.setView(view).create();
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.8f;
//        window.setAttributes(lp);
        SimpleDialog dialog = new SimpleDialog(context, R.style.dialog);
        dialog.setTitle(t);
        return dialog;
    }
}

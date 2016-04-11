package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tonny on 2015/7/5.
 */
public class TitleHelper {
    private Activity activity;

    private ViewGroup parent;

    public static TitleHelper getInstance(Activity activity) {
        return new TitleHelper(activity);
    }

    public static TitleHelper getInstance(ViewGroup parent) {
        return new TitleHelper(parent);
    }

    private TitleHelper(Activity activity) {
        this.activity = activity;
    }

    public TitleHelper(ViewGroup parent) {
        this.parent = parent;
    }

    public TitleHelper enableBack() {
        View view = activity.findViewById(R.id.goback);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return this;
    }

    public TitleHelper setTitle(String titleText) {
        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText(titleText);
        return this;
    }

    public TitleHelper setButton(String text, View.OnClickListener onClickListener) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.list_tool_button);
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextAppearance(getContext(), R.style.activity_title_button);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layout.addView(textView, lp1);
        layout.setOnClickListener(onClickListener);
        return this;
    }

    public TitleHelper setButton(int imageId, View.OnClickListener onClickListener) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.list_tool_button);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(imageId);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layout.addView(imageView, lp1);
        layout.setOnClickListener(onClickListener);
        return this;
    }


    private Context getContext() {
        if (activity != null) {
            return activity;
        }
        if (parent != null) {
            return parent.getContext();
        }
        return null;
    }


    private View findViewById(int id) {
        if (activity != null) {
            return activity.findViewById(id);
        }
        if (parent != null) {
            return parent.findViewById(id);
        }
        return null;
    }
}

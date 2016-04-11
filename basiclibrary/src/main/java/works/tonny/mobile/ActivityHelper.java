package works.tonny.mobile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import works.tonny.mobile.utils.ImageRequest;
import works.tonny.mobile.utils.ImageTools;
import works.tonny.mobile.widget.DateDialogFragment;
import works.tonny.mobile.widget.TipWatcher;

/**
 * 页面组件工具集
 * Created by tonny on 2015/7/5.
 */
public class ActivityHelper {
    private Activity activity;

    private ViewGroup parent;


    public static ActivityHelper getInstance(Activity activity) {
        return new ActivityHelper(activity);
    }

    public static ActivityHelper getInstance(ViewGroup parent) {
        return new ActivityHelper(parent);
    }

    private ActivityHelper(Activity activity) {
        this.activity = activity;
    }

    public ActivityHelper(ViewGroup parent) {
        this.parent = parent;
    }


    /**
     * 使用后退按钮
     *
     * @return
     */
    public ActivityHelper enableBack(int titleGoBackIcon) {
        View view = activity.findViewById(titleGoBackIcon);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return this;
    }


    /**
     * 设置标题栏右侧按钮
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public ActivityHelper setButton(int container, String text, int style, View.OnClickListener onClickListener) {
        ViewGroup layout = (ViewGroup) findViewById(container);
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextAppearance(getContext(), style);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layout.addView(textView, lp1);
        layout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置标题栏右侧按钮
     *
     * @param imageId
     * @param onClickListener
     * @return
     */
    public ActivityHelper setButton(int container, int imageId, View.OnClickListener onClickListener) {
        RelativeLayout layout = (RelativeLayout) findViewById(container);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(imageId);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layout.addView(imageView, lp1);
        layout.setOnClickListener(onClickListener);
        return this;
    }


    /**
     * 设置对象文本
     *
     * @param id
     * @param text
     * @return
     */
    public ActivityHelper setText(int id, String text) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        } else if (view instanceof EditText) {
            ((EditText) view).setText(text);
        }
        return this;
    }

    public void addTextChangedListener(int view, int tip) {
        TextView v = (TextView) findViewById(view);
        TextView t = (TextView) findViewById(tip);
        v.addTextChangedListener(new TipWatcher(t));
    }


    /**
     * 设置对象文本
     *
     * @param id
     * @param visible s
     * @return
     */
    public ActivityHelper setVisible(int id, boolean visible) {
        View view = findViewById(id);
        if (visible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        return this;
    }


    /**
     * 设置对象文本
     *
     * @param id
     * @param visible s
     * @return
     */
    public ActivityHelper enable(int id, boolean enable) {
        View view = findViewById(id);
        view.setClickable(enable);
        return this;
    }


    public String getValue(int id) {
        View view = findViewById(id);
        if (view instanceof CheckBox) {
            CheckBox checkbox = (CheckBox) view;
            if (checkbox.isChecked())
                return checkbox.getText().toString();
            else
                return null;
        }
        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (spinner.getSelectedItem() == null) {
                return "";
            }
            return spinner.getSelectedItem().toString();
        }
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }

        return null;
    }

    /**
     * 设置view的点击事件
     *
     * @param id
     * @param onClickListener
     * @return
     */
    public ActivityHelper setOnClickListener(int id, View.OnClickListener onClickListener) {
        View view = findViewById(id);
        view.setOnClickListener(onClickListener);
        return this;
    }

    public ActivityHelper makeItDate(int id, final Calendar calendar) {
        final TextView text = (TextView) findViewById(id);
        final DateDialogFragment fragment = new DateDialogFragment();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        calendar.set(year, month, day);
                        text.setText(year + "-" + (month + 1) + "-" + day);
                    }
                });
                fragment.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                fragment.show(getActivity().getFragmentManager(), "dateDialog");
            }
        });

        return this;
    }


    /**
     * 设置view的点击事件
     *
     * @param id
     * @param clazz
     * @return
     */
    public ActivityHelper setOnClickListener(int id, final Class clazz) {
        View view = findViewById(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), clazz);
                getContext().startActivity(intent);
            }
        });
        return this;
    }


    /**
     * 设置view的点击事件
     *
     * @param id
     * @param clazz
     * @return
     */
    public ActivityHelper setOnClickListener(int id, final Class clazz, final Map<String, Object> params) {
        View view = findViewById(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), clazz);
                if (params != null)
                    for (String key : params.keySet()) {
                        Object o = params.get(key);
                        if (o instanceof String) {
                            intent.putExtra(key, (String) o);
                        }
                        if (o instanceof Integer) {
                            intent.putExtra(key, (Integer) o);
                        }
                        if (o instanceof Long) {
                            intent.putExtra(key, (Long) o);
                        }
                        if (o instanceof Date) {
                            intent.putExtra(key, (Date) o);
                        }

                        if (o instanceof Boolean) {
                            intent.putExtra(key, (Boolean) o);
                        }
                        if (o instanceof Float) {
                            intent.putExtra(key, (Float) o);
                        }
                        if (o instanceof Serializable) {
                            intent.putExtra(key, (Serializable) o);
                        }
                    }
                getContext().startActivity(intent);
            }
        });
        return this;
    }


    public static void setImage(ImageView view, String file) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        Bitmap bitmap = ImageTools.decodeSampledBitmapFromResource(file, view.getMeasuredWidth(), view.getMeasuredHeight());
//        Bitmap bitmap = ImageTools.tryGetBitmap(new File(file), view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setImageBitmap(bitmap);
    }


    public ActivityHelper setImage(int id, String uri) {
        if (uri == null) {
            return this;
        }
        final ImageView view = (ImageView) findViewById(id);
        if (uri.toLowerCase().startsWith("http://")) {
            new ImageRequest(new ImageRequest.OnRequested() {
                @Override
                public void execute(File file) {
                    setImage(view, file.getAbsolutePath());
                }
            }).execute(uri);
        } else {
            Bitmap bitmap = ImageTools.tryGetBitmap(new File(uri), view.getMeasuredWidth(), view.getMeasuredHeight());
            view.setImageBitmap(bitmap);
        }
        return this;
    }

    public ActivityHelper setImage(int id, int resource) {
        ImageView view = (ImageView) findViewById(id);
        view.setImageResource(resource);
        return this;
    }

    private Activity getActivity() {
        if (activity != null) {
            return activity;
        }
        return null;
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

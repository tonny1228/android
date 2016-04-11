package works.tonny.mobile.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by tonny on 2015/7/6.
 */
public class CustomAdapter extends SimpleAdapter {

    private Map<Integer, View.OnClickListener> viewClickListener;

    private Map<String, Integer> mapping;


    public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = super.getView(position, view, parent);
            if (viewClickListener == null) {
                return view;
            }
            ViewGroup layout = (ViewGroup) view;
            Map<String, Object> data = (Map<String, Object>) getItem(position);
            data.put("_index", position);
            View index = view.findViewById(mapping.get("_index"));
            if (index != null && index instanceof TextView) {
                ((TextView) index).setText(String.valueOf(position));
            }

            for (Integer id : viewClickListener.keySet()) {
                View item = view.findViewById(id);
                item.setOnClickListener(viewClickListener.get(id));
            }
            return view;
        } else {
            Map<String, Object> data = (Map<String, Object>) getItem(position);
            data.put("_index", position);
            if (mapping.containsKey("_index")) {
                View index = view.findViewById(mapping.get("_index"));
                if (index != null && index instanceof TextView) {
                    ((TextView) index).setText(String.valueOf(position));
                }
            }
            return super.getView(position, view, parent);
        }
    }

    public void setMapping(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }

    public void setViewClickListener(Map<Integer, View.OnClickListener> viewClickListener) {
        this.viewClickListener = viewClickListener;
    }
}

package works.tonny.mobile.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.MD5;

/**
 * Created by tonny on 2015/7/9.
 */
public class MultiViewAdapter extends BaseAdapter implements Filterable {
    private int[] mTo;

    private String[] mFrom;

    private ViewBinder mViewBinder;

    private List<IDLinkedHashMap> mData;

    private int mResource;

    private int mDropDownResource;

    private LayoutInflater mInflater;

    private SimpleFilter mFilter;

    private ArrayList<Map<String, ?>> mUnfilteredData;

    private Map<String, Integer> mapping;

    private Map<Integer, View.OnClickListener> viewClickListener;

    private Map<Integer, View> views = new HashMap<>();

    private boolean editMode;


    public MultiViewAdapter(Context context, List<IDLinkedHashMap> data,
                            int resource, Map<String, Integer> mapping) {
        mData = data;
        mResource = mDropDownResource = resource;
        this.mapping = mapping;
        mFrom = new String[mapping.size()];
        mTo = new int[mapping.size()];
        int i = 0;
        for (String key : mapping.keySet()) {
            mFrom[i] = key;
            mTo[i++] = mapping.get(key);
        }
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return mData.size();
    }


    public List<IDLinkedHashMap> getData() {
        return mData;
    }


    public void addData(IDLinkedHashMap data) {
        if (mData.contains(data))
            mData.remove(data);
        mData.add(data);
    }

    public void insertData(IDLinkedHashMap data) {
        if (mData.contains(data))
            mData.remove(data);
        mData.add(0, data);
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    /**
     * 根据data创建view
     *
     * @param position
     * @param convertView
     * @param parent
     * @param resource
     * @return
     */
    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View v;
        if (!views.containsKey(position)) {
            if (mapping != null && mapping.containsKey("_layout_id")) {
                v = mInflater.inflate(mapping.get("_layout_id"), parent, false);
            } else if (mapping != null && mapping.containsKey("_view") && mData.get(position).containsKey("_view")) {
                v = (View) mData.get(position).get("_view");
            } else {
                v = mInflater.inflate(mResource, parent, false);
            }

            if (viewClickListener != null) {
                ViewGroup layout = (ViewGroup) v;

                for (Integer id : viewClickListener.keySet()) {
                    View item = v.findViewById(id);
                    item.setOnClickListener(viewClickListener.get(id));
                }
            }
            views.put(position, v);
        } else {
            v = views.get(position);
        }

        Map<String, Object> data = (Map<String, Object>) getItem(position);
        data.put("_index", position);
        if (mapping.containsKey("_index")) {
            View index = v.findViewById(mapping.get("_index"));
            if (index != null && index instanceof TextView) {
                ((TextView) index).setText(String.valueOf(position));
            }
        }

        bindView(position, v);

        return v;
    }

    /**
     * 根据data创建view
     *
     * @param position
     * @param convertView
     * @param parent
     * @param resource
     * @return
     */
    private View createViewFromResource1(int position, View convertView,
                                         ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            if (mapping != null && mapping.containsKey("_layout_id")) {
                v = mInflater.inflate(mapping.get("_layout_id"), parent, false);
            } else if (mapping != null && mapping.containsKey("_view") && mData.get(position).containsKey("_view")) {
                v = (View) mData.get(position).get("_view");
            } else {
                v = mInflater.inflate(mResource, parent, false);
            }

            if (viewClickListener != null) {
                ViewGroup layout = (ViewGroup) v;

                for (Integer id : viewClickListener.keySet()) {
                    View item = v.findViewById(id);
                    item.setOnClickListener(viewClickListener.get(id));
                }
            }
        } else {
            v = convertView;
        }

        Map<String, Object> data = (Map<String, Object>) getItem(position);
        data.put("_index", position);
        if (mapping.containsKey("_index")) {
            View index = v.findViewById(mapping.get("_index"));
            if (index != null && index instanceof TextView) {
                ((TextView) index).setText(String.valueOf(position));
            }
        }

        bindView(position, v);

        return v;
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, View, ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    private void bindView(int position, View view) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = mViewBinder;
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (holders == null) {
                            holders = new HashMap<>();
                        }

                        final Checkable checkable = (Checkable) v;
                        if (!holders.containsKey(position)) {
                            holders.put(position, new CheckHolder(checkable));
                            if (checkable instanceof CheckBox) {
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dataSet.put("check", checkable.isChecked());
                                    }
                                });
                            }
                        }
                        if (editMode) {
                            v.setVisibility(View.VISIBLE);
                        }
                        if (data == null) {
                            checkable.setChecked(false);
                        } else if (data instanceof Boolean) {
                            checkable.setChecked((Boolean) dataSet.get("check"));
                        } else if (v instanceof TextView) {
                            setViewText((TextView) v, text);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " +
                                    (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else {
                            setViewImage((ImageView) v, position, from[i], text);
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
    }


    private Map<Integer, CheckHolder> holders;


    static class CheckHolder {
        Checkable selected;

        CheckHolder(Checkable selected) {
            this.selected = selected;
        }
    }


    /**
     * Returns the {@link ViewBinder} used to bind data to views.
     *
     * @return a ViewBinder or null if the binder does not exist
     */
    public ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     *
     * @param viewBinder the binder used to bind data to views, can be null to
     *                   remove the existing binder
     * @see #getViewBinder()
     */
    public void setViewBinder(ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     * <p/>
     * if the supplied data is an int or Integer.
     *
     * @param v     ImageView to receive an image
     * @param value the value retrieved from the data set
     */
    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     * <p/>
     * By default, the value will be treated as an image resource. If the
     * value cannot be used as an image resource, the value is used as an
     * image Uri.
     * <p/>
     * This method is called instead of {@link #setViewImage(ImageView, int)}
     * if the supplied data is not an int or Integer.
     *
     * @param v     ImageView to receive an image
     * @param value the value retrieved from the data set
     * @see #setViewImage(ImageView, int)
     */
    public void setViewImage(ImageView v, int p, String from, String value) {
        if (value.matches("\\d+"))
            v.setImageResource(Integer.parseInt(value));
        else if (value.startsWith("/")) {
            ActivityHelper.setImage(v, value);
//            Bitmap bitmap = ImageTools.tryGetBitmap(new File(value), v.getMeasuredWidth(), v.getMeasuredHeight());
//            v.setImageBitmap(bitmap);

//            v.setImageURI(Uri.parse(value));
        } else {
            new ImageRequest(v, p, from).execute(value);
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to a TextView.
     *
     * @param v    TextView to receive text
     * @param text the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SimpleFilter();
        }
        return mFilter;
    }

    /**
     * This class can be used by external clients of SimpleAdapter to bind
     * values to views.
     * <p/>
     * You should use this class to bind values to views that are not
     * directly supported by SimpleAdapter or to change the way binding
     * occurs for views supported by SimpleAdapter.
     *
     * @see SimpleAdapter#setViewImage(ImageView, int)
     * @see SimpleAdapter#setViewImage(ImageView, String)
     * @see SimpleAdapter#setViewText(TextView, String)
     */
    public static interface ViewBinder {
        /**
         * Binds the specified data to the specified view.
         * <p/>
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view               the view to bind the data to
         * @param data               the data to bind to the view
         * @param textRepresentation a safe String representation of the supplied data:
         *                           it is either the result of data.toString() or an empty String but it
         *                           is never null
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Object data, String textRepresentation);
    }

    /**
     * <p>An array filters constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class SimpleFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Map<String, ?>> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(count);

                for (int i = 0; i < count; i++) {
                    Map<String, ?> h = unfilteredValues.get(i);
                    if (h != null) {

                        int len = mTo.length;

                        for (int j = 0; j < len; j++) {
                            String str = (String) h.get(mFrom[j]);

                            String[] words = str.split(" ");
                            int wordCount = words.length;

                            for (int k = 0; k < wordCount; k++) {
                                String word = words[k];

                                if (word.toLowerCase().startsWith(prefixString)) {
                                    newValues.add(h);
                                    break;
                                }
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mData = (List<IDLinkedHashMap>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void setViewClickListener(Map<Integer, View.OnClickListener> viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public void setMapping(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }


    class ImageRequest extends AsyncTask<String, Integer, File> {
        private ImageView view;

        private int position;

        private String from;

        public ImageRequest(ImageView view, int position, String from) {
            this.view = view;
            this.position = position;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            if (view.getDrawable() == null)
                view.setImageResource(R.drawable.empty);
        }

        @Override
        protected File doInBackground(String... params) {
            String url = Application.getUrl(params[0]);
            File dir = FileUtils.getExternalStorageDirectory("/tonny/webcache/");
            File f = new File(dir.getAbsolutePath(), MD5.encode(url));
            if (f.exists()) {
                return f;
            }
            HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, url);
            try {
                request.executeToFile(f);
                return f;
            } catch (Exception e) {
                Log.error(e);
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(File s) {
            if (mData.size() <= position) {
                return;
            }
            ActivityHelper.setImage(view, s.getAbsolutePath());
            mData.get(position).put(from, s);
        }
    }


    public List<Integer> getChecked() {
        List<Integer> checked = new ArrayList<>();
        for (Integer integer : holders.keySet()) {
            if (holders.get(integer).selected.isChecked()) {
                checked.add(integer);
            }
        }
        return checked;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        for (Integer integer : holders.keySet()) {

//            View view = views.get(integer);
            CheckHolder holder = (CheckHolder) holders.get(integer);
            View check = (View) holder.selected;
            if (editMode)
                check.setVisibility(View.VISIBLE);
            else
                check.setVisibility(View.INVISIBLE);
        }
    }
}

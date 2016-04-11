package works.tonny.mobile.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.R;

/**
 * Created by tonny on 2015/8/9.
 */
public class DataView extends Fragment {
    private static final String DATA = "data";

    private static final String ITEM_LAYOUT_ID = "itemLayoutId";

    private ViewGroup view;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MultiViewAdapter mAdapter;


    /**
     * 数据
     */
    private List<IDLinkedHashMap> data;

    /**
     * 每行的组件layout id
     */
    private int itemLayoutId;


    /**
     * map与每条数据组件 id的对应关系
     */
    private Map<String, Integer> mapping;


    /**
     * 数据点击接口
     */
    private ItemClickListener itemClickListener;


    /**
     * 通过数据创建实例
     *
     * @param data
     * @return
     */
    public static DataView newInstance(Object data, int itemLayoutId) {
        DataView fragment = new DataView();
        Bundle args = new Bundle();
        args.putSerializable(DATA, (Serializable) toList(data));
        args.putInt(ITEM_LAYOUT_ID, itemLayoutId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //初始化数据
            data = (List<IDLinkedHashMap>) getArguments().getSerializable(DATA);
            if (data == null) {
                data = new ArrayList<IDLinkedHashMap>();
            }

            itemLayoutId = getArguments().getInt(ITEM_LAYOUT_ID, R.layout.fragment_item_grid);
        }
    }

    /**
     * 初始化视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            mapping = getMapping();
            mAdapter = new MultiViewAdapter(getActivity(), data, itemLayoutId, mapping);
            view = (ViewGroup) inflater.inflate(R.layout.fragment_dataview, container, false);
            refresh(data);
        }
        return view;
    }


    public void refresh(List<IDLinkedHashMap> data) {

        if (data == null) {
            data = new ArrayList<>();
        }

        if (data != mAdapter.getData()) {
            mAdapter.getData().clear();
            mAdapter.getData().addAll(data);
        }
        view.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            final View view = mAdapter.getView(i, null, this.view);
            this.view.addView(view);
            view.setOnClickListener(new OnClickListener(i));
        }
    }

    private class OnClickListener implements View.OnClickListener {
        private int position;


        public OnClickListener(int index) {
            this.position = index;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, position);
        }
    }


    public void refresh(Object data) {
        List<IDLinkedHashMap> list = toList(data);
        if (list == null) return;
        refresh(list);

    }

    private static List<IDLinkedHashMap> toList(Object data) {
        if (data == null) {
            return new ArrayList<IDLinkedHashMap>();
        }
        if (data instanceof List) {
            return (List<IDLinkedHashMap>) data;
        }

        List<IDLinkedHashMap> list = new ArrayList<IDLinkedHashMap>();
        list.add((IDLinkedHashMap) data);
        return list;
    }


    /**
     * 获取或初始化mapping
     *
     * @return
     */
    private Map<String, Integer> getMapping() {
        if (mapping == null) {
            mapping = new HashMap<String, Integer>();
            mapping.put("image", R.id.list_item_image);
            mapping.put("title", R.id.list_item_title);
            mapping.put("summary", R.id.list_item_summary);
            mapping.put("date", R.id.list_item_date);
        }
        return mapping;
    }

    public void setMapping(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public MultiViewAdapter getAdapter() {
        return mAdapter;
    }
}

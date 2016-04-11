package works.tonny.mobile.widget;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.utils.Log;

/**
 * 列表组件
 */
public class ListFragment extends Fragment implements Refreshable {
    private static final String DATA = "data";

    private static final String REFRESHABLE = "refreshable";

    private static final String ITEM_LAYOUT_ID = "itemLayoutId";

    /**
     * 数据
     */
    private List<IDLinkedHashMap> data;

    /**
     * ListView
     */
    private ListView mListView;


    /**
     * map与每条数据组件 id的对应关系
     */
    private Map<String, Integer> mapping;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MultiViewAdapter mAdapter;

    /**
     * 每行的组件layout id
     */
    private int itemLayoutId;

    /**
     * 上拉加载组件
     */
    private RelativeLayout footer;

    /**
     * list view 添加的头
     */
    private List<View> headers;

    /**
     * 是否允许下拉刷新
     */
    private boolean refreshable;

    /**
     * 下拉刷新接口
     */
    private OnRefreshListener refreshListener;

    /**
     * 加载更多接口
     */
    private OnLoadMoreListener loadMoreListener;


    /**
     * 数据点击接口
     */
    private AdapterView.OnItemClickListener itemClickListener;

    private Map<Integer, View.OnClickListener> viewClickListener;

    private View view;


    private int visibleLastIndex;
    /**
     * 是否正在刷新
     */
    private boolean refreshing;
    private OnTouchListener onTouchListener;
    private boolean waitForRefresh;
    private String id;
    private boolean canceled;
    private AdapterView.OnItemLongClickListener itemLongClickListener;


    /**
     * 通过数据创建实例
     *
     * @param data
     * @param refreshable 是否可刷新
     * @return
     */
    public static ListFragment newInstance(String id, ArrayList<? extends Map<String, ?>> data, boolean refreshable) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putSerializable(DATA, data);
        args.putBoolean(REFRESHABLE, refreshable);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 通过数据创建实例
     *
     * @param data
     * @param refreshable 是否可刷新
     * @return
     */
    public static ListFragment newInstance(String id, ArrayList<? extends Map<String, ?>> data, int itemLayoutId, boolean refreshable) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putSerializable(DATA, data);
        args.putInt(ITEM_LAYOUT_ID, itemLayoutId);
        args.putBoolean(REFRESHABLE, refreshable);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 创建动态刷新的
     *
     * @return
     */
    public static ListFragment newInstance(String id, boolean refreshable) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putBoolean(REFRESHABLE, refreshable);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 创建动态刷新的
     *
     * @return
     */
    public static ListFragment newInstance(String id, int itemLayoutId, boolean refreshable) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putBoolean(REFRESHABLE, refreshable);
        args.putInt(ITEM_LAYOUT_ID, itemLayoutId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //初始化数据
            data = (List<IDLinkedHashMap>) getArguments().getSerializable(DATA);
            refreshable = getArguments().getBoolean(REFRESHABLE);
            id = getArguments().getString("id");
            if (data == null) {
                data = new ArrayList<IDLinkedHashMap>();
            }
            if (refreshable) {
                onTouchListener = new OnTouchListener();
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
            mAdapter.setViewClickListener(this.viewClickListener);
            view = inflater.inflate(R.layout.fragment_item_list, container, false);

            // Set the adapter
            mListView = (ListView) view.findViewById(android.R.id.list);
            if (refreshable) {
                if (loadMoreListener != null) {
                    footer = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.list_footer, null);
                    mListView.addFooterView(footer);
                    mListView.setOnScrollListener(getOnScrollListener());
                }

                onTouchListener.bindView(this, (ViewGroup) view.findViewById(R.id.list_parent), mListView);
            }

            if (headers != null) {
                //添加头
                int idx = 0;
                for (View header : headers) {
                    mListView.addHeaderView(header);
                }
            }
            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
            if (itemClickListener != null) {
                mListView.setOnItemClickListener(itemClickListener);
            }
            if (itemLongClickListener != null) {
                mListView.setOnItemLongClickListener(itemLongClickListener);
            }

        }
        return view;
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

    /**
     * 添加数据
     *
     * @param datas
     */
    public void prependDatas(List<IDLinkedHashMap> datas) {
        for (int i = datas.size(); i > 0; i--) {
//            Log.info(datas.get(i - 1));
            mAdapter.insertData(datas.get(i - 1));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void appendDatas(List<IDLinkedHashMap> datas) {
        for (int i = 0; i < datas.size(); i++) {
            mAdapter.addData(datas.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }


    public int size() {
        return mAdapter.getData().size();
    }

    public IDLinkedHashMap data(int index) {

        return mAdapter.getData().get(index);
    }

    public void remove(int idex) {
        mAdapter.getData().remove(idex);
        mAdapter.notifyDataSetChanged();
    }

    public void clearAllData() {
        mAdapter.getData().clear();
    }


    /**
     * 添加头view
     *
     * @param view
     */
    public void addHeader(View view) {
        if (headers == null) {
            headers = new ArrayList<View>();
        }
        headers.add(view);
    }


    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public MultiViewAdapter getmAdapter() {
        return mAdapter;
    }

    public void setMapping(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }


    public int getHeight() {
        return view.getHeight();
    }


    public void setViewClickListener(int id, View.OnClickListener viewClickListener) {
        if (this.viewClickListener == null) {
            this.viewClickListener = new HashMap<Integer, View.OnClickListener>();
        }
        this.viewClickListener.put(id, viewClickListener);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(AdapterView.OnItemLongClickListener longClickListener) {
        this.itemLongClickListener = longClickListener;
    }


    public void refresh() {
        if (onTouchListener != null)
            onTouchListener.refresh();
        else
            this.waitForRefresh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (waitForRefresh && onTouchListener != null) {
            onTouchListener.refresh();
            waitForRefresh = false;
        }
    }

    @Override
    public Object onRefresh(Handler handler) {
        this.refreshing = true;
        return refreshListener.refresh(data);
    }


    @Override
    public void progressUpdate(Object data) {
        //refreshListener.progressUpdate(data);
    }

    @Override
    public void refreshed(Object xml) {
        if (canceled) {
            canceled = false;
            return;
        }
        clearAllData();
        if (xml == null) {
//            LayoutUtils.alert(mListView.getContext(), "没有数据").show();
            view.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
            this.refreshing = false;
            return;
        }
        Log.info(xml);
        if (xml instanceof List) {
            prependDatas((List<IDLinkedHashMap>) xml);
        } else {
            List<IDLinkedHashMap> list = new ArrayList<IDLinkedHashMap>();
            list.add((IDLinkedHashMap) xml);
            prependDatas(list);
        }
        mAdapter.notifyDataSetChanged();
        this.refreshing = false;
        canceled = false;
    }


    @Override
    public void cancelRefresh() {
        this.canceled = true;
        if (refreshListener != null)
            refreshListener.cancelRefresh();
    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    /**
     * 异步加载数据
     */
    class LoadData extends AsyncTask<Void, List<IDLinkedHashMap>, List<IDLinkedHashMap>> {

        @Override
        protected void onPreExecute() {
            footer.findViewById(R.id.list_view_more1).setVisibility(View.VISIBLE);
            footer.findViewById(R.id.list_footer_more).setVisibility(View.GONE);
        }

        @Override
        protected List<IDLinkedHashMap> doInBackground(Void... params) {
            return loadMoreListener.loadMore((List<IDLinkedHashMap>) data);
        }

        @Override
        protected void onPostExecute(List<IDLinkedHashMap> data) {
            prependDatas(data);
//            mAdapter.notifyDataSetChanged();
            footer.findViewById(R.id.list_view_more1).setVisibility(View.GONE);
            footer.findViewById(R.id.list_footer_more).setVisibility(View.VISIBLE);
        }
    }


    private AbsListView.OnScrollListener getOnScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = ListFragment.this.data.size();    //数据集最后一项的索引
                int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
                //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
                //由于用户的操作，屏幕产生惯性滑动时为2
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex + 2 >= lastIndex) {
                    new LoadData().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount;
            }
        };
    }


    @Override
    public String getRrefreshId() {
        return id;
    }

    public static interface OnRefreshListener {
        /**
         * 下拉头部刷新
         *
         * @param data
         */
        Object refresh(List<IDLinkedHashMap> data);

        //void progressUpdate(Object data);

        void cancelRefresh();
    }

    public static interface OnLoadMoreListener {

        /**
         * 加载更多数据
         *
         * @param data
         */
        List<IDLinkedHashMap> loadMore(List<IDLinkedHashMap> data);
    }

}

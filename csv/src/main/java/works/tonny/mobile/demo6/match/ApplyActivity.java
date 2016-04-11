package works.tonny.mobile.demo6.match;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.utils.XMLParser;

/**
 * 申请参赛页面
 */
public class ApplyActivity extends Activity implements View.OnClickListener {
    protected ActivityHelper activityHelper;
    Context mContext;
    MyListAdapter adapter;
    private String url;
    private String queryUrl;

    ListView lv;
    private List<Map<String, ?>> items;
    List<Boolean> mChecked;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_apply);
        Map data = (Map) getIntent().getSerializableExtra("data");
        url = (String) data.get("url");
        queryUrl = url;
        mContext = getApplicationContext();
        lv = (ListView) findViewById(R.id.list);
//        TitleHelper.getInstance(this).enableBack().setTitle("申请参赛").setButton("参赛", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (true) {
//                    return;
//                }

//            }
//        });
        TitleHelper.getInstance(this).enableBack().setTitle("申请参赛").setButton(R.drawable.icon_option, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        RelativeLayout footer = (RelativeLayout) LayoutInflater.from(this).inflate(works.tonny.mobile.R.layout.list_footer, null);
        lv.addFooterView(footer);
        lv.setOnScrollListener(getOnScrollListener());
        if (items == null) {
            items = new ArrayList<>();
        }
        adapter = new MyListAdapter(ApplyActivity.this, items, R.layout.match_apply_dog_item, new String[]{"cname", "blood", "earid"}, new int[]{R.id.mc, R.id.xtzsh, R.id.eh});
//                SimpleAdapter adapter1 = new SimpleAdapter(ApplyActivity.this, items, R.layout.match_apply_dog_item, new String[]{"cname", "blood", "earid"}, new int[]{R.id.mc, R.id.xtzsh, R.id.eh});
        lv.setAdapter(adapter);

        activityHelper = ActivityHelper.getInstance(this);
        new Post().execute();
        initPopupWindow();

    }

    private int visibleLastIndex;

    private AbsListView.OnScrollListener getOnScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = items.size();    //数据集最后一项的索引
                int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
                //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
                //由于用户的操作，屏幕产生惯性滑动时为2
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex + 2 >= lastIndex) {
                    new Post().execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount;
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_menu, menu);
//        menu.add(Menu.NONE, Menu.FIRST + 1, 1, getResource(R.string.edit_text)).setIcon(R.drawable.ic_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {

            finish();
        } else {

        }
    }

    //自定义ListView适配器
    class MyListAdapter extends SimpleAdapter {
        List<? extends Map<String, ?>> list;
        HashMap<Integer, View> map = new HashMap<Integer, View>();

        public MyListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.list = data;
            mChecked = new ArrayList<Boolean>();
            for (int i = 0; i < list.size(); i++) {
                mChecked.add(false);
            }
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;

            if (map.get(position) == null) {
                LayoutInflater mInflater = getLayoutInflater();
                view = mInflater.inflate(R.layout.match_apply_dog_item, null);
                holder = new ViewHolder();
                holder.selected = (CheckBox) view.findViewById(R.id.checkbox);
                holder.name = (TextView) view.findViewById(R.id.mc);
                holder.xtzsh = (TextView) view.findViewById(R.id.xtzsh);
                holder.eh = (TextView) view.findViewById(R.id.eh);
                final int p = position;
                map.put(position, view);
                holder.selected.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        mChecked.set(p, cb.isChecked());
                    }
                });
                view.setTag(holder);
            } else {
//                Log.e("MainActivity", "position2 = " + position);
                view = map.get(position);
                holder = (ViewHolder) view.getTag();
            }

            if (!mChecked.isEmpty()) {
                holder.selected.setChecked(mChecked.get(position));
                holder.name.setText((CharSequence) items.get(position).get("cname"));
                holder.xtzsh.setText((CharSequence) items.get(position).get("blood"));
                holder.eh.setText((CharSequence) items.get(position).get("earid"));
            }
            return view;
        }

    }

    static class ViewHolder {
        CheckBox selected;
        TextView name;
        TextView xtzsh;
        TextView eh;
    }

    public class Post extends AsyncTask<Map<String, Object>, Integer, Map<String, Object>> {

        private HttpRequest request;
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = ProgressDialog.show(ApplyActivity.this, null, "读取中", true);
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            try {
                super.onPostExecute(result);
                myDialog.dismiss();
                refreshed(result);
                works.tonny.mobile.utils.Log.info(items.size());
            } catch (Exception e) {
                works.tonny.mobile.utils.Log.error(e);
            }
        }

        @Override
        protected Map<String, Object> doInBackground(Map<String, Object>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, url);

            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser parser = new XMLParser();
                parser.parse(xml);
                Map<String, Object> datas = parser.getDatas();
                url = (String) datas.get("data.menu.mproxyurl");
                return datas;
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return null;
            } catch (AuthException e) {
                works.tonny.mobile.utils.Log.error(e);
                this.publishProgress(-1);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void refreshed(Map<String, Object> result) {
        Object o = result.get("data.list.item");
        if (o == null) {
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Map<String, ?>> maps = null;
        if (o instanceof Map) {
            maps = new ArrayList<>();
            maps.add((Map<String, ?>) o);
        } else {
            maps = (List<Map<String, ?>>) o;
        }

        for (int i = 0; i < maps.size(); i++) {
            mChecked.add(false);
        }
        items.addAll(maps);
        adapter.notifyDataSetChanged();
    }

    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("cname", R.id.mc);
        mapping.put("blood", R.id.xtzsh);
        mapping.put("earid", R.id.eh);
        return mapping;
    }

    private PopupWindow popupWindow;

    private Button btn_popupwindow;

    private View mPopupWindowView;

    /**
     * 初始化popupwindow
     */
    private void initPopupWindow() {
        initPopupWindowView();
        //初始化popupwindow，绑定显示view，设置该view的宽度/高度
        popupWindow = new PopupWindow(mPopupWindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        works.tonny.mobile.utils.Log.info(popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
        //Background不能设置为null，dismiss会失效
//		popupWindow.setBackgroundDrawable(null);
        //设置渐入、渐出动画效果
//		popupWindow.setAnimationStyle(R.style.popupwindow);
        popupWindow.update();
        //popupWindow调用dismiss时触发，设置了setOutsideTouchable(true)，点击view之外/按键back的地方也会触发
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//				showToast("关闭popupwindow");
            }
        });
    }

    /**
     * 初始化popupwindowView,监听view中的textview点击事件
     */
    private void initPopupWindowView() {

        mPopupWindowView = LayoutInflater.from(mContext).inflate(R.layout.match_apply_dialog, null);
        TextView search = (TextView) mPopupWindowView.findViewById(R.id.search);
        search.setOnClickListener(this);
        TextView textview_file = (TextView) mPopupWindowView.findViewById(R.id.apply);
        textview_file.setOnClickListener(this);
//        TextView textview_about = (TextView) mPopupWindowView.findViewById(R.id.textview_about);
//        textview_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                final ApplySearchDialog applySearchDialog = new ApplySearchDialog(this);
                applySearchDialog.setView(new EditText(ApplyActivity.this));
                applySearchDialog.show();
                final EditText text = (EditText) applySearchDialog.findViewById(R.id.blood);
                Button button = (Button) applySearchDialog.findViewById(R.id.searching);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (popupWindow.isShowing())
                        applySearchDialog.dismiss();
                        new RequestTask(ApplyActivity.this, new RequestTask.Requested() {
                            @Override
                            public void execute(Map<String, Object> map) {
                                mChecked.clear();
                                items.clear();
                                refreshed(map);
                            }
                        }).execute(queryUrl + "&blood=" + text.getText());
                    }
                });
                popupWindow.dismiss();
                break;
            case R.id.apply:
                List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
                for (int i = 0; i < mChecked.size(); i++) {
                    if (mChecked.get(i))
                        list.add(adapter.list.get(i));
                }

                if (list.isEmpty()) {
                    Toast.makeText(ApplyActivity.this, "没有选择犬只", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("list", list);
                map.put("id", StringUtils.substringAfterLast(url, "id="));

                Intent intent = IntentUtils.newInstance(ApplyActivity.this, ApplyConfirmActivity.class, map);
                startActivityForResult(intent, 0);
                popupWindow.dismiss();
                break;
        }
    }


    /**
     * 显示popupwindow
     */
    private void showPopupWindow() {
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(findViewById(R.id.list_tool_button), 50, 0);
        } else {
            popupWindow.dismiss();
        }
    }


}

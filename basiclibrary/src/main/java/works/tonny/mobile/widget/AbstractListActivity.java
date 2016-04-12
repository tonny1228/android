package works.tonny.mobile.widget;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser2;

/**
 * Created by tonny on 2016/2/21.
 */
public abstract class AbstractListActivity extends Activity {
    protected ListFragment listFragment;

    protected ActivityHelper activityHelper;

    protected boolean loaded;
//    /**
//     * 页面标题
//     */
//    private String title;
//    /**
//     * 标题右侧的按钮id
//     */
//    private int titleButtonId;
//
//    /**
//     * 标题右侧的按钮文本
//     */
//    private String titleButtonText;

//    /**
//     * 标题右侧的按钮文本样式
//     */
//    private int titleButtonTextStyle;

//    /**
//     * 标题右侧的按钮点击后进的页面类
//     */
//    private Class titleButtonClass;
//
//
//    private View.OnClickListener onTitleButtonClickListener;

//    /**
//     * 标题右侧的按钮使用图标的id
//     */
//    private int titleButtonIconId;
//
//    /**
//     * 标题的id
//     */
//    private int titleViewId;

//    /**
//     * 标题左侧的返回按钮的id
//     */
//    private int gobackId;

    /**
     * 每个元素的layout
     */
    private int itemLayout;

    private AdapterView.OnItemLongClickListener onLongClickListener;

    /**
     * 访问地址
     */
    private String url;


    private Map<String, Integer> mapping = new HashMap<>();
//
//
//    protected void setTitle(String title) {
//        this.title = title;
//    }

//    protected void setButtonActivityClass(Class clz) {
//        this.titleButtonClass = clz;
//    }

    protected void setItemLayout(int layout) {
        this.itemLayout = layout;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected void addMapping(String tag, Integer id) {
        mapping.put(tag, id);
    }

    protected abstract int getContentLayout();

//    protected void setButtonText(String text, int style) {
//        this.titleButtonText = text;
//        this.titleButtonTextStyle = style;
//    }
//
//    protected void setTitleButtonId(int id) {
//        this.titleButtonId = id;
//    }
//
//    protected void setTitleButtonIconId(int id) {
//        this.titleButtonIconId = id;
//    }
//
//
//    protected void setTitleViewId(int id) {
//        this.titleViewId = id;
//    }
//
//
//    protected void setGobackId(int id) {
//        this.gobackId = id;
//    }
//

    protected String getResultDataName() {
        return "data.list.item";
    }


    protected abstract AdapterView.OnItemClickListener getItemClickListener();


    /**
     * 不查询直接返回数据，需要重写
     *
     * @return
     */
    protected ArrayList<IDLinkedHashMap> getData() {
        return null;
    }


    @Override
    protected void onPause() {
        super.onPause();
        listFragment.cancelRefresh();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHelper = ActivityHelper.getInstance(this);
        setContentView(getContentLayout());
//        createTitleView();

        beforeCreate();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (itemLayout > 0) {
            if (getData() == null) {
                listFragment = ListFragment.newInstance(getClass().getName(), itemLayout, true);
            } else {
                listFragment = ListFragment.newInstance(getClass().getName(), getData(), itemLayout, false);
            }
        } else {
            if (getData() == null) {
                listFragment = ListFragment.newInstance(getClass().getName(), true);
            } else {
                listFragment = ListFragment.newInstance(getClass().getName(), getData(), false);
            }
        }

        if (mapping != null) {
            listFragment.setMapping(mapping);
        }
        fragmentTransaction.replace(getListReplaceId(), listFragment);
        fragmentTransaction.commit();
        if (url != null) {
            listFragment.setRefreshListener(new RemoteRefreshListener());
        } else {
//            listFragment.setRefreshListener(new DataRefreshListener());
        }
        if (getItemClickListener() != null)
            listFragment.setItemClickListener(getItemClickListener());
        if (onLongClickListener != null)
            listFragment.setItemLongClickListener(onLongClickListener);
        init();


    }

    protected void beforeCreate() {
    }

    protected abstract void init();

    protected abstract int getListReplaceId();


    protected void setOnLongClickListener(AdapterView.OnItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

//
//    private void createTitleView() {
//        if (gobackId > 0) {
//            activityHelper.enableBack(gobackId);
//        }
//        if (title != null && titleViewId > 0) {
//            activityHelper.setText(titleViewId, title);
//        }
//        Log.info(titleButtonClass + " ********************************************** " + titleButtonId);
//
//        if (titleButtonClass != null || onTitleButtonClickListener != null) {
//            View.OnClickListener clickListener = null;
//            if (onTitleButtonClickListener != null) {
//                clickListener = onTitleButtonClickListener;
//            } else {
//                clickListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = IntentUtils.newInstance(AbstractListActivity.this, titleButtonClass, new HashMap<String, Object>());
//                        startActivity(intent);
//                    }
//                };
//            }
//
//            if (titleButtonIconId > 0) {
//                activityHelper.setButton(titleButtonId, titleButtonIconId, clickListener);
//            } else if (!StringUtils.isEmpty(titleButtonText)) {
//                activityHelper.setButton(titleButtonId, titleButtonText, titleButtonTextStyle, clickListener);
//            }
//
//        }
//    }


    private OnRequested onRequested;

    public static interface OnRequested {
        void requested(XMLParser2 parser);
    }


    public void setOnRequested(OnRequested onRequested) {
        this.onRequested = onRequested;
    }


    class RemoteRefreshListener implements ListFragment.OnRefreshListener {
        private HttpRequest request;

        @Override
        public Object refresh(List<IDLinkedHashMap> data) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, url);
            try {
                XMLParser2 xmlParser = new XMLParser2();
                String xml = request.executeToString();
                if (StringUtils.isEmpty(xml)) {
                    return null;
                }
                xmlParser.parse(xml);
//                Map<String, Object> datas = xmlParser.getDatas();
                request = null;

                if (onRequested != null) {
                    onRequested.requested(xmlParser);
                }


                List<Object> list = xmlParser.getList(getResultDataName());
                return list;
            } catch (AuthException e) {
                Log.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            } catch (Exception e) {
                Log.error(e);
            }
            return null;
        }

        @Override
        public void cancelRefresh() {
            if (request != null) {
                request.cancel();
            }
        }
    }

    /**
     * 使用本地数据刷新
     */
    class DataRefreshListener implements ListFragment.OnRefreshListener {
        @Override
        public Object refresh(List<IDLinkedHashMap> data) {
            return getData();
        }

        @Override
        public void cancelRefresh() {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!loaded) {
            listFragment.refresh();
            loaded = true;
        }
    }


//    protected void setOnTitleButtonClickListener(View.OnClickListener onTitleButtonClickListener) {
//        this.onTitleButtonClickListener = onTitleButtonClickListener;
//    }
}

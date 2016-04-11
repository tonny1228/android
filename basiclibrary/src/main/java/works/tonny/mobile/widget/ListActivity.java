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
import works.tonny.mobile.utils.XMLParser;

/**
 * 通用的列表页
 * Created by tonny on 2015/8/6.
 */
public abstract class ListActivity extends Activity {
    protected ListFragment listFragment;

    protected ActivityHelper activityHelper;

    protected boolean loaded;

    protected abstract String getListTitle();

    protected boolean isAddEnabled() {
        return false;
    }

    protected Class getAddActivityClass() {
        return null;
    }

    protected int getItemLayout() {
        return 0;
    }

    protected String getUrl() {
        return null;
    }

    protected Map<String, Integer> getMapping() {
        return null;
    }

    protected abstract int getLayout();

    protected String getButtonText() {
        return null;
    }

    protected int getButtonIcon() {
        return 0;
    }


    protected int getTitleTextId() {
        return 0;
    }


    protected int getTitleGoBackIcon() {
        return 0;
    }


    protected int getTitleButtonId() {
        return 0;
    }


    protected int getTitleButtonTextStyle() {
        return 0;
    }


    protected abstract int getListReplaceId();


    protected String getResultDataName() {
        return "data.list.item";
    }


    protected abstract AdapterView.OnItemClickListener getItemClickListener();


    protected void init() {
    }


    protected boolean loadMore() {
        return false;
    }


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
        setContentView(getLayout());
        activityHelper = ActivityHelper.getInstance(this);
        if (getListTitle() != null) {
            int titleGoBackIcon = getTitleGoBackIcon();
            if (titleGoBackIcon > 0) {
                activityHelper.enableBack(titleGoBackIcon);
            }
            activityHelper.setText(getTitleTextId(), getListTitle());
        }
        if (isAddEnabled()) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = IntentUtils.newInstance(ListActivity.this, getAddActivityClass(), new HashMap<String, Object>());
                    startActivity(intent);
                }
            };
            if (getButtonIcon() > 0) {
                activityHelper.setButton(getTitleButtonId(), getButtonIcon(), clickListener);
            } else {
                activityHelper.setButton(getTitleButtonId(), getButtonText(), getTitleButtonTextStyle(), clickListener);
            }

        }


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (getItemLayout() > 0) {
            if (getData() == null) {
                listFragment = ListFragment.newInstance(getClass().getName(), getItemLayout(), true);
            } else {
                listFragment = ListFragment.newInstance(getClass().getName(), getData(), getItemLayout(), false);
            }
        } else {
            if (getData() == null) {
                listFragment = ListFragment.newInstance(getClass().getName(), true);
            } else {
                listFragment = ListFragment.newInstance(getClass().getName(), getData(), false);
            }
        }
        if (getMapping() != null) {
            listFragment.setMapping(getMapping());
        }
        fragmentTransaction.replace(getListReplaceId(), listFragment);
        fragmentTransaction.commit();
        if (getUrl() != null) {
            listFragment.setRefreshListener(new RemoteRefreshListener());
        } else {
//            listFragment.setRefreshListener(new DataRefreshListener());
        }
        if (getItemClickListener() != null)
            listFragment.setItemClickListener(getItemClickListener());

        init();
    }


    class RemoteRefreshListener implements ListFragment.OnRefreshListener {
        private HttpRequest request;

        @Override
        public Object refresh(List<IDLinkedHashMap> data) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, getUrl());
            try {
                XMLParser xmlParser = new XMLParser();
                String xml = request.executeToString();
                if (StringUtils.isEmpty(xml)) {
                    return null;
                }
                xmlParser.parse(xml);
                Map<String, Object> datas = xmlParser.getDatas();
                request = null;

                return datas.get(getResultDataName());
            } catch (AuthException e) {
                Log.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            } catch (Exception e) {
                e.printStackTrace();
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

}

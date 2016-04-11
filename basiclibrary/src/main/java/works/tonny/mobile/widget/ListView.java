package works.tonny.mobile.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tonny on 2016/3/23.
 */
public class ListView  extends Fragment implements Refreshable {

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
//        args.putSerializable(DATA, data);
//        args.putBoolean(REFRESHABLE, refreshable);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Object onRefresh(Handler handler) {
        return null;
    }

    @Override
    public void refreshed(Object data) {

    }

    @Override
    public void progressUpdate(Object data) {

    }

    @Override
    public void cancelRefresh() {

    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    @Override
    public String getRrefreshId() {
        return null;
    }
}

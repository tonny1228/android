package works.tonny.mobile.demo6.query;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.DataView;

public class PeiquanActivity extends Fragment {


    private ActivityHelper helper;

    private View view;

    private Map<String, Object> data;

    /**
     * 创建动态刷新的
     *
     * @return
     */
    public static PeiquanActivity newInstance(Map<String, Object> data) {
        PeiquanActivity fragment = new PeiquanActivity();
        Bundle args = new Bundle();
        args.putSerializable("data", (Serializable) data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (Map<String, Object>) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.query_qzcx_frag, container, false);
            helper = ActivityHelper.getInstance((ViewGroup) view);
            Object o = data.get("data.list.item");
            Log.info(o);
            DataView listFragment = DataView.newInstance((ArrayList<? extends Map<String, ?>>) o, R.layout.query_activity_peiquan);
            listFragment.setMapping(getMapping());
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.list, listFragment);
            fragmentTransaction.commit();
        }
        return view;
    }


    protected String getListTitle() {
        return "配犬";
    }

    protected int getItemLayout() {
        return R.layout.query_activity_peiquan;
    }

//    protected String getUrl() {
//        return getIntent().getStringExtra("prove");
//    }

    protected int getLayout() {
        return R.layout.activity_list;
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return null;
    }


    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("provenums", R.id.provenums);
        mapping.put("fmprovetime", R.id.fmprovetime);
        mapping.put("mblood", R.id.mblood);
        mapping.put("hostm", R.id.hostm);
        mapping.put("birthdate", R.id.birthdate);
        mapping.put("nums", R.id.nums);
        return mapping;
    }
}
package works.tonny.mobile.demo6.query;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.R;

public class XtxxbActivity extends Fragment {

    private ActivityHelper helper;

    private View view;

    private Map<String, Object> data;

    /**
     * 创建动态刷新的
     *
     * @return
     */
    public static XtxxbActivity newInstance(Map<String, Object> data) {
        XtxxbActivity fragment = new XtxxbActivity();
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
        view = inflater.inflate(R.layout.query_activity_xtxxb, container, false);
        helper = ActivityHelper.getInstance((ViewGroup) view);
        helper.setImage(R.id.imagef, (String) data.get("data.finfo.img")).setText(R.id.zwm, (String) data.get("data.finfo.cname"))
                .setText(R.id.ywm, (String) data.get("data.finfo.ename"))
                .setText(R.id.blood, (String) data.get("data.finfo.blood")).setImage(R.id.mimage, (String) data.get("data.minfo.img")).setText(R.id.mzwm, (String) data.get("data.minfo.cname"))
                .setText(R.id.mywm, (String) data.get("data.minfo.ename"))
                .setText(R.id.mblood, (String) data.get("data.minfo.blood"))
        ;
        helper.setOnClickListener(R.id.fq, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", data.get("data.finfo.url").toString());
                startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
            }
        });
       helper.setOnClickListener(R.id.mq, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", data.get("data.minfo.url").toString());
                startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
            }
        });
        return view;
    }


}

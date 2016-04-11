package works.tonny.mobile.demo6;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.demo6.match.ApplyMatchActivity;
import works.tonny.mobile.demo6.match.MatchListActivity;
import works.tonny.mobile.demo6.match.ResultActivity;
import works.tonny.mobile.demo6.match.UserMatchActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.ImageIconGroupFragment;
import works.tonny.mobile.widget.ListFragment;
import works.tonny.mobile.widget.LoadingDialog;
import works.tonny.mobile.widget.NoneNetWorkFragment;


/**
 *
 */
public class MatchFragment extends Fragment {
    private ListFragment fragment;

    private DataView inList;
    private LinearLayout view;
    private LoadingDialog loadingDialog;
    private DataView outList;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            return view;
        }
        view = (LinearLayout) inflater.inflate(R.layout.index_fragment_match, container, false);
        TitleHelper.getInstance(view).setTitle("赛事");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ArrayList<ImageIconGroupFragment.Entity> list2 = new ArrayList<ImageIconGroupFragment.Entity>();
        list2.add(new ImageIconGroupFragment.Entity(R.drawable.match_apply, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ApplyMatchActivity.class);
                startActivity(intent);
//                Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
            }
        }));
        list2.add(new ImageIconGroupFragment.Entity(R.drawable.match_user, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), UserMatchActivity.class);
                startActivity(intent);
            }
        }));
        list2.add(new ImageIconGroupFragment.Entity(R.drawable.match_result, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ResultActivity.class);
                startActivity(intent);
            }
        }));
        ImageIconGroupFragment imageIconGroupFragment = new ImageIconGroupFragment();
        imageIconGroupFragment.init(list2, 3, false);
        fragmentTransaction.replace(R.id.match_icons, imageIconGroupFragment);

//
//        ImageView imageView = new ImageView(container.getContext());
//        imageView.setImageResource(R.drawable.top);

        View v = inflater.inflate(R.layout.matcher_header, null);


        inList = DataView.newInstance(new ArrayList<Map<String, ?>>(), R.layout.match_index_item_grid);
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", R.id.list_item_image);
        mapping.put("title", R.id.list_item_title);
        mapping.put("date", R.id.match_date);
        mapping.put("time", R.id.match_time);
//        mapping.put("jbdw", R.id.match_jbdw);
//        mapping.put("cbdw", R.id.match_cbdw);
//        mapping.put("cp", R.id.match_cp);
        mapping.put("addr", R.id.match_addr);
        inList.setMapping(mapping);
        fragmentTransaction.replace(R.id.list_guonei, inList);
        outList = DataView.newInstance(new ArrayList<Map<String, ?>>(), R.layout.match_index_item_grid2);
        outList.setMapping(mapping);
        fragmentTransaction.replace(R.id.list_guoji, outList);

        fragmentTransaction.commitAllowingStateLoss();
        new Request().execute();
        inList.setItemClickListener(new DataView.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                intent.putExtra("url", (String) inList.getAdapter().getData().get(position).get("url"));
                intent.putExtra("title", "赛事报道");
                startActivity(intent);
            }
        });
        outList.setItemClickListener(new DataView.ItemClickListener() {
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                intent.putExtra("url", (String) outList.getAdapter().getData().get(position).get("url"));
                intent.putExtra("title", "赛事预告");
                startActivity(intent);
            }
        });

        ActivityHelper.getInstance(view).setOnClickListener(R.id.ssbd, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MatchListActivity.class);
                intent.putExtra("title", "赛事报道");
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Application.getUser() == null && !MainActivity.switchToHome) {
            LoginActivity.startLoginActivity(getActivity(), null);
            return;
        }
    }

    /**
     * 载入banner
     */
    private void loadMatch(Map<String, Object> data) {
        //inList.getData().clear();
//        getView().findViewById(R.id.list_guonei).setVisibility(View.GONE);

        try {
            if (view.findViewById(R.id.none_network) != null)
                view.findViewById(R.id.none_network).setVisibility(View.GONE);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Log.info(data.get("data.inland.item"));
            inList.refresh(data.get("data.inland.item"));
            outList.refresh(data.get("data.outland.item"));
            fragmentTransaction.commit();
            loadingDialog.hide();
        } catch (Exception e) {
            Log.error(e);
        }
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {
        @Override
        protected void onCancelled() {
            super.onCancelled();
            view.findViewById(R.id.scrollView).setVisibility(View.GONE);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NoneNetWorkFragment fragment = new NoneNetWorkFragment();
            fragmentTransaction.replace(R.id.match_parent, fragment);
            fragmentTransaction.commitAllowingStateLoss();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Request().execute();
                }
            });
            loadingDialog.hide();
        }

        @Override
        protected void onPostExecute(Map<String, Object> data) {
            loadMatch(data);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            view.findViewById(R.id.scrollView).setVisibility(View.GONE);
            loadingDialog = LoadingDialog.newInstance(view.getContext());
            loadingDialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            if (!DeviceUtils.isNetworkConnected(view.getContext())) {
                this.cancel(true);
            }
            HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_match));
            try {
                XMLParser xmlParser = new XMLParser();
                String xml = request.executeToString();
                xmlParser.parse(xml);

                IOUtils.cacheObject(xmlParser.getDatas(), FileUtils.getCacheDirFile("/match_index"));
                return (Map<String, Object>) xmlParser.getDatas();
            } catch (Exception e) {
                Log.error(e);
            }
            request = null;
            return null;
        }
    }

}

package works.tonny.mobile.demo6;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.demo6.match.MatchListActivity;
import works.tonny.mobile.demo6.paihang.HjPahangActivity;
import works.tonny.mobile.demo6.paihang.HoudaiSLActivity;
import works.tonny.mobile.demo6.paihang.KongwoActivity;
import works.tonny.mobile.demo6.paihang.PaihangActivity;
import works.tonny.mobile.demo6.query.QzcxViewActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.ImageViewPaperFragment;
import works.tonny.mobile.widget.ListFragment;
import works.tonny.mobile.widget.Refreshable;
import works.tonny.mobile.widget.ScrollView;


/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements Refreshable {

    private View view;
    private Refreshable.OnTouchListener onTouchListener;
    private HttpRequest request;
    private DataView listFragment;
    private boolean canceled;
    private ScrollView scrollView;
    private DataView matchFragment;
    private ListFragment hjphFragment;
    private ListFragment slphFragment;
    private boolean refresh = false;
    private ActivityHelper helper;

    public IndexFragment() {
    }


    public String getRrefreshId() {
        return "index";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            try {
                view = inflater.inflate(R.layout.index_fragment_home, container, false);
                helper = ActivityHelper.getInstance((ViewGroup) view);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                loadBanner(fragmentTransaction);
                loadNews(fragmentTransaction);
                loadDogs(fragmentTransaction);
                loadGoods();
                loadMatch(fragmentTransaction);
                loadSlph();
                loadHjph();
                loadHdslph();
                loadKwph();
                //loadClub();
                fragmentTransaction.commitAllowingStateLoss();
                scrollView = (ScrollView) view.findViewById(R.id.scrollView);

                onTouchListener = new OnTouchListener();
                onTouchListener.bindView(this, (ViewGroup) view.findViewById(R.id.index_parent), scrollView);


                ActivityHelper instance = ActivityHelper.getInstance((ViewGroup) this.view);
                instance.setOnClickListener(R.id.c_zxgg, NewsActivity.class);
                instance.setOnClickListener(R.id.c_ssbd, MatchListActivity.class);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("url", Application.getUrl(R.string.url_index_slph));
                params.put("title", "种公犬配犬数量排行");
                instance.setOnClickListener(R.id.zgqpqslph, PaihangActivity.class, params);
                Map<String, Object> params1 = new HashMap<String, Object>();
                params1.put("url", Application.getUrl(R.string.url_index_hdph));
                params1.put("title", "种公犬后代获奖排行");
                instance.setOnClickListener(R.id.hdhjph, HjPahangActivity.class, params1);
                Map<String, Object> params2 = new HashMap<String, Object>();
                params2.put("url", Application.getUrl(R.string.url_index_kwph));
                params2.put("title", "种公犬空窝排行");
                instance.setOnClickListener(R.id.kwph, KongwoActivity.class, params2);
                Map<String, Object> params3 = new HashMap<String, Object>();
                params3.put("url", Application.getUrl(R.string.url_index_hdsl));
                params3.put("title", "种公犬后代数量排行");
                instance.setOnClickListener(R.id.hdsiph, HoudaiSLActivity.class, params3);
            } catch (Exception e) {
                Log.error(e);
            }
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!refresh) {
            onTouchListener.refresh();
            refresh = true;

        }
    }

    /**
     * 启动时或更改后载入banner
     *
     * @param fragmentTransaction
     */
    private void loadBanner(FragmentTransaction fragmentTransaction) {
        ArrayList<ImageViewPaperFragment.Entity> list = new ArrayList<ImageViewPaperFragment.Entity>();
        try {
            ArrayList<Map<String, Object>> img = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_banner"));
            for (int i = 0; img != null && i < img.size(); i++) {
                String image = ((Map) img.get(i)).get("image").toString();
                String url = ((Map) img.get(i)).get("url").toString();
                Log.info("xsddd:" + url);
                Map<String, String> data = new HashMap<String, String>();
                data.put("url", url);
                data.put("title", "头条");
                ImageViewPaperFragment.Entity entity = new ImageViewPaperFragment.Entity(image, ((Map) img.get(i)).get("title").toString(), null, works.tonny.mobile.demo6.WebActivity.VIEW, data);
                list.add(entity);
            }
        } catch (Exception e) {
            Log.error(e);
//            list.add(new ImageViewPaperFragment.Entity(R.drawable.header, "加入我们", null, null, null));
        }
        ImageViewPaperFragment fragment = ImageViewPaperFragment.newInstance(list);
        fragmentTransaction.replace(R.id.index_banner, fragment);
    }


    /**
     * 刷新banner
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshBanner(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List img = (List) xmlParser.getDatas().get("data.images.image");
            File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);

            request = null;
            try {
                IOUtils.cacheObject(img, FileUtils.getCacheDirFile("/index_banner"));
            } catch (IOException e) {
                Log.error(e);
            }
            Message message = new Message();
            message.what = 2;
            message.obj = img;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后更新并
     *
     * @param xml
     */
    private void addBanner(ArrayList<Map<String, Object>> xml) {
        if (canceled) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        loadBanner(fragmentTransaction);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 启动时或更改后载入banner
     *
     * @param fragmentTransaction
     */
    private void loadDogs(FragmentTransaction fragmentTransaction) {
        ArrayList<ImageViewPaperFragment.Entity> list = new ArrayList<ImageViewPaperFragment.Entity>();
        try {
            ArrayList<Map<String, Object>> img = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_dogs"));
            for (int i = 0; img != null && i < img.size(); i++) {
                String image = ((Map) img.get(i)).get("image").toString();
                String url = ((Map) img.get(i)).get("url").toString();
                Map<String, String> data = new HashMap<String, String>();
                data.put("url", url);
                data.put("title", "种公展示");
                ImageViewPaperFragment.Entity entity = new ImageViewPaperFragment.Entity(image, ((Map) img.get(i)).get("title").toString(), null, works.tonny.mobile.demo6.WebActivity.VIEW, data);
                list.add(entity);
            }
        } catch (Exception e) {
            Log.error(e);
//            list.add(new ImageViewPaperFragment.Entity(R.drawable.header, "加入我们", null, null, null));
        }
        ImageViewPaperFragment fragment = ImageViewPaperFragment.newInstance(list);
        fragmentTransaction.replace(R.id.index_dogs, fragment);
    }


    /**
     * 刷新banner
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshDogs(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List img = (List) xmlParser.getDatas().get("data.dogs.image");
            File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);

            request = null;
            try {
                IOUtils.cacheObject(img, FileUtils.getCacheDirFile("/index_dogs"));
            } catch (IOException e) {
                Log.error(e);
            }
            Message message = new Message();
            message.what = 4;
            message.obj = img;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后更新并
     *
     * @param xml
     */
    private void addDogs(ArrayList<Map<String, Object>> xml) {
        if (canceled) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        loadDogs(fragmentTransaction);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 启动或刷新后更新
     */
    private void loadGoods() {
        if (true)
            return;
        try {
            final List<Map<String, Object>> goods = (List<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_goods"));
//            ArrayList<ImageIconGroupFragment.Entity> list3 = new ArrayList<ImageIconGroupFragment.Entity>();
//            for (int i = 0; i < goods.size(); i++) {
//                final int idx = 0;
//                list3.add(new ImageIconGroupFragment.Entity(goods.get(i).get("image").toString(), null, new View.OnClickListener() {
//                    Map<String, Object> item = (Map<String, Object>) goods.get(idx);
//
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent();
//                        intent.setClass(view.getContext(), SaleItemActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("data", (Serializable) item);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                }));
//            }
//            ImageIconGroupFragment fragment = new ImageIconGroupFragment();
//            fragment.init(list3, 2, true);
//            fragmentTransaction.replace(R.id.index_adv, fragment);
            //addGoods(goods);
//            helper.setOnClickListener(R.id.sale, SaleActivity.class);
        } catch (IOException e) {
            Log.error(2);
        }
    }


    /**
     * 刷新商品
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    /*
    private void refreshGoods(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            Message message;
            List<Map<String, Object>> goods = (List<Map<String, Object>>) xmlParser.getDatas().get("data.goods.item");
            try {
                IOUtils.cacheObject(goods, FileUtils.getCacheDirFile("/index_goods"));
            } catch (IOException e) {
                Log.error(e);
            }

            message = new Message();
            message.what = 1;
            message.obj = goods;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }
*/

    /**
     * 刷新后载入
     *
     * @param list
     */
    /**
     private void addGoods(List<Map<String, Object>> list) {
     if (canceled) {
     return;
     }
     //        FragmentManager fragmentManager = getFragmentManager();
     //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
     //        loadGoods(fragmentTransaction);
     //        fragmentTransaction.commitAllowingStateLoss();
     if (list == null) {
     return;
     }
     try {
     final Map<String, Object> data = list.get(0);
     Log.info(data.get("image"));
     helper.setImage(R.id.sc_left, (String) data.get("image"));
     helper.setText(R.id.sc_mc1, (String) data.get("title"));
     helper.setText(R.id.sc_scj1, (String) data.get("scjg"));
     helper.setText(R.id.sc_hyj1, (String) data.get("hyj"));
     helper.setOnClickListener(R.id.sc_left, new View.OnClickListener() {
    @Override public void onClick(View v) {
    Intent intent = new Intent();
    intent.setClass(getActivity(), SaleItemActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("data", (Serializable) data);
    intent.putExtras(bundle);
    startActivity(intent);
    }
    });

     final Map<String, Object> data2 = list.get(1);
     helper.setImage(R.id.sc_right, data2.get("image").toString());
     helper.setText(R.id.sc_mc2, (String) data2.get("title"));
     helper.setText(R.id.sc_scj2, (String) data2.get("scjg"));
     helper.setText(R.id.sc_hyj2, (String) data2.get("hyj"));
     helper.setOnClickListener(R.id.sc_right, new View.OnClickListener() {
    @Override public void onClick(View v) {
    Intent intent = new Intent();
    intent.setClass(getActivity(), SaleItemActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("data", (Serializable) data2);
    intent.putExtras(bundle);
    startActivity(intent);
    }
    });
     } catch (Exception e) {

     Log.error(e);
     }
     }
     **/

    /**
     * 载入公告
     *
     * @param fragmentTransaction
     */
    private void loadNews(FragmentTransaction fragmentTransaction) {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_item"));
            } catch (IOException e) {
                Log.error(e);
                datas = new ArrayList<Map<String, Object>>();
            }

            listFragment = DataView.newInstance(datas, R.layout.index_news_item);
            fragmentTransaction.replace(R.id.index_listview, listFragment);
            listFragment.setItemClickListener(new DataView.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.setAction(WebActivity.VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    Log.info(position + " " + listFragment.getAdapter().getData());
                    intent.putExtra("url", listFragment.getAdapter().getData().get(position).get("url").toString());
                    intent.putExtra("title", "最新公告");
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshNews(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List<Map<String, Object>> item = (List<Map<String, Object>>) xmlParser.getDatas().get("data.list.item");
            Log.info(item);
            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_item"));
            } catch (IOException e) {
                Log.error(e);
            }
            Message message = new Message();
            message.what = 0;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param list
     */
    private void addNews(List list) {
//        listFragment.clearAllData();
        listFragment.refresh(list);
//        listFragment.getmAdapter().notifyDataSetChanged();
    }


    /**
     * 载入公告
     */
    private void loadSlph() {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_slph"));
                Log.info("ooooooooooooooooooooooo,,,,,,,,,,,,,,,,,,,,,,,,,,:" + datas);
            } catch (IOException e) {
                e.printStackTrace();

            }

            if (datas == null) {
                datas = new ArrayList<Map<String, Object>>();
            }

//        slphFragment = ListFragment.newInstance(getClass().getName(), datas, R.layout.index_ph_item, false);
//        fragmentTransaction.replace(R.id.index_slph, slphFragment);
//        slphFragment.setItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
//                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
//                intent.putExtra("url", listFragment.data(position).get("url").toString());
//                startActivity(intent);
//            }
//        });

            addSlph(datas);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshSlph(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List<Map<String, Object>> item = (List<Map<String, Object>>) xmlParser.getDatas().get("data.slph.item");
            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_slph"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 6;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param datas
     */
    private void addSlph(List<Map<String, Object>> datas) {

        try {
            if (datas.size() > 0) {
                if (getActivity().findViewById(R.id.list_sl1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl1_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(0);
                helper.setText(R.id.list_sl1_title, data.get("title").toString());
                helper.setText(R.id.list_sl1_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_sl1_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_sl1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl1_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 1) {
                if (getActivity().findViewById(R.id.list_sl2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl2_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(1);
                helper.setText(R.id.list_sl2_title, data.get("title").toString());
                helper.setText(R.id.list_sl2_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_sl2_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_sl2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl2_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 2) {
                if (getActivity().findViewById(R.id.list_sl3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl3_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(2);
                helper.setText(R.id.list_sl3_title, data.get("title").toString());
                helper.setText(R.id.list_sl3_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_sl3_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_sl3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_sl3_title).getParent()).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.error(e);
        }

//        slphFragment.getmAdapter().notifyDataSetChanged();
    }


    /**
     * 载入公告
     */
    private void loadHjph() {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_hjph"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (datas == null) {
                datas = new ArrayList<Map<String, Object>>();
            }

//
//        hjphFragment.setItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
//                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
//                intent.putExtra("url", listFragment.data(position).get("url").toString());
//                startActivity(intent);
//            }
//        });
            addHjph(datas);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshHjph(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List<Map<String, Object>> item = (List<Map<String, Object>>) xmlParser.getDatas().get("data.hjph.item");
            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_hjph"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 7;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param datas
     */
    private void addHjph(List<Map<String, Object>> datas) {

        try {
            if (datas.size() > 0) {
                if (getActivity().findViewById(R.id.list_hd1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd1_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(0);
                helper.setText(R.id.list_hd1_title, data.get("title").toString());
                helper.setText(R.id.list_hd1_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hd1_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hd1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd1_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 1) {
                if (getActivity().findViewById(R.id.list_hd2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd2_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(1);
                helper.setText(R.id.list_hd2_title, data.get("title").toString());
                helper.setText(R.id.list_hd2_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hd2_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hd2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd2_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 2) {
                if (getActivity().findViewById(R.id.list_hd3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd3_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(2);
                helper.setText(R.id.list_hd3_title, data.get("title").toString());
                helper.setText(R.id.list_hd3_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hd3_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hd3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hd3_title).getParent()).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.error(e);
        }
//
//        hjphFragment.clearAllData();
//        hjphFragment.appendDatas(list);
//        hjphFragment.getmAdapter().notifyDataSetChanged();
    }


    /**
     * 载入公告
     */
    private void loadHdslph() {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_hdslph"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (datas == null) {
                datas = new ArrayList<Map<String, Object>>();
            }

            addHdslph(datas);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshHdslph(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List<Map<String, Object>> item = (List<Map<String, Object>>) xmlParser.getDatas().get("data.hdslph.item");
            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_hdslph"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 8;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param datas
     */
    private void addHdslph(List<Map<String, Object>> datas) {

        try {
            if (datas.size() > 0) {
                if (getActivity().findViewById(R.id.list_hdsl1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl1_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(0);
                helper.setText(R.id.list_hdsl1_title, data.get("title").toString());
                helper.setText(R.id.list_hdsl1_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hdsl1_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hdsl1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl1_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 1) {
                if (getActivity().findViewById(R.id.list_hdsl2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl2_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(1);
                helper.setText(R.id.list_hdsl2_title, data.get("title").toString());
                helper.setText(R.id.list_hdsl2_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hdsl2_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hdsl2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl2_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 2) {
                if (getActivity().findViewById(R.id.list_hdsl3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl3_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(2);
                helper.setText(R.id.list_hdsl3_title, data.get("title").toString());
                helper.setText(R.id.list_hdsl3_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_hdsl3_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_hdsl3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_hdsl3_title).getParent()).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 载入公告
     */
    private void loadKwph() {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_kwph"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (datas == null)
                datas = new ArrayList<Map<String, Object>>();

            addKwph(datas);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshKwph(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            Object o = xmlParser.getDatas().get("data.kongph.item");
            List<Map<String, Object>> item = null;

            if (o instanceof List) {
                item = (List<Map<String, Object>>) o;
            } else {
                item = new ArrayList<Map<String, Object>>();
                item.add((Map<String, Object>) o);
            }

            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_kwph"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 9;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param datas
     */
    private void addKwph(List<Map<String, Object>> datas) {

        try {
            if (datas.size() > 0) {
                if (getActivity().findViewById(R.id.list_kw1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw1_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(0);
                helper.setText(R.id.list_kw1_title, data.get("title").toString());
                helper.setText(R.id.list_kw1_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_kw1_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));

                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_kw1_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw1_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 1) {
                if (getActivity().findViewById(R.id.list_kw2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw2_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(1);
                helper.setText(R.id.list_kw2_title, data.get("title").toString());
                helper.setText(R.id.list_kw2_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_kw2_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_kw2_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw2_title).getParent()).setVisibility(View.GONE);
            }
            if (datas.size() > 2) {
                if (getActivity().findViewById(R.id.list_kw3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw3_title).getParent()).setVisibility(View.VISIBLE);
                final Map<String, Object> data = datas.get(2);
                helper.setText(R.id.list_kw3_title, data.get("title").toString());
                helper.setText(R.id.list_kw3_date, data.get("date").toString());
                helper.setOnClickListener(R.id.list_kw3_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", data.get("url").toString());
                        startActivity(IntentUtils.newInstance(getActivity(), QzcxViewActivity.class, bundle));
                    }
                });
            } else {
                if (getActivity().findViewById(R.id.list_kw3_title) != null)
                    ((View) getActivity().findViewById(R.id.list_kw3_title).getParent()).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * 载入公告
     *
     * @param fragmentTransaction
     */
    private void loadMatch(FragmentTransaction fragmentTransaction) {
        try {
            ArrayList<Map<String, Object>> datas = null;
            try {
                datas = (ArrayList<Map<String, Object>>) IOUtils.getCachedObject(FileUtils.getCacheDirFile("/index_match"));
            } catch (IOException e) {
                e.printStackTrace();
                datas = new ArrayList<Map<String, Object>>();
            }

            matchFragment = DataView.newInstance(datas, R.layout.match_index_item_grid);
            Map<String, Integer> mapping = new HashMap<String, Integer>();
            mapping.put("image", R.id.list_item_image);
            mapping.put("title", R.id.list_item_title);
            mapping.put("date", R.id.match_date);
            mapping.put("time", R.id.match_time);
            //mapping.put("jbdw", R.id.match_jbdw);
            //mapping.put("cbdw", R.id.match_cbdw);
            //mapping.put("cp", R.id.match_cp);
            mapping.put("addr", R.id.match_addr);
            matchFragment.setMapping(mapping);
            fragmentTransaction.replace(R.id.index_match, matchFragment);
            matchFragment.setItemClickListener(new DataView.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.setAction(WebActivity.VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra("url", (String) matchFragment.getAdapter().getData().get(position).get("url"));
                    intent.putExtra("title", "赛事报道");
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * 刷新公告
     *
     * @param handler
     * @param xmlParser
     * @throws HttpRequestException
     */
    private void refreshMatch(Handler handler, XMLParser xmlParser) throws HttpRequestException {
        try {
            List<Map<String, Object>> item = (List<Map<String, Object>>) xmlParser.getDatas().get("data.match.item");
            try {
                IOUtils.cacheObject(item, FileUtils.getCacheDirFile("/index_match"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 5;
            message.obj = item;
            handler.sendMessage(message);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 刷新后添加新闻
     *
     * @param list
     */
    private void addMatch(List list) {
//        matchFragment.clearAllData();
        matchFragment.refresh(list);
//        matchFragment.getmAdapter().notifyDataSetChanged();
    }


    @Override
    public Object onRefresh(Handler handler) {
        //new Request().execute();

        if (!DeviceUtils.isNetworkConnected(view.getContext())) {
            cancelRefresh();
            return null;
        }

        request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_index));
        XMLParser xmlParser = new XMLParser();
        try {
            String xml = request.executeToString();
            xmlParser.parse(xml);
            refreshNews(handler, xmlParser);
            //refreshGoods(handler, xmlParser);
            refreshBanner(handler, xmlParser);
            refreshMatch(handler, xmlParser);
            refreshDogs(handler, xmlParser);
//            refreshClub(handler, xmlParser);
            refreshSlph(handler, xmlParser);
            refreshHjph(handler, xmlParser);
            refreshHdslph(handler, xmlParser);
            refreshKwph(handler, xmlParser);
        } catch (Exception e) {
            Log.error(e);
            request = null;
            return e;
        }
        request = null;
        return true;
    }


    @Override
    public void progressUpdate(Object data) {
        Object[] os = (Object[]) data;
        int type = (Integer) os[0];
        switch (type) {
            case 0:
                addNews((List) os[1]);
                break;
            case 1:
                //addGoods((List) os[1]);
                break;
            case 2:
                addBanner((ArrayList<Map<String, Object>>) os[1]);
                break;

            case 4:
                addDogs((ArrayList<Map<String, Object>>) os[1]);
                break;
            case 5:
                addMatch((ArrayList<Map<String, Object>>) os[1]);
                break;
            case 6:
                addSlph((ArrayList<Map<String, Object>>) os[1]);
                break;
            case 7:
                addHjph((ArrayList<Map<String, Object>>) os[1]);
                break;
            case 8:
                addHdslph((ArrayList<Map<String, Object>>) os[1]);
                break;
            case 9:
                addKwph((ArrayList<Map<String, Object>>) os[1]);
        }
    }

    @Override
    public void refreshed(Object os) {
        if ((os == null && !canceled) || os instanceof Exception) {
            LayoutUtils.alert(view.getContext(), "访问失败").show();
            return;
        }

    }


    public void cancelRefresh() {
        if (request != null) {
            request.cancel();
            request = null;
        }
        this.canceled = true;
    }

    @Override
    public boolean isRefreshing() {
        return request != null;
    }


    @Override
    public void onPause() {
        super.onPause();
        this.cancelRefresh();
    }
}

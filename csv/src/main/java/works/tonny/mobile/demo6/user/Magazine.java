package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.utils.ImageRequest;
import works.tonny.mobile.utils.ImageRequestManager;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser2;
import works.tonny.mobile.widget.AbstractListActivity;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.IDLinkedHashMap;

/**
 * Created by tonny on 2016/3/12.
 */
public class Magazine extends Activity {


    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);
        init();
        request(Application.getUrl(R.string.url_magazine));
        TitleHelper.getInstance(this).enableBack().setTitle("电子杂志").setButton(R.drawable.icon_option, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void request(String url) {
        new RequestTask(this, new RequestTask.Requested() {
            @Override
            public void execute(Map<String, Object> map) {
                if (map == null) {
                    Toast.makeText(Magazine.this, "没有杂志", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (map.get("s2m.body.tag") != null) {
                    Toast.makeText(Magazine.this, ((Map) map.get("s2m.body.tag")).get("value").toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                Object o = map.get("data.list.item");
                String years = (String) map.get("data.menu.years");
                Message m = new Message();
                m.obj = years;
                handler.sendMessage(m);

                ViewGroup group = (ViewGroup) findViewById(R.id.list);
                group.removeAllViews();
                if (o instanceof Map) {
                    View view = getLayoutInflater().inflate(R.layout.user_magazie_item, null);
                    final ImageView img = (ImageView) view.findViewById(R.id.list_item_right);
                    TextView title = (TextView) view.findViewById(R.id.list_item_title);
                    ImageRequestManager.getInstance().addTask(new ImageRequest(((Map) o).get("img").toString(), new ImageRequest.SetImage(img)));
//                    new ImageRequest(new ImageRequest.OnRequested() {
//                        @Override
//                        public void execute(File file) {
//                            ActivityHelper.setImage(img, file.getAbsolutePath());
//                        }
//                    }).execute(((Map) o).get("img").toString());

                    title.setText(((Map) o).get("name").toString());
                    final Map item = (Map) o;
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item));
                        }
                    });
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item));
                        }
                    });
                } else {
                    List l = (List) o;
//                    Log.info(l.size());
                    if (l == null) {
                        return;
                    }
                    for (int i = 0; i < l.size(); i++) {
                        final Map item = (Map) l.get(i);
                        View view = getLayoutInflater().inflate(R.layout.user_magazie_item, null);
                        group.addView(view);
                        final ImageView img = (ImageView) view.findViewById(R.id.list_item_right);
                        TextView title = (TextView) view.findViewById(R.id.list_item_title);
//                        new ImageRequest(img).execute(((Map) l.get(i)).get("img").toString());
                        ImageRequestManager.getInstance().addTask(new ImageRequest(((Map) l.get(i)).get("img").toString(), new ImageRequest.SetImage(img)));
//                        new ImageRequest(new ImageRequest.OnRequested() {
//                            @Override
//                            public void execute(File file) {
//                                ActivityHelper.setImage(img, file.getAbsolutePath());
//                            }
//                        }).execute(((Map) l.get(i)).get("img").toString());

                        title.setText(((Map) l.get(i)).get("name").toString());
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item));
                            }
                        });
                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item));
                            }
                        });
                        i++;
                        if (i >= l.size()) {
                            return;
                        }
                        final Map item1 = (Map) l.get(i);
                        if (i < l.size()) {
                            final ImageView img1 = (ImageView) view.findViewById(R.id.list_item_right1);
                            TextView title1 = (TextView) view.findViewById(R.id.list_item_title1);
//                            new ImageRequest(img1).execute(((Map) l.get(i)).get("img").toString());
                            ImageRequestManager.getInstance().addTask(new ImageRequest(((Map) l.get(i)).get("img").toString(), new ImageRequest.SetImage(img1)));
//                            new ImageRequest(new ImageRequest.OnRequested() {
//                                @Override
//                                public void execute(File file) {
//                                    ActivityHelper.setImage(img1, file.getAbsolutePath());
//                                }
//                            }).execute(((Map) l.get(i)).get("img").toString());

                            title1.setText(((Map) l.get(i)).get("name").toString());
                            img1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item1));
                                }
                            });
                            title1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(IntentUtils.newInstance(Magazine.this, MagazineViewActivity.class, item1));
                                }
                            });
                        }
                    }
                }

            }
        }).execute(url);
    }


    //    @Override
    protected void init() {

        initPopupWindow();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String years = (String) msg.obj;
                ViewGroup menu = (ViewGroup) mPopupWindowView.findViewById(R.id.menu);
                if (menu.getChildCount() > 0) {
                    return;
                }
                String[] strings = years.split(",");
                for (final String string : strings) {
                    View inflate = LayoutInflater.from(Magazine.this).inflate(R.layout.user_mgz_dialog_item, null);
                    TextView text = (TextView) inflate.findViewById(R.id.text);
                    text.setText(string);
                    inflate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            request(Application.getUrl(R.string.url_magazine) + "?years=" + string);
//                            listFragment.refresh();
                            popupWindow.dismiss();
                        }
                    });

                    menu.addView(inflate);
                }
                super.handleMessage(msg);
            }
        };
//
//        setOnRequested(new OnRequested() {
//            @Override
//            public void requested(XMLParser2 parser) {
//                String years = (String) parser.get("data.menu.years");
//                Message m = new Message();
//                m.obj = years;
//                handler.sendMessage(m);
//                List<Object> list = parser.getList("data.list.item");
//                List<Map> n = new ArrayList<Map>();
//                for (int i = 0; i < list.size(); i++) {
//                    Map map = new IDLinkedHashMap();
//                    Map o = (Map) list.get(i);
//                    map.put("img1", o.get("img"));
//                    map.put("name1", o.get("name"));
//                    i++;
//                    if (i < list.size()) {
//                        o = (Map) list.get(i);
//                        map.put("img2", o.get("img"));
//                        map.put("name2", o.get("name"));
//                    }
//                    n.add(map);
//                }
//                parser.datas.put("list", n);
////                DataView dataView = DataView.newInstance(list, R.layout.user_mgz_dialog_item);
////                Map<String, Integer> mapping = new HashMap<String, Integer>();
////                mapping.put("year", R.id.text);
////                dataView.setMapping(mapping);
////                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
////                fragmentTransaction.replace(R.id.menu, dataView);
////                fragmentTransaction.commit();
//
//
//            }
//        });
    }

    protected int getListReplaceId() {
        return R.id.list;
    }

    private PopupWindow popupWindow;

    private RelativeLayout mPopupWindowView;


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
        popupWindow.update();
        //popupWindow调用dismiss时触发，设置了setOutsideTouchable(true)，点击view之外/按键back的地方也会触发
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });
    }

    /**
     * 初始化popupwindowView,监听view中的textview点击事件
     */
    private void initPopupWindowView() {
        mPopupWindowView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.user_mgz_dialog, null);
//        TextView search = (TextView) mPopupWindowView.findViewById(R.id.search);
//        search.setOnClickListener(this);
//        TextView textview_file = (TextView) mPopupWindowView.findViewById(R.id.apply);
//        textview_file.setOnClickListener(this);
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

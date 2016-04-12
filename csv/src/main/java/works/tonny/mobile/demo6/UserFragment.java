package works.tonny.mobile.demo6;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.demo6.user.Magazine;
import works.tonny.mobile.demo6.user.MessageActivity;
import works.tonny.mobile.demo6.user.Nearby;
import works.tonny.mobile.demo6.user.PayActivity;
import works.tonny.mobile.demo6.user.PayListActivity;
import works.tonny.mobile.demo6.user.RechargeActivity;
import works.tonny.mobile.demo6.user.SettingActivity;
import works.tonny.mobile.demo6.user.UserActivity;
import works.tonny.mobile.demo6.user.YoujiActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.LoadingDialog;


/**
 *
 */
public class UserFragment extends Fragment {

    private ViewGroup inflate;
    private TableRow userSetting;
    private ImageView imageView;
    private LoadingDialog loadingDialog;
    private ActivityHelper instance;
    private String settingUrl;
    private boolean logined;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        inflate = (ViewGroup) inflater.inflate(R.layout.index_fragment_user, container, false);
        TitleHelper.getInstance(inflate).setTitle("设置");

        instance = ActivityHelper.getInstance(inflate);


        imageView = (ImageView) inflate.findViewById(R.id.user_head);
        logined = Application.getUser() != null;
//        if (logined) {
////            new Request().execute();
//            LoginActivity.startLoginActivity(getActivity(), null);
//        }
//        setUser();
        if (Application.getUser() != null) {
            new Request().execute();
        }
        return inflate;
    }


    @Override
    public void onResume() {
        super.onResume();
        setUser();
    }

    private void setUser() {

        try {
            Log.info(Application.getUser());
            if (Application.getUser() != null) {
                instance.setOnClickListener(R.id.user, UserActivity.class);
                instance.setOnClickListener(R.id.fj, Nearby.class);

                instance.setVisible(R.id.message, true);
                instance.setOnClickListener(R.id.message, MessageActivity.class);
                instance.setOnClickListener(R.id.recharge, RechargeActivity.class);
//                instance.setOnClickListener(R.id.cart, CartActivity.class);
//                instance.setOnClickListener(R.id.order, OrderActivity.class);
                instance.setOnClickListener(R.id.pay_list, PayListActivity.class);
                instance.setOnClickListener(R.id.pay, PayActivity.class);
                instance.setOnClickListener(R.id.setting, SettingActivity.class);
                if (!logined) {
                    Log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    new Request().execute();
                }
                logined = true;
                instance.setImage(R.id.user_head, Application.getUser().getHeader());
                instance.setText(R.id.nickname, Application.getUser().getName());
                instance.setText(R.id.quanshe_name, Application.getUser().getProperty("qs"));
                instance.setOnClickListener(R.id.magazine, Magazine.class);
                instance.setOnClickListener(R.id.youji, YoujiActivity.class);
                logined = Application.getUser() != null;
                return;
            }

            instance.setOnClickListener(R.id.user, LoginActivity.class);
            instance.setOnClickListener(R.id.user_head, LoginActivity.class);
            instance.setOnClickListener(R.id.message, LoginActivity.class);
            instance.setOnClickListener(R.id.recharge, LoginActivity.class);
            instance.setOnClickListener(R.id.fj, LoginActivity.class);
//            instance.setOnClickListener(R.id.cart, LoginActivity.class);
//            instance.setOnClickListener(R.id.order, LoginActivity.class);
            instance.setOnClickListener(R.id.pay_list, LoginActivity.class);
            instance.setOnClickListener(R.id.pay, LoginActivity.class);
            instance.setOnClickListener(R.id.setting, LoginActivity.class);
            instance.setText(R.id.nickname, "请登录");
            instance.setText(R.id.quanshe_name, "");
            instance.setImage(R.id.user_head, R.drawable.noface);
//            instance.setVisible(R.id.quanshe, false);
            instance.setVisible(R.id.message, false);

            logined = Application.getUser() != null;
            logined = false;
        } catch (Exception e) {
            Log.error(e);
        }
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {


        @Override
        protected void onPostExecute(Map<String, Object> data) {
            try {
//                loadingDialog.dismiss();
//            setUser(data);
                if (data == null) {

                    return;
                }

                settingUrl = (String) ((Map) data.get("data.item[4]")).get("url");
                Map<String, Object> url = new HashMap<String, Object>();
                url.put("url", settingUrl);
                instance.setOnClickListener(R.id.setting, SettingActivity.class, url);
                String uri = (String) data.get("data.info.img");
                Application.getUser().setHeader(uri);
                String name = (String) data.get("data.info.name");
                String title = (String) data.get("data.info.title");
                Application.getUser().setName(name);
                Application.saveUser(Application.getUser());
                Application.getUser().addProperty("qs", title);
                instance.setText(R.id.quanshe_name, title);
                String num = (String) data.get("data.info.mnum");
                instance.setVisible(R.id.message, true);
                if (num != null && !num.equals("0")) {
//                    instance.setImage(R.id.message_num, R.drawable.num_bg);
                    instance.setVisible(R.id.message_num_bg, true);
                    instance.setText(R.id.message_num, num);
                }
                String mailnum = (String) data.get("data.info.mailnum");
//                instance.setVisible(R.id.message, true);
                if (num != null && !num.equals("0")) {
                    instance.setImage(R.id.mail_image, R.drawable.num_bg);
                    instance.setText(R.id.mail_num, mailnum);
//                    instance.setVisible(R.id.message_num, true);
                }

//                instance.setVisible(R.id.quanshe, true);
//                instance.setImage(R.id.user_head, Application.getUser().getHeader());
                instance.setText(R.id.nickname, Application.getUser().getName());
            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loadingDialog = new LoadingDialog(inflate.getContext());
//            loadingDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            loadingDialog.dismiss();
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(UserFragment.this.getActivity(), null);
                logined = false;
                this.cancel(true);
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            if (!DeviceUtils.isNetworkConnected(inflate.getContext())) {
                this.cancel(true);
            }
            HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_user));
            try {
                XMLParser xmlParser = new XMLParser();
                String xml = request.executeToString();
                xmlParser.parse(xml);
                IOUtils.cacheObject(xmlParser.getDatas(), FileUtils.getCacheDirFile("/user"));
                return (Map<String, Object>) xmlParser.getDatas();
            } catch (AuthException e) {
                e.printStackTrace();
                this.publishProgress(-1);
                return null;
            } catch (Exception e) {
                Log.error(e);
            }
            request = null;
            return null;
        }
    }

}

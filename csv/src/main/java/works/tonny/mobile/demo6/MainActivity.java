package works.tonny.mobile.demo6;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.demo6.query.QueryFragment;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.Refreshable;


public class MainActivity extends Activity {
    private TabHost tabHost;

    private RadioGroup radioGroup;

    private Map<String, Fragment> maps = new HashMap<String, Fragment>();

    private long mExitTime;

    private IndexFragment indexFragment;

    private boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            tabHost = (TabHost) findViewById(R.id.tabHost);
            radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
            radioGroup.setOnCheckedChangeListener(listener);

//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            if (indexFragment == null) {
//                indexFragment = new IndexFragment();
//            }
//            maps.put("home", indexFragment);
//            fragmentTransaction.add(R.id.content_fragment_container, indexFragment);
//            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.error(e);
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (radioGroup.getCheckedRadioButtonId() != R.id.radio_home) {
                RadioButton radio = (RadioButton) findViewById(R.id.radio_home);
                radio.setChecked(true);
            } else if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Refreshable r = (Refreshable) maps.get("home");
                if (r.isRefreshing()) {
                    r.cancelRefresh();
                    return true;
                }
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != R.id.radio_home)
                switchToHome = false;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment top = null;
            switch (checkedId) {
                case R.id.radio_home:
                    if (!maps.containsKey("home")) {
                        top = new IndexFragment();
                        maps.put("home", top);
                    }
                    top = maps.get("home");
                    tabHost.setCurrentTabByTag("home");
                    break;
                case R.id.radio_fz:
                    if (!maps.containsKey("fz")) {
                        top = new BreedFragment();
                        maps.put("fz", top);
                    }
                    top = maps.get("fz");
                    break;
                case R.id.radio_sl:
                    if (!maps.containsKey("sl")) {
                        top = new MatchFragment();
                        maps.put("sl", top);
                    }
                    top = maps.get("sl");
                    tabHost.setCurrentTabByTag("sl");
                    break;
                case R.id.radio_cx:
                    if (!maps.containsKey("cx")) {
                        top = new QueryFragment();
                        maps.put("cx", top);
                    }
                    top = maps.get("cx");
                    tabHost.setCurrentTabByTag("cx");
                    break;
                case R.id.radio_user:
                    if (!maps.containsKey("user")) {
                        top = new UserFragment();
                        maps.put("user", top);
                    }
                    top = maps.get("user");
                    tabHost.setCurrentTabByTag("user");
                    break;
            }

            fragmentTransaction.replace(R.id.content_fragment_container, top);
            fragmentTransaction.commit();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
//        radioGroup.check(R.id.radio_home);
        if (!loaded) {
            RadioButton button = (RadioButton) radioGroup.findViewById(R.id.radio_home);
            button.setChecked(true);
            loaded = true;
        }
        if (switchToHome) {
            Log.info("xxxxxxxxxxxxxxxxxxxxxxxxxx");
            RadioButton radio = (RadioButton) findViewById(R.id.radio_home);
            radio.setChecked(true);
//            switchToHome = false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        switchToHome = false;
    }

    public static boolean switchToHome;

    public static void home() {
        switchToHome = true;
        Log.info("UUUUUUUUUUUUUUUUUUUUUUUUU");
    }
}

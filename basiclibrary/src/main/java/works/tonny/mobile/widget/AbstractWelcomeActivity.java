package works.tonny.mobile.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import works.tonny.mobile.Launcher;

/**
 * Created by tonny on 2016/2/21.
 */
public abstract class AbstractWelcomeActivity extends Activity {
    private Handler handler = new Handler();
    protected long delay = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Launcher.init(this.getBaseContext());
        final Runnable task = new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(AbstractWelcomeActivity.this, getMainClass());
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(task, delay);//延迟调用
        init();
    }

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 转向地址
     *
     * @return
     */
    protected abstract Class getMainClass();
}

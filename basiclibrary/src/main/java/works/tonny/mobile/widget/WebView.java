package works.tonny.mobile.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Method;

/**
 * Created by tonny on 2015/7/3.
 */
public class WebView extends android.webkit.WebView {
    private ZoomButtonsController zoomController = null;
    public WebView(Context context) {
        super(context);
        disableControls();
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableControls();
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableControls();
    }

    /**
     * Disable the controls
     */
    private void disableControls() {
        WebSettings settings = this.getSettings();
        //基本的设置
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);//support zoom
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);

        this.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //去掉滚动条
        this.setVerticalScrollBarEnabled(true);
        this.setHorizontalScrollBarEnabled(false);

        //去掉缩放按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Use the API 11+ calls to disable the controls
            this.getSettings().setBuiltInZoomControls(true);
            this.getSettings().setDisplayZoomControls(false);
        } else {
            // Use the reflection magic to make it work on earlier APIs
            getControlls();
        }
    }

    /**
     * This is where the magic happens :D
     */
    private void getControlls() {
        try {
            Class webview = Class.forName("android.webkit.WebView");
            Method method = webview.getMethod("getZoomButtonsController");
            zoomController = (ZoomButtonsController) method.invoke(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

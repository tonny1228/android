package works.tonny.mobile.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import works.tonny.mobile.R;

public class WebViewActivity extends Activity {

    public static final String VIEW = "works.tonny.mobile.widget.WebViewActivity.VIEW";


    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(VIEW);
        intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = (WebView) findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("url");
        int titleLayout = getIntent().getIntExtra("titleLayout", 0);
        if (titleLayout > 0) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            View inflate = WebViewActivity.this.getLayoutInflater().inflate(titleLayout, parent);
            parent.addView(inflate, 0);
        }
        webView.loadUrl(url);
    }


}

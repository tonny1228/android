package works.tonny.mobile.demo6;

import android.os.Bundle;

import works.tonny.mobile.widget.WebView;

/**
 * Created by tonny on 2015/9/17.
 */
public class WebActivity extends works.tonny.mobile.widget.WebViewActivity {
    public static final String VIEW = "works.tonny.demo6.WebActivity.VIEW";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = (WebView) findViewById(works.tonny.mobile.R.id.web_view);
        TitleHelper.getInstance(this).enableBack().setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");

        webView.loadUrl(url);
    }

}

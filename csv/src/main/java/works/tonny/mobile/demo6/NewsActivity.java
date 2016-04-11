package works.tonny.mobile.demo6;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.widget.ListFragment;


public class NewsActivity extends ListActivity {
    private ListFragment listFragment;

    private boolean loaded;

    @Override
    protected String getListTitle() {
        return "最新公告";
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_news_list);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                intent.putExtra("url", ((Map<String, Object>) parent.getItemAtPosition(position)).get("url").toString());
                intent.putExtra("title", "最新公告");
                startActivity(intent);
            }
        };
    }

    @Override
    protected int getItemLayout() {
        return R.layout.index_news_item;
    }

    @Override
    protected boolean loadMore() {
        return true;
    }

}

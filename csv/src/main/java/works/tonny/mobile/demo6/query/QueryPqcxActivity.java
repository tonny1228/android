package works.tonny.mobile.demo6.query;

import android.widget.AdapterView;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;


public class QueryPqcxActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "种公犬配犬信息查询";
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_query_pqcx).replaceAll("key", getIntent().getStringExtra("key"));
    }

    @Override
    protected int getLayout() {
        return R.layout.query_result_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return null;
    }


    @Override
    protected int getItemLayout() {
        return R.layout.query_result_item;
    }
}

package works.tonny.mobile.demo6.breed;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;


public class PeiQuanxxActivity extends BreedListActivity {

    @Override
    protected String getListTitle() {
        return "配犬信息提交列表";
    }

    @Override
    protected boolean isAddEnabled() {
        return true;
    }

    @Override
    protected Class getAddActivityClass() {
        return PeiQuanxxAddActivity.class;
    }

    @Override
    protected Class getViewActivityClass() {
        return PeiquanxxViewActivity.class;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.breed_pqxx_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_peiquanzmlist);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("title", R.id.list_item_title);
        mapping.put("fblood", R.id.list_item_summary);
        mapping.put("mblood", R.id.mq);
        mapping.put("date", R.id.list_item_date);
        return mapping;
    }
}

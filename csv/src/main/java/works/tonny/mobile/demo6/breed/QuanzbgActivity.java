package works.tonny.mobile.demo6.breed;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;

public class QuanzbgActivity extends BreedListActivity {

    private Map<String, Integer> mapping;

    @Override
    protected String getListTitle() {
        return "犬主变更";
    }

    @Override
    protected boolean isAddEnabled() {
        return false;
    }

    @Override
    protected Class getAddActivityClass() {
        return null;
    }

    @Override
    protected Class getViewActivityClass() {
        return null;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.breed_qzbg_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_quanzbg);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        if (mapping == null) {
            mapping = new HashMap<String, Integer>();
            mapping.put("name", R.id.list_item_title);
            mapping.put("summary", R.id.list_item_eh);
            mapping.put("title", R.id.list_item_yqz);
            mapping.put("date", R.id.list_item_state);
        }
        return mapping;
    }

}


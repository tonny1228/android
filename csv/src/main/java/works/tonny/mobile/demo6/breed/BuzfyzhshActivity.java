package works.tonny.mobile.demo6.breed;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;

public class BuzfyzhshActivity extends BreedListActivity {

    @Override
    protected String getListTitle() {
        return "补做繁育证书";
    }

    @Override
    protected Class getViewActivityClass() {
        return null;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.breed_buzfyzhsh_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_buzfyzs);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        return null;
    }


}

package works.tonny.mobile.demo6.breed;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;


public class PeiqzhmActivity extends BreedListActivity {


    @Override
    protected String getListTitle() {
        return "申请配犬证明";
    }

    @Override
    protected boolean isAddEnabled() {
        return true;
    }

    @Override
    protected Class getAddActivityClass() {
        return PeiqzmAddActivity.class;
    }

    @Override
    protected Class getViewActivityClass() {
        return PeiqzmViewActivity.class;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.breed_pqzmsq_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_peiquanzhengming);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        return null;
    }

}

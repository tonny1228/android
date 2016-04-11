package works.tonny.mobile.demo6.breed;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;


public class DNAActivity extends BreedListActivity {

    @Override
    protected String getListTitle() {
        return "DNA检测申请";
    }

    @Override
    protected boolean isAddEnabled() {
        return true;
    }

    @Override
    protected Class getAddActivityClass() {
        return DNAAddActivity.class;
    }

    @Override
    protected Class getViewActivityClass() {
        return DNAViewActivity.class;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.breed_dna_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_dnalist);
    }


    @Override
    protected Map<String, Integer> getMapping() {
        return null;
    }
}

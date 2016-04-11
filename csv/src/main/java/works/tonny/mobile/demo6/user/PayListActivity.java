package works.tonny.mobile.demo6.user;

import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;

/**
 * Created by tonny on 2015/7/22.
 */
public class PayListActivity extends ListActivity {
    @Override
    protected String getListTitle() {
        return "我的缴费";
    }

    @Override
    protected int getItemLayout() {
        return R.layout.user_pay_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_user_paylist);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("td", R.id.title);
        mapping.put("money", R.id.je);
        mapping.put("date", R.id.rq);
        mapping.put("changetype", R.id.zfb);
        mapping.put("state", R.id.zt);
        return mapping;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return null;
    }

}
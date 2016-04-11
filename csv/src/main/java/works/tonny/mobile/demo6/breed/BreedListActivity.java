package works.tonny.mobile.demo6.breed;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.MainActivity;
import works.tonny.mobile.demo6.R;

/**
 * Created by tonny on 2015/8/6.
 */
public abstract class BreedListActivity extends ListActivity {
    protected String getButtonText() {
        return "申请";
    }


    protected abstract Class getViewActivityClass();

    @Override
    protected boolean loadMore() {
        return false;
    }

    protected int getButtonIcon() {
        return 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Application.getUser() == null && !MainActivity.switchToHome) {
            LoginActivity.startLoginActivity(this, null);
            return;
        }
        if (!loaded) {
            listFragment.refresh();
            loaded = true;
        }
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        if (getViewActivityClass() == null) {
            return null;
        }
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", (String) item.get("url"));
                startActivity(IntentUtils.newInstance(BreedListActivity.this, getViewActivityClass(), bundle));
            }
        };
    }

    @Override
    protected int getLayout() {
        return R.layout.breed_list_activity;
    }
}

package works.tonny.mobile.demo6.user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.RequestActivity;
import works.tonny.mobile.widget.ZoomImagePaperFragment;

public class MagazineViewActivity extends RequestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_magazine_view);
        TitleHelper.getInstance(this).enableBack().setTitle(getIntent().getStringExtra("name"));

    }

    @Override
    protected void create() {
        new Request().execute(getIntent().getStringExtra("url"));
    }

    @Override
    protected void executeResult(Map<String, Object> result) {
        ArrayList<ZoomImagePaperFragment.Entity> list = new ArrayList<ZoomImagePaperFragment.Entity>();
        try {
            ArrayList<Map<String, Object>> img = (ArrayList<Map<String, Object>>) result.get("data.list.item");
            for (int i = 0; img != null && i < img.size(); i++) {
                String image = ((Map) img.get(i)).get("img").toString();

                Map<String, String> data = new HashMap<String, String>();
//                data.put("url", url);
//                data.put("title", "头条");
                ZoomImagePaperFragment.Entity entity = new ZoomImagePaperFragment.Entity(image, (String) ((Map) img.get(i)).get("title"), null, works.tonny.mobile.demo6.WebActivity.VIEW, data);
                list.add(entity);
            }
        } catch (Exception e) {
            Log.error(e);
//            list.add(new ImageViewPaperFragment.Entity(R.drawable.header, "加入我们", null, null, null));
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ZoomImagePaperFragment fragment = ZoomImagePaperFragment.newInstance(list);
        fragmentTransaction.replace(R.id.list, fragment);
        fragmentTransaction.commit();

    }


}

package works.tonny.mobile.demo6;

/**
 * Created by tonny on 2015/8/19.
 */
public abstract class ListActivity extends works.tonny.mobile.widget.ListActivity {

    @Override
    public int getTitleTextId() {
        return R.id.titleText;
    }

    @Override
    protected int getListReplaceId() {
        return R.id.list;
    }

    @Override
    protected int getTitleButtonTextStyle() {
        return R.style.activity_title_button;
    }

    @Override
    protected int getTitleButtonId() {
        return R.id.list_tool_button;
    }

    @Override
    protected int getTitleGoBackIcon() {
        return R.id.goback;
    }
}
package works.tonny.mobile.demo6.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.WebActivity;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.AbstractListActivity;

public class YoujiActivity extends AbstractListActivity {

    private TitleHelper titleHelper;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_list;
    }

    public YoujiActivity() {
        setUrl(Application.getUrl(R.string.url_youji));
        setItemLayout(R.layout.user_youji_item);
        addMapping("title", R.id.list_item_title);
        addMapping("content", R.id.list_item_content);
        addMapping("state", R.id.list_item_state);
        addMapping("date", R.id.list_item_date);
        addMapping("check", R.id.checkbox);


        setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listFragment.getmAdapter().setEditMode(!listFragment.getmAdapter().isEditMode());

                if (listFragment.getmAdapter().isEditMode()) {
                    titleHelper.setButton("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(YoujiActivity.this).setTitle("删除吗？").setCancelable(true).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    List<Integer> checked = listFragment.getmAdapter().getChecked();
                                    if (checked.isEmpty()) {
                                        Toast.makeText(YoujiActivity.this, "没有选择数据", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    StringBuilder builder = new StringBuilder();
                                    for (Integer integer : checked) {
                                        builder.append(listFragment.getmAdapter().getData().get(integer).get("id")).append(",");
                                    }
                                    if (builder.length() > 0) {
                                        builder.deleteCharAt(builder.length() - 1);
                                    }

                                    new RequestTask(YoujiActivity.this, new RequestTask.Requested() {
                                        @Override
                                        public void execute(Map<String, Object> map) {
                                            //Toast.makeText(MessageActivity.this, listFragment.getmAdapter().getChecked().toString(), Toast.LENGTH_SHORT).show();
                                            listFragment.getmAdapter().setEditMode(false);
                                            titleHelper.removeButton();
                                            listFragment.refresh();
                                        }
                                    }).execute(Application.getUrl(R.string.url_youji_delete) + builder.toString());
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                        }
                    });
                } else {
                    titleHelper.removeButton();
                }
//                CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
//                if (Boolean.valueOf(true).equals(listFragment.getmAdapter().getData().get(position).get("check"))) {
//                    checked.remove(position);
//                    listFragment.getmAdapter().getData().get(position).put("check", false);
//                    checkbox.setVisibility(View.INVISIBLE);
//                    checkbox.setChecked(false);
//                } else {
//                    checked.put(position, true);
//                    listFragment.getmAdapter().getData().get(position).put("check", true);
//                    checkbox.setVisibility(View.VISIBLE);
//                    checkbox.setChecked(true);
//                }
                return true;
            }
        });
    }


    @Override
    protected void init() {
        titleHelper = TitleHelper.getInstance(this);
        titleHelper.enableBack().setTitle("邮寄查询");
    }

    @Override
    protected int getListReplaceId() {
        return R.id.list;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && listFragment.getmAdapter().isEditMode()) {
            listFragment.getmAdapter().setEditMode(false);
            titleHelper.removeButton();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("url", (String) item.get("url"));
                Log.info(item);
                item.put("title", "追踪");


                final String re = (String) item.get("return");

                RequestTask requestTask = new RequestTask(YoujiActivity.this, new RequestTask.Requested() {
                    @Override
                    public void execute(Map<String, Object> map) {
                        Intent intent = new Intent();
                        intent.setAction(WebActivity.VIEW);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.putExtra("url", (String) item.get("url"));
                        intent.putExtra("title", "邮寄追踪");
                        startActivity(intent);
                    }
                });
                requestTask.execute(re);
            }
        };
    }


//    @Override
//    protected boolean loadMore() {
//        return true;
//    }
//
//
//    @Override
//    protected Map<String, Integer> getMapping() {
//        Map<String, Integer> mapping = new HashMap<String, Integer>();
//        mapping.put("id", R.id.list_item_title);
//        mapping.put("content", R.id.list_item_content);
//        mapping.put("state", R.id.list_item_state);
//        mapping.put("date", R.id.list_item_date);
//        return mapping;
//    }


}
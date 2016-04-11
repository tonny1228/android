package works.tonny.mobile.demo6.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.AbstractListActivity;
import works.tonny.mobile.widget.Refreshable;

public class MessageActivity extends AbstractListActivity {
    private Map<Integer, Boolean> checked = new HashMap<>();
    private TitleHelper titleHelper;

    public MessageActivity() {
        setUrl(Application.getUrl(R.string.url_user_message));
        setItemLayout(R.layout.user_message_item);
        addMapping("title", R.id.list_item_title);
        addMapping("content", R.id.list_item_content);
        addMapping("state", R.id.list_item_state);
        addMapping("date", R.id.list_item_date);
        addMapping("check", R.id.checkbox);

        setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                ViewGroup g = (ViewGroup) view.getParent();
//                for (int i = 0; i < g.getChildCount(); i++) {
//                    CheckBox checkbox = (CheckBox) g.getChildAt(i).findViewById(R.id.checkbox);
//                    checkbox.setVisibility(View.VISIBLE);
//                    final int p = position;
//                    checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                            listFragment.getmAdapter().getData().get(p).put("check", isChecked);
////                            Log.info(p + " " + listFragment.getmAdapter().getData().get(p) + isChecked);
//                        }
//                    });
//                }
                listFragment.getmAdapter().setEditMode(!listFragment.getmAdapter().isEditMode());

                if (listFragment.getmAdapter().isEditMode()) {
                    titleHelper.setButton("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(MessageActivity.this).setTitle("删除吗？").setCancelable(true).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    List<Integer> checked = listFragment.getmAdapter().getChecked();
                                    if (checked.isEmpty()) {
                                        Toast.makeText(MessageActivity.this, "没有选择数据", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    StringBuilder builder = new StringBuilder();
                                    for (Integer integer : checked) {
                                        builder.append(listFragment.getmAdapter().getData().get(integer).get("id")).append(",");
                                    }
                                    if (builder.length() > 0) {
                                        builder.deleteCharAt(builder.length() - 1);
                                    }

                                    new RequestTask(MessageActivity.this, new RequestTask.Requested() {
                                        @Override
                                        public void execute(Map<String, Object> map) {
                                            //Toast.makeText(MessageActivity.this, listFragment.getmAdapter().getChecked().toString(), Toast.LENGTH_SHORT).show();
                                            listFragment.getmAdapter().setEditMode(false);
                                            titleHelper.removeButton();
                                            listFragment.refresh();
                                        }
                                    }).execute(Application.getUrl(R.string.url_user_message_delete) + builder.toString());
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
    protected int getContentLayout() {
        return R.layout.activity_list;
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
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("url", (String) item.get("url"));
                Log.info(item);
                startActivity(IntentUtils.newInstance(MessageActivity.this, MessageViewActivity.class, item));
            }
        };
    }

    @Override
    protected void init() {
        titleHelper = TitleHelper.getInstance(this);
        titleHelper.enableBack().setTitle("我的消息");

    }

    @Override
    protected int getListReplaceId() {
        return R.id.list;
    }


}
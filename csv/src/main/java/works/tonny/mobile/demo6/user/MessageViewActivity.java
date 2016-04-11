package works.tonny.mobile.demo6.user;

import android.widget.Toast;

import java.util.Map;

import works.tonny.mobile.demo6.R;
import works.tonny.mobile.widget.RequestActivity;

public class MessageViewActivity extends RequestActivity {

    protected void create() {
        setContentView(R.layout.user_message_view);
        activityHelper.enableBack(R.id.goback).setText(R.id.titleText, "查看消息");
        activityHelper.setText(R.id.title, getIntent().getStringExtra("title")).setText(R.id.date, getIntent().getStringExtra("date"))
                .setText(R.id.content, getIntent().getStringExtra("content"));
//        Log.info(getIntent().);

        new Request().execute(getIntent().getStringExtra("return"));
    }

    @Override
    protected void executeResult(Map<String, Object> result) {
        if (result == null) {
            Toast.makeText(this, "回执失败", Toast.LENGTH_SHORT);
        }
    }

}

package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import works.tonny.mobile.ActivityHelper;

/**
 * Created by tonny on 2016/2/4.
 */
public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final EditText text = (EditText) findViewById(R.id.text);
        text.setText(getIntent().getStringExtra("data"));
        ActivityHelper helper = ActivityHelper.getInstance(this);
//        helper.setVisible(R.id.goback, true).setText(R.id.title, getIntent().getStringExtra("title"));
//        helper.setOnClickListener(R.id.goback, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishActivity(0);
//            }
//        });
//
//        helper.setButton(R.id.list_tool_button, "确定", R.style.activity_title_button, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setIntent(new Intent().putExtra("data", text.getText().toString()));
//                finishActivity(getIntent().getIntExtra("id", 0));
//            }
//        });
        TitleHelper.getInstance(this).enableBack().setTitle(getIntent().getStringExtra("title")).setButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1, new Intent().putExtra("data", text.getText().toString()));
                finish();
            }
        });
    }
}

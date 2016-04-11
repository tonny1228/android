package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.utils.Log;


public class FileBrowserActivity extends Activity {
    private File folder;
    private List<Map<String, Object>> data;
    private SimpleAdapter adapter;
    private LinearLayout navs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        folder = FileUtils.getExternalStorageDirectory("/");
        navs = (LinearLayout) findViewById(R.id.nav);

        ActivityHelper.getInstance(this).setOnClickListener(R.id.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2, new Intent().putExtra("data", folder));
                Log.info("))))))))))(((((((((((((((((((((((((");
                finish();
            }
        });

        initNav();
        data = new ArrayList<>();
        listFiles();
        ListView fileList = (ListView) findViewById(R.id.files);
        adapter = new SimpleAdapter(this, data, R.layout.layout_folder_item, new String[]{"name"}, new int[]{R.id.folder});
        fileList.setAdapter(adapter);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                browse((File) data.get(position).get("path"));
            }
        });
    }

    private void listFiles() {
        File[] files = folder.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", file.getName());
            map.put("path", file);
            data.add(map);
        }


    }

    private void initNav() {
        clearNav();
        final StringBuilder buffer = new StringBuilder();
        addNavText(navs, buffer, "/");
        String[] split = folder.getAbsolutePath().split("/");
        for (String s : split) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            addNavText(navs, buffer, s);
        }
    }

    private void clearNav() {
        int childCount = navs.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            navs.removeViewAt(i);
        }
    }

    private void addNavText(LinearLayout navs, StringBuilder buffer, String s) {
        TextView text = new TextView(this);
        text.setText(s);
        text.setMinWidth(LayoutUtils.dip2px(50));
        text.setBackgroundResource(R.drawable.nav_style);
        text.setPadding(LayoutUtils.dip2px(10), LayoutUtils.dip2px(0), LayoutUtils.dip2px(10), LayoutUtils.dip2px(00));
        navs.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        buffer.append(s).append("/");
        final String path = buffer.toString();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browse(new File(path));
            }
        });
    }

    private void browse(File file) {
        this.folder = file;
        data.clear();
        Log.info(folder.getAbsolutePath());
        listFiles();
        adapter.notifyDataSetChanged();

        initNav();
    }
}

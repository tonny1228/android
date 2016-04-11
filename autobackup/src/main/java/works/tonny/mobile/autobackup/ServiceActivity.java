package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.Launcher;
import works.tonny.mobile.utils.DateUtils;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;


public class ServiceActivity extends Activity {

    private ActivityHelper helper;
    private SimpleAdapter adapter;
    private ArrayList<Map<String, Object>> data;
    private BackupConfig backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Launcher.init(this.getBaseContext());
        helper = ActivityHelper.getInstance(this);
        backup = (BackupConfig) getIntent().getSerializableExtra("data");
        if (backup == null) {
            backup = new BackupConfig();
        }
//        helper.setText(R.id.titleText, "服务器");
        TitleHelper.getInstance(this).setTitle("服务器").setButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup.setFolder(helper.getValue(R.id.folder));
                backup.setHost(helper.getValue(R.id.server));
                backup.setUsername(helper.getValue(R.id.username));
                backup.setPassword(helper.getValue(R.id.password));
                backup.getLocals().clear();
                for (Map<String, Object> stringObjectMap : data) {
                    backup.getLocals().add(new File(stringObjectMap.get("file").toString()));
                }
                try {
                    File cacheDirFile = FileUtils.getCacheDirFile("/cache");
                    Log.info(cacheDirFile);
                    List<BackupConfig> cachedObject = (List<BackupConfig>) IOUtils.getCachedObject(cacheDirFile);
                    if (cachedObject == null) {
                        cachedObject = new ArrayList<BackupConfig>();
                    }

                    int index = getIntent().getIntExtra("index", cachedObject.size());
                    if (cachedObject.size() > index)
                        cachedObject.set(index, backup);
                    else {
                        cachedObject.add(backup);
                    }
                    IOUtils.cacheObject(cachedObject, cacheDirFile);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        helper.setText(R.id.summary, "要备份" + backup.getTodo().size() + "个文件夹," + "已备份" + backup.getFinished() + "个文件。上次备份时间" + DateUtils.toString(new Date(backup.getLast())));
        helper.setText(R.id.server, backup.getHost());
        helper.setText(R.id.folder, backup.getFolder());
        helper.setText(R.id.username, backup.getUsername());
        helper.setText(R.id.password, backup.getPassword());
        helper.setOnClickListener(R.id.server, new ServerOnClickListener(R.id.server, backup.getHost(), "修改地址"));
        helper.setOnClickListener(R.id.folder, new ServerOnClickListener(R.id.folder, backup.getFolder(), "修改保存路径"));
        helper.setOnClickListener(R.id.username, new ServerOnClickListener(R.id.username, backup.getUsername(), "修改用户名"));
        helper.setOnClickListener(R.id.password, new ServerOnClickListener(R.id.password, backup.getPassword(), "修改密码"));
//        helper.setOnClickListener(R.id.save,);

        helper.setOnClickListener(R.id.addFolder, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = IntentUtils.newInstance(ServiceActivity.this, FileBrowserActivity.class);
                startActivityForResult(intent, Integer.MAX_VALUE);
            }
        });

        List<File> files = backup.getLocals();
        data = new ArrayList<>();
        for (File file : files) {
            Map<String, Object> d = new HashMap<String, Object>();
            d.put("file", file.toString());
            data.add(d);
        }


        final ListView list = (ListView) findViewById(R.id.folders);
        adapter = new SimpleAdapter(this, data, R.layout.layout_folder_item, new String[]{"file"}, new int[]{R.id.folder});
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = IntentUtils.newInstance(ServiceActivity.this, FileBrowserActivity.class);
                startActivityForResult(intent, position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ServiceActivity.this).setTitle("删除吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        backup.getLocals().remove(position);
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否", null).show();
                return true;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.info(resultCode);
        Log.info(requestCode);
        if (resultCode == 1)
            helper.setText(requestCode, data.getStringExtra("data"));
        if (resultCode == 2) {
            if (requestCode == Integer.MAX_VALUE) {
                Map<String, Object> d = new HashMap<String, Object>();
                d.put("file", data.getSerializableExtra("data").toString());
                this.data.add(d);
            } else {
                this.data.get(requestCode).put("file", data.getSerializableExtra("data").toString());
            }
            adapter.notifyDataSetChanged();
        }
    }

    class ServerOnClickListener implements View.OnClickListener {
        int id;
        String text;
        private String title;

        public ServerOnClickListener(int id, String textView, String title) {
            this.id = id;
            this.text = textView;
            this.title = title;
        }

        public void onClick(View v) {
            Intent intent = IntentUtils.newInstance(ServiceActivity.this, EditActivity.class, "id", String.valueOf(id), "data", text, "title", title);
            startActivityForResult(intent, id);
        }
    }
}

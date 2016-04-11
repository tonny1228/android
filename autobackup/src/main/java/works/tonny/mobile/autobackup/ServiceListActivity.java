package works.tonny.mobile.autobackup;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.Launcher;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.AbstractListActivity;
import works.tonny.mobile.widget.IDLinkedHashMap;


public class ServiceListActivity extends AbstractListActivity {

    public List<BackupConfig> backupList;
    private Button button;
    private Window window;
    private ProcessDialog dialog;
    private TextView view;
    private BackupService backupService;

    public ServiceListActivity() {
        setTitle("任务列表");
        setButtonText("添加", R.style.activity_title_button);
        setTitleViewId(R.id.titleText);
        setTitleButtonId(R.id.list_tool_button);
        mapping();
        setItemLayout(R.layout.layout_server_item);
        setButtonActivityClass(ServiceActivity.class);
        Launcher.init(this);
        setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ServiceListActivity.this).setTitle("删除吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        backupList.remove(position);
                        listFragment.getmAdapter().getData().remove(position);
                        listFragment.getmAdapter().notifyDataSetChanged();
                        FileService.save();
                    }
                }).setNegativeButton("否", null).show();
                return true;
            }
        });
    }
//
//    public void complete() {
//        FileService.save();
//        button.setText("备份");
//        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////        dialog.dismiss();
//        ((View) view.getParent()).setVisibility(View.GONE);
////        view.setVisibility(View.GONE);
//    }
//

    public void showMessage(String text) {
        view.setText(text);
    }


    @Override
    protected void init() {
        button = (Button) findViewById(R.id.sync);
        window = getWindow();


        //绑定Service
        Intent i = new Intent(this, BackupService.class);
        bindService(i, conn, Context.BIND_AUTO_CREATE);

        view = (TextView) findViewById(R.id.message);
//        Intent intent = IntentUtils.newInstance(ServiceListActivity.this, BackupService.class);
//        startService(intent);
        activityHelper.setOnClickListener(R.id.sync, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button) v).getText().toString().equals("备份")) {
                    Intent intent = IntentUtils.newInstance(ServiceListActivity.this, BackupService.class);
                    startService(intent);
                    button.setText("终止");
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                    dialog = new ProcessDialog(ServiceListActivity.this);
//                    dialog.setCancelable(false);
//                    dialog.show();

                    view = (TextView) findViewById(R.id.message);
                    ((View) view.getParent()).setVisibility(View.VISIBLE);

//                    activityHelper.setVisible(R.id.message_box, true);

//                stopService(intent);
                } else {
                    BackupService.getInstance().setState(0);
                    if (backupService != null) {
                        unbindService(conn);
                    } else {
                        Intent intent = IntentUtils.newInstance(ServiceListActivity.this, BackupService.class);
                        stopService(intent);
                    }
                    button.setText("备份");


                }
            }
        });


    }

    @Override
    protected ArrayList<IDLinkedHashMap> getData() {
        backupList = FileService.list();
        ArrayList<IDLinkedHashMap> data = new ArrayList<>();

        for (BackupConfig backup : backupList) {
            IDLinkedHashMap m = new IDLinkedHashMap();
            m.put("server", "\\\\" + backup.getHost() + "\\" + backup.getFolder() + "\\");
            m.put("folder", backup.getLocals());
            m.put("size", backup.getLocals().size() + "个文件夹");
            m.setIdField("server");
            m.put("data", backup);
            data.add(m);
        }
        return data;
    }

    protected void mapping() {
        addMapping("server", R.id.server);
        addMapping("size", R.id.folder);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_service_list;
    }

    @Override
    protected int getListReplaceId() {
        return R.id.list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDLinkedHashMap bundle = getData().get(position);
                bundle.put("index", position);
                Intent intent = IntentUtils.newInstance(ServiceListActivity.this, ServiceActivity.class, bundle);
                startActivity(intent);
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        listFragment.getmAdapter().getData().clear();
        listFragment.getmAdapter().getData().addAll(getData());
        listFragment.getmAdapter().notifyDataSetChanged();
        if (BackupService.getInstance() != null && BackupService.getInstance().getState() == 1) {
            button.setText("终止");
        } else {
            button.setText("备份");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }


    private int lastState = -1;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            backupService.setListener(null);
            Log.info("CCCCCCCCCCCCCCCCCCCCC unun unun ");
            backupService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.info("CCCCCCCCCCCCCCCCCCCCC" + service);
            //返回一个MsgService对象
            backupService = ((BackupService.MsgBinder) service).getService();

            //注册回调接口来接收下载进度的变化
            backupService.setListener(new BackupService.Listener() {

                @Override
                public void onMessage(int state, int total, int finished, String file) {
                    if (state == 1 && lastState != state) {
                        button.setText("终止");
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        ((View) view.getParent()).setVisibility(View.VISIBLE);
                    } else if (state == 0 && lastState != state) {
                        button.setText("备份");
                        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                        ((View) view.getParent()).setVisibility(View.GONE);
                        return;
                    }

                    showMessage(finished + "/" + total + "\n" + file);
                }
            });

        }
    };

}

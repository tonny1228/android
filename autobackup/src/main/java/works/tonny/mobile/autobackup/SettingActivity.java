package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.utils.IOUtils;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityHelper.getInstance(this).setOnClickListener(R.id.go, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Store().execute(new File("/storage/emulated/0/DCIM/Camera/1.jpg"));
            }
        });
    }


    class Store extends AsyncTask<File, Integer, Integer> {
        @Override
        protected Integer doInBackground(File... params) {
            for (int i = 0; i < params.length; i++) {
                try {
                    SmbFileOutputStream sout = new SmbFileOutputStream(new SmbFile("smb://tonny1230%40hotmail.com:oiamlxd@192.168.0.218/upload/1.jpg"));
                    InputStream in = new FileInputStream(params[i]);
                    IOUtils.copy(in, sout);
                    IOUtils.close(in);
                    IOUtils.close(sout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return params.length;
        }
    }


}
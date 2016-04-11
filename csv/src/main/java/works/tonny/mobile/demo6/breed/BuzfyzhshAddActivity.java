package works.tonny.mobile.demo6.breed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.utils.Log;

public class BuzfyzhshAddActivity extends FormActivity {

    private Calendar sqsj = Calendar.getInstance();

    private Calendar yjsj = Calendar.getInstance();

    private Calendar csrq = Calendar.getInstance();
    private ActivityHelper activityHelper;
    private File gwxtzs;
    private File gwxtzsnr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.breed_buzfyzhsh_add);
            final ActivityHelper helper = ActivityHelper.getInstance(this);
            TitleHelper.getInstance(this).enableBack().setTitle("补做繁育证书申请").setButton("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String djywm = helper.getValue(R.id.djywm);
                    if (!assertNotNull(djywm, "请填写登记犬英文名")) return;

                    String sfyyj = helper.getValue(R.id.sfyyj);
                    String xb = helper.getValue(R.id.xb);

                    String sqsj = helper.getValue(R.id.sqsj);
                    if (!assertNotNull(sqsj, "请填写申请时间")) return;

                    String yjsj = helper.getValue(R.id.yjsj);
                    if (!assertNotNull(yjsj, "请填写邮寄时间")) return;

                    String csrq = helper.getValue(R.id.csrq);
                    if (!assertNotNull(csrq, "请填写出生日期")) return;

                    String eh = helper.getValue(R.id.eh);
                    if (!assertNotNull(eh, "请填写耳号")) return;

                    String gwxtzsh = helper.getValue(R.id.gwxtzsh);
                    String ssgj = helper.getValue(R.id.ssgj);
                    String fzr = helper.getValue(R.id.fzr);
                    String fzrdz = helper.getValue(R.id.fzrdz);
                    String gwjca6 = helper.getValue(R.id.gwjca6);
                    String gwjced6 = helper.getValue(R.id.gwjced6);

                    String fqxtzsh = helper.getValue(R.id.fqxtzsh);
                    if (!assertNotNull(fqxtzsh, "请填写父犬血统证书号")) return;

                    String mqxtzsh = helper.getValue(R.id.mqxtzsh);
                    if (!assertNotNull(mqxtzsh, "请填写母犬血统证书号")) return;


                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("djywm", djywm);
                    map.put("sfyyj", sfyyj);
                    map.put("xb", xb);
                    map.put("sqsj", sqsj);
                    map.put("yjsj", yjsj);
                    map.put("csrq", csrq);
                    map.put("eh", eh);
                    map.put("gwxtzsh", gwxtzsh);
                    map.put("ssgj", ssgj);
                    map.put("fzr", fzr);
                    map.put("fzrdz", fzrdz);
                    map.put("gwjca6", gwjca6);
                    map.put("gwjced6", gwjced6);
                    map.put("fqxtzsh", fqxtzsh);
                    map.put("mqxtzsh", mqxtzsh);
                    map.put("gwxtzs", gwxtzs);
                    map.put("gwxtzsnr", gwxtzsnr);
                    new Post().execute(map);
                }
            });
            activityHelper = ActivityHelper.getInstance(this);
            activityHelper.makeItDate(R.id.sqsj, sqsj);
            activityHelper.makeItDate(R.id.yjsj, yjsj);
            activityHelper.makeItDate(R.id.csrq, csrq);
            activityHelper.setOnClickListener(R.id.gwxtzs, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openAlbumIntent, 1);
                }
            });
            activityHelper.setOnClickListener(R.id.gwxtzsnr, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openAlbumIntent, 2);
                }
            });
        } catch (Exception e) {
            Log.error(e);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode != -1) {
                return;
            }
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    gwxtzs = new File(FileUtils.getPath(this, uri));
                    activityHelper.setText(R.id.gwxtzs, gwxtzs.getName());
                    break;
                case 2:
                    uri = data.getData();
                    gwxtzsnr = new File(FileUtils.getPath(this, uri));
                    activityHelper.setText(R.id.gwxtzsnr, gwxtzsnr.getName());
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_buzfyzs_add);
    }

    @Override
    protected String getActivityTitle() {
        return "补做繁育证书提交";
    }


}

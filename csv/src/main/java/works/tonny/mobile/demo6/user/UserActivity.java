package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.ImageTools;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.LoadingDialog;


public class UserActivity extends Activity {

    private static final int REQUEST_TAKE_PHOTO = 1;

    private LoadingDialog loadingDialog;
    private ActivityHelper activityHelper;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        activityHelper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("用户");

        activityHelper.setOnClickListener(R.id.button_logout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.logout();
                finish();
            }
        });


        activityHelper.setOnClickListener(R.id.user_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(UserActivity.this)
                        .setTitle("修改头像")
                        .setItems(new String[]{"拍照上传", "本地上传"}, onselect).create();
                dialog.show();
            }
        });
        imageView = (ImageView) findViewById(R.id.user_head);
        setUser();
    }


    private void setUser() {
//        Application.getUser().setName((String) data.get("data.user.name"));
//        Application.getUser().setUsername((String) data.get("data.user.username"));
//        Application.getUser().setNickname((String) data.get("data.user.nickname"));
//        Application.getUser().setHeader((String) data.get("data.user.header"));
        activityHelper.setText(R.id.nickname, Application.getUser().getName());
        Log.info(Application.getUser().getName());
        activityHelper.setText(R.id.login_username, Application.getUser().getUsername());
        activityHelper.setImage(R.id.user_head, Application.getUser().getHeader());
    }

    private void capturePicture() {
        Uri imageUri = null;
        String fileName = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //保存本次截图临时文件名字
        imageUri = Uri.fromFile(FileUtils.getExternalStorageDirectory("/tonny/head.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            File file = FileUtils.getExternalStorageDirectory("/tonny/head.jpg");
            switch (requestCode) {
                case 1:
//                    scaleImage(file, file);
                    cropImage(Uri.fromFile(file), 200, 200, 2);
                    break;
                case 2:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();

                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap) extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    ImageTools.savePhoto(photo, file);
                    imageView.setImageBitmap(photo);
                    uploadHeader(file);
                    break;
                case 3:
                    Uri originalUri = data.getData();
                    cropImage(originalUri, 200, 200, 2);
                    break;
            }
        }
    }

    private void scaleImage(File file, File newFile) {
        Bitmap bitmap = ImageTools.tryGetBitmap(file, 100000, 1000000);
        Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);
        ImageTools.savePhoto(newBitmap, newFile);
        cropImage(Uri.fromFile(file), 500, 500, 2);
    }

    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
//        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

    DialogInterface.OnClickListener onselect = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    capturePicture();
                    break;
                case 1:
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openAlbumIntent, 3);
                    break;
            }
        }
    };

    private void uploadHeader(File header) {
        new UploadFile().execute(header);
    }


    class UploadFile extends AsyncTask<File, Integer, Void> {


        @Override
        protected void onPostExecute(Void v) {
            loadingDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(UserActivity.this);
            loadingDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(UserActivity.this, null);
                this.cancel(true);
            }
        }

        @Override
        protected Void doInBackground(File... params) {
            if (!DeviceUtils.isNetworkConnected(UserActivity.this)) {
                this.cancel(true);
            }
            HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, Application.getUrl(R.string.url_user_header));//
            try {
                request.addFile("photo", params[0]);
                request.addFormParam("action", "updatePhoto");
                String s = request.executeToString();
                Log.info(s);
            } catch (AuthException e) {
                e.printStackTrace();
                this.publishProgress(-1);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            request = null;
            return null;
        }
    }

}

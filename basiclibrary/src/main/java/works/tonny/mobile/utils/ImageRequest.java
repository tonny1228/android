package works.tonny.mobile.utils;

import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;

/**
 * Created by tonny on 2015/10/25.
 */
public class ImageRequest extends AsyncTask<String, Integer, File> {
//    private ImageView view;

    private OnRequested onRequested;

    private String url;

    public ImageRequest(String url, OnRequested onRequested) {
        this.url = url;
        this.onRequested = onRequested;
    }

    @Override
    protected void onPreExecute() {
//        if (view.getResources() == null)
//            view.setImageResource(R.drawable.empty);

    }

    @Override
    protected File doInBackground(String... params) {
        Log.info("downloading " + url);
        File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);
        File f = new File(dir.getAbsolutePath(), MD5.encode(url));
        if (f.exists()) {

            return f;
        }
        HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, url);
        try {
            request.executeToFile(f);
            Log.info("finished download " + url + "; " + f + " " + f.length());
            return f;
        } catch (HttpRequestException e) {
            Log.error(e);
        } catch (AuthException e) {
            Log.error(e);
        }
//        if (view.getDrawable() == null)
//            view.setImageResource(R.drawable.empty);
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    public OnRequested getOnRequested() {
        return onRequested;
    }

    public void setOnRequested(OnRequested onRequested) {
        this.onRequested = onRequested;
    }

    @Override
    protected void onPostExecute(File s) {
        onRequested.execute(s);
//        ActivityHelper.setImage(view, s.getAbsolutePath());
//        onRequested.execute(s);
    }

    public static interface OnRequested {
        void execute(File file);
    }

    public static class SetImage implements OnRequested {
        private ImageView imageView;

        public SetImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void execute(File file) {
            ActivityHelper.setImage(imageView, file.getAbsolutePath());
        }
    }
}

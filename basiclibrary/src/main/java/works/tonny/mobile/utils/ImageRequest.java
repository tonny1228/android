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

    public ImageRequest(OnRequested onRequested) {
        this.onRequested = onRequested;
    }

    @Override
    protected void onPreExecute() {
//        if (view.getResources() == null)
//            view.setImageResource(R.drawable.empty);

    }

    @Override
    protected File doInBackground(String... params) {
        String url = Application.getUrl(params[0]);
        File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);
        File f = new File(dir.getAbsolutePath(), MD5.encode(url));
        if (f.exists()) {
            return f;
        }
        HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, url);
        try {
            request.executeToFile(f);
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

    @Override
    protected void onPostExecute(File s) {
        onRequested.execute(s);
//        ActivityHelper.setImage(view, s.getAbsolutePath());
        onRequested.execute(s);
    }

    public static interface OnRequested {
        void execute(File file);
    }
}

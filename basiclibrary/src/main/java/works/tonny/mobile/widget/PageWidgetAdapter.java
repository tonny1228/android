package works.tonny.mobile.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.utils.ImageRequest;
import works.tonny.mobile.utils.ImageTools;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.MD5;
import works.tonny.mobile.utils.XMLParser;


public class PageWidgetAdapter extends BaseAdapter {

    private Context mContext;
    private int count;
    private LayoutInflater inflater;
    //    private List<ImageView> images;
    private List<File> urls;
    private ImageView image;

    public PageWidgetAdapter(Context context) {
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        count = (int) Math.ceil(imgs.length);
    }


    public void setImages(List<String> url) {
//        this.images = url;
//        this.urls = url;
//        images = new ArrayList<>();
//        ImageView image = (ImageView) group.findViewById(R.id.item_layout_leftImage);
//        new ImageRequest(image).execute(url.get(0));

        urls = new ArrayList<>();
        for (int i = 0; i < url.size(); i++) {
            new RequestTask(mContext, i).execute(url.get(i));
            urls.add(null);
        }
//        for (String u : url) {
////            ImageView view = new ImageView(mContext);
////            new ImageRequest(view).execute(u);
////            images.add(view);
//
//        }
    }


    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup layout;
        if (convertView == null) {
            layout = (ViewGroup) inflater.inflate(R.layout.page_item_layout, null);
        } else {
            layout = (ViewGroup) convertView;
        }
        setViewContent(layout, position);

        return layout;
    }

    private void setViewContent(ViewGroup group, int position) {
        ImageView viewById = (ImageView) group.findViewById(R.id.item_layout_leftImage);
        if (image == null) {
            image = viewById;
        }
        Log.info("Imageeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + position + " " + viewById);
//        File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);
        File f = urls.get(position);
        if (f == null || !f.exists()) {
            viewById.setImageResource(R.drawable.empty);
            return;
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        viewById.measure(width, height);
        Bitmap bitmap = ImageTools.decodeSampledBitmapFromResource(f.getAbsolutePath(), viewById.getMeasuredWidth(), viewById.getMeasuredHeight());
//        Bitmap bitmap = ImageTools.tryGetBitmap(f, image.getMeasuredWidth(), image.getMeasuredHeight());
//            view.setImageBitmap(bitmap);
        viewById.setImageBitmap(bitmap);
//        new ImageRequest(image).execute(images.get(position).toString());
//        text = (TextView) group.findViewById(R.id.item_layout_rightText);
//        text.setText(String.valueOf(position * 2 + 2));
//        image = (ImageView) group.findViewById(R.id.item_layout_rightImage);
//        image.setImageResource(imgs[position * 2 + 1]);
    }


    class RequestTask extends AsyncTask<String, Integer, File> {

        private HttpRequest request;
        private LoadingDialog loadingDialog;
        private Context context;
        private int i;

        public RequestTask(Context context, int i) {
            this.context = context;
            this.i = i;
        }

        @Override
        protected File doInBackground(String... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, params[0]);
            try {
                String url = params[0];
                File dir = FileUtils.getExternalStorageDirectory(FileUtils.WEB_CACHE_DIR);
                File f = new File(dir.getAbsolutePath(), MD5.encode(url));
                if (f.exists()) {
                    return f;
                }
                request.executeToFile(f);
                return f;
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(context);
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();
            urls.set(i, result);
            if (i == 0 && image != null) {
                Log.info("settttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttting " + image);
                int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                image.measure(width, height);
                Bitmap bitmap = ImageTools.decodeSampledBitmapFromResource(result.getAbsolutePath(), image.getMeasuredWidth(), image.getMeasuredHeight());
                image.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

}

package works.tonny.mobile.widget;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.Launcher;
import works.tonny.mobile.R;
import works.tonny.mobile.utils.ImageRequest;
import works.tonny.mobile.utils.ImageRequestManager;
import works.tonny.mobile.utils.ImageTools;
import works.tonny.mobile.utils.Log;


/**
 *
 */
public class ZoomImagePaperFragment extends Fragment {
    public static final String KEY = "ENTITY";
    private static View titleBg;
    List<Entity> entity;
    private View view;
    private static Scroller runnable;
    LinearLayout[] imageView;
    private Context context;
    private TextView titleView;
    private ViewPager viewPager;
    ZoomImageView zoomImageView;
    ZoomImageView prev;
    ZoomImageView next;

    private PageChangeListener listener;

    public static ZoomImagePaperFragment newInstance(ArrayList<Entity> entity) {
        ZoomImagePaperFragment fragment = new ZoomImagePaperFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    public ZoomImagePaperFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (List<Entity>) getArguments().getSerializable(KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        if (view == null) {
            view = createView(inflater, container, savedInstanceState, entity);
        }

        return view;
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, final List<Entity> entity) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_image_zoom_paper, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_image_view_paper);
        titleView = (TextView) view.findViewById(R.id.viewpaper_title);
        titleBg = view.findViewById(R.id.viewpaper_title);

        imageView = new LinearLayout[entity.size()];
        if (zoomImageView == null) {
            zoomImageView = new ZoomImageView(context, null);
        }
        if (prev == null) {
            prev = new ZoomImageView(context, null);
        }
        if (next == null) {
            next = new ZoomImageView(context, null);
        }

        views = new ZoomImageView[]{zoomImageView, next, prev};
        for (int i = 0; i < entity.size(); i++) {
            imageView[i] = new LinearLayout(context);
        }
        listener = new PageChangeListener(viewPager, titleView, entity);
        initImage(0);
        viewPager.setAdapter(new ImageViewPaperAdapter(imageView));

        viewPager.addOnPageChangeListener(listener);

        View nextBnt = view.findViewById(R.id.next);
        View prevBnt = view.findViewById(R.id.prev);
        nextBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.last < entity.size() - 1)
                    viewPager.setCurrentItem(listener.last + 1);
            }
        });

        prevBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.last > 0)
                    viewPager.setCurrentItem(listener.last - 1);
            }
        });


        return view;
    }

    /**
     * 初始化图片等信息
     *
     * @param position
     */
    void initImage(final int position) {
        if (entity.size() <= position) {
            return;
        }
        final Entity e = this.entity.get(position);
        if (StringUtils.isNotEmpty(e.getTitle())) {
            titleBg.setVisibility(View.VISIBLE);
            titleView.setText(e.getTitle());
        } else {
//            titleBg.setVisibility(View.GONE);
            titleBg.setVisibility(View.VISIBLE);
            titleView.setText((position + 1) + "/" + entity.size());
        }

        if (position > 2) {
            if (imageView[position - 2].getChildCount() > 0) {
                imageView[position - 2].removeAllViews();
            }
        }

        if (position < entity.size() - 2) {
            if (imageView[position + 2].getChildCount() > 0) {
                imageView[position + 2].removeAllViews();
            }
        }


        if (imageView[position].getChildCount() == 0) {
            setImageResource(e, imageView[position], next(position), position);

        }
        if (listener.last < position) {
            if (position < entity.size() - 1) {
                setImageResource(entity.get(position + 1), imageView[position + 1], next(position + 1), position + 1);
            }
        } else {
            if (position > 0) {
                setImageResource(entity.get(position - 1), imageView[position - 1], next(position - 1), position - 1);
            }
        }
        if (listener.last < 0) {
            listener.last = 0;
        }
    }

    private ZoomImageView[] views = null;


    public ZoomImageView next(int position) {
        ZoomImageView view = views[position % 3];
        return view;
    }

    private void setImageResource(final Entity e, final LinearLayout view, ZoomImageView image, final int position) {
        final ZoomImageView set = image;
        if (e.getFile() == null) {
            ImageRequestManager.getInstance().addTask(new ImageRequest(e.getPath(), new ImageRequest.OnRequested() {
                @Override
                public void execute(File file) {
                    e.setFile(file.getAbsolutePath());
                    int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED) / 2;
                    int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED) / 2;
                    view.measure(width, height);
                    Bitmap bitmap = ImageTools.decodeSampledBitmapFromResource(file.getAbsolutePath(), view.getMeasuredWidth() / 10, view.getMeasuredHeight() / 10);
                    if (set.getBitmap() != null) {
                        set.getBitmap().recycle();
                    }
                    set.setImage(bitmap);
                    bitmap = null;
                }
            }));
//            new ImageRequest().execute(e.getPath());
        } else {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED) / 2;
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED) / 2;
            view.measure(width, height);
            Bitmap bitmap = ImageTools.decodeSampledBitmapFromResource(e.getFile(), view.getMeasuredWidth() / 10, view.getMeasuredHeight() / 10);
            if (set.getBitmap() != null) {
                set.getBitmap().recycle();
            }
            set.setImage(bitmap);
            bitmap = null;
        }
        ViewGroup viewGroup = (ViewGroup) image.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        view.addView(image, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        System.gc();
    }


    static class Scroller implements Runnable {
        private final List<Entity> entity;
        private final Handler handler;
        private final PageChangeListener listener;
        private final ViewPager viewPager;
        public int i = 1;

        public Scroller(ViewPager viewPager, Handler handler, List<Entity> entity, PageChangeListener listener) {
            this.entity = entity;
            this.listener = listener;
            this.handler = handler;
            this.viewPager = viewPager;
        }

        @Override
        public void run() {
            if (listener.last < entity.size() - 1) {
                viewPager.setCurrentItem(listener.last + 1);
            } else {
                viewPager.setCurrentItem(0);
//                listener.last = 0;
            }
            handler.postDelayed(this, 5000);
        }
    }

    /**
     * 图片改变监听
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {
        private ViewPager viewPager;

        private TextView textView;

        private List<Entity> entity;

        public PageChangeListener(ViewPager viewPager, TextView textView, List<Entity> entity) {
            this.viewPager = viewPager;
            this.textView = textView;
            this.entity = entity;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            viewPager.invalidate();
        }

        int last = -1;

        @Override
        public void onPageSelected(final int position) {
            initImage(position);
            last = position;
//            this.textView.setText(position + "/" + entity.size());
//            runnable.i = last + 1;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }


    /**
     * 图片适配器
     */
    static class ImageViewPaperAdapter extends PagerAdapter {
        private LinearLayout[] imageView;

        public ImageViewPaperAdapter(LinearLayout[] imageView) {
            this.imageView = imageView;
        }

        @Override
        public int getCount() {
            return imageView.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            if (imageView[position] != null && imageView[position].getParent() == null)
                container.addView(imageView[position]);
            return imageView[position];
        }
    }


    /**
     * 每个节点的信息
     */
    public static class Entity implements Serializable {
        private String path;
        private String title;
        private String intentCategory;
        private String intentAction;
        private Map<String, String> intentData;
        private Class intentClass;
        private Bundle bundle;
        private int id;
        private String file;


        public Entity(String path, String title, String intentCategory, String intentAction, Map<String, String> intentData) {
            this.path = path;
            this.title = title;
            this.intentCategory = intentCategory;
            this.intentAction = intentAction;
            this.intentData = intentData;
        }


        public Entity(int id, String title, Class intentClass, Bundle bundle) {
            this.id = id;
            this.title = title;
            this.intentClass = intentClass;
            this.bundle = bundle;
        }


        public Entity(int id, String title, String intentCategory, String intentAction, Map<String, String> intentData) {
            this.id = id;
            this.title = title;
            this.intentCategory = intentCategory;
            this.intentAction = intentAction;
            this.intentData = intentData;
        }

        public String getIntentAction() {
            return intentAction;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setIntentAction(String intentAction) {
            this.intentAction = intentAction;
        }

        public String getIntentCategory() {
            return intentCategory;
        }

        public void setIntentCategory(String intentCategory) {
            this.intentCategory = intentCategory;
        }

        public Map<String, String> getIntentData() {
            return intentData;
        }

        public void setIntentData(Map<String, String> intentData) {
            this.intentData = intentData;
        }

        public Class getIntentClass() {
            return intentClass;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public int getId() {
            return id;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}

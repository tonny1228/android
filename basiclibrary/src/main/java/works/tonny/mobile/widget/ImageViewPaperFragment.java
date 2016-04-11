package works.tonny.mobile.widget;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.R;
import works.tonny.mobile.utils.ImageRequest;
import works.tonny.mobile.utils.Log;


/**
 *
 */
public class ImageViewPaperFragment extends Fragment {
    public static final String KEY = "ENTITY";
    private static View titleBg;
    List<Entity> entity;
    private View view;
    private static Scroller runnable;
    ImageView[] imageView;
    ImageView current;
    ImageView prev;
    ImageView next;
    private boolean autoScroll;
    private Context context;
    private View.OnClickListener[] onClickListeners;
    private TextView titleView;
    private LinearLayout controller;
    private ViewPager viewPager;
    int index = 0;

    public static ImageViewPaperFragment newInstance(ArrayList<Entity> entity) {
        ImageViewPaperFragment fragment = new ImageViewPaperFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewPaperFragment() {
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

        view = inflater.inflate(R.layout.fragment_image_view_paper, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_image_view_paper);
        controller = (LinearLayout) view.findViewById(R.id.fragment_image_viewpaper_controller);
        titleView = (TextView) view.findViewById(R.id.viewpaper_title);
        titleBg = view.findViewById(R.id.viewpaper_title);

        imageView = new ImageView[entity.size()];

//            current = new ImageView(context);
//            prev = new ImageView(context);
//            next = new ImageView(context);
        onClickListeners = new View.OnClickListener[entity.size()];
        new DotController().init();
        initClickListener(entity);
        initImage(0);
        viewPager.setAdapter(new ImageViewPaperAdapter(imageView));
        final PageChangeListener listener = new PageChangeListener(viewPager, controller, titleView, entity);
        viewPager.addOnPageChangeListener(listener);
        if (entity.size() > 1 && autoScroll) {
            final Handler handler = new Handler();
            runnable = new Scroller(viewPager, handler, entity, listener);
            handler.postDelayed(runnable, 5000);
        }
        return view;
    }

    /**
     * 初始化
     *
     * @param entity
     */
    private void initClickListener(List<Entity> entity) {
        for (int i = 0; i < entity.size(); i++) {
            imageView[i] = new ImageView(context);
            final Entity e = entity.get(i);
            onClickListeners[i] = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (e.getIntentClass() == null && e.getIntentAction() == null) {
                        return;
                    }
                    Intent intent = new Intent();
                    if (e.getIntentClass() != null) {
                        intent.setClass(v.getContext(), e.getIntentClass());
                    } else {
                        intent.setAction(e.getIntentAction());
                        Set<String> keySet = e.getIntentData().keySet();
                        for (String s : keySet) {
                            intent.putExtra(s, e.getIntentData().get(s));
                        }
                    }
                    if (e.getBundle() != null) {
                        intent.putExtras(e.getBundle());
                    }
                    v.getContext().startActivity(intent);
                }
            };
        }
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
        if (entity.get(position).getTitle() != null) {
            titleBg.setVisibility(View.VISIBLE);
            titleView.setText(e.getTitle());
        } else {
            titleBg.setVisibility(View.GONE);
        }

        if (position > 2)
            imageView[position - 2].setImageResource(R.drawable.empty);
        if (position < entity.size() - 2)
            imageView[position + 2].setImageResource(R.drawable.empty);

//            ActivityHelper.getInstance(context).setImage(current, e.getPath());
//        if (current.getParent() != null)
//            ((ViewGroup) current.getParent()).removeView(current);

//        imageView[position] = new ImageView(context);
        Log.info("xxxxxxxxxxxxxxxxxxxxxxxxxx00x  " + position + " " + imageView[position]);
        setImageResource(e, imageView[position], position);
//        imageView[position] = current;
        imageView[position].setOnClickListener(onClickListeners[position]);

        if (position < entity.size() - 1) {
            setImageResource(entity.get(position + 1), imageView[position + 1], position + 1);
        }
        if (position > 0) {
            setImageResource(entity.get(position - 1), imageView[position - 1], position - 1);
        }

        imageView[position].setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView[position].setOnTouchListener(new TouchListener(imageView[position]));
    }

    private void setImageResource(final Entity e, final ImageView view, final int position) {
        if (e.getFile() == null) {
            new ImageRequest(new ImageRequest.OnRequested() {
                @Override
                public void execute(File file) {
                    e.setFile(file.getAbsolutePath());
                    if (position == index)
                        ActivityHelper.setImage(view, e.getFile());
                }
            }).execute(e.getPath());
        } else {
            ActivityHelper.setImage(view, e.getFile());
        }
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
        private LinearLayout controller;

        private ViewPager viewPager;

        private TextView textView;

        private List<Entity> entity;

        public PageChangeListener(ViewPager viewPager, LinearLayout controller, TextView textView, List<Entity> entity) {
            this.controller = controller;
            this.viewPager = viewPager;
            this.textView = textView;
            this.entity = entity;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            viewPager.invalidate();
        }

        int last = 0;

        @Override
        public void onPageSelected(final int position) {
            ((ImageView) controller.getChildAt(last)).setImageResource(R.drawable.viewpaper_no);
            ((ImageView) controller.getChildAt(position)).setImageResource(R.drawable.viewpaper_yes);
            initImage(position);
            index = position;

            last = position;
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
        private ImageView[] imageView;

        public ImageViewPaperAdapter(ImageView[] imageView) {
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


    public interface Contoller {

    }

    public class DotController implements Contoller {

        public void init() {
            for (int i = 0; i < entity.size(); i++) {
                ImageView c = new ImageView(context);
                if (i == 0) {
                    c.setImageResource(R.drawable.viewpaper_yes);
                } else
                    c.setImageResource(R.drawable.viewpaper_no);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(2, 2, 2, 2);
                controller.addView(c, params);
            }
        }
    }

    /**
     * 两侧箭头的控制
     */
    public class ArrawController implements Contoller {
        public void init() {
            for (int i = 0; i < entity.size(); i++) {
                ImageView c = new ImageView(context);
                if (i == 0) {
                    c.setImageResource(R.drawable.viewpaper_yes);
                } else
                    c.setImageResource(R.drawable.viewpaper_no);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(2, 2, 2, 2);
                controller.addView(c, params);
            }
        }
    }


    private final class TouchListener implements View.OnTouchListener {

        /**
         * 记录是拖拉照片模式还是放大缩小照片模式
         */
        private int mode = 0;// 初始状态
        /**
         * 拖拉照片模式
         */
        private static final int MODE_DRAG = 1;
        /**
         * 放大缩小照片模式
         */
        private static final int MODE_ZOOM = 2;

        /**
         * 用于记录开始时候的坐标位置
         */
        private PointF startPoint = new PointF();
        /**
         * 用于记录拖拉图片移动的坐标位置
         */
        private Matrix matrix = new Matrix();
        /**
         * 用于记录图片要进行拖拉时候的坐标位置
         */
        private Matrix currentMatrix = new Matrix();

        /**
         * 两个手指的开始距离
         */
        private float startDis;
        /**
         * 两个手指的中间点
         */
        private PointF midPoint;

        private ImageView imageView;

        public TouchListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }

        /**
         * 计算两个手指间的距离
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return FloatMath.sqrt(dx * dx + dy * dy);
        }

        /**
         * 计算两个手指间的中间点
         */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }

    }
}

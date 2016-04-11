package works.tonny.mobile.widget;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
public class ImageGroupFragment extends Fragment {
    public static final String KEY = "ENTITY";
    private static View titleBg;
    List<Entity> entity;
    private View view;
    private static Scroller runnable;
    private ImageView[] imageView;


    public static ImageGroupFragment newInstance(ArrayList<Entity> entity) {
        ImageGroupFragment fragment = new ImageGroupFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageGroupFragment() {
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
        if (view == null) {
            view = createView(inflater, container, savedInstanceState, entity);

        }


        return view;
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, final List<Entity> entity) {
        View view = null;

        view = inflater.inflate(R.layout.fragment_image_view_paper, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_image_view_paper);
        LinearLayout controller = (LinearLayout) view.findViewById(R.id.fragment_image_viewpaper_controller);
        TextView textView = (TextView) view.findViewById(R.id.viewpaper_title);
        titleBg = view.findViewById(R.id.viewpaper_title);

        imageView = new ImageView[entity.size()];
        for (int i = 0; i < entity.size(); i++) {
            imageView[i] = new ImageView(container.getContext());
            final Entity e = entity.get(i);
            if (e.getId() > 0) {
                imageView[i].setImageResource(e.getId());
            } else if (e.getPath() != null && e.getPath().startsWith("/"))
                imageView[i].setImageBitmap(BitmapFactory.decodeFile(e.getPath()));
            else {
                final ImageView c = imageView[i];
                new ImageRequest(new ImageRequest.OnRequested() {
                    @Override
                    public void execute(File file) {
                        ActivityHelper.setImage(c, file.getAbsolutePath());
                    }
                }).execute(e.getPath());
            }
            imageView[i].setScaleType(ImageView.ScaleType.FIT_XY);
            ImageView c = new ImageView(container.getContext());
            if (i == 0) {
                c.setImageResource(R.drawable.viewpaper_yes);
                textView.setText(e.getTitle());
                if (e.getTitle() != null) {
                    titleBg.setVisibility(View.VISIBLE);
                    textView.setText(e.getTitle());
                } else {
                    titleBg.setVisibility(View.GONE);
                }
            } else
                c.setImageResource(R.drawable.viewpaper_no);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 2, 2, 2);
            controller.addView(c, params);
            imageView[i].setOnClickListener(new View.OnClickListener() {
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
//                    intent.addCategory(e.getIntentCategory());
                        //intent.setData(Uri.parse(e.getIntentData()));

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
            });
        }
        viewPager.setAdapter(new ImageViewPaperAdapter(imageView));
        final PageChangeListener listener = new PageChangeListener(viewPager, controller, textView, entity);
        viewPager.addOnPageChangeListener(listener);
        if (entity.size() > 1) {
            final Handler handler = new Handler();
            runnable = new Scroller(viewPager, handler, entity, listener);
            handler.postDelayed(runnable, 5000);
        }
        return view;
    }

    public void setEntity(int i, Entity e) {
        if (i < imageView.length) {
            Log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
            entity.set(i, e);
            if (!entity.get(i).getIntentData().get("url").equals(e.getIntentData().get("url"))) {
                Log.info("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                if (e.getId() > 0) {
                    imageView[i].setImageResource(e.getId());
                } else if (e.getPath() != null && e.getPath().startsWith("/"))
                    imageView[i].setImageBitmap(BitmapFactory.decodeFile(e.getPath()));
                else {
                    final ImageView c = imageView[i];
                    new ImageRequest(new ImageRequest.OnRequested() {
                        @Override
                        public void execute(File file) {
                            ActivityHelper.setImage(c, file.getAbsolutePath());
                        }
                    }).execute(e.getPath());
                }
            }
        } else {
            Log.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            entity.set(i, e);
            ImageView[] temp = imageView;
            imageView = new ImageView[i + 1];
            for (int j = 0; j < temp.length; j++) {
                imageView[j] = temp[j];
            }
            if (e.getId() > 0) {
                imageView[i].setImageResource(e.getId());
            } else if (e.getPath() != null && e.getPath().startsWith("/"))
                imageView[i].setImageBitmap(BitmapFactory.decodeFile(e.getPath()));
            else {
//                new ImageRequest(imageView[i]).execute(e.getPath());
                final ImageView c = imageView[i];
                new ImageRequest(new ImageRequest.OnRequested() {
                    @Override
                    public void execute(File file) {
                        ActivityHelper.setImage(c, file.getAbsolutePath());
                    }
                }).execute(e.getPath());
            }
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


    static class PageChangeListener implements ViewPager.OnPageChangeListener {
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

        private int last = 0;

        @Override
        public void onPageSelected(int position) {
            ((ImageView) controller.getChildAt(last)).setImageResource(R.drawable.viewpaper_no);
            ((ImageView) controller.getChildAt(position)).setImageResource(R.drawable.viewpaper_yes);

            if (entity.get(position).getTitle() != null) {
                titleBg.setVisibility(View.VISIBLE);
                textView.setText(entity.get(position).getTitle());
            } else {
                titleBg.setVisibility(View.GONE);
            }
            last = position;
//            runnable.i = last + 1;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }


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
            container.addView(imageView[position]);
            return imageView[position];
        }
    }


    public static class Entity implements Serializable {
        private String path;
        private String title;
        private String intentCategory;
        private String intentAction;
        private Map<String, String> intentData;
        private Class intentClass;
        private Bundle bundle;
        private int id;


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
    }

}

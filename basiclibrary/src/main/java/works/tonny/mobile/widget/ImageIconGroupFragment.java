package works.tonny.mobile.widget;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.utils.ImageRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ImageIconGroupFragment extends Fragment {
    private List<Entity> entity;

    private int columns;
    private ViewGroup view;
    private boolean showLine;


    public ImageIconGroupFragment() {
    }


    public void init(ArrayList<Entity> list, int columns, boolean showLine) {
        this.entity = list;
        this.columns = columns;
        this.showLine = showLine;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = createView(inflater, container, entity, columns, showLine);
        }

        return view;
    }


    public static ViewGroup createView(LayoutInflater inflater, final ViewGroup container,
                                       List<Entity> entity, int columns, boolean showLine) {
        int rows = entity.size() / columns;
        if (entity.size() % columns > 0) {
            rows++;
        }
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_image_icon_group, null);
        for (int i = 0; i < rows; i++) {
            LinearLayout layout = new LinearLayout(container.getContext());
            LinearLayout.LayoutParams playoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            //layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            if (showLine)
                layout.setBackgroundColor(Color.parseColor("#aaaaaa"));
            for (int j = 0; j < columns && i * columns + j < entity.size(); j++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                RelativeLayout clayout = new RelativeLayout(container.getContext());
                RelativeLayout.LayoutParams clayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (showLine)
                    layoutParams.setMargins(j > 0 ? 1 : 0, i > 0 ? 1 : 0, 0, 0);
                clayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                //clayout.setLayoutParams(clayoutParams);
                if (showLine)
                    clayout.setBackgroundColor(Color.parseColor("#ffffff"));
                ImageView img = new ImageView(clayout.getContext());
                final Entity e = entity.get(i * columns + j);


                if (e.getIconId() > 0) {
                    img.setImageResource(e.getIconId());
                } else if (e.getPath() != null && e.getPath().startsWith("/"))
                    img.setImageBitmap(BitmapFactory.decodeFile(e.getPath()));
                else {
//                    new ImageRequest(img).execute(e.getPath());
                    final ImageView c = img;
                    new ImageRequest(new ImageRequest.OnRequested() {
                        @Override
                        public void execute(File file) {
                            ActivityHelper.setImage(c, file.getAbsolutePath());
                        }
                    }).execute(e.getPath());
                }

                if (e.getOnClickListener() != null) {
                    img.setOnClickListener(e.getOnClickListener());
                } else
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = IntentUtils.newInstance(container.getContext(), e.getIntentCategory(), e.getIntentAction(), e.getIntentData(), e.getIntentClass());
                            container.getContext().startActivity(intent);
                        }
                    });
                clayout.addView(img, clayoutParams);
                layout.addView(clayout, layoutParams);
            }
            view.addView(layout, playoutParams);
        }
        return view;
    }


    public List<Entity> getEntity() {
        return entity;
    }

    public void setEntity(List<Entity> entity) {
        this.entity = entity;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
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
        private int iconId;
        private String path;
        private String title;
        private String intentCategory;
        private String intentAction;
        private String intentData;
        private String intentClass;
        private View.OnClickListener onClickListener;

        public Entity(int iconId, String title, String intentCategory, String intentAction, String intentData, String intentClass) {
            this.iconId = iconId;
            this.title = title;
            this.intentCategory = intentCategory;
            this.intentAction = intentAction;
            this.intentData = intentData;
            this.intentClass = intentClass;
        }

        public Entity(String path, String title, String intentCategory, String intentAction, String intentData, String intentClass) {
            this.path = path;
            this.title = title;
            this.intentCategory = intentCategory;
            this.intentAction = intentAction;
            this.intentData = intentData;
            this.intentClass = intentClass;
        }


        public Entity(int iconId, String title, View.OnClickListener onClickListener) {
            this.iconId = iconId;
            this.title = title;
            this.onClickListener = onClickListener;
        }


        public Entity(String path, String title, View.OnClickListener onClickListener) {
            this.path = path;
            this.title = title;
            this.onClickListener = onClickListener;
        }


        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntentCategory() {
            return intentCategory;
        }

        public void setIntentCategory(String intentCategory) {
            this.intentCategory = intentCategory;
        }

        public String getIntentAction() {
            return intentAction;
        }

        public void setIntentAction(String intentAction) {
            this.intentAction = intentAction;
        }

        public String getIntentData() {
            return intentData;
        }

        public void setIntentData(String intentData) {
            this.intentData = intentData;
        }

        public String getIntentClass() {
            return intentClass;
        }

        public void setIntentClass(String intentClass) {
            this.intentClass = intentClass;
        }

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }
    }
}

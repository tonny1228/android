package works.tonny.mobile.widget;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.R;

/**
 * 支持下拉刷新
 * Created by tonny on 2015/6/26.
 */
public interface Refreshable {

    Object onRefresh(Handler handler);

    void refreshed(Object data);

    void progressUpdate(Object data);

    void cancelRefresh();

    boolean isRefreshing();

    String getRrefreshId();

    public static class OnTouchListener implements View.OnTouchListener {

        /**
         * 下拉状态
         */
        public static final int STATUS_PULL_TO_REFRESH = 0;

        /**
         * 释放立即刷新状态
         */
        public static final int STATUS_RELEASE_TO_REFRESH = 1;

        /**
         * 正在刷新状态
         */
        public static final int STATUS_REFRESHING = 2;

        /**
         * 刷新完成或未刷新状态
         */
        public static final int STATUS_REFRESH_FINISHED = 3;

        /**
         * 下拉头部回滚的速度
         */
        public static final int SCROLL_SPEED = -20;


        /**
         * 一分钟的毫秒值，用于判断上次的更新时间
         */
        public static final long ONE_MINUTE = 60 * 1000;

        /**
         * 一小时的毫秒值，用于判断上次的更新时间
         */
        public static final long ONE_HOUR = 60 * ONE_MINUTE;

        /**
         * 一天的毫秒值，用于判断上次的更新时间
         */
        public static final long ONE_DAY = 24 * ONE_HOUR;

        /**
         * 一月的毫秒值，用于判断上次的更新时间
         */
        public static final long ONE_MONTH = 30 * ONE_DAY;

        /**
         * 一年的毫秒值，用于判断上次的更新时间
         */
        public static final long ONE_YEAR = 12 * ONE_MONTH;

        /**
         * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
         */
        private static final String UPDATED_AT = "updated_at";


        /**
         * 用于存储上次更新时间
         */
        private SharedPreferences preferences;


        /**
         * 刷新时显示的进度条
         */
        private ProgressBar progressBar;

        /**
         * 指示下拉和释放的箭头
         */
        private ImageView arrow;

        /**
         * 指示下拉和释放的文字描述
         */
        private TextView description;

        /**
         * 上次更新时间的文字描述
         */
        private TextView updateAt;

        /**
         * 上次更新时间的毫秒值
         */
        private long lastUpdateTime;


        /**
         * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
         * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
         */
        private int currentStatus = STATUS_REFRESH_FINISHED;

        /**
         * 记录上一次的状态是什么，避免进行重复操作
         */
        private int lastStatus = currentStatus;

        /**
         * 手指按下时的屏幕纵坐标
         */
        private float yDown;

        /**
         * 在被判定为滚动之前用户手指可以移动的最大值。
         */
        private int touchSlop;

        private ViewGroup scrollView;

        private int begin = -1;

        /**
         * 下拉头
         */
        private RelativeLayout header;

        private Refreshable refreshable;

        private LinearLayout.LayoutParams playoutParams;

        private int hideHeaderHeight;
        private boolean ableToPull;


        /**
         * 设置下拉刷新的容器等
         *
         * @param refreshable 下拉刷新的对象
         * @param parent      下拉头的父容器，头插入到第一个位置
         * @param scrollView  下拉的对象
         */
        public void bindView(Refreshable refreshable, ViewGroup parent, View scrollView) {
            this.refreshable = refreshable;
            this.scrollView = (ViewGroup) scrollView;
            preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());

            header = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.pull_to_refresh, null, true);

            progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
            arrow = (ImageView) header.findViewById(R.id.arrow);
            description = (TextView) header.findViewById(R.id.description);
            updateAt = (TextView) header.findViewById(R.id.updated_at);
            touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();

            refreshUpdatedAtValue();

            parent.addView(header, 0);
            playoutParams = (LinearLayout.LayoutParams) header.getLayoutParams();
            //往上移100
            playoutParams.topMargin = -LayoutUtils.dip2px(60);
            hideHeaderHeight = playoutParams.topMargin;
            header.setLayoutParams(playoutParams);
            scrollView.setOnTouchListener(this);
        }


        /**
         * 刷新下拉头中上次更新时间的文字描述。
         */
        private void refreshUpdatedAtValue() {
            lastUpdateTime = preferences.getLong(UPDATED_AT + refreshable.getRrefreshId(), -1);
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lastUpdateTime;
            long timeIntoFormat;
            String updateAtValue;
            if (lastUpdateTime == -1) {
                updateAtValue = "暂未更新过";
            } else if (timePassed < 0) {
                updateAtValue = "时间错误";
            } else if (timePassed < ONE_MINUTE) {
                updateAtValue = "一分钟前";
            } else if (timePassed < ONE_HOUR) {
                timeIntoFormat = timePassed / ONE_MINUTE;
                String value = timeIntoFormat + "分钟";
                updateAtValue = String.format("上次更新于%1$s前", value);
            } else if (timePassed < ONE_DAY) {
                timeIntoFormat = timePassed / ONE_HOUR;
                String value = timeIntoFormat + "小时";
                updateAtValue = String.format("上次更新于%1$s前", value);
            } else if (timePassed < ONE_MONTH) {
                timeIntoFormat = timePassed / ONE_DAY;
                String value = timeIntoFormat + "天";
                updateAtValue = String.format("上次更新于%1$s前", value);
            } else if (timePassed < ONE_YEAR) {
                timeIntoFormat = timePassed / ONE_MONTH;
                String value = timeIntoFormat + "个月";
                updateAtValue = String.format("上次更新于%1$s前", value);
            } else {
                timeIntoFormat = timePassed / ONE_YEAR;
                String value = timeIntoFormat + "年";
                updateAtValue = String.format("上次更新于%1$s前", value);
            }
            updateAt.setText(updateAtValue);
        }


        /**
         * 更新数据完成，隐藏头
         */
        public void refreshed() {
            playoutParams.topMargin = -1 * header.getHeight();
            header.setLayoutParams(playoutParams);
            currentStatus = STATUS_REFRESH_FINISHED;
//            refreshUpdatedAtValue();
            preferences.edit().putLong(UPDATED_AT + refreshable.getRrefreshId(), System.currentTimeMillis()).commit();
            begin = -1;
        }


        /**
         * 手滑动时的事件
         *
         * @param v
         * @param event
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            setIsAbleToPull(event);
            if (!ableToPull) {
                return false;
            }
            int y = LayoutUtils.getScrollY(scrollView);
            boolean state = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //记住开始位置
                    //if (y == 0)
                    begin = (int) event.getRawY();
                    refreshUpdatedAtValue();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (begin < 0) {
                        begin = (int) event.getRawY();
                        refreshUpdatedAtValue();
                    }
                    float yMove = event.getRawY();
                    int distance = (int) (yMove - begin);

                    if (distance <= 0 && playoutParams.topMargin <= -header.getHeight()) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    playoutParams.topMargin = (distance / 2) - header.getHeight();
                    header.setLayoutParams(playoutParams);
                    if (currentStatus != STATUS_REFRESHING) {
                        if (playoutParams.topMargin > 0) {
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            currentStatus = STATUS_PULL_TO_REFRESH;
                        }
                    }

                    state = true;
                    break;
                case MotionEvent.ACTION_UP:

                default:
                    if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                        new RefreshingTask().execute();
                        //refreshable.onRefresh();
                        //
                    } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
                        // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                        new HideHeaderTask().execute();
                        begin = -1;
                        // refreshable.
                    } else if (currentStatus == STATUS_REFRESH_FINISHED && y == 0) {
                        begin = -1;
                    }

                    break;
            }


            // 时刻记得更新下拉头中的信息
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
                scrollView.setPressed(false);
                scrollView.setFocusable(false);
                scrollView.setFocusableInTouchMode(false);
                lastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                return true;
            }
            return state;
        }

        private void setIsAbleToPull(MotionEvent event) {
            int firstVisiblePos = LayoutUtils.getScrollY(scrollView);
            if (firstVisiblePos == 0) {
                if (!ableToPull) {
                    begin = (int) event.getRawY();
                }
                // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
                ableToPull = true;
            } else {
                if (playoutParams.topMargin != hideHeaderHeight) {
                    playoutParams.topMargin = hideHeaderHeight;
                    header.setLayoutParams(playoutParams);
                }
                ableToPull = false;
            }
        }


        public void refresh() {
            new RefreshingTask().execute();
        }


        /**
         * 更新下拉头中的信息。
         */

        private void updateHeaderView() {
            if (lastStatus == currentStatus) {
                return;
            }
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                description.setText("下拉刷新");
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText("释放更新数据");
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_REFRESHING) {
                description.setText("刷新中");
                progressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }
//            refreshUpdatedAtValue();

        }


        /**
         * 根据当前的状态来旋转箭头。
         */
        private void rotateArrow() {
            float pivotX = arrow.getWidth() / 2f;
            float pivotY = arrow.getHeight() / 2f;
            float fromDegrees = 0f;
            float toDegrees = 0f;
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                fromDegrees = 180f;
                toDegrees = 360f;
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                fromDegrees = 0f;
                toDegrees = 180f;
            }
            RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
            animation.setDuration(100);
            animation.setFillAfter(true);
            arrow.startAnimation(animation);
        }


        /**
         * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
         *
         * @author guolin
         */
        class RefreshingTask extends AsyncTask<Void, Object, Object> {

            @Override
            protected void onPreExecute() {
                if (!DeviceUtils.isNetworkConnected(scrollView.getContext())) {
//                    refreshed();
//                    this.cancel(true);
                    //refreshable.refreshed(null);
//                    refreshed();
                    LayoutUtils.alert(scrollView.getContext(), "网络不可用").show();
                }
            }

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    refreshable.progressUpdate(new Object[]{msg.what, msg.obj});
                }
            };

            @Override
            protected Object doInBackground(Void... params) {
                int topMargin = playoutParams.topMargin;
                while (true) {
                    topMargin = topMargin + SCROLL_SPEED;
                    if (topMargin <= 0) {
                        topMargin = 0;
                        break;
                    }
                    publishProgress(topMargin);
                    sleep(10);
                }
                currentStatus = STATUS_REFRESHING;
                publishProgress(0);
                return refreshable.onRefresh(handler);
            }

            @Override
            protected void onProgressUpdate(Object... topMargin) {
                updateHeaderView();
                playoutParams.topMargin = (int) topMargin[0];
                header.setLayoutParams(playoutParams);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                refreshed();
            }

            @Override
            protected void onPostExecute(Object s) {
                refreshable.refreshed(s);
                refreshed();
            }
        }

        /**
         * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
         *
         * @author guolin
         */
        class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

            @Override
            protected Integer doInBackground(Void... params) {
                int topMargin = playoutParams.topMargin;
                while (true) {
                    topMargin = topMargin + SCROLL_SPEED;
                    if (topMargin <= hideHeaderHeight) {
                        topMargin = hideHeaderHeight;
                        break;
                    }
                    publishProgress(topMargin);
                    sleep(10);
                }
                return topMargin;
            }

            @Override
            protected void onProgressUpdate(Integer... topMargin) {
                playoutParams.topMargin = topMargin[0];

                header.setLayoutParams(playoutParams);
            }

            @Override
            protected void onPostExecute(Integer topMargin) {
                playoutParams.topMargin = topMargin;
                header.setLayoutParams(playoutParams);
                currentStatus = STATUS_REFRESH_FINISHED;
            }
        }

        /**
         * 使当前线程睡眠指定的毫秒数。
         *
         * @param time 指定当前线程睡眠多久，以毫秒为单位
         */
        private void sleep(int time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

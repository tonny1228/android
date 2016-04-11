package works.tonny.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tonny on 2015/6/29.
 */
public class ScrollView extends android.widget.ScrollView {

    private int positionY;

    private float xDistance, yDistance, xLast, yLast;

    int index;

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;


    public ScrollView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);

    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        positionY = t;
    }

    public int getPositionY() {
        return positionY;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//
//        if (index++ > 0) {
//            return true;
//        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
                if (xDistance > yDistance) {
                    //Log.info(",,,,,,,,,,,,,,,,, false");

                }
                break;
        }

        /**/
//        if (yDistance > xDistance) {
//            Log.info(",,,,,,,,,,,,,,,,, true");
//            return true;
//        }
        boolean b = super.onInterceptTouchEvent(ev);
        return b;//&& mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}

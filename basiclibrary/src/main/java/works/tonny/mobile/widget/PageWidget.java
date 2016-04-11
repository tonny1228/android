package works.tonny.mobile.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class PageWidget extends FrameLayout {

	private int mWidth = 0;
	private int mHeight = 0;
	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;
	private Path mPath0;
	private Path mPath1;


	PointF mTouch = new PointF(); // 拖拽点
	PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

	PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	PointF mBezierControl2 = new PointF();
	PointF mBeziervertex2 = new PointF();
	PointF mBezierEnd2 = new PointF();

	PointF mLT = new PointF();
	PointF mRT = new PointF();
	PointF mLB = new PointF();
	PointF mRB = new PointF();
	PointF mBztemp;
	PointF mBztempStart = new PointF();

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	float mTouchToCornerDis;
	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // 是否属于右上左下
	float mMaxLength;
	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;

	Paint mPaint;

	Scroller mScroller;
	private boolean isAnimated = false;
	private View currentView = null;
	private View nextView = null;
	private View nextViewTranscript = null;
	private Context mContext;

	private BaseAdapter mAdapter = null;
	private int currentPosition = -1;
	private int itemCount = 0;
	private OnPageTurnListener turnListener;

	public PageWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		viewInit();
	}


	public PageWidget(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		viewInit();
	}

	private void viewInit() {
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		setOnTouchListener(new FingerTouchListener());

	}

	private class FingerTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (v == PageWidget.this && mAdapter != null) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if(itemCount == 0) { return false; }
					abortAnimation();
					calcCornerXY(event.getX(), event.getY());
					if (DragToRight()) {
						if(currentPosition == 0) { mCornerX = 0; mCornerY = 0; return false; }
//						Log.d("PageWidget->DragToRight", "" + (currentPosition-1));
						nextView = mAdapter.getView(currentPosition-1, nextView, null);
						nextViewTranscript = mAdapter.getView(currentPosition-1, nextViewTranscript, null);
					} else {
						if(currentPosition == itemCount-1) { mCornerX = 0; mCornerY = 0; return false; }
						if(currentPosition < 0) {currentPosition = 0;}
//						Log.d("PageWidget->", "" + (currentPosition+1));
						nextView = mAdapter.getView(currentPosition+1, nextView, null);
						nextViewTranscript = mAdapter.getView(currentPosition+1, nextViewTranscript, null);
					}
					isAnimated = false;
					mTouch.x = event.getX();
					mTouch.y = event.getY();
					nextView.setVisibility(View.VISIBLE);
					nextViewTranscript.setVisibility(View.VISIBLE);
					PageWidget.this.postInvalidate();

				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float x = event.getX();
					float y = event.getY();
					if (x > mWidth) {
						mTouch.x = mWidth-0.01f;
					} else if (x < 0){
						mTouch.x = 0.01f;
					} else {
						mTouch.x = x;
					}

					if (y > mHeight) {
						mTouch.y = mHeight-0.01f;
					} else if (y < 0){
						mTouch.y = 0.01f;
					} else {
						mTouch.y = y;
					}
					PageWidget.this.postInvalidate();

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (canDragOver()) {
						isAnimated = true;
						startAnimation(500);
					} else {
						mTouch.x = 0.01f;
						mTouch.y = 0.01f;
						mCornerX = 0;
						mCornerY = 0;
						nextView.setVisibility(View.INVISIBLE);
						nextViewTranscript.setVisibility(View.INVISIBLE);
					}
					PageWidget.this.postInvalidate();
				}
				return true;

			} else {
				return false;
			}

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if(mWidth == 0) {
			mWidth = getWidth();
			mHeight = getHeight();

			mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
			mTouch.y = 0.01f;
			mLT.x = 0;
			mLT.y = 0;
			mLB.x = 0;
			mLB.y = mHeight;
			mRT.x = mWidth;
			mRT.y = 0;
			mRB.x = mWidth;
			mRB.y = mHeight;
			mMaxLength = (float) Math.hypot(mWidth, mHeight);
		}

	}


	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		calcPoints();
		super.dispatchDraw(canvas);
		if(itemCount > 1){
			drawCurrentPageShadow(canvas);
			drawCurrentBackArea(canvas, nextViewTranscript);
		}

	}


	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		// TODO Auto-generated method stub
		if(child.equals(currentView)) {
			drawCurrentPageArea(canvas, child, mPath0);
		} else {
			drawNextPageAreaAndShadow(canvas, child);
		}

		return true;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 计算拖拽点对应的拖拽脚
	 */
	public void calcCornerXY(float x, float y) {
		if (x <= mWidth / 2)
			mCornerX = 0;
		else
			mCornerX = mWidth;
		if (y <= mHeight / 2)
			mCornerY = 0;
		else
			mCornerY = mHeight;
		if ((mCornerX == 0 && mCornerY == mHeight)
				|| (mCornerX == mWidth && mCornerY == 0))
			mIsRTandLB = true;
		else
			mIsRTandLB = false;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 求解直线P1P2和直线P3P4的交点坐标
	 */
	public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// 二元函数通式： y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	private void calcPoints() {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
				* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);



		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
				/ 3;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
		// 如果继续翻页，会出现BUG故在此限制
		if(!isAnimated)	{
			if(mCornerX == 0 && mBezierStart1.x > mWidth/2) {
				float f1 = Math.abs(mCornerX - mTouch.x);
				float f2 = mWidth/2 * f1 / mBezierStart1.x;
				mTouch.x = Math.abs(mCornerX - f2);

				float f3 = Math.abs(mCornerX - mTouch.x)
						* Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);

				mMiddleX = (mTouch.x + mCornerX) / 2;
				mMiddleY = (mTouch.y + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
						* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
						* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

				mBezierStart1.x = mBezierControl1.x
						- (mCornerX - mBezierControl1.x) / 3;
			}
			if(mCornerX == mWidth && mBezierStart1.x < mWidth/2) {
				mBezierStart1.x = mWidth - mBezierStart1.x;
				float f1 = Math.abs(mCornerX - mTouch.x);
				float f2 = mWidth/2 * f1 / mBezierStart1.x;
				mTouch.x = Math.abs(mCornerX - f2);
				float f3 = Math.abs(mCornerX - mTouch.x)
						* Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);
				mMiddleX = (mTouch.x + mCornerX) / 2;
				mMiddleY = (mTouch.y + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
						* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
						* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
				mBezierStart1.x = mBezierControl1.x
						- (mCornerX - mBezierControl1.x) / 3;
			}
		}

		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
				/ 3;

		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
				(mTouch.y - mCornerY));

		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1,
				mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1,
				mBezierStart2);

		/*
		 * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	private void drawCurrentPageArea(Canvas canvas, View child, Path path) {
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
				mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
				mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();

		canvas.save();
		canvas.clipPath(path, Region.Op.XOR);
		child.draw(canvas);
		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas, View child) {
		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();

		mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
				- mCornerX, mBezierControl2.y - mCornerY));
		int leftx;
		int rightx;
		GradientDrawable mBackShadowDrawable;
		if (mIsRTandLB) {
			leftx = (int) (mBezierStart1.x);
			rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
			rightx = (int) mBezierStart1.x;
			mBackShadowDrawable = mBackShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		child.draw(canvas);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
				(int) (mMaxLength + mBezierStart1.y));
		mBackShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void setScreen(int w, int h) {
		mWidth = w;
		mHeight = h;
	}


	/**
	 * Author : hmg25 Version: 1.0 Description : 创建阴影的GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0x111111 };
		mBackShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(
				GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 绘制翻起页的阴影
	 */
	private void drawCurrentPageShadow(Canvas canvas) {
		double degree;
		if (mIsRTandLB) {
			degree = Math.PI
					/ 4
					- Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x
					- mBezierControl1.x);
		} else {
			degree = Math.PI
					/ 4
					- Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x
					- mBezierControl1.x);
		}

		if(Math.toDegrees(degree) > 220) {
			return;
		}

		// 翻起页阴影顶点与touch点的距离
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (mTouch.x + d1);
		float y;
		if (mIsRTandLB) {
			y = (float) (mTouch.y + d2);
		} else {
			y = (float) (mTouch.y - d2);
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
		mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl1.x);
			rightx = (int) mBezierControl1.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mBezierControl1.x - 25);
			rightx = (int) mBezierControl1.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x
				- mBezierControl1.x, mBezierControl1.y - mTouch.y));
		canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
		mCurrentPageShadow.setBounds(leftx,
				(int) (mBezierControl1.y - mMaxLength), rightx,
				(int) (mBezierControl1.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

		int offset = mCornerX > 0 ? 30 : -30;

		if(mBezierControl2.y < 0) {
			mBztemp = getCross(mLT, mRT, mTouch, mBezierControl2);
			mBztempStart.x = mBztemp.x - offset;
			mBztempStart.y = mBztemp.y;
		} else if(mBezierControl2.y > mHeight){
			mBztemp = getCross(mLB, mRB, mTouch, mBezierControl2);
			mBztempStart.x = mBztemp.x - offset;
			mBztempStart.y = mBztemp.y;
		} else {
			mBztemp = mBezierControl2;
			mBztempStart = mBezierStart2;
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBztemp.x, mBztemp.y);
		mPath1.lineTo(mBztempStart.x, mBztempStart.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		if (mIsRTandLB) {
			leftx = (int) (mBztemp.y);
			rightx = (int) (mBztemp.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mBztemp.y - 25);
			rightx = (int) (mBztemp.y );
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mBztemp.y
				- mTouch.y, mBztemp.x - mTouch.x));
		canvas.rotate(rotateDegrees, mBztemp.x, mBztemp.y);
		mCurrentPageShadow.setBounds(
				(int) (mBztemp.x - mMaxLength), leftx,
				(int) (mBztemp.x), rightx);
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 绘制翻起页背面
	 */
	private void drawCurrentBackArea(Canvas canvas, View view) {
		int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
		float f1 = Math.abs(i - mBezierControl1.x);
		/*int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
		float f2 = Math.abs(i1 - mBezierControl2.y);*/
		float f3 = f1;
		mPath1.reset();
		mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mBezierStart1.x - 1);
			right = (int) (mBezierStart1.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mBezierStart1.x - f3 - 1);
			right = (int) (mBezierStart1.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		//mPaint.setColorFilter(mColorMatrixFilter);

		float rotateDegrees = (float) Math.toDegrees(Math.PI/2 + Math.atan2(mBezierControl2.y
				- mTouch.y, mBezierControl2.x - mTouch.x));

		if(mCornerY == 0) {
			rotateDegrees -= 180;
		}
		mMatrix.reset();
		mMatrix.setPolyToPoly(new float[] {Math.abs(mWidth-mCornerX), mCornerY}, 0, new float[] {mTouch.x, mTouch.y}, 0, 1);
		mMatrix.postRotate(rotateDegrees, mTouch.x, mTouch.y);
		canvas.save();
		canvas.concat(mMatrix);
		view.draw(canvas);
		canvas.restore();
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right+2,
				(int) (mBezierStart1.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;
			postInvalidate();
		}
		if(isAnimated && mScroller.isFinished()) {
			isAnimated = false;
			if(DragToRight()) {
				currentPosition -= 1;
			} else {
				currentPosition += 1;
			}
			currentView = mAdapter.getView(currentPosition, currentView, null);

			mTouch.x = 0.01f;
			mTouch.y = 0.01f;
			mCornerX = 0;
			mCornerY = 0;
			nextView.setVisibility(View.INVISIBLE);
			nextViewTranscript.setVisibility(View.INVISIBLE);
			postInvalidate();
			if(turnListener != null) {
				turnListener.onTurn(itemCount, currentPosition);
			}
		}
	}


	private void startAnimation(int delayMillis) {
		int dx, dy;
		// dx 水平方向滑动的距离，负值会使滚动向左滚动
		// dy 垂直方向滑动的距离，负值会使滚动向上滚动

		if (mCornerX > 0) {
			dx = (int) (- mTouch.x + 1);
		} else {
			dx = (int) (mWidth - mTouch.x - 1);
		}
		if (mCornerY > 0) {
			dy = (int) (mHeight - mTouch.y - 1);
		} else {
			dy = (int) (1 - mTouch.y); // 防止mTouch.y最终变为0
		}
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy,
				delayMillis);

	}


	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
			computeScroll();
		}
	}

	public boolean canDragOver() {
		if (mTouchToCornerDis > mWidth / 10)
			return true;
		return false;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 是否从左边翻向右边
	 */
	public boolean DragToRight() {
		if (mCornerX > 0)
			return false;
		return true;
	}

	public void setAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
		itemCount = mAdapter.getCount();
		currentView = null;
		nextView = null;
		nextViewTranscript = null;
		removeAllViews();
		if(itemCount != 0) {
			currentPosition = 0;
			currentView = mAdapter.getView(currentPosition, null, null);
			addView(currentView);
			if(itemCount > 1) {
				nextView = mAdapter.getView(currentPosition, null, null);
				nextViewTranscript = mAdapter.getView(currentPosition, null, null);
				nextView.setVisibility(View.INVISIBLE);
				nextViewTranscript.setVisibility(View.INVISIBLE);
				addView(nextView);
				addView(nextViewTranscript);
			}

		} else {
			currentPosition = -1;
		}
		mTouch.x = 0.01f;
		mTouch.y = 0.01f;
		mCornerX = 0;
		mCornerY = 0;
		postInvalidate();
		if(turnListener != null) {
			turnListener.onTurn(itemCount, currentPosition);
		}

	}

	/**用于翻页结束后的页码通知
	 * @author xf
	 *
	 */
	public interface OnPageTurnListener {
		public void onTurn(int count, int currentPosition);
	}

	public void setOnPageTurnListener(OnPageTurnListener listener) {
		turnListener = listener;
	}
}

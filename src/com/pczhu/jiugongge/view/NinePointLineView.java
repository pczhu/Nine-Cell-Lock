package com.pczhu.jiugongge.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.pczhu.jiugongge.R;
import com.pczhu.jiugongge.R.drawable;
import com.pczhu.jiugongge.ui.LoginActivity;
import com.pczhu.jiugongge.ui.MainActivity;

/**
 * 作用：手势密码的九宫格 
 * 作者：ufnind 
 * 时间：2013年10月29日 09:34:52
 * */
public class NinePointLineView extends View {
	//划线笔
	Paint linePaint = new Paint();
	//划线白边
	Paint whiteLinePaint = new Paint();
	//文字笔
	Paint textPaint = new Paint();
	//单元图片改变
	Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.lock);
	//图片宽度一半作为默认点半径
	int defaultBitmapRadius = defaultBitmap.getWidth() / 2;
	//选中区域的大圆圈
	Bitmap selectedBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.indicator_lock_area);
	//选中区域的宽度
	int selectedBitmapDiameter = selectedBitmap.getWidth();
	//选中区域的一半作为半径
	int selectedBitmapRadius = selectedBitmapDiameter / 2;
	//九个点的集合
	PointInfo[] points = new PointInfo[9];
	//起始点
	PointInfo startPoint = null;

	int width, height;

	int moveX, moveY;
	//是否是按下后抬起的状态
	boolean isUp = false;

	Context cxt;
	//密码
	StringBuffer lockString = new StringBuffer();
	private boolean isLock = false;
	public NinePointLineView(Context context,boolean isLock) {
		this(context);
		this.isLock  = isLock;
	}
	public NinePointLineView(Context context) {

		super(context);

		cxt = context;
		shake = AnimationUtils.loadAnimation(cxt, R.anim.shake);
		//初始化笔
		initPaint(true);
	}

	public NinePointLineView(Context context, AttributeSet attrs) {

		super(context, attrs);

//		this.setBackgroundColor(Color.WHITE);//设置整个九宫格的背景

		initPaint(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		width = getWidth();

		height = getHeight();

		if (width != 0 && height != 0) {

			initPoints(points);

		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		super.onLayout(changed, left, top, right, bottom);

	}

	private int startX = 0, startY = 0;

	private Animation shake;

	@Override
	protected void onDraw(Canvas canvas) {

		// canvas.drawText("passwd:" + lockString, 0, 40, textPaint);

		if (moveX != 0 && moveY != 0 && startX != 0 && startY != 0) {
			// drawLine(canvas, startX, startY, moveX, moveY);
		}

		drawNinePoint(canvas);

		super.onDraw(canvas);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean flag = true;

		if (isUp) {

			finishDraw();

			flag = false;

		} else {
			handlingEvent(event);

			flag = true;

		}
		return flag;
	}

	private void handlingEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:

			moveX = (int) event.getX();

			moveY = (int) event.getY();

			for (PointInfo temp : points) {

				if (temp.isInMyPlace(moveX, moveY)
						&& temp.isSelected() == false) {

					temp.setSelected(true);

					startX = temp.getCenterX();

					startY = temp.getCenterY();

					int len = lockString.length();

					if (len != 0) {

						int preId = lockString.charAt(len - 1) - 48;

						points[preId].setNextId(temp.getId());

					}

					lockString.append(temp.getId());

					break;
				}
			}

			invalidate(0, height - width, width, height);

			break;

		case MotionEvent.ACTION_DOWN:
			
			int downX = (int) event.getX();

			int downY = (int) event.getY();

			for (PointInfo temp : points) {

				if (temp.isInMyPlace(downX, downY)) {

					temp.setSelected(true);

					startPoint = temp;

					startX = temp.getCenterX();

					startY = temp.getCenterY();

					lockString.append(temp.getId());

					break;
				}
			}
			invalidate(0, height - width, width, height);

			break;

		case MotionEvent.ACTION_UP:

			startX = startY = moveX = moveY = 0;

			isUp = true;

			invalidate();
			
			savePwd();

			break;

		default:
			break;
		}
	}

	private void finishDraw() {

			for (PointInfo temp : points) {

				temp.setSelected(false);

				temp.setNextId(temp.getId());

			}

		lockString.delete(0, lockString.length());

		isUp = false;

		invalidate();
	}
	/**
	 * 初始化点阵
	 * @param points 点阵数组
	 */
	private void initPoints(PointInfo[] points) {
		//点个数
		int len = points.length;
		//点阵间隔  （总控件宽度-三个选中圈的宽度）/4个间隔区域
		int seletedSpacing = (width - selectedBitmapDiameter * 3) / 4;

		int seletedX = seletedSpacing;

		int seletedY = height - width + seletedSpacing;

		int defaultX = seletedX + selectedBitmapRadius - defaultBitmapRadius;

		int defaultY = seletedY + selectedBitmapRadius - defaultBitmapRadius;

		for (int i = 0; i < len; i++) {

			if (i == 3 || i == 6) {

				seletedX = seletedSpacing;

				seletedY += selectedBitmapDiameter + seletedSpacing;

				defaultX = seletedX + selectedBitmapRadius
						- defaultBitmapRadius;

				defaultY += selectedBitmapDiameter + seletedSpacing;

			}

			points[i] = new PointInfo(i, defaultX, defaultY, seletedX, seletedY);

			seletedX += selectedBitmapDiameter + seletedSpacing;

			defaultX += selectedBitmapDiameter + seletedSpacing;

		}
	}

	private void initPaint(boolean b) {
		if(!b){
			initLinePaint(linePaint,b);

			initTextPaint(textPaint);

			initWhiteLinePaint(whiteLinePaint,b);
		}else{
			initLinePaint(linePaint,b);

			initTextPaint(textPaint);

			initWhiteLinePaint(whiteLinePaint,b);
		}


	}

	/**
	 * @param paint
	 */
	private void initTextPaint(Paint paint) {

		textPaint.setTextSize(30);

		textPaint.setAntiAlias(true);

		textPaint.setTypeface(Typeface.MONOSPACE);

	}

	/**
	 * @param paint
	 */
	private void initLinePaint(Paint paint,boolean b) {
		if(b){
			paint.setColor(Color.GRAY);
		}else{
			paint.setColor(Color.TRANSPARENT);
		}

		paint.setStrokeWidth(30);//设置两个点之间的线的北京的宽度

		paint.setAntiAlias(true);

		paint.setStrokeCap(Cap.ROUND);

	}

	/**
	 * @param paint
	 */
	private void initWhiteLinePaint(Paint paint,boolean b) {
		if(b){
			paint.setColor(Color.WHITE);
		}else{
			paint.setColor(Color.TRANSPARENT);
		}

		paint.setStrokeWidth(20);//设置两个点之间的线的宽度

		paint.setAntiAlias(true);

		paint.setStrokeCap(Cap.ROUND);

	}

	/**
	 * 
	 * @param canvas
	 */
	private void drawNinePoint(Canvas canvas) {
		SharedPreferences mySharedPreferences = cxt.getSharedPreferences("SET", Activity.MODE_PRIVATE); 
		if (startPoint != null) {
			if(mySharedPreferences.getBoolean(""+0, true)){
				drawEachLine(canvas, startPoint);
			}

		}
		
		
			for (PointInfo pointInfo : points) {
	
				if (pointInfo != null) {
	
					if (pointInfo.isSelected()) {
						if(mySharedPreferences.getBoolean(""+0, true)){
							canvas.drawBitmap(selectedBitmap, pointInfo.getSeletedX(),
									pointInfo.getSeletedY(), null);
						}
					}
	
					canvas.drawBitmap(defaultBitmap, pointInfo.getDefaultX(),
							pointInfo.getDefaultY(), null);
	
				}
			}
		

	}

	/**
	 * @param canvas
	 * @param point
	 */
	private void drawEachLine(Canvas canvas, PointInfo point) {
		if (point.hasNextId()) {
			int n = point.getNextId();
			drawLine(canvas, point.getCenterX(), point.getCenterY(),
					points[n].getCenterX(), points[n].getCenterY());
			drawEachLine(canvas, points[n]);
		}
	}

	/**
	 * 
	 * @param canvas
	 * @param startX
	 * @param startY
	 * @param stopX
	 * @param stopY
	 */
	private void drawLine(Canvas canvas, float startX, float startY,
			float stopX, float stopY) {

		canvas.drawLine(startX, startY, stopX, stopY, linePaint);

		canvas.drawLine(startX, startY, stopX, stopY, whiteLinePaint);

	}

	/**
	 * @author zkwlx
	 * 
	 */
	private class PointInfo {

		private int id;

		private int nextId;

		private boolean selected;

		private int defaultX;

		private int defaultY;

		private int seletedX;

		private int seletedY;

		public PointInfo(int id, int defaultX, int defaultY, int seletedX,
				int seletedY) {
			this.id = id;
			this.nextId = id;
			this.defaultX = defaultX;
			this.defaultY = defaultY;
			this.seletedX = seletedX;
			this.seletedY = seletedY;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public int getId() {
			return id;
		}

		public int getDefaultX() {
			return defaultX;
		}

		public int getDefaultY() {
			return defaultY;
		}

		public int getSeletedX() {
			return seletedX;
		}

		public int getSeletedY() {
			return seletedY;
		}

		public int getCenterX() {
			return seletedX + selectedBitmapRadius;
		}

		public int getCenterY() {
			return seletedY + selectedBitmapRadius;
		}

		public boolean hasNextId() {
			return nextId != id;
		}

		public int getNextId() {
			return nextId;
		}

		public void setNextId(int nextId) {
			this.nextId = nextId;
		}

		/**
		 * @param x
		 * @param y
		 */
		public boolean isInMyPlace(int x, int y) {
			boolean inX = x > seletedX
					&& x < (seletedX + selectedBitmapDiameter);
			boolean inY = y > seletedY
					&& y < (seletedY + selectedBitmapDiameter);

			return (inX && inY);
		}

	}

	public String getPwd() {//获取本次的密码

		return lockString.toString();

	}
	
	/**
	 * 作用：保存密码并且判断界面的跳转
	 * 作者：unfind
	 * 时间：2013年10月29日 14:47:47
	 * 描述：每次抬起手指都会进行判断
	 * */
	public void savePwd(){
		if(!isEffective()){
			return;
		}
		
		
		SharedPreferences shareDate = cxt.getSharedPreferences("GUE_PWD", 0);
		if(isLock){
			String pwd = this.getPwd();
			String mPwd = shareDate.getString("GUE_PWD", null);
			if(pwd.equals(mPwd)){
				finishDraw();
				onStateChangedListener.OnStateChanged(6);
				((Activity)cxt).finish();
			}else{
				this.startAnimation(shake);
				finishDraw();
				onStateChangedListener.OnStateChanged(5);
			}
		}else{
			boolean isSetFirst = shareDate.getBoolean("IS_SET_FIRST", false);
			if(isSetFirst){//如果第一次已经设置密码，验证第二次和第一次是否一致
				String pwd = this.getPwd();
				String first_pwd = shareDate.getString("FIRST_PWD", "NO HAVE PWD");
				if(pwd.equals(first_pwd)){//第二次密码和第一次密码一样   设置成功
					shareDate.edit().clear().commit();
					shareDate.edit().putBoolean("IS_SET", true).commit();
					shareDate.edit().putString("GUE_PWD", pwd).commit();
					finishDraw();
					Toast.makeText(cxt, "手势密码设置成功", 0).show();
					onStateChangedListener.OnStateChanged(3);
					((Activity)cxt).finish();
				}else{//第二次输入的密码和第一次输入的密码不一致
					shareDate.edit().putBoolean("SECOND_ERROR", true).commit();
					this.startAnimation(shake);
					finishDraw();
					onStateChangedListener.OnStateChanged(2);
				}
				
			}else{//第一次设置手势密码
				shareDate.edit().clear().commit();
				shareDate.edit().putString("FIRST_PWD", this.getPwd()).commit();
				shareDate.edit().putBoolean("IS_SET_FIRST", true).commit();
				finishDraw();
				onStateChangedListener.OnStateChanged(1);
			}
		}

//		cxt.startActivity(intent);
//		((Activity)cxt).finish();
		
	}
	private boolean isEffective() {
		if(lockString.length() < 4){
			this.startAnimation(shake);
			finishDraw();
			onStateChangedListener.OnStateChanged(4);
			return false;
		}
		return true;
	}
	OnStateChangedListener onStateChangedListener;
	public void setOnStateChanged(OnStateChangedListener onStateChangedListener){
		this.onStateChangedListener = onStateChangedListener;
	}
	public interface OnStateChangedListener{
		public void OnStateChanged(int statue);
	}
}

package com.example.androiddraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View
{
	Paint paint;
	RectF oval;
	int timeStrokeWidth = 30;
	int btnStrokeWidth = 5;
	int textSize = 30;
	int btnRadius = (timeStrokeWidth - btnStrokeWidth) / 2;
	float radius_hour, radius_minu, radius_sec;

	private Handler timeHandler = new Handler();
	int timeMillis = 0;

	float degree_hour = 0;
	float degree_minu = 0;
	float degree_sec = 0;

	float touchDegree_hour = 90;
	float touchDegree_minu = 90;
	float touchDegree_sec = 90;

	float hour_x = 0, hour_y = 0;
	float minu_x = 0, minu_y = 0;
	float sec_x = 0, sec_y = 0;

	float center_x, center_y;

	boolean isTouch_Hour = false;
	boolean isTouch_Minu = false;
	boolean isTouch_Sec = false;

	boolean isStopwatch = false;
	boolean isReciprocal = false;

	public MyView(Context context)
	{
		super(context);
		paint = new Paint();
		oval = new RectF();

		timeHandler.postDelayed(timerRun, 10); //Timer
	}

	private final Runnable timerRun = new Runnable() //運動計時
	{
		public void run()
		{
			if (isStopwatch) //碼表
			{
				degree_sec = (timeMillis * (360f / 60f / 100f)) % 360f;
				degree_minu = (timeMillis * (360f / 3600f / 100f)) % 360f;
				degree_hour = (timeMillis * (360f / 86400f / 100f)) % 360f;

				invalidate();
				timeMillis += 1;
			}
			if (isReciprocal)
			{
				degree_sec = (timeMillis * (360f / 60f / 100f)) % 360f;
				degree_minu = (timeMillis * (360f / 3600f / 100f)) % 360f;
				degree_hour = (timeMillis * (360f / 86400f / 100f)) % 360f;

				invalidate();
				timeMillis -= 1;
			}
			timeHandler.postDelayed(this, 10);
		}
	};

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		float width = (float) getWidth();
		float height = (float) getHeight();

		if (width > height)
		{
			radius_sec = height / 3;
		}
		else
		{
			radius_sec = width / 3;
		}

		//		Path path = new Path();
		//		path.addCircle(width / 2, height / 2, radius_hour, Path.Direction.CW);

		center_x = width / 2;
		center_y = height / 2;

		radius_minu = radius_sec - timeStrokeWidth;
		radius_hour = radius_minu - timeStrokeWidth;

		//底色		
		paint.setColor(Color.LTGRAY);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_sec, center_y - radius_sec, center_x + radius_sec, center_y + radius_sec);
		canvas.drawArc(oval, 0, 360, false, paint);

		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_minu, center_y - radius_minu, center_x + radius_minu, center_y + radius_minu);
		canvas.drawArc(oval, 0, 360, false, paint);

		paint.setColor(Color.DKGRAY);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_hour, center_y - radius_hour, center_x + radius_hour, center_y + radius_hour);
		canvas.drawArc(oval, 0, 360, false, paint); //順時針,3點方向為0

		//秒
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_sec, center_y - radius_sec, center_x + radius_sec, center_y + radius_sec);
		canvas.drawArc(oval, -90, degree_sec, false, paint);

		if (!isStopwatch && !isReciprocal)
		{
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(btnStrokeWidth);

			if (isTouch_Sec)
				paint.setStyle(Paint.Style.FILL);

			//android 座標 正向下
			sec_x = center_x + (float) ((radius_sec + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_sec)));
			sec_y = center_y - (float) ((radius_sec + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_sec)));

			canvas.drawCircle(sec_x, sec_y, btnRadius, paint);
		}

		//分
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_minu, center_y - radius_minu, center_x + radius_minu, center_y + radius_minu);
		canvas.drawArc(oval, -90, degree_minu, false, paint);

		if (!isStopwatch && !isReciprocal)
		{
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(btnStrokeWidth);

			if (isTouch_Minu)
				paint.setStyle(Paint.Style.FILL);

			//android 座標 正向下
			minu_x = center_x + (float) ((radius_minu + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_minu)));
			minu_y = center_y - (float) ((radius_minu + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_minu)));

			canvas.drawCircle(minu_x, minu_y, btnRadius, paint);
		}

		//時
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_hour, center_y - radius_hour, center_x + radius_hour, center_y + radius_hour);
		canvas.drawArc(oval, -90, degree_hour, false, paint); //順時針,3點方向為0

		if (!isStopwatch && !isReciprocal)
		{
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(btnStrokeWidth);

			if (isTouch_Hour)
				paint.setStyle(Paint.Style.FILL);

			//android 座標 正向下
			hour_x = center_x + (float) ((radius_hour + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_hour)));
			hour_y = center_y - (float) ((radius_hour + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_hour)));

			canvas.drawCircle(hour_x, hour_y, btnRadius, paint);
		}
		
		canvas.drawCircle(center_x, center_y, btnRadius, paint);

		//字
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setStrokeWidth(0);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		paint.setColor(Color.BLACK);
		paint.setTextSize(textSize);

		String timeString = String.format("%02d:%02d:%02d", (int) (degree_hour / 15) % 24, (int) (degree_minu / 6) % 60, (int) (degree_sec / 6) % 60);
		canvas.drawText(timeString, center_x, center_y, paint);
		
		if (isStopwatch)
		{
			paint.setTextSize(textSize / 2);
			String timeMilliString = String.format(".%02d", timeMillis % 100);
			canvas.drawText(timeMilliString, center_x + paint.measureText(timeString)+ paint.measureText(timeMilliString) / 2, center_y, paint);
		}
	}
	
	private void DrawDemo(Canvas canvas)
	{
		/*
		 * 方法 說明 drawRect 繪製矩形 drawCircle 繪製圓形 drawOval 繪製橢圓 drawPath 繪製任意多邊形
		 * drawLine 繪製直線 drawPoin 繪製點
		 */
		// 創建畫筆
		Paint p = new Paint();
		p.setColor(Color.RED);// 設置紅色

		canvas.drawText("畫圓：", 10, 20, p);// 畫文本
		canvas.drawCircle(60, 20, 10, p);// 小圓
		p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
		canvas.drawCircle(120, 20, 20, p);// 大圓

		canvas.drawText("畫線及弧線：", 10, 60, p);
		p.setColor(Color.GREEN);// 設置綠色
		canvas.drawLine(60, 40, 100, 40, p);// 畫線
		canvas.drawLine(110, 40, 190, 80, p);// 斜線
		//畫笑臉弧線
		p.setStyle(Paint.Style.STROKE);//設置空心
		RectF oval1=new RectF(150,20,180,40);
		canvas.drawArc(oval1, 180, 180, false, p);//小弧形
		oval1.set(190, 20, 220, 40);
		canvas.drawArc(oval1, 180, 180, false, p);//小弧形
		oval1.set(160, 30, 210, 60);
		canvas.drawArc(oval1, 0, 180, false, p);//小弧形

		canvas.drawText("畫矩形：", 10, 80, p);
		p.setColor(Color.GRAY);// 設置灰色
		p.setStyle(Paint.Style.FILL);//設置填滿
		canvas.drawRect(60, 60, 80, 80, p);// 正方形
		canvas.drawRect(60, 90, 160, 100, p);// 長方形

		canvas.drawText("畫扇形和橢圓:", 10, 120, p);
		/* 設置漸變色 這個正方形的顏色是改變的 */
		Shader mShader = new LinearGradient(0, 0, 100, 100,
				new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
						Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一個材質,打造出一個線性梯度沿著一條線。
		p.setShader(mShader);
		// p.setColor(Color.BLUE);
		RectF oval2 = new RectF(60, 100, 200, 240);// 設置個新的長方形，掃瞄測量
		canvas.drawArc(oval2, 200, 130, true, p);
		// 畫弧，第一個參數是RectF：該類是第二個參數是角度的開始，第三個參數是多少度，第四個參數是真的時候畫扇形，是假的時候畫弧線
		//畫橢圓，把oval改一下
		oval2.set(210,100,250,130);
		canvas.drawOval(oval2, p);

		canvas.drawText("畫三角形：", 10, 200, p);
		// 繪製這個三角形,你可以繪製任意多邊形
		Path path = new Path();
		path.moveTo(80, 200);// 此點為多邊形的起點
		path.lineTo(120, 250);
		path.lineTo(80, 250);
		path.close(); // 使這些點構成封閉的多邊形
		canvas.drawPath(path, p);

		// 你可以繪製很多任意多邊形，比如下面畫六連形
		p.reset();//重置
		p.setColor(Color.LTGRAY);
		p.setStyle(Paint.Style.STROKE);//設置空心
		Path path1=new Path();
		path1.moveTo(180, 200);
		path1.lineTo(200, 200);
		path1.lineTo(210, 210);
		path1.lineTo(200, 220);
		path1.lineTo(180, 220);
		path1.lineTo(170, 210);
		path1.close();//封閉
		canvas.drawPath(path1, p);
		/*
		 * Path類封裝復合(多輪廓幾何圖形的路徑
		 * 由直線段*、二次曲線,和三次方曲線，也可畫以油畫。drawPath(路徑、油漆),要麼已填充的或撫摸
		 * (基於油漆的風格),或者可以用於剪斷或畫畫的文本在路徑。
		 */
		
		//畫圓角矩形
		p.setStyle(Paint.Style.FILL);//充滿
		p.setColor(Color.LTGRAY);
		p.setAntiAlias(true);// 設置畫筆的鋸齒效果
		canvas.drawText("畫圓角矩形:", 10, 260, p);
		RectF oval3 = new RectF(80, 260, 200, 300);// 設置個新的長方形
		canvas.drawRoundRect(oval3, 20, 15, p);//第二個參數是x半徑，第三個參數是y半徑
		
		//畫貝塞爾曲線
		canvas.drawText("畫貝塞爾曲線:", 10, 310, p);
		p.reset();
		p.setStyle(Paint.Style.STROKE);
		p.setColor(Color.GREEN);
		Path path2=new Path();
		path2.moveTo(100, 320);//設置Path的起點
		path2.quadTo(150, 310, 170, 400); //設置貝塞爾曲線的控制點坐標和終點坐標
		canvas.drawPath(path2, p);//畫出貝塞爾曲線
		
		//畫點
		p.setStyle(Paint.Style.FILL);
		canvas.drawText("畫點：", 10, 390, p);
		canvas.drawPoint(60, 390, p);//畫一個點
		canvas.drawPoints(new float[]{60,400,65,400,70,400}, p);//畫多個點
		
		//畫圖片，就是貼圖
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		canvas.drawBitmap(bitmap, 250,360, p);
	}

	private int touchX1, touchY1, touchX2, touchY2;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		touchX1 = (int) event.getX();
		touchY1 = (int) event.getY();

		int pointerCount = event.getPointerCount(); // 幾個觸控點

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN: // 按下
				if (Math.abs(touchX1 - hour_x) <= 25 && Math.abs(touchY1 - hour_y) <= 25)
					isTouch_Hour = true;
				else if (Math.abs(touchX1 - minu_x) <= 25 && Math.abs(touchY1 - minu_y) <= 25)
					isTouch_Minu = true;
				else if (Math.abs(touchX1 - sec_x) <= 25 && Math.abs(touchY1 - sec_y) <= 25)
					isTouch_Sec = true;
				
				if (Math.abs(touchX1 - center_x) <= 25 && Math.abs(touchY1 - center_y) <= 25)
				{
					isStopwatch = false;
					isReciprocal = true;
					
					timeMillis = (int) ((degree_hour / 15) % 24) * 3600 + (int)( (degree_minu / 6) % 60) *60 +(int) (degree_sec / 6) % 60;
					timeMillis *= 100;
				}

				//Log.e("isTouch_Minu", "" + Math.abs(touchX1 - minu_x) + ", " + Math.abs(touchY1 - minu_y));
				break;

			case MotionEvent.ACTION_MOVE: // 拖曳移動
				if (pointerCount == 1) // 單點觸控
				{
					if (isTouch_Hour)
					{
						//android 座標 正向下
						touchDegree_hour = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_hour < 0) // -180 ~ 180
							touchDegree_hour = 360 + touchDegree_hour;

						degree_hour = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_hour < 0) // -180 ~ 180
							degree_hour = 360 + degree_hour;
					}
					else if (isTouch_Minu)
					{
						//android 座標 正向下
						touchDegree_minu = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_minu < 0) // -180 ~ 180
							touchDegree_minu = 360 + touchDegree_minu;

						degree_minu = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_minu < 0) // -180 ~ 180
							degree_minu = 360 + degree_minu;
					}
					else if (isTouch_Sec)
					{
						//android 座標 正向下
						touchDegree_sec = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_sec < 0) // -180 ~ 180
							touchDegree_sec = 360 + touchDegree_sec;

						degree_sec = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_sec < 0) // -180 ~ 180
							degree_sec = 360 + degree_sec;
					}					
				}
				else if (pointerCount == 2) // 多點觸控
				{
					isStopwatch = true;
					isReciprocal = false;
					timeMillis = 0;
				}				
				else if (pointerCount == 3)
				{
					isStopwatch = false;
					isReciprocal = false;
					timeMillis = 0;
					degree_hour = 0;
					degree_minu = 0;
					degree_sec = 0;
				}

				// 重繪, 再一次執行 onDraw 程序
				invalidate();
				break;

			case MotionEvent.ACTION_UP: // 放開
				isTouch_Hour = false;
				isTouch_Minu = false;
				isTouch_Sec = false;
				break;

		}

		// TODO Auto-generated method stub
		return true;
	}

}
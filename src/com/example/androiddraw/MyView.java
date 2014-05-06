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

	private final Runnable timerRun = new Runnable() //�B�ʭp��
	{
		public void run()
		{
			if (isStopwatch) //�X��
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

		//����		
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
		canvas.drawArc(oval, 0, 360, false, paint); //���ɰw,3�I��V��0

		//��
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

			//android �y�� ���V�U
			sec_x = center_x + (float) ((radius_sec + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_sec)));
			sec_y = center_y - (float) ((radius_sec + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_sec)));

			canvas.drawCircle(sec_x, sec_y, btnRadius, paint);
		}

		//��
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

			//android �y�� ���V�U
			minu_x = center_x + (float) ((radius_minu + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_minu)));
			minu_y = center_y - (float) ((radius_minu + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_minu)));

			canvas.drawCircle(minu_x, minu_y, btnRadius, paint);
		}

		//��
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(timeStrokeWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		oval.set(center_x - radius_hour, center_y - radius_hour, center_x + radius_hour, center_y + radius_hour);
		canvas.drawArc(oval, -90, degree_hour, false, paint); //���ɰw,3�I��V��0

		if (!isStopwatch && !isReciprocal)
		{
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(btnStrokeWidth);

			if (isTouch_Hour)
				paint.setStyle(Paint.Style.FILL);

			//android �y�� ���V�U
			hour_x = center_x + (float) ((radius_hour + timeStrokeWidth) * Math.cos(Math.toRadians(touchDegree_hour)));
			hour_y = center_y - (float) ((radius_hour + timeStrokeWidth) * Math.sin(Math.toRadians(touchDegree_hour)));

			canvas.drawCircle(hour_x, hour_y, btnRadius, paint);
		}
		
		canvas.drawCircle(center_x, center_y, btnRadius, paint);

		//�r
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
		 * ��k ���� drawRect ø�s�x�� drawCircle ø�s��� drawOval ø�s��� drawPath ø�s���N�h���
		 * drawLine ø�s���u drawPoin ø�s�I
		 */
		// �Ыصe��
		Paint p = new Paint();
		p.setColor(Color.RED);// �]�m����

		canvas.drawText("�e��G", 10, 20, p);// �e�奻
		canvas.drawCircle(60, 20, 10, p);// �p��
		p.setAntiAlias(true);// �]�m�e���������ĪG�C true�O�h���A�j�a�@�ݮĪG�N���դF
		canvas.drawCircle(120, 20, 20, p);// �j��

		canvas.drawText("�e�u�Ω��u�G", 10, 60, p);
		p.setColor(Color.GREEN);// �]�m���
		canvas.drawLine(60, 40, 100, 40, p);// �e�u
		canvas.drawLine(110, 40, 190, 80, p);// �׽u
		//�e���y���u
		p.setStyle(Paint.Style.STROKE);//�]�m�Ť�
		RectF oval1=new RectF(150,20,180,40);
		canvas.drawArc(oval1, 180, 180, false, p);//�p����
		oval1.set(190, 20, 220, 40);
		canvas.drawArc(oval1, 180, 180, false, p);//�p����
		oval1.set(160, 30, 210, 60);
		canvas.drawArc(oval1, 0, 180, false, p);//�p����

		canvas.drawText("�e�x�ΡG", 10, 80, p);
		p.setColor(Color.GRAY);// �]�m�Ǧ�
		p.setStyle(Paint.Style.FILL);//�]�m��
		canvas.drawRect(60, 60, 80, 80, p);// �����
		canvas.drawRect(60, 90, 160, 100, p);// �����

		canvas.drawText("�e���ΩM���:", 10, 120, p);
		/* �]�m���ܦ� �o�ӥ���Ϊ��C��O���ܪ� */
		Shader mShader = new LinearGradient(0, 0, 100, 100,
				new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
						Color.LTGRAY }, null, Shader.TileMode.REPEAT); // �@�ӧ���,���y�X�@�ӽu�ʱ�תu�ۤ@���u�C
		p.setShader(mShader);
		// p.setColor(Color.BLUE);
		RectF oval2 = new RectF(60, 100, 200, 240);// �]�m�ӷs������ΡA���˴��q
		canvas.drawArc(oval2, 200, 130, true, p);
		// �e���A�Ĥ@�ӰѼƬORectF�G�����O�ĤG�ӰѼƬO���ת��}�l�A�ĤT�ӰѼƬO�h�֫סA�ĥ|�ӰѼƬO�u���ɭԵe���ΡA�O�����ɭԵe���u
		//�e���A��oval��@�U
		oval2.set(210,100,250,130);
		canvas.drawOval(oval2, p);

		canvas.drawText("�e�T���ΡG", 10, 200, p);
		// ø�s�o�ӤT����,�A�i�Hø�s���N�h���
		Path path = new Path();
		path.moveTo(80, 200);// ���I���h��Ϊ��_�I
		path.lineTo(120, 250);
		path.lineTo(80, 250);
		path.close(); // �ϳo���I�c���ʳ����h���
		canvas.drawPath(path, p);

		// �A�i�Hø�s�ܦh���N�h��ΡA��p�U���e���s��
		p.reset();//���m
		p.setColor(Color.LTGRAY);
		p.setStyle(Paint.Style.STROKE);//�]�m�Ť�
		Path path1=new Path();
		path1.moveTo(180, 200);
		path1.lineTo(200, 200);
		path1.lineTo(210, 210);
		path1.lineTo(200, 220);
		path1.lineTo(180, 220);
		path1.lineTo(170, 210);
		path1.close();//�ʳ�
		canvas.drawPath(path1, p);
		/*
		 * Path���ʸ˴_�X(�h�����X��ϧΪ����|
		 * �Ѫ��u�q*�B�G�����u,�M�T���覱�u�A�]�i�e�H�o�e�CdrawPath(���|�B�o��),�n��w��R���μ��N
		 * (���o��������),�Ϊ̥i�H�Ω���_�εe�e���奻�b���|�C
		 */
		
		//�e�ꨤ�x��
		p.setStyle(Paint.Style.FILL);//�R��
		p.setColor(Color.LTGRAY);
		p.setAntiAlias(true);// �]�m�e���������ĪG
		canvas.drawText("�e�ꨤ�x��:", 10, 260, p);
		RectF oval3 = new RectF(80, 260, 200, 300);// �]�m�ӷs�������
		canvas.drawRoundRect(oval3, 20, 15, p);//�ĤG�ӰѼƬOx�b�|�A�ĤT�ӰѼƬOy�b�|
		
		//�e���뺸���u
		canvas.drawText("�e���뺸���u:", 10, 310, p);
		p.reset();
		p.setStyle(Paint.Style.STROKE);
		p.setColor(Color.GREEN);
		Path path2=new Path();
		path2.moveTo(100, 320);//�]�mPath���_�I
		path2.quadTo(150, 310, 170, 400); //�]�m���뺸���u�������I���ЩM���I����
		canvas.drawPath(path2, p);//�e�X���뺸���u
		
		//�e�I
		p.setStyle(Paint.Style.FILL);
		canvas.drawText("�e�I�G", 10, 390, p);
		canvas.drawPoint(60, 390, p);//�e�@���I
		canvas.drawPoints(new float[]{60,400,65,400,70,400}, p);//�e�h���I
		
		//�e�Ϥ��A�N�O�K��
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		canvas.drawBitmap(bitmap, 250,360, p);
	}

	private int touchX1, touchY1, touchX2, touchY2;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		touchX1 = (int) event.getX();
		touchY1 = (int) event.getY();

		int pointerCount = event.getPointerCount(); // �X��Ĳ���I

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN: // ���U
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

			case MotionEvent.ACTION_MOVE: // �즲����
				if (pointerCount == 1) // ���IĲ��
				{
					if (isTouch_Hour)
					{
						//android �y�� ���V�U
						touchDegree_hour = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_hour < 0) // -180 ~ 180
							touchDegree_hour = 360 + touchDegree_hour;

						degree_hour = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_hour < 0) // -180 ~ 180
							degree_hour = 360 + degree_hour;
					}
					else if (isTouch_Minu)
					{
						//android �y�� ���V�U
						touchDegree_minu = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_minu < 0) // -180 ~ 180
							touchDegree_minu = 360 + touchDegree_minu;

						degree_minu = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_minu < 0) // -180 ~ 180
							degree_minu = 360 + degree_minu;
					}
					else if (isTouch_Sec)
					{
						//android �y�� ���V�U
						touchDegree_sec = (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (touchDegree_sec < 0) // -180 ~ 180
							touchDegree_sec = 360 + touchDegree_sec;

						degree_sec = 90 - (float) Math.toDegrees(Math.atan2(-(touchY1 - center_y), touchX1 - center_x));

						if (degree_sec < 0) // -180 ~ 180
							degree_sec = 360 + degree_sec;
					}					
				}
				else if (pointerCount == 2) // �h�IĲ��
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

				// ��ø, �A�@������ onDraw �{��
				invalidate();
				break;

			case MotionEvent.ACTION_UP: // ��}
				isTouch_Hour = false;
				isTouch_Minu = false;
				isTouch_Sec = false;
				break;

		}

		// TODO Auto-generated method stub
		return true;
	}

}
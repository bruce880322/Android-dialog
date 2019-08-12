package com.example.Eric_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Region;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class PaintView extends View implements Serializable {

    public static final int PAINTER_COLOR = Color.BLACK;
    public static final int PAINTER_COLOR2 = Color.WHITE;
    public static final int BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 100;
    private int backgroundcolor;
    private int height;
    private int width;
    private Paint paint;
    private Paint paint2;
    private Paint paint_BG1;
    private Paint paint_BG2;
    private Paint textpaint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint BitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<PointF> points = new ArrayList<>();
    private float DotX;
    private float DotY;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float fwidth;
    private float fheight;
    private int horgrid = 100,vergrid = 100;
    float downX = 1;
    float downY = -1;
    Region Dot;
    Region Element;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //設定畫點的筆
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(PAINTER_COLOR);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        //設定畫線的筆
        paint2 = new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(10);
        paint2.setColor(PAINTER_COLOR2);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setAntiAlias(true);

        //設定背景粗網格畫筆
        paint_BG1 = new Paint();
        paint_BG1.setStyle(Paint.Style.FILL);
        paint_BG1.setStrokeWidth(10);
        paint_BG1.setColor(Color.GRAY);
        paint_BG1.setAlpha(200);
        paint_BG1.setAntiAlias(true);

        //設定背景細網格畫筆
        paint_BG2 = new Paint();
        paint_BG2.setStyle(Paint.Style.FILL);
        paint_BG2.setStrokeWidth(2);
        paint_BG2.setColor(Color.GRAY);
        paint_BG2.setAlpha(128);
        paint_BG2.setAntiAlias(true);

        //設定文字畫筆
        textpaint = new Paint();
        textpaint.setColor(Color.BLUE);
        textpaint.setStyle(Paint.Style.FILL);
        textpaint.setTextSize(60);

    }

    //獲取螢幕的長寬
    public void init(DisplayMetrics metrics) {
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        fheight = height;
        fwidth = width;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    //確認螢幕的長寬
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    //變數轉換：float to String
    public String floatToString(float s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s);
        String string = stringBuilder.toString();
        return string;
    }

    //變數轉化:String to Int
    public int stringToInt(String string){
        int j = 0;
        String str = string.substring(0, string.indexOf(".")) + string.substring(string.indexOf(".") + 1);
        int intgeo = Integer.parseInt(str);
        return intgeo;
    }

    public void drawDot() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        PaintDot(downX,downY);
                        invalidate();
                        break;
                }
                return true;
            }
        });
    }

    public void setElement() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int x = (int)motionEvent.getX();
                        int y = (int)motionEvent.getY();

                        if (Dot.contains(x, y)) {
                            //SelectDot();
                        }
                }
                return true;
            }
        });
    }


    @Override
    public void onDraw(Canvas canvas) {
        int vernum = (height/vergrid)+1;
        int hornum = (width/horgrid)+1;
        canvas.drawColor(backgroundcolor);

        canvas.translate(fwidth / 2, fheight / 2);
        canvas.scale(1, -1);

        drawTranslateCoordinateSpace(canvas);

        Matrix invertMatrix = new Matrix();
        canvas.getMatrix().invert(invertMatrix);


        for (int i = 0; i < vernum; i++) {
            canvas.drawLine(i*vergrid,fheight/2,i*vergrid,(-fheight/2),paint_BG2);
            canvas.drawLine((-i-1)*vergrid,fheight/2,(-i-1)*vergrid,(-fheight/2),paint_BG2);
        }
        for (int i = 0; i < hornum; i++) {
            canvas.drawLine(fwidth/2,i*horgrid,(-fwidth/2),i*horgrid,paint_BG2);
            canvas.drawLine(fwidth/2,(-i-1)*horgrid,(-fwidth/2),(-i-1)*horgrid,paint_BG2);
        }
        canvas.drawBitmap(bitmap, invertMatrix, BitmapPaint);
    }


    private void drawTranslateCoordinateSpace(Canvas canvas) {
        canvas.save();
        CanvasAidUtils.set2DAxisLength(fwidth / 2, fwidth / 2, fheight / 2, fheight / 2);
        CanvasAidUtils.setLineColor(Color.GRAY);
        CanvasAidUtils.draw2DCoordinateSpace(canvas);
        CanvasAidUtils.draw2DCoordinateSpace(canvas);
        canvas.restore();
    }

    /**
     *
     * 繪製端點
     *
    */
    private void PaintDot(float downX, float downY) {
        float[] pts = {downX, downY};
        float[] transpts = new float[2];

        //取得畫面座標反矩陣，讓使用者的觸摸處和畫布座標吻合
        Matrix invertMatrix = new Matrix();
        canvas.getMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(transpts,pts);

        //儲存碰觸的座標點
        DotX = transpts[0] - fwidth / 2;
        DotY = -transpts[1] + fheight / 2;
        int iDotX = (int)transpts[0];//stringToInt(floatToString(transpts[0]))-width/2;
        int iDotY = (int)transpts[1]; //-stringToInt(floatToString(transpts[1]))+height/2;

        //建立起始點丟入Arraylist<Point> points
        PointF pointF = new PointF(DotX, DotY);
        points.add(pointF);

        //在觸摸點繪製圓點，並定義為region讓後續的行動能夠點擊
        canvas.drawCircle(transpts[0], transpts[1], 15, paint);
        //Dot.set(iDotX-5,iDotY+5,iDotX+5,iDotY-5);

        Log.d("TAG", points.toString());
    }

    /**
     *
     * 選取端點以建立桿件
     * @param x
     * @param y
     *
     */
    private void SelectDot(float x, float y) {
        float[] pts = {x, y};
        Matrix invertMatrix = new Matrix();
        canvas.getMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(pts);
    }

    


    public Bundle points() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("points", points);
        return bundle;
    }

    public void removeArray() {
        Iterator<PointF> iterator = points.iterator();
        while (iterator.hasNext()) {
            PointF next = iterator.next();
            iterator.remove();
        }
    }

    public void clear() {
        backgroundcolor = BG_COLOR;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.translate(0, 0);
        removeArray();
    }
}

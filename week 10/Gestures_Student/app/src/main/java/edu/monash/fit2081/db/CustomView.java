/*
You can draw on a Canvas, this holds draw calls which are written to a bitmap which is used as a View's background
A View's (or View's sub class) Canvas is passed to you in its onDraw(Canvas canvas) method
We should never call a View's onDraw rather we invalidate the View (e.g. myView.invalidate())
This instructs Android to execute onDraw at its earliest convenience (if it's necessary)
 */
package edu.monash.fit2081.db;

import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//so we don't have to fully qualify references to members of this class we coded
import edu.monash.fit2081.db.provider.ShapeValues;

public class CustomView extends View {

    public Paint paint;
    public Canvas canvas;
    public ShapeValues[] shapes = null;
    public int numberShapes = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    public CustomView(Context context) {
        super(context);

        //set some painting and colouring defaults
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
    }

    //drawing instructions to draw all shapes currently in the db
    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;

        canvas.drawColor(Color.WHITE);

        for (int i = 0; i < numberShapes; i++) {
            paint.setColor(Color.parseColor("#" + Integer.toHexString(Integer.parseInt(shapes[i].getColor())).substring(2))); //avoids alpha
            paint.setStrokeWidth(shapes[i].getBorder());

            if (shapes[i].getShapeType().equals("Circle"))
                canvas.drawCircle(shapes[i].getX(), shapes[i].getY(), shapes[i].getRadius(), paint);
            else if (shapes[i].getShapeType().equals("Rectangle"))
                canvas.drawRect(shapes[i].getX(), shapes[i].getY(), shapes[i].getX() + shapes[i].getWidth(), shapes[i].getY() + shapes[i].getHeight(), paint);
        }
    }
}
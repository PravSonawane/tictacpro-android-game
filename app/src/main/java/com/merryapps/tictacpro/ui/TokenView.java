package com.merryapps.tictacpro.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Token;

/**
 * Created by mephisto on 8/31/16.
 */
public class TokenView extends View {

    private static final String TAG = "TokenView";

    private Paint xPaint;
    private Paint oPaint;
    private Paint borderPaint;
    private Paint emptyTilePaint;

    private int rowIndex;
    private int columnIndex;
    private static Drawable tileBackground;

    private Token token;

    //values will be set by onMeasure
    private int calculatedWidth;
    private int calculatedHeight;

    //ranges from 10 to 25. Optimum value is 20
    private static final float BACKGROUND_PADDING_PERCENTAGE_OF_WIDTH = 3;

    //ranges from 10 to 25. Optimum value is 20
    private static final float TOKEN_PADDING_PERCENTAGE_OF_WIDTH = 20;


    public TokenView(Context context) {
        super(context);
        init();
    }

    public TokenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TokenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TokenView(Context context,int rowIndex, int columnIndex) {
        super(context);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        init();
    }

    private void init() {

        token = Token.E;

        emptyTilePaint = new Paint();
        emptyTilePaint.setColor(getResources().getColor(R.color.emptyBackgroundColor));

        borderPaint = new Paint();

        xPaint = new Paint();
        xPaint.setStrokeWidth(20);
        xPaint.setStrokeCap(Paint.Cap.ROUND);

        oPaint = new Paint();
        oPaint.setStyle(Paint.Style.STROKE);
        oPaint.setStrokeWidth(20);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            xPaint.setColor(getResources().getColor(R.color.xColor,null));
            oPaint.setColor(getResources().getColor(R.color.oColor,null));
        } else {
            xPaint.setColor(getResources().getColor(R.color.xColor));
            oPaint.setColor(getResources().getColor(R.color.oColor));
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tileBackground = getResources().getDrawable(R.drawable.tile_background, null);
        } else {
            tileBackground = getResources().getDrawable(R.drawable.tile_background);
        }

        Log.d(TAG, "init: this.getWidth():" + this.getWidth());
        Log.d(TAG, "init: this.getHeight():" + this.getHeight());

        this.setMinimumWidth(50);
        this.setMinimumHeight(50);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged() called with: " + "w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "onMeasure: widthMode:" + specMode(widthMode));
        Log.d(TAG, "onMeasure: heightMode:" + specMode(heightMode));
        Log.d(TAG, "onMeasure: totalWidth:" + totalWidth);
        Log.d(TAG, "onMeasure: totalHeight:" + totalHeight);
        Log.d(TAG, "onMeasure: this.suggestedMinimumWidth():" + this.getSuggestedMinimumWidth());
        Log.d(TAG, "onMeasure: this.suggestedMinimumHeight():" + this.getSuggestedMinimumHeight());

        switch (widthMode) {
            case MeasureSpec.AT_MOST: {
                calculatedWidth = totalWidth/3;
                calculatedHeight = totalWidth/3;
                break;
            }
            case MeasureSpec.EXACTLY: {
                calculatedWidth = totalWidth/3;
                calculatedHeight = totalWidth;
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                calculatedWidth = getSuggestedMinimumWidth();
                calculatedHeight = getSuggestedMinimumHeight();
                break;
            }
        }

        this.setMeasuredDimension(calculatedWidth,calculatedHeight);

        Log.d(TAG, "onMeasure: calculatedWidth:" + calculatedWidth);
        Log.d(TAG, "onMeasure: calculatedHeight:" + calculatedHeight);
    }

    private String specMode(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);

        switch (mode) {
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
        }
        throw new AssertionError("Unknown spec mode:" + MeasureSpec.getMode(measureSpec));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);

        int tilePadding = (int)(canvas.getWidth() * (BACKGROUND_PADDING_PERCENTAGE_OF_WIDTH/100f));

        tileBackground.setBounds(0 + tilePadding, 0 + tilePadding, canvas.getWidth() - tilePadding, canvas.getHeight() - tilePadding);
        tileBackground.draw(canvas);

        //padding for tokens to be draw in this view
        int padding = (int)((canvas.getWidth() - tilePadding) * (TOKEN_PADDING_PERCENTAGE_OF_WIDTH / 100f));


        if (token.equals(Token.X)) {
            //drawing the X token
            canvas.drawLine(padding, padding,canvas.getWidth()/2, canvas.getHeight()/2, xPaint);
            canvas.drawLine(canvas.getWidth()- padding,canvas.getHeight()- padding,canvas.getWidth()/2,canvas.getHeight()/2, xPaint);
            canvas.drawLine(canvas.getWidth()- padding, padding,canvas.getWidth()/2, canvas.getHeight()/2, xPaint);
            canvas.drawLine(padding,canvas.getHeight()- padding,canvas.getWidth()/2,canvas.getHeight()/2, xPaint);
        } else if (token.equals(Token.O)) {
            //drawing the O token
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2,canvas.getWidth()/2 - padding, oPaint);
        }

        //set border width to 1% of view width
        borderPaint.setStrokeWidth(getWidth()/100);
        //draw borders
        /*canvas.drawLines(new float[]{0,0,canvas.getWidth(),0,
                canvas.getWidth(),0,canvas.getWidth(),canvas.getHeight(),
                canvas.getWidth(),canvas.getHeight(),0,canvas.getHeight(),
                0,canvas.getHeight(),0,0},borderPaint);*/
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setToken(Token token) {
        if(!this.token.equals(token)) {
            this.token = token;
            invalidate();
        }
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TokenView{" +
                "rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                ", token=" + token +
                '}';
    }
}


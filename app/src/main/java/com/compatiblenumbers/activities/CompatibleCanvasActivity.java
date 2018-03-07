package com.compatiblenumbers.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.compatiblenumbers.R;
import com.compatiblenumbers.helper.ui.CirclesDrawingView;
import com.compatiblenumbers.model.TextBlock;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.compatiblenumbers.R.color.purple;

/**
 * Created by avinash on 5/10/17.
 */

public class CompatibleCanvasActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = CompatibleCanvasActivity.class.getSimpleName();

    private Button firstBtn;

    private Button secondBtn;

    private Button thirdBtn;

    private Button fourthBtn;

    public static EditText firstInput;

    public static EditText secondInput;

    private float MaxSize = 100;

    public static int userWidth = 10;

    public static int userHeight = 10;

    private Button fifthBtn;

    private CirclesDrawingView canvasView;

    private int height;

    private int width;

    private int canvasWidth;

    private float magicNumber, userWidthMagic, userHeightMagic;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compatible_canvas);

        firstBtn = (Button) findViewById(R.id.first_value);
        secondBtn = (Button) findViewById(R.id.second_value);
        thirdBtn = (Button) findViewById(R.id.third_value);
        fourthBtn = (Button) findViewById(R.id.fourth_value);
        fifthBtn = (Button) findViewById(R.id.fifth_value);

        firstInput = (EditText) findViewById(R.id.number_one);
        secondInput = (EditText) findViewById(R.id.number_two);

        Button findCompatible = (Button) findViewById(R.id.show_me);
        Button refresh = (Button) findViewById(R.id.refresh);
        Button letMeTry = (Button) findViewById(R.id.me_try);

        findCompatible.setOnClickListener(this);
        refresh.setOnClickListener(this);
        letMeTry.setOnClickListener(this);

        canvasView = (CirclesDrawingView) findViewById(R.id.canvas);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = Math.round(metrics.heightPixels);
        width = Math.round(metrics.widthPixels);

//        float dp20Pixel = convertDpToPixel((float) 20.0, this);
//        canvasWidth = Math.round(width - 2*dp20Pixel);
        canvasWidth = width;
        Log.d("PAVAN", "CanvasWidth global"+canvasWidth);

        magicNumber = ((float)canvasWidth)/100;
        Log.d("PAVAN", "Magic number global"+magicNumber);

        int margin = width - canvasWidth;
        int marginDp = Math.round(convertPixelsToDp((float)margin, this));
        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(canvasWidth, canvasWidth);
        canvasView.setLayoutParams(canvasParams);
        canvasView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(firstInput.getText().toString().equals("") || secondInput.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter numbers and click let me try for using canvas", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_try:
                if(firstInput.getText().toString().equals("") || secondInput.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Enter numbers first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    canvasView.setOnTouchListener(this);
                    int first = Integer.parseInt(firstInput.getText().toString());
                    int second = Integer.parseInt(secondInput.getText().toString());
                    int product = first*second;
                    userWidth = first;
                    userHeight = second;
                    //MaxSize = (float) Math.sqrt(product);
                    magicNumber = (((float)canvasWidth)/MaxSize);

                    userWidthMagic = (((float)canvasWidth)/userWidth);
                    userHeightMagic = (((float)canvasWidth)/userHeight);

                    Log.d("PAVAN", "MaxSize "+MaxSize);
                    Log.d("PAVAN", "magicnum "+magicNumber);
                    Log.d("PAVAN", "userWidthMagic "+userWidthMagic);
                    Log.d("PAVAN", "userHeightMagic "+userHeightMagic);

                    canvasView.invalidate();
                    Toast.makeText(getApplicationContext(), "Try finding the numbers on canvas", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.show_me:
                if(!firstInput.getText().toString().equals("") && !secondInput.getText().toString().equals(""))
                {
                    //Canvas adjusted
                    canvasView.setOnTouchListener(this);
                    int firstNum = Integer.parseInt(firstInput.getText().toString());
                    int secondNum = Integer.parseInt(secondInput.getText().toString());

                    userWidth = firstNum;
                    userHeight = secondNum;

                    userWidthMagic = (((float)canvasWidth)/userWidth);
                    userHeightMagic = (((float)canvasWidth)/userHeight);

                    //Numbers found
                    int firstRemaining = firstNum%10;
                    int firstCompatible = firstNum - firstRemaining;
                    int secondRemaining = secondNum%10;
                    int secondCompatible = secondNum - secondRemaining;

                    drawRectsOnCanvas(firstCompatible, secondCompatible);

                    int cp = firstCompatible*secondCompatible;
                    int frSC = firstRemaining*secondCompatible;
                    int srFC = secondRemaining*firstCompatible;
                    int frSR = firstRemaining*secondRemaining;
                    findCompatible(firstCompatible, firstRemaining, secondCompatible, secondRemaining, cp, frSC, srFC, frSR);
                    ScrollView sv = (ScrollView)findViewById(R.id.scroller);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                    sv.scrollTo(0, sv.getBottom());
                    Toast.makeText(getApplicationContext(), "These are the compatible numbers", Toast.LENGTH_SHORT).show();
                    break;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter numbers first", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.refresh:
                refresh();
                break;

            default:
        }
    }

    private void findCompatible(int firstCompatible, int firstRemaining, int secondCompatible, int secondRemaining, int cp, int frSC, int srFC, int frSR) {


        firstBtn.setText(firstCompatible+ " X "+ secondCompatible + " = " + cp);

        secondBtn.setText(firstRemaining+ " X " + secondCompatible + " = " + frSC);

        thirdBtn.setText(secondRemaining+ " X " + firstCompatible + " = " + srFC);

        fourthBtn.setText(firstRemaining+ " X " + secondRemaining + " = " + frSR);

        fifthBtn.setText(cp +" + "+ frSC +" + "+ srFC +" + "+ frSR +" = "+ (cp+frSC+srFC+frSR));

        int grand = (firstCompatible-1) + secondCompatible*10;
        int selectedIndex = grand/10;


    }

    private void refresh() {
        firstInput.setText("");
        secondInput.setText("");
        firstBtn.setText("");
        secondBtn.setText("");
        thirdBtn.setText("");
        fourthBtn.setText("");
        fifthBtn.setText("");
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                callDrawRect(event);
                break;
            default:
        }
        return false;
    }

    private void callDrawRect(MotionEvent event) {


        Log.d("PAVAN", event.getX()+": X");
        Log.d("PAVAN", event.getY()+": Y");

        //int roundedOffX = Math.round((event.getX()/magicNumber));
        //int roundedOffY = Math.round((event.getY()/magicNumber));

        int roundedOffX = Math.round((event.getX()/userWidthMagic));
        int roundedOffY = Math.round((event.getY()/userHeightMagic));

        Log.d("PAVAN", magicNumber+": Magic number for calculation");
        Log.d("PAVAN", roundedOffX+": Rounded X");
        Log.d("PAVAN", roundedOffY+": Rounded Y");

        drawRectsOnCanvas(roundedOffX, roundedOffY);
    }

    public void drawRectsOnCanvas(int roundedOffX, int roundedOffY) {

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        List<Rect> rectList = new ArrayList<>(CirclesDrawingView.RECT_LIMIT);
        List<TextBlock> textBlocks = new ArrayList<>(CirclesDrawingView.RECT_LIMIT);

        //int pointX = Math.round(roundedOffX * magicNumber);
        //int pointY = Math.round(roundedOffY * magicNumber);

        int pointX = Math.round(roundedOffX * userWidthMagic);
        int pointY = Math.round(roundedOffY * userHeightMagic);

        Rect rect = new Rect();
        rect.set(0,0,pointX, pointY);
        rectList.add(rect);

        rect = new Rect();
        rect.set(pointX, 0, canvasWidth, pointY);
        rectList.add(rect);

        rect = new Rect();
        rect.set(0, pointY, pointX, canvasWidth);
        rectList.add(rect);

        rect = new Rect();
        rect.set(pointX, pointY, canvasWidth, canvasWidth);
        rectList.add(rect);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(255, 255, 255));
        myPaint.setStrokeWidth(10);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(40);
        myPaint.setTextAlign(Paint.Align.CENTER);

        //----------------------------------------------------------------------------------

        int xblock1 = pointX/2;
        int yblock1 = pointY/2;
        int xvalblock1 = roundedOffX;
        int yvalblock1 = roundedOffY;
        int resultblock1 = xvalblock1*yvalblock1;

        TextBlock textBlock1 = new TextBlock();
        textBlock1.setPointX(xblock1);
        textBlock1.setPointY(yblock1);
        textBlock1.setPaint(myPaint);
        textBlock1.setText(df.format(xvalblock1)+ " X "+ df.format(yvalblock1) + " = " + df.format(resultblock1));
        textBlocks.add(textBlock1);

        //----------------------------------------------------------------------------------

        int xblock2 = pointX + (canvasWidth-pointX)/2;
        int yblock2 = pointY/2;
        //float xvalblock2 = (MaxSize-roundedOffX);
        float xvalblock2 = (userWidth-roundedOffX);
        int yvalblock2 = roundedOffY;
        //float resultblock2 = (MaxSize-roundedOffX)*roundedOffY;
        float resultblock2 = (userWidth-roundedOffX)*roundedOffY;

        TextBlock textBlock2 = new TextBlock();
        textBlock2.setPointX(xblock2);
        textBlock2.setPointY(yblock2);
        textBlock2.setPaint(myPaint);
        textBlock2.setText(df.format(xvalblock2)+ " X "+ df.format(yvalblock2) + " = " + df.format(resultblock2));
        textBlocks.add(textBlock2);

        //----------------------------------------------------------------------------------

        int xblock3 = pointX/2;
        int yblock3 = pointY + (canvasWidth-pointY)/2;
        int xvalblock3 = roundedOffX;
        //float yvalblock3 = MaxSize-roundedOffY;
        float yvalblock3 = userHeight-roundedOffY;
        float resultblock3 = xvalblock3 * yvalblock3;

        TextBlock textBlock3 = new TextBlock();
        textBlock3.setPointX(xblock3);
        textBlock3.setPointY(yblock3);
        textBlock3.setPaint(myPaint);
        textBlock3.setText(df.format(xvalblock3)+ " X "+ df.format(yvalblock3) + " = " + df.format(resultblock3));
        textBlocks.add(textBlock3);

        //----------------------------------------------------------------------------------

        int xblock4 = pointX + (canvasWidth-pointX)/2;
        int yblock4 = pointY + (canvasWidth-pointY)/2;
        //float xvalblock4 = MaxSize-roundedOffX;
        //float yvalblock4 = MaxSize-roundedOffY;
        float xvalblock4 = userWidth-roundedOffX;
        float yvalblock4 = userHeight-roundedOffY;
        float resultblock4 = xvalblock4 * yvalblock4;

        TextBlock textBlock4 = new TextBlock();
        textBlock4.setPointX(xblock4);
        textBlock4.setPointY(yblock4);
        textBlock4.setPaint(myPaint);
        textBlock4.setText(df.format(xvalblock4)+ " X "+ df.format(yvalblock4) + " = " + df.format(resultblock4));
        textBlocks.add(textBlock4);

        //----------------------------------------------------------------------------------

        canvasView.setRectangles(rectList, textBlocks);

        firstBtn.setText(textBlock1.getText());
        secondBtn.setText(textBlock2.getText());
        thirdBtn.setText(textBlock3.getText());
        fourthBtn.setText(textBlock4.getText());

        fifthBtn.setText(df.format(resultblock1)+"+"+df.format(resultblock2)+"+"+df.format(resultblock3)+"+"+df.format(resultblock4)+" ="+" "+df.format(resultblock1+resultblock2+resultblock3+resultblock4));


    }
}

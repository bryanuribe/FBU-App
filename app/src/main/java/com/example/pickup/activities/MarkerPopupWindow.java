package com.example.pickup.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.example.pickup.R;
import com.example.pickup.enums.Direction;

public class MarkerPopupWindow extends Activity {

    private static final String TAG = "MarkerPopupWindow";

    private MarkerDialogueListener listener;
    private GestureDetectorCompat gestureDetector;
    private Button btnCancel;

    public interface MarkerDialogueListener {
        void applyUserAction(MarkerPopupWindow markerDialogue);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_marker_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width *.6), (int) (height *.4));

        float mCurrentX = getWindow().getAttributes().x;
        float mCurrentY = getWindow().getAttributes().y;;

        Log.i(TAG, "onCreate: " + mCurrentX);
        Log.i(TAG, "onCreate: " + mCurrentY);

        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void finishRight() {
        super.finish();
        overridePendingTransition(0, R.anim.to_right);
    }
    public void finishLeft() {
        super.finish();
        overridePendingTransition(0, R.anim.to_left);
    }
    public void finishDown() {
        super.finish();
        overridePendingTransition(0, R.anim.to_down);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final String TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d(TAG, "onFling: " + motionEvent.toString() + motionEvent1.toString());
            float x1 = motionEvent.getX();
            float y1 = motionEvent.getY();

            float x2 = motionEvent1.getX();
            float y2 = motionEvent1.getY();

            Direction direction = getDirection(x1, y1, x2, y2);

            if (direction == Direction.RIGHT) {
                Log.i(TAG, "onFling: right");
                finishRight();
            } else if (direction == Direction.DOWN) {
                Log.i(TAG, "onFling: down");
                finishDown();
            } else if (direction == Direction.LEFT) {
                Log.i(TAG, "onFling: left");
                finishLeft();
            }

            return true;
        }

        public Direction getDirection(float x1, float y1, float x2, float y2){
            double angle = getAngle(x1, y1, x2, y2);
            return Direction.fromAngle(angle);
        }

        public double getAngle(float x1, float y1, float x2, float y2) {

            double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
            return (rad*180/Math.PI + 180)%360;
        }

        private void moveWindow(Direction direction) {
            finish();
        }
    }
}

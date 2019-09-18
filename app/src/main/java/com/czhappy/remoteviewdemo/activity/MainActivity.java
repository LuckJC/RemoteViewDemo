package com.czhappy.remoteviewdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.czhappy.remoteviewdemo.R;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private static LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = (LinearLayout) this.findViewById(R.id.mylayout);
    }

    public static class MyHandler extends Handler {
        WeakReference<Context> weakReference;
        public MyHandler(Context context, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage");
            switch (msg.what) {
                case 1: //RemoteViews的AIDL实现
                    RemoteViews remoteViews = msg.getData().getParcelable("remoteViews");
                    if (remoteViews != null) {
                        Log.i(TAG, "updateUI");

                        View view = remoteViews.apply(weakReference.get(), mLinearLayout);
                        mLinearLayout.addView(view);
                    }
                    break;
                case 2: //修改MainActivity中TextView的内容
                    Bundle bundle = msg.getData();
                    TextView textView = (TextView) mLinearLayout.findViewById(bundle.getInt("id"));
                    textView.setText(bundle.getString("text"));
                    break;
                case 3: //在MainActivity中添加View视图
                    LayoutInflater inflater = LayoutInflater.from(weakReference.get());
                    View view = inflater.inflate(msg.getData().getInt("layoutId"),null);
                    mLinearLayout.addView(view);
                default:
                    break;
            }
        }

    }

    public void readyGo(View view){
        //Intent intent = new Intent(MainActivity.this, TempActivity.class);
        Intent intent = new Intent(MainActivity.this, Temp2Activity.class);
        startActivity(intent);
    }

    private static View getRootView(Activity context)
    {
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }
}

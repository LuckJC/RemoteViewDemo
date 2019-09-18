package com.czhappy.remoteviewdemo.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.czhappy.remoteviewdemo.IremoteViewsManager;
import com.czhappy.remoteviewdemo.R;
import com.czhappy.remoteviewdemo.service.RemoteViewsAIDLService;

public class Temp2Activity extends AppCompatActivity {

    private String TAG = "Temp2Activity";

    private IremoteViewsManager remoteViewsManager;
    private boolean isBind = false;


    private ServiceConnection remoteViewServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"onServiceConnected");
            remoteViewsManager = IremoteViewsManager.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //回收
            remoteViewsManager = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_temp);
    }

    /**
     * 绑定服务
     */
    public void bindService(View view) {
        Intent viewServiceIntent = new Intent(this, RemoteViewsAIDLService.class);
        isBind = bindService(viewServiceIntent,remoteViewServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 更新UI
     */
    public void UpdateUI(View view){
        RemoteViews remoteViews = new RemoteViews(Temp2Activity.this.getPackageName(), R.layout.button_layout);

        Intent intentClick = new Intent(Temp2Activity.this,FirstActivity.class);
        PendingIntent openFirstActivity = PendingIntent.getActivity(Temp2Activity.this,0,intentClick,0);
        remoteViews.setOnClickPendingIntent(R.id.firstButton,openFirstActivity);

        Intent secondClick = new Intent(Temp2Activity.this,SecondActivity.class);
        PendingIntent openSecondActivity = PendingIntent.getActivity(Temp2Activity.this,0,secondClick,0);
        remoteViews.setOnClickPendingIntent(R.id.secondButton,openSecondActivity);

        remoteViews.setCharSequence(R.id.secondButton,"setText","想改就改");
        try {
            remoteViewsManager.addRemoteView(remoteViews);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind){
            unbindService(remoteViewServiceConnection);
            isBind = false;
        }

    }

}

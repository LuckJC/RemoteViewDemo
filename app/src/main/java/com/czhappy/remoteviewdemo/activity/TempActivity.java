package com.czhappy.remoteviewdemo.activity;

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

import com.czhappy.remoteviewdemo.IViewManager;
import com.czhappy.remoteviewdemo.R;
import com.czhappy.remoteviewdemo.service.ViewAIDLService;

/**
 * Description:
 * User: chenzheng
 * Date: 2017/2/9 0009
 * Time: 16:05
 */
public class TempActivity extends AppCompatActivity {

    private String TAG = "TempActivity";

    private IViewManager viewsManager;
    private boolean isBind = false;


    private ServiceConnection viewServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG,"onServiceConnected");
            viewsManager = IViewManager.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //回收
            viewsManager = null;
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
        Intent viewServiceIntent = new Intent(this,ViewAIDLService.class);
        isBind = bindService(viewServiceIntent,viewServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 更新UI
     */
    public void UpdateUI(View view){
        try {
            viewsManager.setTextViewText(R.id.mytext,"通过AIDL跨进程修改TextView内容");
            viewsManager.addView(R.layout.button_layout);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind){
            unbindService(viewServiceConnection);
            isBind = false;
        }

    }

}

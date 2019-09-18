package com.czhappy.remoteviewdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.czhappy.remoteviewdemo.IViewManager;
import com.czhappy.remoteviewdemo.activity.MainActivity;

/**
 * Description:
 * User: chenzheng
 * Date: 2017/2/9 0009
 * Time: 16:00
 */
public class ViewAIDLService extends Service {
    private static final String TAG = "ViewAIDLService";
    private Binder viewManager = new IViewManager.Stub(){
        @Override
        public void setTextViewText(int id, String text) throws RemoteException {
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putString("text",text);
            message.setData(bundle);
            new MainActivity.MyHandler(ViewAIDLService.this,getMainLooper()).sendMessage(message);
        }

        @Override
        public void addView(int layoutId) throws RemoteException {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putInt("layoutId",layoutId);
            message.setData(bundle);
            Log.i(TAG,"thread name = "+Thread.currentThread().getName());
            new MainActivity.MyHandler(ViewAIDLService.this,getMainLooper()).sendMessage(message);
        }

    };
    public ViewAIDLService() {
    }

    /**
     * 当客户端绑定到该服务时调用
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return viewManager;
    }
}
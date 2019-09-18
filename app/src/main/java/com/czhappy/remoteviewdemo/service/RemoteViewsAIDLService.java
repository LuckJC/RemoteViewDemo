package com.czhappy.remoteviewdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.RemoteViews;

import com.czhappy.remoteviewdemo.IremoteViewsManager;
import com.czhappy.remoteviewdemo.activity.MainActivity;

/**
 * Description:
 * User: chenzheng
 * Date: 2017/2/10 0010
 * Time: 10:56
 */
public class RemoteViewsAIDLService extends Service {
    private static final String TAG = "RemoteViewsAIDLService";

    private Binder remoteViewsBinder = new IremoteViewsManager.Stub(){
        @Override
        public void addRemoteView(RemoteViews remoteViews) throws RemoteException {
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putParcelable("remoteViews",remoteViews);
            message.setData(bundle);
            new MainActivity.MyHandler(RemoteViewsAIDLService.this,getMainLooper()).sendMessage(message);
        }
    };

    public RemoteViewsAIDLService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return remoteViewsBinder;
    }

}

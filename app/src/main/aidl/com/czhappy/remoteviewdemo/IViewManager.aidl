// IViewManager.aidl
package com.czhappy.remoteviewdemo;

// Declare any non-default types here with import statements

interface IViewManager {
    void setTextViewText(in int id,in String text);
    void addView(in int layoutId);
}
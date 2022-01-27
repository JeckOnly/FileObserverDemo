package com.luwei.fileobeserverdemo.util;

import android.os.FileObserver;
import android.util.Log;

public class MyFileObserver extends FileObserver {
    public MyFileObserver(String path) {
        super(path, ALL_EVENTS);
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.ACCESS:
                Log.i("MyFileObserver", "ACCESS: " + path);
                break;
            case FileObserver.ATTRIB:
                Log.i("MyFileObserver", "ATTRIB: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
                Log.i("MyFileObserver", "CLOSE_NOWRITE: " + path);
                break;
            case FileObserver.CLOSE_WRITE:
                Log.i("MyFileObserver", "CLOSE_WRITE: " + path);
                break;
            case FileObserver.CREATE:
                Log.i("MyFileObserver", "CREATE: " + path);
                break;
            case FileObserver.DELETE:
                Log.i("MyFileObserver", "DELETE: " + path);
                break;
            case FileObserver.DELETE_SELF:
                Log.i("MyFileObserver", "DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY:
                Log.i("MyFileObserver", "MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF:
                Log.i("MyFileObserver", "MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM:
                Log.i("MyFileObserver", "MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO:
                Log.i("MyFileObserver", "MOVED_TO: " + path);
                break;
            case FileObserver.OPEN:
                Log.i("MyFileObserver", "OPEN: " + path);
                break;
            default:
                Log.i("MyFileObserver", "DEFAULT(" + event + "): " + path);
                break;
        }
    }
}
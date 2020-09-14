package com.clicker.tantan;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.clicker.tantan.db.DaoMaster;
import com.clicker.tantan.db.DaoSession;
import com.clicker.tantan.db.MyDBHelper;
import com.tencent.bugly.Bugly;

/**
 * Created by hao on 2017/3/28.
 */

public class APP extends Application {
    public static APP app;
    public static boolean DEBUG = false;
    final String KEY = "e90782a2ef";

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initDB();
        Bugly.init(getApplicationContext(), KEY, DEBUG);
    }

    private void initDB() {
        MyDBHelper helper = new MyDBHelper(this, "safedata");
        SQLiteDatabase db = helper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

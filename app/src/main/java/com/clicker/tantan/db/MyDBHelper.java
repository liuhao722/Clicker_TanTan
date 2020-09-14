package com.clicker.tantan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.clicker.tantan.utils.LogUtils;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hao on 2017/3/16.
 */

public class MyDBHelper extends DaoMaster.OpenHelper {
    public MyDBHelper(Context context, String name) {
        super(context, name);
    }

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Override this if you do not want to depend on {@link SQLiteDatabase}.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.e("info", "updateDB\told:" + oldVersion + "\tnew:" + newVersion);
        if (oldVersion == 1 && newVersion == 2) {
//            InventListDao.dropTable(db, true);
//            NonCoalListDao.dropTable(db, true);
//            db.execSQL("alter table INVENT_LIST add PRODUCT_ID text");
        }
//        DaoMaster.createAllTables(db, true);
    }
}

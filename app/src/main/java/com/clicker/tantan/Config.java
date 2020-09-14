package com.clicker.tantan;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hao on 2017/3/30.
 */

public class Config {
    //最终执行循环的次数
    public static int doLikeCount = 10;   //点击喜欢人的次数
    public static int doReplyCount = 1;   //回复消息的条数
    public static int doPraiseCount = 8; //朋友圈点赞的次数

    public static final int defaultSleepTime = 5000;

    private static final Config ourInstance = new Config();

    public static Config ins() {
        return ourInstance;
    }

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private static final String SAFE_CONFIG_NAME = "clicker_config_name";
    public static final String IS_ANTI_VILLAIN = "is_anti_villain";
    public static final String DEFAULT = "default";
    public static final String SAY_HELLO = "say_hello";


    private Config() {
        sp = APP.app.getSharedPreferences(SAFE_CONFIG_NAME,
                Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    /**
     * 防坏人设置
     */
    public void setAntiVillain(boolean isAntiVillain) {
        edit.putBoolean(IS_ANTI_VILLAIN, isAntiVillain);
        edit.commit();
    }

    public boolean isAntiVillain() {
        return sp.getBoolean(IS_ANTI_VILLAIN, false);
    }
}

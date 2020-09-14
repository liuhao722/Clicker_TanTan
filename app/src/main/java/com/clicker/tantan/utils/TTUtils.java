package com.clicker.tantan.utils;

import com.clicker.tantan.entity.PraiseInfo;
import com.clicker.tantan.entity.TTPraiseXY;

/**
 * Created by hao on 2017/4/1.
 */

public class TTUtils {
    private static final TTUtils ourInstance = new TTUtils();

    public static TTUtils ins() {
        return ourInstance;
    }

    private TTUtils() {
    }

    /**
     * 解析朋友圈的赞按钮的xy坐标
     *
     * @param bounds
     * @return
     */
    public TTPraiseXY parsePraiseXY(String bounds) {
        TTPraiseXY mTTPraiseXY = new TTPraiseXY();
        int x1 = Integer.parseInt(bounds.substring(1, bounds.indexOf(",")));
        int y1 = Integer.parseInt(bounds.substring(bounds.indexOf(",") + 1, bounds.indexOf("]")));
        int x2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf("[") + 1, bounds.lastIndexOf(",")));
        int y2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf(",") + 1, bounds.length() - 1));
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        mTTPraiseXY.x = x;
        mTTPraiseXY.y = y;
        return mTTPraiseXY;
    }

    /**
     * 解析朋友圈的赞按钮的xy坐标
     *
     * @param bounds
     * @return
     */
    public void parsePraiseXY(PraiseInfo.Info info, String bounds) {
        int x1 = Integer.parseInt(bounds.substring(1, bounds.indexOf(",")));
        int y1 = Integer.parseInt(bounds.substring(bounds.indexOf(",") + 1, bounds.indexOf("]")));
        int x2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf("[") + 1, bounds.lastIndexOf(",")));
        int y2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf(",") + 1, bounds.length() - 1));
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        info.x = x;
        info.y = y;
    }

    /**
     * 解析朋友圈的赞按钮的xy坐标
     *
     * @param bounds  点击左上角的喜欢按钮
     * @param leftTop 点击左上角的喜欢按钮
     * @return
     */
    public void parsePraiseXY(PraiseInfo.Info info, String bounds, boolean leftTop) {
        int x1 = Integer.parseInt(bounds.substring(1, bounds.indexOf(",")));
        int y1 = Integer.parseInt(bounds.substring(bounds.indexOf(",") + 1, bounds.indexOf("]")));
        int x2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf("[") + 1, bounds.lastIndexOf(",")));
        int y2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf(",") + 1, bounds.length() - 1));
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        info.x = x;
        info.y = y1 + 10;
    }
}

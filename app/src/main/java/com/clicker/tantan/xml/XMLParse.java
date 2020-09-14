package com.clicker.tantan.xml;

import android.text.TextUtils;
import android.util.Xml;

import com.clicker.tantan.APP;
import com.clicker.tantan.entity.ChatInfo;
import com.clicker.tantan.entity.PraiseInfo;
import com.clicker.tantan.entity.TanTanText;
import com.clicker.tantan.entity.TTPraiseXY;
import com.clicker.tantan.utils.AppUtils;
import com.clicker.tantan.utils.CmdUtilsTanTan;
import com.clicker.tantan.utils.LogUtils;
import com.clicker.tantan.utils.TTID;
import com.clicker.tantan.utils.TTUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2017/4/1.
 */

public class XMLParse {
    private static final XMLParse ourInstance = new XMLParse();
    static File file;
    static InputStream is = null;

    public static XMLParse ins() {
        file = new File(CmdUtilsTanTan.fileName);
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ourInstance;
    }

    private XMLParse() {
    }

    public List<TTPraiseXY> parse(InputStream is) throws Exception {
        List<TTPraiseXY> mList = new ArrayList<>(); // 初始化books集合
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        boolean hasChild = false;
        List<String> nameList = new ArrayList<>();
        String userName = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.resourceId)) && xpp.getAttributeValue(null, TTID.resourceId).equals(TTID.userName)) { // 判断开始标签元素是否是book
                        userName = xpp.getAttributeValue(null, TTID.text);
                    }
                    //微信朋友圈
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.resourceId)) && xpp.getAttributeValue(null, TTID.resourceId).equals(TTID.praiseId)) { // 判断开始标签元素是否是book
                        TTPraiseXY entity = TTUtils.ins().parsePraiseXY(xpp.getAttributeValue(null, TTID.bounds));
                        if (!TextUtils.isEmpty(userName)) {
                            entity.name = userName;
                            userName = "";
                        }
                        mList.add(entity);
                    }
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.resourceId)) && xpp.getAttributeValue(null, TTID.resourceId).equals(TTID.praiselist)) { // 判断开始标签元素是否是book
                        String index = xpp.getAttributeValue(null, TTID.index);
                        if (!TextUtils.isEmpty(index)) {
                            if (Integer.parseInt(index) > 1) {
                                hasChild = true;
                            }
                        }
                    } else {//已经结束了遍历点赞列表的集合
                        if (hasChild) {
                            if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.resourceId)) && xpp.getAttributeValue(null, TTID.resourceId).equals(TTID.praiselistItem)) { // 判断开始标签元素是否是book
                                //判断当前点赞里面的集合 根据index进行判断是否已经结尾了
                                String name = xpp.getAttributeValue(null, TTID.text);
                                nameList.add(name);
                            } else {
                                //获取最后一个item进行添加
                                if (mList.size() > 0 && nameList.size() > 0) {
                                    TTPraiseXY lastItem = mList.get(mList.size() - 1);
                                    for (int i = 0; i < nameList.size(); i++) {
                                        lastItem.nameList.add(nameList.get(i));
                                    }
                                    mList.set(mList.size() - 1, lastItem);
                                }
                                nameList.clear();
                                hasChild = false;
                            }
                        }
                    }

                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return mList;
    }

    public static final int IS_LEFT = 1;    // 左页面
    public static final int IS_CENTER = 2;  // 中间页面
    public static final int IS_RIGHT = 3;   // 右侧页面

    public int isMainLayer() throws Exception {
        TanTanText mTanTanText = new TanTanText(); // 初始化books集合
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.text))) {
                        mTanTanText.textList.add(xpp.getAttributeValue(null, TTID.text));
                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        boolean leftFirstMatch = false;
        boolean leftSecondMatch = false;
        boolean leftThirdMatch = false;

        boolean rightFirstMatch = false;
        boolean rightSecondMatch = false;
        boolean rightThirdMatch = false;

        for (int i = 0; i < mTanTanText.textList.size(); i++) {
            String name = mTanTanText.textList.get(i);
            //left
            if (name.equals("探探")) {
                leftFirstMatch = true;
            }
            if (name.equals("设置")) {
                leftSecondMatch = true;
            }
            if (name.equals("新手引导")) {
                leftThirdMatch = true;
            }

            //right
            if (name.equals("聊天")) {
                rightFirstMatch = true;
            }
            if (name.equals("朋友圈")) {
                rightSecondMatch = true;
            }
            if (name.equals("所有配对")) {
                rightThirdMatch = true;
            }
        }
        if (is != null) {
            is.close();
        }
        if (leftFirstMatch && leftSecondMatch && leftThirdMatch) {
            return IS_LEFT;
        } else if (rightFirstMatch && rightSecondMatch && rightThirdMatch) {
            return IS_RIGHT;
        } else {
            return IS_CENTER;
        }
    }


    public ChatInfo parseChatContent() throws Exception {
        ChatInfo data = new ChatInfo(); // 初始化books集合
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.text))) { // 判断开始标签元素是否是book
                        String content = xpp.getAttributeValue(null, TTID.text);
                        if (!content.equals("输入聊天内容...")) {
                            LogUtils.e(content);
                            data.msg.add(content);
                        }
                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        if (is != null) {
            is.close();
        }
        return data;
    }

    /**
     * 朋友圈
     *
     * @return
     * @throws Exception
     */
    public PraiseInfo parseFriendContent() throws Exception {
        PraiseInfo data = new PraiseInfo(); // 初始化books集合
        PraiseInfo.Info info = null;
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.text))) { // 判断开始标签元素是否是book
                        String content = xpp.getAttributeValue(null, TTID.text);
                        if (content.equals("私密评论")) {
                            info = data.new Info();
                            info.comment = content;
                        }
                        if (content.equals("喜欢") && info != null && !TextUtils.isEmpty(info.comment)) {
                            info.like = content;
                            TTUtils.ins().parsePraiseXY(info, xpp.getAttributeValue(null, TTID.bounds));
                            data.list.add(info);
                            info = null;
                        }
                        LogUtils.e(content);
                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        if (is != null) {
            is.close();
        }
        return data;
    }

    public String parseMainLikeBtnPosition() throws Exception {
        String bounds = "";
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(TTID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.bounds)) && !TextUtils.isEmpty(xpp.getAttributeValue(null, TTID.NAF))) {
                        bounds = xpp.getAttributeValue(null, TTID.bounds);
                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return bounds;
    }

}

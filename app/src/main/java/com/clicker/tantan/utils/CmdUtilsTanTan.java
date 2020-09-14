package com.clicker.tantan.utils;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;

import com.clicker.tantan.APP;
import com.clicker.tantan.Config;
import com.clicker.tantan.db.ChatData;
import com.clicker.tantan.db.ChatDataDao;
import com.clicker.tantan.entity.ChatInfo;
import com.clicker.tantan.entity.PraiseInfo;
import com.clicker.tantan.floatwindow.FloatWindowService;
import com.clicker.tantan.xml.XMLParse;

import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by hao on 2017/3/28.
 */

public class CmdUtilsTanTan {

    public int likeCount = 0;
    public int replyCount = 0;
    public int praiseCount = 0;

    ClipboardManager copy = (ClipboardManager) APP.app.getSystemService(CLIPBOARD_SERVICE);
    private static final CmdUtilsTanTan ourInstance = new CmdUtilsTanTan();

    public static CmdUtilsTanTan ins() {
        return ourInstance;
    }

    private CmdUtilsTanTan() {
    }

    private OutputStream os;
    Process process;

    /**
     * <dd>方法作用： 执行adb命令
     * <dd>注意事项： 注意，Runtime.getRuntime().exec("su").getOutputStream();网上前辈的经验说这句话貌似很耗时，所以不要每次都执行这句代码
     *
     * @param cmd 具体命令语句
     */
    public void execute(String cmd) {
        try {
            if (os == null) {
                process = Runtime.getRuntime().exec("su");
                os = process.getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String fileName = "/mnt/sdcard/window_dump.xml";
    public String TT_CLASS = "com.p1.mobile.putong/com.p1.mobile.putong.ui.splash.SplashAct";
    /**
     * 需要的命令集合
     */
    public String[] cmdStr = {
            "input keyevent %d\n",                      // 0---点击
            "input keyevent --longpress %d\n",          // 1---长按 +KEYCODE
            "input text %s\n",                          // 2---输入对应的text
            "input tap %d %d\n",                        // 3---指定的坐标 x-y
            "sleep %d\n",                               // 4---休眠时间
            "am start -n %s\n",                         // 5---打开指定的activity【全路径】
            "am start -n " + TT_CLASS + "\n",           // 6---打开微信
            "input swipe %d %d %d %d %d\n",             // 7---滑动 点击 或者长按都可以使用该命令 五个参数从x1 y1 到x2 y2 持续多少毫秒
            "uiautomator dump " + fileName + "\n"       // 8---截取屏幕
    };

    public void openMySelf() {
        execute(String.format(cmdStr[5], "com.clicker.tantan/com.clicker.tantan.activity.MainActivity"));// 打开主Activity
    }

    /**
     * 打开微信并且隐藏至后台
     */
    public void openTanTan() {
//        execute(String.format(cmdStr[0], KeyEvent.KEYCODE_HOME));// 切换至后台
        execute(cmdStr[6]);// 打开微信客户端
    }

    /**
     * 滑动 点击 或者长按都可以使用该命令 五个参数从x1 y1 到x2 y2 持续多少毫秒
     *
     * @param fromX 起点X位置
     * @param fromY 起点Y位置
     * @param toX   滑动到X的位置
     * @param toY   滑动到Y的位置
     * @param time  长按时长
     */
    public void swipe(int fromX, int fromY, int toX, int toY, int time) {
        execute(String.format(cmdStr[7], fromX, fromY, toX, toY, time));
    }

    /**
     * 点击指定位置
     *
     * @param positionX 点击的位置_X
     * @param positionY 点击的位置_Y
     */
    public void click(int positionX, int positionY) {
        execute(String.format(cmdStr[3], positionX, positionY));//单纯点击
    }

    //返回按钮的位置xy坐标 左
    private final int BACK_LEFT_X = (int) (0.0777778 * AppUtils.getScreenW());
    //返回按钮的位置xy坐标 右
    private final int BACK_RIGHT_X = (int) (0.9222222 * AppUtils.getScreenW());
    private final int BACK_Y = (int) (0.0828125 * AppUtils.getScreenH());

    //---------------------------主页start----------------------------------
    //tab1 2 3的xy
    private int TAB1_X = (int) (0.1666667 * AppUtils.getScreenW());
    private int TAB2_X = (int) (0.5 * AppUtils.getScreenW());
    private int TAB3_X = (int) (0.8333333 * AppUtils.getScreenW());
    private int TAB_Y = (int) (0.08125 * AppUtils.getScreenH());
    //喜欢的xy坐标
    private int LIKE_X = (int) (0.7027778 * AppUtils.getScreenW());
    private int LIKE_Y = (int) (0.9161458 * AppUtils.getScreenH());
    //继续点击的x
    private int CONTINUE_LIKE_X = (int) (0.8444444 * AppUtils.getScreenH());
    //---------------------------主页end------------------------------------

    //---------------------------朋友圈start--------------------------------
    //朋友圈按钮的xy坐标
    private final int FRIEND_X = (int) (0.5 * AppUtils.getScreenW());
    private final int FRIEND_Y = (int) (0.1953125 * AppUtils.getScreenH());
    //---------------------------朋友圈end----------------------------------

    //---------------------------聊天start----------------------------------
    //探探第一条消息的位置 xy坐标
    private final int FIRST_MSG_Y = (int) (0.6666667 * AppUtils.getScreenH());
    //朋友圈下面 聊天tab按钮的xy坐标
    private final int CHAT_X = (int) (0.7916667 * AppUtils.getScreenW());
    private final int CHAT_Y = (int) (0.2736979 * AppUtils.getScreenH());

    //消息回复框粘贴xy位置
    private final int COPY_X = (int) (0.2888889 * AppUtils.getScreenW());
    private final int COPY_Y = (int) (0.4666667 * AppUtils.getScreenH());

    //消息回复框xy位置--未弹起输入法时候
    private final int REPLY_EDIT_X = (int) (0.5555556 * AppUtils.getScreenW());
    private final int REPLY_EDIT_Y = (int) (0.9627604 * AppUtils.getScreenH());

    //消息回复框xy位置--输入法弹起后
    private final int REPLY_EDIT_SHOW_KEY_Y = (int) (0.5330729 * AppUtils.getScreenH());

    //发送按钮位置的xy
    private final int SEND_X = (int) (0.9 * AppUtils.getScreenW());
    private final int SEND_Y = (int) (0.52083333 * AppUtils.getScreenH());
    //清空聊天记录位置的xy
    private final int CLEAR_Y = (int) (0.55677083 * AppUtils.getScreenH());
    //清楚  好 的xy
    //发送按钮位置的xy
    private final int OK_X = (int) (0.81388889 * AppUtils.getScreenW());
    private final int OK_Y = (int) (0.578125 * AppUtils.getScreenH());
    //---------------------------聊天end----------------------------------

    //屏幕一半的位置x
    private final int SCREEN_X_CENTER = (int) (AppUtils.getScreenW() / 2);

    public void autoDoAllCommend() {
        if (!FloatWindowService.stop) {
            if (likeCount < Config.doLikeCount) {    // 执行点击喜欢的次数
                click(LIKE_X, LIKE_Y);               // 点击喜欢按钮
                likeCount++;
                sleep(1000);
                autoDoAllCommend();
            } else if (replyCount < Config.doReplyCount) {     //  执行回复信息
                click(CONTINUE_LIKE_X, LIKE_Y);               // 点击喜欢按钮
                if (replyCount == 0) {
                    click(TAB3_X, TAB_Y);               // 点击右侧的按钮打开朋友圈的聊天右侧页面
                    click(CHAT_X, CHAT_Y);              // 点击聊天按钮的tab
                    //第一次进入查看数据时候解析页面的数据条数

                }
                click(SCREEN_X_CENTER, FIRST_MSG_Y);    //点击第一条信息
                //解析页面数据获取聊天信息
                chatMsg();
                click(REPLY_EDIT_X, REPLY_EDIT_Y);     //点击输入框
                swipe(REPLY_EDIT_X, REPLY_EDIT_SHOW_KEY_Y, REPLY_EDIT_X, REPLY_EDIT_SHOW_KEY_Y, 1000);    // 长按输入框弹出黏贴的标识
                click(COPY_X, COPY_Y);     //点击黏贴按钮
                click(SEND_X, SEND_Y);     //点击发送按钮
                click(BACK_LEFT_X, BACK_Y);     //点击返回按钮
                sleep(1000);
                swipe(SCREEN_X_CENTER, FIRST_MSG_Y, SCREEN_X_CENTER, FIRST_MSG_Y, 1000);    // 长按第一条信息出删除提示当前聊天的提示
                if (!APP.DEBUG) {
                    click(SCREEN_X_CENTER, CLEAR_Y);    //点击清除第一条信息内容
                    click(OK_X, OK_Y);    //点击清除第一条信息内容
                } else {
                    click(SCREEN_X_CENTER, 120);        //点击任意位置去除弹窗
                }
                replyCount++;
                sleep(2000);
                autoDoAllCommend();
            } else if (praiseCount < Config.doPraiseCount) {       // 执行指定朋友圈点赞次数
                if (praiseCount == 0) {
                    click(FRIEND_X, FRIEND_Y);           // 点击朋友圈
                    sleep(2000);
                } else {
                    swipe(SCREEN_X_CENTER, 1300, SCREEN_X_CENTER, 825, 100);  // 滑动刚好一屏幕的事件
                }
                execute(cmdStr[8]);
                sleep(Config.defaultSleepTime);
                try {
                    PraiseInfo data = XMLParse.ins().parseFriendContent();
                    if (data != null && data.list.size() > 0) {//如果当前页面有喜欢的按钮
                        for (int i = data.list.size() - 1; i >= 0; i--) {
                            PraiseInfo.Info likeItem = data.list.get(i);
                            click(likeItem.x, likeItem.y);
                            sleep(500);
                            if (i == 0) {
                                praiseCount++;
                                if (praiseCount >= Config.doPraiseCount) {
                                    clearCount();
                                }
                                autoDoAllCommend();
                                continue;
                            }
                        }
                    } else {
                        praiseCount++;
                        if (praiseCount >= Config.doPraiseCount) {
                            clearCount();
                        }
                        autoDoAllCommend();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    praiseCount++;
                    if (praiseCount >= Config.doPraiseCount) {
                        clearCount();
                    }
                    autoDoAllCommend();
                }
            }
        }
    }

    ChatInfo mChatInfo = null;

    private void chatMsg() {
        sleep(1000);
        execute(cmdStr[8]);
        sleep(Config.defaultSleepTime);
        try {
            mChatInfo = XMLParse.ins().parseChatContent();
            if (mChatInfo.msg.size() > 0) {
                copy.setText(MatchChatInfo.ins().matchAnswer(mChatInfo.msg.get(mChatInfo.msg.size() - 1))); // 匹配问题的答案进行设置到粘贴内容里
            } else {
                List<ChatData> dataList = APP.app.getDaoSession().getChatDataDao().queryBuilder().where(ChatDataDao.Properties.Question.eq(Config.SAY_HELLO)).list();
                String question = dataList.get(new Random().nextInt(dataList.size())).answer;
                copy.setText(question); // 匹配问题的答案进行设置到粘贴内容里
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCount() {
        likeCount = 0;
        replyCount = 0;
        praiseCount = 0;
        click(BACK_LEFT_X, BACK_Y);      // 点击返回按钮
        sleep(500);
        click(TAB2_X, TAB_Y);      // 点击返回主页面按钮
    }


    public void stopCMD(Context context, boolean stop) {
        if (stop) {
            FloatWindowService.stop = true;
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses("com.clicker.tantan");
        }
    }


    public void dump(final Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                execute(cmdStr[8]);
                sleep(Config.defaultSleepTime);
                try {
                    int isMainLayer = XMLParse.ins().isMainLayer();
                    switch (isMainLayer) {
                        case XMLParse.IS_LEFT:      //左侧页面
                            click(TAB2_X, TAB_Y);
                            getMainLikeBtnPosition();
                            autoDoAllCommend();
                            break;
                        case XMLParse.IS_CENTER:    //中间页面
                            getMainLikeBtnPosition();
                            autoDoAllCommend();
                            break;
                        case XMLParse.IS_RIGHT:     //右侧页面
                            click(TAB2_X, TAB_Y);
                            getMainLikeBtnPosition();
                            autoDoAllCommend();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    public void start() {
        click(TAB2_X, TAB_Y);
        sleep(5000);
        getMainLikeBtnPosition();
        autoDoAllCommend();
    }

    /**
     * 是否包含
     *
     * @param list
     * @return
     */
    public boolean contains(List<String> list) {
        boolean isContains = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).trim().contains("刘浩")) {
                isContains = true;
            }
        }
        return isContains;
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取喜欢的按钮位置
     */
    public void getMainLikeBtnPosition() {
        execute(cmdStr[8]);
        sleep(Config.defaultSleepTime);
        try {
            String bounds = XMLParse.ins().parseMainLikeBtnPosition();
            LogUtils.e(bounds);
            PraiseInfo mPraiseInfo = new PraiseInfo();
            PraiseInfo.Info info = mPraiseInfo.new Info();
            TTUtils.ins().parsePraiseXY(info, bounds, true);
            LIKE_X = info.x;
            LIKE_Y = info.y;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dump() {
        sleep(2000);
        execute(cmdStr[8]);
    }
}

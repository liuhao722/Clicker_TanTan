package com.clicker.tantan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clicker.tantan.APP;
import com.clicker.tantan.Config;
import com.clicker.tantan.R;
import com.clicker.tantan.base.BaseActivity;
import com.clicker.tantan.floatwindow.FloatWindowService;
import com.clicker.tantan.utils.CmdUtilsTanTan;
import com.clicker.tantan.utils.DevicesUtils;
import com.clicker.tantan.utils.MatchChatInfo;
import com.clicker.tantan.utils.ParseFile;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hao on 2017/3/28.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;
    @Bind(R.id.btn4)
    Button btn4;
    @Bind(R.id.btn5)
    Button btn5;
    @Bind(R.id.btn6)
    Button btn6;
    @Bind(R.id.btn7)
    Button btn7;
    @Bind(R.id.show_msg)
    TextView show_msg;
    @Bind(R.id.msg_content)
    TextView msg_content;
    @Bind(R.id.et1)
    EditText et1;
    @Bind(R.id.et2)
    EditText et2;
    @Bind(R.id.et3)
    EditText et3;

    @Override
    protected int setLayoutId() {
        DevicesUtils.checkRegister(MainActivity.this);
        startService(new Intent(MainActivity.this, FloatWindowService.class));
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseFile.ins().readXLSX();// 读取数据文件进行插入数据库
        String answer = MatchChatInfo.ins().matchAnswer("胡");
//        String answer = MatchChatInfo.ins().matchAnswer("？ 加聊 聊天 聊吧 你我自动刷附近的人次数");
        StringBuilder sb = new StringBuilder();
        sb.append("自动点喜欢的人次数:" + Config.doLikeCount + "次\n");
        sb.append("自动回好友消息次数:" + Config.doReplyCount + "次\n");
        sb.append("自动朋友圈点赞次数:" + Config.doPraiseCount + "次\n\n\n");
        sb.append(APP.DEBUG ? "测试环境:不删除聊天数据" : "正式环境:删除聊天数据");
        show_msg.setText(sb.toString());
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                eventAction(ACTION_BTN1, MainActivity.this);
                break;
            case R.id.btn2:
                eventAction(ACTION_BTN2, MainActivity.this);
                break;
            case R.id.btn3:
                eventAction(ACTION_BTN3, MainActivity.this);
                break;
            case R.id.btn4:
                eventAction(ACTION_BTN4, MainActivity.this);
                break;
            case R.id.btn5:
                eventAction(ACTION_BTN5, MainActivity.this);
                break;
            case R.id.btn6:
                if (!TextUtils.isEmpty(et1.getText().toString())) {
                    Config.doLikeCount = Integer.parseInt(et1.getText().toString());
                }
                if (!TextUtils.isEmpty(et2.getText().toString())) {
                    Config.doReplyCount = Integer.parseInt(et2.getText().toString());
                }
                if (!TextUtils.isEmpty(et3.getText().toString())) {
                    Config.doPraiseCount = Integer.parseInt(et3.getText().toString());
                }
                eventAction(ACTION_BTN6, MainActivity.this);
                break;
            case R.id.btn7:
                APP.DEBUG = !APP.DEBUG;
                btn7.setText(APP.DEBUG ? "当前测试环境--->点击切换" : "当前正式环境--->点击切换");
                if (!TextUtils.isEmpty(et1.getText().toString())) {
                    Config.doLikeCount = Integer.parseInt(et1.getText().toString());
                }
                if (!TextUtils.isEmpty(et2.getText().toString())) {
                    Config.doReplyCount = Integer.parseInt(et2.getText().toString());
                }
                if (!TextUtils.isEmpty(et3.getText().toString())) {
                    Config.doPraiseCount = Integer.parseInt(et3.getText().toString());
                }
                StringBuilder sb = new StringBuilder();
                sb.append("自动点喜欢的人次数:" + Config.doLikeCount + "次\n");
                sb.append("自动回好友消息次数:" + Config.doReplyCount + "次\n");
                sb.append("自动朋友圈点赞次数:" + Config.doPraiseCount + "次\n\n\n");
                sb.append(APP.DEBUG ? "测试环境:不删除聊天数据" : "正式环境:删除聊天数据");
                show_msg.setText(sb.toString());
                break;
        }
    }

    public static final int ACTION_BTN1 = 1;
    public static final int ACTION_BTN2 = 2;
    public static final int ACTION_BTN3 = 3;
    public static final int ACTION_BTN4 = 4;
    public static final int ACTION_BTN5 = 5;
    public static final int ACTION_BTN6 = 6;

    public static void eventAction(int action, Context context) {
        CmdUtilsTanTan.ins().openTanTan();
        switch (action) {
            case ACTION_BTN1:
//                CmdUtilsTanTan.ins().autoReply(handler, DataArray.ins().getSendMsg());
                break;
            case ACTION_BTN2:
//                CmdUtilsTanTan.ins().autoReply(handler);
                break;
            case ACTION_BTN3:
//                CmdUtilsTanTan.ins().autoDiscover(handler);
                break;
            case ACTION_BTN4:
//                CmdUtilsTanTan.ins().autoPraise(handler);
                break;
            case ACTION_BTN5:
//                new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        CmdUtilsTanTan.ins().autoDoAllCommend(handler);
//                    }
//                }.start();
                CmdUtilsTanTan.ins().dump();
                break;
            case ACTION_BTN6:
                CmdUtilsTanTan.ins().start();
                break;
        }
    }
}

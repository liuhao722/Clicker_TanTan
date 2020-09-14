package com.clicker.tantan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天内容实体类
 * Created by hao on 2017/4/10.
 */

public class ChatInfo implements Serializable {
    public List<String> msg = new ArrayList<>();
}

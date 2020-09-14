package com.clicker.tantan.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈点赞的位置
 * Created by hao on 2017/4/12.
 */

public class PraiseInfo {

    public List<Info> list = new ArrayList<>();

    public class Info {
        public String like;
        public String comment;
        public int x;
        public int y;
        public boolean hasLiker = false;
    }
}

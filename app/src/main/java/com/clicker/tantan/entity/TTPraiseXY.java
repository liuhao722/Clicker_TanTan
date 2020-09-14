package com.clicker.tantan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 探探朋友圈的喜欢坐标
 */
public class TTPraiseXY implements Serializable{
    public String name;//发表人的名称
    public int x;
    public int y;
    public List<String> nameList = new ArrayList<>();// 点赞列表内点赞人的全部数量

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("name=" + name + ",x=" + x +
                ", y=" + y + ", textList=");
        if (nameList != null) {
            for (int i = 0; i < nameList.size(); i++) {
                sb.append("name" + i + nameList.get(i) + "\t");
            }
        }
        return sb.toString();
    }
}

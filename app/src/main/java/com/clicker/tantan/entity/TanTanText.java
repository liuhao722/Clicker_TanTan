package com.clicker.tantan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 探探的首页信息列表，根据信息判断当前的页面是左侧还是右侧
 */
public class TanTanText implements Serializable{
    public List<String> textList = new ArrayList<>();// 点赞列表内点赞人的全部数量

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("textList=");
        if (textList != null) {
            for (int i = 0; i < textList.size(); i++) {
                sb.append("name" + i + textList.get(i) + "\t");
            }
        }
        return sb.toString();
    }
}

package com.clicker.tantan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 2.14 生产单位详情
 * Created by hao on 2016/11/25.
 */
@Entity
public class ChatData {
    @Id
    public long id;
    /**
     * 用户提出的问题
     */
    public String question;
    /**
     * 搜索数据库得出的答案
     */
    public String answer;

    // 扩展字段String
    public String es_1;
    // 扩展字段String
    public String es_2;
    // 扩展字段String
    public String es_3;

    // 扩展字段int
    public int ei_1;
    // 扩展字段int
    public int ei_2;
    // 扩展字段int
    public int ei_3;

    // 扩展字段boolean
    public boolean eb1;
    // 扩展字段boolean
    public boolean eb2;
    // 扩展字段boolean
    public boolean eb3;
    @Generated(hash = 530016505)
    public ChatData(long id, String question, String answer, String es_1,
            String es_2, String es_3, int ei_1, int ei_2, int ei_3, boolean eb1,
            boolean eb2, boolean eb3) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.es_1 = es_1;
        this.es_2 = es_2;
        this.es_3 = es_3;
        this.ei_1 = ei_1;
        this.ei_2 = ei_2;
        this.ei_3 = ei_3;
        this.eb1 = eb1;
        this.eb2 = eb2;
        this.eb3 = eb3;
    }
    @Generated(hash = 45695075)
    public ChatData() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getEs_1() {
        return this.es_1;
    }
    public void setEs_1(String es_1) {
        this.es_1 = es_1;
    }
    public String getEs_2() {
        return this.es_2;
    }
    public void setEs_2(String es_2) {
        this.es_2 = es_2;
    }
    public String getEs_3() {
        return this.es_3;
    }
    public void setEs_3(String es_3) {
        this.es_3 = es_3;
    }
    public int getEi_1() {
        return this.ei_1;
    }
    public void setEi_1(int ei_1) {
        this.ei_1 = ei_1;
    }
    public int getEi_2() {
        return this.ei_2;
    }
    public void setEi_2(int ei_2) {
        this.ei_2 = ei_2;
    }
    public int getEi_3() {
        return this.ei_3;
    }
    public void setEi_3(int ei_3) {
        this.ei_3 = ei_3;
    }
    public boolean getEb1() {
        return this.eb1;
    }
    public void setEb1(boolean eb1) {
        this.eb1 = eb1;
    }
    public boolean getEb2() {
        return this.eb2;
    }
    public void setEb2(boolean eb2) {
        this.eb2 = eb2;
    }
    public boolean getEb3() {
        return this.eb3;
    }
    public void setEb3(boolean eb3) {
        this.eb3 = eb3;
    }
}

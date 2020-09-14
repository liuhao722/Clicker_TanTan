package com.clicker.tantan.utils;

import android.text.TextUtils;
import android.util.Log;

import com.clicker.tantan.APP;
import com.clicker.tantan.Config;
import com.clicker.tantan.db.ChatData;
import com.clicker.tantan.db.ChatDataDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 根据对方的问题从数据库中匹配答案
 * Created by hao on 2017/4/7.
 */

public class MatchChatInfo {
    private static final MatchChatInfo ourInstance = new MatchChatInfo();

    public static MatchChatInfo ins() {
        return ourInstance;
    }

    private MatchChatInfo() {
    }

    /**
     * 匹配问题的答案
     */
    public String matchAnswer(String question) {
        List<String> questionList = new ArrayList<>();
        for (int i = 0; i < question.length(); i++) {
            questionList.add(question.substring(i, i + 1));
        }
        QueryBuilder<ChatData> bd = APP.app.getDaoSession().getChatDataDao().queryBuilder();
        for (int i = 0; i < questionList.size(); i++) {
            if (i == 0) {
                bd.whereOr(ChatDataDao.Properties.Question.like("%" + (questionList.get(i)) + "%"), ChatDataDao.Properties.Question.like("%" + (questionList.get((i + 1) < questionList.size() ? i + 1 : i)) + "%"));
            } else {
                bd.or(ChatDataDao.Properties.Question.like("%" + (questionList.get(i)) + "%"), ChatDataDao.Properties.Question.like("%" + (questionList.get((i + 1) < questionList.size() ? i + 1 : i)) + "%"));
            }
            i++;
        }
        List<ChatData> list = bd.list();
        String checkStr = removedSymbols(question);
        if (TextUtils.isEmpty(checkStr)) {
            String anwserResult = list.get(new Random().nextInt(list.size())).answer;
            LogUtils.e("info", "最终结果：\n" + anwserResult);
            return anwserResult;
        } else {
            int index = 0;
            double max = 0;
            for (int i = 0; i < list.size(); i++) {
                double result = SimilarDegree(checkStr, list.get(i).question);
                LogUtils.e("info", checkStr + "\t<---->\t" + list.get(i).question + "\t相似度:\t" + result);
                if (result > max) {
                    max = result;
                    index = i;
                }
            }
            if (list.size() > 0) {
                LogUtils.e("info", "最终结果：\n" + checkStr + "\t<---->\t" + list.get(index).question + "\t回答：" + list.get(index).answer + "\t相似度：\t" + max);
                if (max > 0) {
                    return list.get(index).answer;
                } else {
                    String answer = APP.app.getDaoSession().getChatDataDao().queryBuilder().where(ChatDataDao.Properties.Question.eq(Config.DEFAULT)).unique().answer;
                    LogUtils.e("info", "max=" + max + "使用默认回答：\t" + answer);
                    return answer;
                }
            } else {
                String answer = APP.app.getDaoSession().getChatDataDao().queryBuilder().where(ChatDataDao.Properties.Question.eq(Config.DEFAULT)).unique().answer;
                LogUtils.e("info", "使用默认回答：\t" + answer + "\t" + max);
                return answer;
            }
        }
    }

    /**
     * 相似度比较
     *
     * @param strA
     * @param strB
     * @return
     */

    public static double SimilarDegree(String strA, String strB) {
        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);
        int temp = Math.max(newStrA.length(), newStrB.length());
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();
        return temp2 * 1.0 / temp;
    }

    private static String removeSign(String str) {
        StringBuffer sb = new StringBuffer();
        for (char item : str.toCharArray())
            if (charReg(item)) {
                sb.append(item);
            }
        return sb.toString();
    }

    private static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FA5)
                || (charValue >= 'a' && charValue <= 'z')
                || (charValue >= 'A' && charValue <= 'Z')
                || (charValue >= '0' && charValue <= '9');
    }

    private static String longestCommonSubstring(String strA, String strB) {
        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray();
        int m = chars_strA.length;
        int n = chars_strB.length;
        if (m < n) {
            chars_strA = strB.toCharArray();
            chars_strB = strA.toCharArray();
            m = chars_strA.length;
            n = chars_strB.length;
        }
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars_strA[i - 1] == chars_strB[j - 1])
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                else
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
            }
        }

        char[] result = new char[matrix[m][n]];
        int currentIndex = result.length - 1;
        while (matrix[m][n] != 0) {
            if (matrix[n] ==
                    matrix[n - 1])
                n--;
            else if (matrix[m][n] == matrix[m - 1][n])
                m--;
            else {
                result[currentIndex] = chars_strA[m - 1];
                currentIndex--;
                n--;
                m--;
            }
        }
        return new String(result);
    }

    /**
     * 〈剔除符号〉
     * 〈传入一个字符串,包括标点符号,返回一个剔除了标点符号的字符串〉
     *
     * @param source
     * @return String
     * 如果有违例，请使用@exception/throws [违例类型]   [违例说明：异常的注释必须说明该异常的含义及什么条件下抛出该
     * @see [类、类#方法、类#成员]
     */
    public String removedSymbols(String source) {

        //各种标点符号
        String reg2 = "[\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\`\\-\\=\\～\\！\\＠\\＃\\＄\\" +
                "％\\＾\\＆\\＊\\\\（\\）\\＿\\＋\\＝\\－\\｀\\[\\]\\\\'\\;\\/\\.\\,\\<\\>\\?\\:" +
                "\"\\{\\}\\|\\，\\．\\／\\；\\＇\\［\\］\\＼\\＜\\＞\\？\\：\\＂\\｛\\｝\\｜\\“\\”\\" +
                "‘\\’\\。\\r+\\n+\\t+\\s\\\" \"]";

        //剔除标点符号
        source = source.replaceAll(reg2, "");

        return source;
    }

}

package com.clicker.tantan.utils;

import android.util.Log;
import android.util.Xml;

import com.clicker.tantan.APP;
import com.clicker.tantan.db.ChatData;
import com.clicker.tantan.db.ChatDataDao;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 解析assets文件下的xlsx文档并准村到数据库
 * Created by hao on 2017/4/6.
 */

public class ParseFile {
    String filePath = "/mnt/sdcard/data.xlsx";
    private static final ParseFile ourInstance = new ParseFile();

    public static ParseFile ins() {
        return ourInstance;
    }

    private ParseFile() {
    }

    //当前状态是->存储/问题
    boolean saveQuestion = true;
    ChatData data = new ChatData();

    /**
     * 读取文件并存储到数据库
     */
    public void readXLSX() {
        String v = null;
        boolean flat = false;
        List<String> ls = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            // copy资源文件到本地目录中
            writeBytesToFile(APP.app.getAssets().open("data.xlsx"), new File(filePath));

            ZipFile xlsxFile = new ZipFile(new File(filePath));
            ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            ls.add(xmlParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamsheet = xlsxFile.getInputStream(sheetXML);
            XmlPullParser xmlParsersheet = Xml.newPullParser();
            xmlParsersheet.setInput(inputStreamsheet, "utf-8");
            int evtTypesheet = xmlParsersheet.getEventType();
            while (evtTypesheet != XmlPullParser.END_DOCUMENT) {
                switch (evtTypesheet) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParsersheet.getName();
                        if (tag.equalsIgnoreCase("row")) {
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = xmlParsersheet.getAttributeValue(null, "t");
                            if (t != null) {
                                flat = true;
                            } else {
                                flat = false;
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            v = xmlParsersheet.nextText();
                            if (v != null) {
                                if (flat) {
                                    if (saveQuestion) {
                                        saveQuestion = false;
                                        data.question = ls.get(Integer.parseInt(v));
                                    } else {
                                        saveQuestion = true;
                                        data.answer = ls.get(Integer.parseInt(v));
                                    }
                                } else {
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParsersheet.getName().equalsIgnoreCase("row")
                                && v != null) {
                            long count = APP.app.getDaoSession().getChatDataDao().count();
                            if (count > 0) {
                                data.id = count + 1;
                            } else {
                                data.id = 1;
                            }
//                            LogUtils.e("info","question: "+ data.question+" \tanswer:"+data.answer);
                            if (APP.app.getDaoSession().getChatDataDao().queryBuilder().where(ChatDataDao.Properties.Question.eq(data.question), ChatDataDao.Properties.Answer.eq(data.answer)).limit(1).unique() == null) {
                                APP.app.getDaoSession().getChatDataDao().insert(data);
                            }
                        }
                        break;
                }
                evtTypesheet = xmlParsersheet.next();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            LogUtils.e("Exception", ex.getMessage());
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}

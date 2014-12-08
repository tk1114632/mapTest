package com.example.mapTest;

import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Comparator;

/**
 * Created by tk1114632 on 12/9/14.
 */
public class PinyinComparator2 implements Comparator<ContactInfo_new> {
    public int compare(ContactInfo_new o1, ContactInfo_new o2) {
        String name1 = o1.getLastname()+o1.getFirstname();
        String name2 = o2.getLastname()+o2.getFirstname();
        String str1 = getPingYin(name1);
        String str2 = getPingYin(name2);
        int flag = str1.compareTo(str2);
        return flag;


    }
    public String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                // \\u4E00是unicode编码，判断是不是中文
                if (Character.toString(input[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    // 将汉语拼音的全拼存到temp数组
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            input[i], format);
                    // 取拼音的第一个读音
                    output += temp[0];
                }
                // 大写字母转化成小写字母
                else if (input[i] > 'A' && input[i] < 'Z') {
                    output += Character.toString(input[i]);
                    output = output.toLowerCase();
                }
                output += Character.toString(input[i]);
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return output;
    }

}

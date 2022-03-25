package org.enumUtils;

public class StringUtil {
    /**
     * 判断一个字符是否是汉字
     * PS：中文汉字的编码范围：[\u4e00-\u9fa5]
     *
     * @param c 需要判断的字符
     * @return 是汉字(true), 不是汉字(false)
     */
    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    /**
     * 判断一个字符是否是英文字母
     *
     * @param c 需要判断的字符
     * @return 是英文字母(true), 不是英文字母(false)
     */
    public static boolean isEnglishChar(char c) {
        // 对每一个字符进行判断
        if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

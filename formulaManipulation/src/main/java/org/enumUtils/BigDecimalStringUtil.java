package org.enumUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigDecimalStringUtil {

    /**
     * 数字字符串，转成BigDecima格式，兼容大数字
     * 并且保留两位小数
     * @return
     */
    public static String str2BigDecimalKeepDouble(String numStr){
        numStr = numStr.trim();
        if(!isNumeric(numStr)){
            // 不是数字，直接返回空
            return "";
        }else{
            // 是数字，那么那么就返回式子，保证有两位小数，整数前面不会出现0。
            BigDecimal bigDecimal = new BigDecimal(numStr);
            double   doubleNum   =   bigDecimal.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            /**
             * String s=String.format("%.2f",d);
             * 若double d=0.6566，输出结果为0.66；
             * 若double d=0，输出结果为0.00;
             */
            String keepTwoDecimalPlaces=String.format("%.2f",doubleNum);
            return keepTwoDecimalPlaces;
        }
    }


    /**
     * 匹配是否为数字,小数点，大数字 负述适用
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return
     * @author yutao
     * @date 2016年11月14日下午7:41:22
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}

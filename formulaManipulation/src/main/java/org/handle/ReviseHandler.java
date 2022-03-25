package org.handle;

import org.enumUtils.StringUtil;
import org.enums.ReviseEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 这个方法还不够完善，只能优化 (地面堆载_{1} \times) 或 (地面堆载_{1} -)。
 * 对于(地面堆载_{1} \times 重度 \times)没办法处理。
 */
public class ReviseHandler implements IHandler<ReviseEnum>{
    private ReviseEnum reviseEnum;
    private static HashSet<String> operand;
    static {
        operand = new HashSet<>(2);
        operand.add("\\times");
        operand.add("\\div");
    }
    @Override
    public String execute(String fromula) {
        String result = fromula;
        if(this.reviseEnum == ReviseEnum.公式修正){
            result = reviseFromula(fromula);
        }
        return result;
    }

    @Override
    public IHandler setParams(ReviseEnum reviseEnum) {
        this.reviseEnum = reviseEnum;
        return this;
    }

    /**
     * 修正不正确的计算公式和展示公式
     * (地面堆载_{1}+)*主动土压力系数_{1}-2*内聚力_{1}*根号主动土压力系数_{1}
     * 需要修正为
     * 地面堆载_{1}*主动土压力系数_{1}-2*内聚力_{1}*根号主动土压力系数_{1}
     * @param fromula
     * @return
     */
    private String reviseFromula(String fromula){
        char[] chars = fromula.toCharArray();
        int leftTimes = 0;//左括号出现的次数
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            //表明这是latex 类型的操作数，但当前只对 \times 和 \div 做处理。
            //其他的都放过
            if(chars[i] == '('){
                StringBuilder sub = new StringBuilder();
                do{
                    if(chars[i] == '('){
                        leftTimes++;
                    }
                    if(chars[i] == ')'){
                        leftTimes--;
                    }
                    sub.append(chars[i]);
                    i++;
                }while (leftTimes != 0);
                //(地面堆载_{1}+)，判断括号内是否只有一个操作数和操作符号
                String doFilter = doFilter(sub.toString());
                tempFromula.append(doFilter);
                tempFromula.append(chars[i]);
                continue;
            }
            tempFromula.append(String.valueOf(chars[i]));
        }
        return tempFromula.toString();
    }

    //(地面堆载_{1}+)，判断括号内是否只有一个操作数和操作符号
    private String doFilter(String subString){
        char[] chars = subString.toCharArray();
        StringBuilder sub1 = new StringBuilder();
        List<String> param = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' || chars[i] == ')') {
                continue;
            }
            if(chars[i] == '+' || chars[i] == '-' || chars[i] =='*' || chars[i] == '/' || chars[i]=='÷'){
                param.add(sub1.toString());
                sub1.delete(0,sub1.length());
                continue;
            }else if(chars[i] == '\\'){
                StringBuilder sub2 = new StringBuilder();
                do{
                    sub2.append(chars[i]);
                    i++;
                }while (StringUtil.isEnglishChar(chars[i]));
                String sOperation = sub2.toString();
                if(operand.contains(sOperation)){
                    param.add(sub1.toString());
                    sub1.delete(0,sub1.length());
                }
                continue;
            }
            sub1.append(chars[i]);
        }
        if(param.size() < 2){
            return param.get(0);
        }else{
            return subString;
        }
    }
}

package org.handle;

import org.enumUtils.StringUtil;
import org.enums.ReviseEnum;
import org.enums.StateMachineEnum;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
        operand.add("+");
        operand.add("-");
        operand.add("*");
        operand.add("/");
        operand.add("÷");
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
        Stack<Character> stack = new Stack<Character>();
        StringBuilder subEquation = new StringBuilder();
        //保存在[]内各部分被解析的公式，有可能[[]...[]...[]] 这种公式，需要先解析里面各个子部分
        //然后再合成一个整体
        ArrayList<String> subAnalysis = new ArrayList<String>(8);
        boolean isPush = Boolean.FALSE;
        //在对JkzhConfigEnum下 原始 latex、calculate、latexCal 展开之前做特殊处理。
        //比如：在展开[<地面堆载+>[重度*厚度+...]]*主动土压力系数-2*内聚力*math.sqrt(主动土压力系数)
        //的时候，是否在每个中文指标结束后添加_{n}标识:
        //[<地面堆载_{n}+>[重度_{n}*厚度_{n}+...]]*主动土压力系数_{n}-2*内聚力_{n}*math.sqrt(主动土压力系数_{n})
        // 这样再展开后如下：
        // 一般展开后的公式，是通过_n下标，去匹配具体的值代入。而对于不带入参数的公式，是不需要这个特殊处理的。
        for(char ch:fromula.toCharArray()){
            if(ch == '('){
                isPush = Boolean.TRUE;
                stack.push(ch);
                continue;
            }
            if (ch == ')') {
                stack.push(ch);
                //持续出栈
                StringBuilder subPart = new StringBuilder();
                do{
                    subPart.append(stack.pop());
                }while (!stack.empty() && stackConditions(ch,stack.peek()));
                //最后一个元素也出栈
                subPart.append(stack.pop());
                String analysis = subPart.reverse().toString();
                //展开计算公式
                String expansionPart = doFilter(analysis);
                subAnalysis.add(expansionPart);
                boolean isPushContinue =  stack.search('(')>0;
                if(isPushContinue){
                    isPush = Boolean.TRUE;
                }else{
                    isPush = Boolean.FALSE;
                }
                continue;
            }
            if(isPush){
                stack.push(ch);
            }else{
                if(!CollectionUtils.isEmpty(subAnalysis)){
                    for (String item : subAnalysis) {
                        subEquation.append(item);
                    }
                    subAnalysis.clear();
                }
                subEquation.append(ch);
            }
        }
        if(!CollectionUtils.isEmpty(subAnalysis)){
            for (String item : subAnalysis) {
                subEquation.append(item);
            }
            subAnalysis.clear();
        }
        return subEquation.toString();
    }

    //(地面堆载_{1}+)，判断括号内是否只有一个操作数和操作符号
    private String doFilter(String subString){
        for(int i = 1; i <50; i++){
            if(("(地面堆载_{"+i+"}+)").equals(subString)){
                return "地面堆载_{"+i+"}";
            }
        }
        return subString;
    }

    /**
     * 展开公式时，判断出栈条件
     * @param stackBottom 终止出栈字符
     * @param curItem 当前字符
     * @return
     */
    private Boolean stackConditions(char stackBottom,char curItem){
        return stackBottom == ')' ? curItem != '(' : false;
    }
}

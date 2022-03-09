package org.handle;


import lombok.extern.slf4j.Slf4j;
import org.context.IContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Stack;

@Slf4j
public class JkzhFromulaHandle extends DefaultFromulaHandle{

    /**
     * 前置处理
     * [<地面堆载+>[重度 \times 厚度+...]] \times 主动土压力系数- 2 \times 内聚力 \times \sqrt{主动土压力系数}
     * 若是当前是主动土压力或被动土压力第一层计算。那么[重度 \times 厚度+...]这一段需要去掉，不需要展开。
     * @param equation
     * @param time
     * @param beginFloor
     * @param endFloor
     * @return
     */
    public String beforHandler(String equation,
                               IContext iContext,
                               int time,
                               int beginFloor,
                               int endFloor){
        log.info("前置处理前:{}",equation);
        if(time <= 0){
            //只有计算主动土压力计算的时候，若是计算第一层计算，那么扩展部分舍弃。
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
            for(char ch:equation.toCharArray()){
                if(ch == '['){
                    isPush = Boolean.TRUE;
                    stack.push(ch);
                    continue;
                }
                if (ch == ']') {
                    stack.push(ch);
                    //持续出栈
                    StringBuilder subPart = new StringBuilder();
                    do{
                        subPart.append(stack.pop());
                    }while (!stack.empty() && stack.peek()!='[');
                    //最后一个元素也出栈
                    subPart.append(stack.pop());
                    String analysis = subPart.reverse().toString();
                    String substring = analysis.substring(1, analysis.length() - 1);
                    boolean isPushContinue =  stack.search('[')>0 || stack.search('<')>0;
                    if(isPushContinue){
                        isPush = Boolean.TRUE;
                    }else{
                        isPush = Boolean.FALSE;
                    }
                    //表明需要扩展
                    if(substring.endsWith("...")) {
                        continue;
                    }else if(substring.startsWith("<") && substring.endsWith(">")){
                        substring = substring.replaceAll("[\\+\\-\\*/]", "");
                    }
                    //展开计算公式
                    subAnalysis.add(substring);
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
            equation = subEquation.toString();
        }
        log.info("前置处理后:{}",equation);
        return equation;
    }

    /**
     * 普通公式处理，针对不需要展开的计算公式，进行参数简化包装
     * @param iContext 上下文
     * @param fromulaHandle 解析公式
     * @param curFloor 展开次数
     * @param equation 需解析的公式
     * @return
     */
    public String generalFromulaHandle(IContext iContext,
                                       FromulaHandle fromulaHandle,
                                       int curFloor,
                                       String equation,
                                       GetValues getValue){
        String calculateExpression = getCalculateExpression(iContext, fromulaHandle, curFloor, curFloor, curFloor, equation,getValue);
        return calculateExpression;
    }

    /**
     * 后置处理
     * @param equation
     * @param time
     * @param beginFloor
     * @param endFloor
     * @return
     */
    public String afterHandler(String equation,
                               IContext iContext,
                               int time,
                               int beginFloor,
                               int endFloor){
        log.info("后置处理前:{}",equation);
        equation = equation.trim();
        int skipChar = 0;
        char[] chars = equation.toCharArray();
        String result = equation;
        if(chars[0] == '+' || chars[0] == '-'||chars[0] == '*'||chars[0] == '/' || chars[0] == '\\'){
            for (int i = 1; i < equation.length(); i++) {
                if(chars[i] == '+' || chars[i] == '-'||chars[i] == '*'||chars[i] == '/') {
                    skipChar = i;
                    break;
                }
            }
            result = equation.substring(skipChar+1,equation.length());
        }
        log.info("后置处理后:{}",result);
        return result;
    }
}

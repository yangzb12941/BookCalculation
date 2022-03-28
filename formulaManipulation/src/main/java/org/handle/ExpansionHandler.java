package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 带有需展开部分的公式处理
 */
@Slf4j
public class ExpansionHandler implements IHandler<ExpansionParam>{
    //展开参数
    private ExpansionParam expansionParam;

    @Override
    public String execute(String fromula) {
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
                }while (!stack.empty() && stackConditions(ch,stack.peek()));
                //最后一个元素也出栈
                subPart.append(stack.pop());
                String analysis = subPart.reverse().toString();
                //展开计算公式
                String expansionPart = doExpansion(analysis,expansionParam.getTimes(),expansionParam.getBeginFloor());
                subAnalysis.add(expansionPart);
                boolean isPushContinue =  stack.search('[')>0;
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
        return nToIndex(subEquation.toString(),String.valueOf(expansionParam.getEndFloor()));
    }


    /**
     * 展开字符次数拼接
     * @param subEquation 需展开的字符
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @return
     */
    private String doExpansion(String subEquation,
                               int time,
                               int beginFloor){
        String substring = subEquation.substring(1, subEquation.length() - 1);
        //表明需要扩展
        if(substring.endsWith("...")){
            //获取展开公式之间的连接符 [重度*厚度+...]
            //连接符是 +
            substring = substring.substring(0,substring.length() - 3);
            StringBuilder subPart = new StringBuilder();
            for(int i = 0;i< time;i++){
                subPart.append(nToIndex(substring,String.valueOf(beginFloor+i)));
            }
            String toString = subPart.toString();
            if(!StringUtils.isEmpty(toString)){
                toString = subPart.substring(0,subPart.length() - 1);
            }
            return toString;
        }else{
            //表明不需要扩展
            return substring;
        }
    }

    /**
     * 把公式中"{}"中是否存在n,若存在则把n替换为具体的数值。
     * @param substring 源字符串
     * @param index 具体数字
     * @return
     */
    private String nToIndex(String substring,String index){
        Stack<String> stack = new Stack<String>();
        for(char ch:substring.toCharArray()){
            stack.push(String.valueOf(ch));
            if (ch == '}') {
                //持续出栈
                StringBuilder subPart = new StringBuilder();
                do{
                    String pop = stack.pop();
                    subPart.append(pop.equals("n")?index:pop);
                }while (!stack.empty() && !stack.peek().equals("{"));
                subPart.append(stack.pop());
                stack.push(subPart.toString());
            }
        }
        StringBuilder subPart = new StringBuilder();
        do{
            subPart.append(stack.pop());
        }while (!stack.empty());
        return subPart.reverse().toString();
    }

    /**
     * 展开公式时，判断出栈条件
     * @param stackBottom 终止出栈字符
     * @param curItem 当前字符
     * @return
     */
    private Boolean stackConditions(char stackBottom,char curItem){
        return stackBottom == ']' ? curItem != '[' : false;
    }

    @Override
    public ExpansionHandler setParams(ExpansionParam expansionParam) {
        this.expansionParam = expansionParam;
        return this;
    }

    /**
     * 获取当前参数
     * @return
     */
    public ExpansionParam getExpansionParam() {
        return expansionParam;
    }
}

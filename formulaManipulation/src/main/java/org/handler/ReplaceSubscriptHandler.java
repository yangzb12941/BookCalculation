package org.handler;

import org.enumUtils.StringUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Stack;

/**
 * 元素标记:
 * (载堆面地+度重 \times 度厚...)
 * ->
 * (载堆面地_{n}+度重_{n} \times 度厚_{n}...)
 */
public class ReplaceSubscriptHandler implements IHandler<String>{
    private String flagIndex;
    @Override
    public String execute(String fromula) {
        Stack<String> stack = new Stack<String>();
        StringBuilder subEquation = new StringBuilder();
        boolean isPush = Boolean.FALSE;
        for(char ch:fromula.toCharArray()){
            if(ch == '{'){
                isPush = Boolean.TRUE;
                stack.push(String.valueOf(ch));
                continue;
            }
            if (ch == '}') {
                stack.push(String.valueOf(ch));
                //持续出栈
                StringBuilder subPart = new StringBuilder();
                do{
                    subPart.append(stack.pop());
                }while (!stack.isEmpty() && stackConditions(ch,stack.peek()));
                //最后一个元素也出栈
                subPart.append(stack.pop());
                String analysis = subPart.reverse().toString();
                //展开计算公式
                String expansionPart = nToIndex(analysis,flagIndex);
                stack.push((new StringBuilder(expansionPart)).reverse().toString());
                boolean isPushContinue =  stack.contains("{");
                if(isPushContinue){
                    isPush = Boolean.TRUE;
                }else{
                    isPush = Boolean.FALSE;
                    if(!stack.isEmpty()){
                        subEquation.append((new StringBuilder(stack.pop())).reverse().toString());
                    }
                }
                continue;
            }
            if(isPush){
                stack.push(String.valueOf(ch));
            }else{
                subEquation.append(ch);
            }
        }
        return nToIndex(subEquation.toString(),flagIndex);
    }

    @Override
    public ReplaceSubscriptHandler setParams(String flagIndex) {
        this.flagIndex = flagIndex;
        return this;
    }

    /**
     * 展开公式时，判断出栈条件
     * @param stackBottom 终止出栈字符
     * @param curItem 当前字符
     * @return
     */
    private Boolean stackConditions(char stackBottom,String curItem){
        return stackBottom == '}' ? !curItem.equals("{") : false;
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
}

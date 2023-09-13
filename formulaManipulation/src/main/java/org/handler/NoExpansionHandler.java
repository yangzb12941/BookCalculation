package org.handler;

import lombok.extern.slf4j.Slf4j;
import org.entity.NoExpansionParam;

import java.util.Stack;

/**
 * 不需要展开的公式处理
 */
@Slf4j
public class NoExpansionHandler implements IHandler<NoExpansionParam>{
    //展开参数
    private NoExpansionParam noExpansionParam;

    @Override
    public String execute(String fromula) {
        //字符串扩展处理
        char[] chars = fromula.toCharArray();
        boolean isPush = Boolean.FALSE;
        Stack<String> stack = new Stack<String>();
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '['){
                isPush = Boolean.TRUE;
                stack.push(String.valueOf(chars[i]));
                continue;
            }

            if(chars[i] == ']' && isPush){
                if(stack.isEmpty()){
                    isPush = Boolean.FALSE;
                    continue;
                }else{
                    StringBuilder sub = new StringBuilder();
                    do{
                        sub.append(stack.pop());
                    }while (!stack.peek().equals("["));
                    stack.pop();
                    String sExpansion = sub.toString();
                    String subString = doExpansion(sExpansion,this.noExpansionParam.getCurFloor());
                    stack.push(subString);
                    if(stack.isEmpty()){
                        isPush = Boolean.FALSE;
                        continue;
                    }else{
                        continue;
                    }
                }
            }
            if(isPush){
                stack.push(String.valueOf(chars[i]));
                continue;
            }
            tempFromula.append(chars[i]);
        }
        if(!stack.isEmpty()){
            StringBuilder sub = new StringBuilder();
            do{
                sub.append(stack.pop());
            }while (!stack.isEmpty());
            StringBuilder reverse = sub.reverse();
            tempFromula.append(reverse);
        }
        String result = doExpansion(tempFromula.toString(),this.noExpansionParam.getCurFloor());
        log.info("展开:{}",result);
        return result;
    }

    /**
     * 展开字符次数拼接
     * @param subEquation 需展开的字符
     * @param curFloor 当前层
     * @return
     */
    private String doExpansion(String subEquation,
                               int curFloor){
        String nToIndex = nToIndex(subEquation, String.valueOf(curFloor));
        return nToIndex;
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

    @Override
    public NoExpansionHandler setParams(NoExpansionParam noExpansionParam) {
        this.noExpansionParam = noExpansionParam;
        return this;
    }

    public NoExpansionParam getNoExpansionParam() {
        return noExpansionParam;
    }
}

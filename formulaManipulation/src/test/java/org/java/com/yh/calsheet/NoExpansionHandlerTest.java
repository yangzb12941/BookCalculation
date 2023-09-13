package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.NoExpansionParam;
import org.junit.Test;

import java.util.Stack;

@Slf4j
public class NoExpansionHandlerTest {
    @Test
    public void execute() {
        NoExpansionParam noExpansionParam = new NoExpansionParam(3);
        String fromula = "math.pow(math.tan(math.toRadians(45+内摩擦角_{n}/2)),2)";
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
                    String subString = doExpansion(sExpansion,noExpansionParam.getCurFloor());
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
        String result = doExpansion(tempFromula.toString(),noExpansionParam.getCurFloor());
        log.info("展开:{}",result);
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
}

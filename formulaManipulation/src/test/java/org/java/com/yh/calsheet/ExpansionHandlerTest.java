package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.enums.ConditionEnum;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Stack;

@Slf4j
public class ExpansionHandlerTest {
    @Test
    public void execute() {
        //判断是否存在堆载，有堆载则需要进行堆载计算
        char[] chars = "<③(<①地面堆载+>[重度*厚度+...]<②-([厚度+...]-基坑外水位)*水常量>)*主动土压力系数->2*内聚力*根号主动土压力系数<②+([厚度+...]-基坑外水位)*水常量>".toCharArray();
        boolean isPush = Boolean.FALSE;
        Stack<String> flagStack = new Stack<String>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '<'){
                if(String.valueOf(chars[i+1]).equals(ConditionEnum.地面堆载.getValue())){
                    isPush = Boolean.TRUE;
                }
                //<③ 入标识栈
                if(isPush){
                    deque.push(chars[i]);
                    String flagS = chars[i] + String.valueOf(chars[i+1]);
                    flagStack.push(flagS);
                    continue;
                }
            }
            if(chars[i] == '>' && isPush){
                if(flagStack.isEmpty()){
                    throw new RuntimeException("< is not match at index "+i);
                }
                String pop = flagStack.pop();
                //表明已经是匹配上了<③...>
                if(("<"+ConditionEnum.地面堆载.getValue()+">").equals(pop+">")){
                    //需要根据条件判断是否需要保留这部分公式
                    if(Boolean.FALSE){
                        StringBuilder sub = new StringBuilder();
                        do{
                            Character character = deque.pollLast();
                            if(String.valueOf(deque.peekLast()).equals(ConditionEnum.地面堆载.getValue()) && character == '<'
                                    || String.valueOf(character).equals(ConditionEnum.地面堆载.getValue())){

                            }else{
                                sub.append(character);
                            }
                        }while (!String.valueOf(deque.peekFirst()).equals(ConditionEnum.地面堆载.getValue()) && !deque.isEmpty());
                        tempFromula.append(sub);
                        isPush = Boolean.FALSE;
                        continue;
                    }else{
                        isPush = Boolean.FALSE;
                        continue;
                    }
                }else{
                    deque.push(chars[i]);
                    continue;
                }
            }
            if(isPush){
                deque.push(chars[i]);
                continue;
            }
            tempFromula.append(chars[i]);
        }
        log.info("展开:{}",tempFromula.toString());
    }

    @Test
    public void expansionHandlerTest() {
        //判断是否存在堆载，有堆载则需要进行堆载计算
        char[] chars = "(<①地面堆载+>[重度*厚度+...]<②-([厚度+...]-[基坑外水位])*水常量>)*主动土压力系数-2*内聚力*根号主动土压力系数<②+([厚度+...]-[基坑外水位])*水常量>".toCharArray();
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
                    String s1 = doExpansion(sExpansion,2,2);
                    stack.push(s1);
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
        log.info("展开:{}",tempFromula.toString());
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
        String substring = subEquation;
        //表明需要扩展
        if(substring.startsWith("...")){
            //获取展开公式之间的连接符 [重度*厚度+...]
            //连接符是 +
            substring = substring.substring(3,substring.length());
            StringBuilder subPart = new StringBuilder();
            for(int i = 0;i< time;i++){
                subPart.append(nToIndex(substring,String.valueOf(beginFloor+i)));
            }
            return subPart.substring(1,subPart.length());
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
}

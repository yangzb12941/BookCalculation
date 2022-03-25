package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
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
                    String sExpansion = sub.reverse().toString();
                    String subString = doExpansion(sExpansion,
                            this.expansionParam.getTimes(),
                            this.expansionParam.getBeginFloor(),
                            this.expansionParam.getEndFloor());
                    if(!StringUtils.isEmpty(subString)){
                        stack.push(subString);
                    }
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
        String result = tempFromula.toString();
        log.info("展开:{}",result);
        return nToIndexOrder(result,String.valueOf(this.expansionParam.getEndFloor()));
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
                               int beginFloor,
                               int endFloor){
        String substring = subEquation;
        //表明需要扩展
        if(substring.endsWith("...")){
            //获取展开公式之间的连接符 [重度*厚度+...]
            //连接符是 +
            substring = substring.substring(0,substring.length()-3);
            StringBuilder subPart = new StringBuilder();
            int i = 0;
            while (i< time) {
                subPart.append(nToIndexOrder(substring,String.valueOf(beginFloor+i)));
                i++;
            }
            if(StringUtils.isEmpty(subPart.toString())){
                return "";
            }
            substring = subPart.substring(0,subPart.length()-1);
        }else{
            substring = nToIndexOrder(substring, String.valueOf(endFloor));
        }
        return substring;
    }

    /**
     * 把公式中"{}"中是否存在n,若存在则把n替换为具体的数值。
     * @param substring 源字符串
     * @param index 具体数字
     * @return
     */
    private String nToIndexOrder(String substring,String index){
        Stack<Character> stack = new Stack<Character>();
        StringBuilder temp = new StringBuilder();
        Boolean isPush = Boolean.FALSE;
        for(char ch:substring.toCharArray()){
            if (ch == '{') {
                stack.push(ch);
                isPush = Boolean.TRUE;
                //持续出栈
                continue;
            }
            if(ch == '}'){
                stack.push(ch);
                StringBuilder subPart = new StringBuilder();
                while (!stack.isEmpty()){
                    char pop = stack.pop();
                    subPart.append(pop == 'n'?index:pop);
                }
                temp.append(subPart.reverse().toString());
                isPush = Boolean.FALSE;
                continue;
            }
            if(isPush){
                stack.push(ch);
                continue;
            }
            temp.append(ch);
        }
        return temp.toString();
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

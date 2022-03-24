package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import java.util.Stack;

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
                    String sExpansion = sub.toString();
                    String subString = doExpansion(sExpansion,this.expansionParam.getExpansionTimes(),this.expansionParam.getBeginFloor());
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
        String result = tempFromula.toString();
        log.info("展开:{}",result);
        return result;
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

    @Override
    public void setParams(ExpansionParam expansionParam) {
        this.expansionParam = expansionParam;
    }
}

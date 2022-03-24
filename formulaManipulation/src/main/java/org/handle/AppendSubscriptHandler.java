package org.handle;

import org.enumUtils.StringUtil;

import java.util.ArrayDeque;

/**
 * 元素标记:
 * (载堆面地+度重 \times 度厚...)
 * ->
 * (载堆面地_{n}+度重_{n} \times 度厚_{n}...)
 */
public class AppendSubscriptHandler implements IHandler<String>{
    private String flagIndex;
    @Override
    public String execute(String fromula) {
        StringBuilder result = new StringBuilder();
        ArrayDeque<String> deque = new ArrayDeque<String>();
        for(char ch:fromula.toCharArray()){
            if(StringUtil.isChineseChar(ch)){
                deque.push(String.valueOf(ch));
            }else{
                if(deque.isEmpty()){
                    result.append(ch);
                }else{
                    do{
                        result.append(deque.pollLast());
                    }while (!deque.isEmpty());
                    result.append(flagIndex);
                    result.append(ch);
                }
            }
        }
        if(!deque.isEmpty()){
            do{
                result.append(deque.pollLast());
            }while (!deque.isEmpty());
            result.append(flagIndex);
        }
        return result.toString();
    }

    @Override
    public void setParams(String flagIndex) {
        this.flagIndex = flagIndex;
    }
}

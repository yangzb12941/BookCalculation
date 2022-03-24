package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.enums.ConditionEnum;
import org.table.JkzhBasicParam;

import java.util.ArrayDeque;
import java.util.Stack;

@Slf4j
public class FirstFloorHandler implements IHandler<JkzhBasicParam>{
    //展开参数
    private JkzhBasicParam jkzhBasicParam;

    @Override
    public String execute(String fromula) {
        //判断是否存在堆载，有堆载则需要进行堆载计算
        char[] chars = fromula.toCharArray();
        boolean isPush = Boolean.FALSE;
        Stack<String> flagStack = new Stack<String>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '<'){
                if(String.valueOf(chars[i+1]).equals(ConditionEnum.首层土计算.getValue())){
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
                if(("<"+ConditionEnum.首层土计算.getValue()+">").equals(pop+">")){
                    //需要根据条件判断是否需要保留这部分公式
                    if(Boolean.FALSE){
                        StringBuilder sub = new StringBuilder();
                        do{
                            Character character = deque.pollLast();
                            if(String.valueOf(deque.peekLast()).equals(ConditionEnum.首层土计算.getValue()) && character == '<'
                                    || String.valueOf(character).equals(ConditionEnum.首层土计算.getValue())){

                            }else{
                                sub.append(character);
                            }
                        }while (!String.valueOf(deque.peekFirst()).equals(ConditionEnum.首层土计算.getValue()) && !deque.isEmpty());
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
        String sResult = tempFromula.toString();
        log.info("SurchargeHandler execute:{}",sResult);
        return sResult;
    }

    @Override
    public void setParams(JkzhBasicParam jkzhBasicParam) {
        this.jkzhBasicParam = jkzhBasicParam;
    }
}

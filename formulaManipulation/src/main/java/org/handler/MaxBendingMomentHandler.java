package org.handler;

import lombok.extern.slf4j.Slf4j;
import org.calParam.JkzhBasicParam;
import org.enums.ConditionEnum;
import org.handleParams.MaxBendingMomentParams;

import java.util.ArrayDeque;
import java.util.Stack;

@Slf4j
public class MaxBendingMomentHandler implements IHandler<MaxBendingMomentParams>{
    private MaxBendingMomentParams maxBendingMomentParams;

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
                if(String.valueOf(chars[i+1]).equals(ConditionEnum.最大弯矩位置.getValue())
                ||String.valueOf(chars[i+1]).equals(ConditionEnum.最大弯矩值.getValue())){
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
                if(("<"+ConditionEnum.最大弯矩位置.getValue()+">").equals(pop+">")
                ||("<"+ConditionEnum.最大弯矩值.getValue()+">").equals(pop+">")){
                    //需要根据条件判断是否需要保留这部分公式
                    if(maxBendingMomentParams.getTcAtLand()>=maxBendingMomentParams.getLastDepthLand()){
                        StringBuilder sub = new StringBuilder();
                        do{
                            Character character = deque.pollLast();
                            if(
                                    (String.valueOf(deque.peekLast()).equals(ConditionEnum.最大弯矩位置.getValue()) && character == '<'
                                    || String.valueOf(character).equals(ConditionEnum.最大弯矩位置.getValue()))
                                    || (String.valueOf(deque.peekLast()).equals(ConditionEnum.最大弯矩值.getValue()) && character == '<'
                                    || String.valueOf(character).equals(ConditionEnum.最大弯矩值.getValue()))
                            ){

                            }else{
                                sub.append(character);
                            }
                        }while ((!String.valueOf(deque.peekFirst()).equals(ConditionEnum.最大弯矩位置.getValue())
                                || !String.valueOf(deque.peekFirst()).equals(ConditionEnum.最大弯矩值.getValue()))
                                && !deque.isEmpty());
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
        return sResult;
    }

    @Override
    public MaxBendingMomentHandler setParams(MaxBendingMomentParams maxBendingMomentParams) {
        this.maxBendingMomentParams = maxBendingMomentParams;
        return this;
    }
}

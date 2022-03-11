package org.handle;


import lombok.extern.slf4j.Slf4j;
import org.context.IContext;
import org.enumUtils.StringUtil;
import org.getValue.GetValues;
import org.show.ILayout;

import java.util.ArrayDeque;

@Slf4j
public class DefaultFromulaHandle extends FromulaHandle{


    /**
     * 获取可计算的数学表达式
     * @param iContext 上下文
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param equation 需解析的公式
     * @return
     */
    public String getCalculateExpression(IContext iContext,
                                         FromulaHandle fromulaHandle,
                                         int time,
                                         int beginFloor,
                                         int endFloor,
                                         String equation,
                                         GetValues getValue){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",equation,time,beginFloor,endFloor);
        //前置处理
        String bhString = beforHandler(equation,iContext,time,beginFloor,endFloor);
        //处理计算结果的公式
        String target = fromulaHandle.expansionEquation(bhString,time,beginFloor,endFloor);
        //后置处理
        String ahString = afterHandler(target,iContext,time,beginFloor,endFloor);
        log.info("cal展开:{}",ahString);
        String[] values = getValue.getValues(iContext, ahString);
        String placeholder = fromulaHandle.placeholder(ahString);
        log.info("cal占位:{}",placeholder);
        String fillingCal = fromulaHandle.valuesFillCal(placeholder, values);
        log.info("cal填充:{}",fillingCal);
        return fillingCal;
    }

    /**
     * 获取在word展示的Latex表达式。
     * @param iContext 上下文
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param iLayout 自定义替换显示字符
     * @param equation 公式字符串
     * @return
     */
    public String getLatexExpression(IContext iContext,
                                     FromulaHandle fromulaHandle,
                                     int time,
                                     int beginFloor,
                                     int endFloor,
                                     ILayout iLayout,
                                     String equation){
        log.info("latex入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",equation,time,beginFloor,endFloor);
        //前置处理
        String bhString = beforHandler(equation,iContext, time,beginFloor,endFloor);
        //处理计算结果的公式
        String target = fromulaHandle.replaceLayoutChar(bhString, iLayout);
        log.info("latex替换:{}",target);
        String expansion = fromulaHandle.expansionEquation(target,time,beginFloor,endFloor);
        //后置处理
        String ahString = afterHandler(expansion,iContext,time,beginFloor,endFloor);
        log.info("latex展开:{}",ahString);
        return ahString;
    }

    /**
     * 获取可计算的数学表达式
     * @param iContext 上下文
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param iLayout 自定义替换显示字符
     * @param equation 公式字符串
     * @return
     */
    public String getLatexCalExpression(IContext iContext,
                      FromulaHandle fromulaHandle,
                      int time,
                      int beginFloor,
                      int endFloor,
                      ILayout iLayout,
                      String equation,
                      GetValues getValue){
        log.info("latexCal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",equation,time,beginFloor,endFloor);
        //前置处理
        String bhString = beforHandler(equation,iContext, time,beginFloor,endFloor);
        //处理计算结果的公式
        String target = fromulaHandle.expansionEquation(bhString,time,beginFloor,endFloor);
        //后置处理
        String ahString = afterHandler(target,iContext,time,beginFloor,endFloor);
        log.info("latexCal展开:{}",ahString);
        String[] values = getValue.getValues(iContext, ahString);
        String placeholder = fromulaHandle.placeholder(ahString);
        log.info("latexCal占位:{}",placeholder);
        String fillingCal = fromulaHandle.valuesFillCal(placeholder, values);
        log.info("latexCal填充:{}",fillingCal);
        return fillingCal;
    }

    public String preProcessing(String source){
        StringBuilder result = new StringBuilder();
        ArrayDeque<String> deque = new ArrayDeque<String>();
        for(char ch:source.toCharArray()){
            if(StringUtil.isChineseChar(ch)){
                deque.push(String.valueOf(ch));
            }else{
                if(deque.isEmpty()){
                    result.append(ch);
                }else{
                    do{
                        result.append(deque.pollLast());
                    }while (!deque.isEmpty());
                    result.append("_{n}");
                    result.append(ch);
                }
            }
        }
        if(!deque.isEmpty()){
            do{
                result.append(deque.pollLast());
            }while (!deque.isEmpty());
            result.append("_{n}");
        }
        return result.toString();
    }

    /**
     * 前置处理
     * @param equation
     * @param time
     * @param beginFloor
     * @param endFloor
     * @return
     */
    public String beforHandler(String equation,
                               IContext iContext,
                               int time,
                               int beginFloor,
                               int endFloor){
        log.info("前置处理前:{}",equation);
        String result = equation;
        log.info("前置处理后:{}",result);
        return result;
    }

    /**
     * 后置处理
     * @param equation
     * @param time
     * @param beginFloor
     * @param endFloor
     * @return
     */
    public String afterHandler(String equation,
                               IContext iContext,
                               int time,
                               int beginFloor,
                               int endFloor){
        log.info("后置处理前:{}",equation);
        String result = equation;
        log.info("后置处理后:{}",result);
        return result;
    }
}

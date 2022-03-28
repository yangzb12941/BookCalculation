package org.handle;


import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.constant.Constant;
import org.context.AbstractContext;
import org.entity.ExpansionParam;
import org.enumUtils.StringUtil;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.GetValues;
import org.getValue.JkzhGetValues;
import org.show.ILayout;

import java.util.ArrayDeque;

@Slf4j
public class DefaultFromulaHandle extends FromulaHandle {

    /**
     * 土压力计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param waterWhichEnum 水土合算、水土分算
     * @param jkzhGetValues 获取值的方式
     * @return
     */
    public String soilPressureToCal(FromulaHandle fromulaHandle,
                                    int time,
                                    int beginFloor,
                                    int endFloor,
                                    JkzhConfigEnum jkzhConfigEnum,
                                    WaterWhichEnum waterWhichEnum,
                                    JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(time,beginFloor,endFloor);
        FromulaEntity fromulaEntity = fromulaHandle.soilPressureToCal(jkzhGetValues, jkzhConfigEnum.getCalculate(), waterWhichEnum,expansionParam);
        String fillingCal = fromulaEntity.compile();
        return fillingCal;
    }

    /**
     * 土压力计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param waterWhichEnum 水土合算、水土分算
     * @param jkzhGetValues 获取值的方式
     * @return
     */
    public String soilPressureToLatex(FromulaHandle fromulaHandle,
                                      int time,
                                      int beginFloor,
                                      int endFloor,
                                      JkzhConfigEnum jkzhConfigEnum,
                                      WaterWhichEnum waterWhichEnum,
                                      JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(time,beginFloor,endFloor);
        FromulaEntity fromulaEntity = fromulaHandle.soilPressureToLatex(jkzhGetValues, jkzhConfigEnum.getLatexCal(), waterWhichEnum,expansionParam);
        String fillingLatex = fromulaEntity.compile();
        return fillingLatex;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param leftFromula 左公式
     * @param rightFromula 右公式
     * @return
     */
    public String solveEquationsToCal(FromulaHandle fromulaHandle,String leftFromula,String rightFromula){
        log.info("solveEquationsToCal:左公式{},右公式{}",leftFromula,rightFromula);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = fromulaHandle.solveEquationsToCal(leftFromula, rightFromula);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param curFloor 当前层
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCal(FromulaHandle fromulaHandle,
                              int curFloor,
                              JkzhConfigEnum jkzhConfigEnum,
                              JkzhGetValues jkzhGetValues){
        log.info("extendToCal入参:公式{},当前层:{}",jkzhConfigEnum,curFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(curFloor,curFloor,curFloor);
        FromulaEntity fromulaEntity = fromulaHandle.extendToCal(jkzhGetValues, jkzhConfigEnum.getCalculate(),expansionParam);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param curFloor 当前层
     * @param fromula 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCal(FromulaHandle fromulaHandle,
                              int curFloor,
                              String fromula,
                              JkzhGetValues jkzhGetValues){
        log.info("extendToCal入参:公式{},当前层:{}",fromula,curFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(curFloor,curFloor,curFloor);
        FromulaEntity fromulaEntity = fromulaHandle.extendToCal(jkzhGetValues, fromula,expansionParam);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param curFloor 当前层
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToLatex(FromulaHandle fromulaHandle,
                                int curFloor,
                                JkzhConfigEnum jkzhConfigEnum,
                                JkzhGetValues jkzhGetValues){
        log.info("extendToLatex入参:公式{},当前层:{}",jkzhConfigEnum,curFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(curFloor,curFloor,curFloor);
        FromulaEntity fromulaEntity = fromulaHandle.extendToLatex(jkzhGetValues, jkzhConfigEnum.getLatexCal(),expansionParam);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToCalN(FromulaHandle fromulaHandle,
                               int time,
                               int beginFloor,
                               int endFloor,
                              JkzhConfigEnum jkzhConfigEnum,
                              JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(time,beginFloor,endFloor);
        FromulaEntity fromulaEntity = fromulaHandle.extendToCal(jkzhGetValues, jkzhConfigEnum.getCalculate(),expansionParam);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 获取可计算的数学表达式
     * @param fromulaHandle 解析公式
     * @param time 展开次数
     * @param beginFloor 第几层开始
     * @param endFloor 第几层开始
     * @param jkzhConfigEnum 被解析的基础公式
     * @param jkzhGetValues 获取值
     * @return
     */
    public String extendToLatexN(FromulaHandle fromulaHandle,
                                 int time,
                                 int beginFloor,
                                 int endFloor,
                                JkzhConfigEnum jkzhConfigEnum,
                                JkzhGetValues jkzhGetValues){
        log.info("cal入参:公式{},展开次数:{},当前第{}层开始,第几{}层结束",jkzhConfigEnum,time,beginFloor,endFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(time,beginFloor,endFloor);
        FromulaEntity fromulaEntity = fromulaHandle.extendToLatex(jkzhGetValues, jkzhConfigEnum.getLatexCal(),expansionParam);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     *
     * @param fromulaHandle 解析公式
     * @param fromula 公式字符串
     * @param curFloor 当前层
     * @param iLayout 公式展示字符集
     * @return
     */
    public String replaceExtendToCal(FromulaHandle fromulaHandle,
                                     String fromula,
                                     int curFloor,
                                     ILayout iLayout){
        log.info("extendToCal入参:公式{},当前层:{}",fromula,curFloor);
        //处理计算结果的公式
        ExpansionParam expansionParam = new ExpansionParam(curFloor,curFloor,curFloor);
        FromulaEntity fromulaEntity = fromulaHandle.replaceExtendToCal(fromula,expansionParam,iLayout);
        String compile = fromulaEntity.compile();
        return compile;
    }

    /**
     * 固定公式展示处理器：
     * 1、公式替换元素处理器
     * @param fromula 解析公式
     * @param iLayout 公式展示字符集
     * @return
     */
    public String replaceLayoutChar(FromulaHandle fromulaHandle,String fromula,ILayout iLayout){
        log.info("extendToCal入参:公式{},",fromula);
        //处理计算结果的公式
        FromulaEntity fromulaEntity = fromulaHandle.replaceLayoutChar(fromula,iLayout);
        String compile = fromulaEntity.compile();
        return compile;
    }
}

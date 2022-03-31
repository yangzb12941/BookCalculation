package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.calParam.CalResult;
import org.calculation.JkzhCalculation;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.constant.Constant;
import org.context.JkzhContext;
import org.context.JkzhContextFactory;
import org.element.FormulaElement;
import org.element.TextElement;
import org.entity.ExpansionParam;
import org.enums.ReviseEnum;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handleParams.FirstFloorHandlerParam;
import org.handleParams.WaterHandlerParams;
import org.handler.*;
import org.junit.Test;
import org.show.JkzhCalTemporaryPart;
import org.calParam.JkzhBasicParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class ZDPressureTest {
    private static HashSet<String> operand;
    static {
        operand = new HashSet<>(6);
        operand.add("+");
        operand.add("-");
        operand.add("*");
        operand.add("/");
        operand.add("\\times");
        operand.add("\\div");
    }

    private JkzhContext jkzhContext;
    @Test
    public void execute() {
        List<JkzhBasicParam> jkzhBasicParams = createJkzhBasicParam();
        final JkzhContext jkzhContext = JkzhContextFactory.getJkzhContext(jkzhBasicParams, createTable());
        jkzhContext.refresh(1);
        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhContext);
        JkzhCalTemporaryPart jkzhCalTemporaryPart = new JkzhCalTemporaryPart();
        FromulaEntity fromulaToCal = creatFromulaToCal(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        FromulaEntity fromulaToLatex = creatFromulaToLatex(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        int layer = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getAllLands();
        //②、计算主动土压力强度
        for(int i = 1; i <= layer; i++){
            ExpansionHandler handlerLatexUp = (ExpansionHandler) fromulaToLatex.getHandler(ExpansionHandler.class);
            ExpansionParam paramUp = handlerLatexUp.getExpansionParam();
            paramUp.setTimes(i-1);
            paramUp.setBeginFloor(1);
            paramUp.setEndFloor(i);
            String latexCalUp = fromulaToLatex.compile();

            ExpansionHandler handlerCalUp = (ExpansionHandler) fromulaToCal.getHandler(ExpansionHandler.class);
            ExpansionParam paramCalUp = handlerCalUp.getExpansionParam();
            paramCalUp.setTimes(i-1);
            paramCalUp.setBeginFloor(1);
            paramCalUp.setEndFloor(i);
            String calUp = fromulaToCal.compile();

            this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力上" + i, calUp);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力上" + i, new FormulaElement(i, jkzhCalTemporaryPart, "主动土压力上", latexCalUp + "=" + calUp + "kPa"));
            log.info("主动土压力第{}层展示公式—上:{}={}", i, latexCalUp, calUp);

            paramUp.setTimes(i);
            paramUp.setBeginFloor(1);
            paramUp.setEndFloor(i);
            String latexCalDown = fromulaToLatex.compile();

            paramCalUp.setTimes(i-1);
            paramCalUp.setBeginFloor(1);
            paramCalUp.setEndFloor(i);
            String zdCalRtDown = fromulaToCal.compile();
            log.info("主动土压力第{}层展示公式-下:{}={}",i,latexCalDown,zdCalRtDown);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土层"+i,new TextElement(i,"主动土层",String.valueOf(i)));
            this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+i,zdCalRtDown);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+i,new FormulaElement(i, jkzhCalTemporaryPart,"主动土压力下",latexCalDown+"="+zdCalRtDown +"kPa"));
        }
    }
    private FromulaEntity creatFromulaToCal(JkzhGetValueModelEnum jkzhGetValueModelEnum,JkzhConfigEnum jkzhConfigEnum,WaterWhichEnum waterWhichEnum){
        JkzhGetValues jkzhGetValues = new JkzhGetValues(jkzhGetValueModelEnum,this.jkzhContext);
        //用于计算结果
        FromulaEntity calFromulaEntity = new FromulaEntity(jkzhConfigEnum.getCalculate());
        calFromulaEntity
                //添加首层土判断处理器
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()),0,0,jkzhConfigEnum)))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes())))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.jkzhContext.getSoilQualityTable(),this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()), waterWhichEnum)))
                //添加元素标记处理器
                .addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
                //添加展开公式处理器
                .addHandler(new ExpansionHandler().setParams(new ExpansionParam(0,0,0)))
                //添加公式修正处理器
                .addHandler(new ReviseHandler().setParams(ReviseEnum.公式修正))
                //添加值填充处理器
                .addHandler(new FillValueHandler().setParams(jkzhGetValues))
                //添加值填充处理器
                .addHandler(new CalHandler());
        return calFromulaEntity;
    }

    private FromulaEntity creatFromulaToLatex(JkzhGetValueModelEnum jkzhGetValueModelEnum,JkzhConfigEnum jkzhConfigEnum,WaterWhichEnum waterWhichEnum){
        JkzhGetValues jkzhGetValues = new JkzhGetValues(jkzhGetValueModelEnum,this.jkzhContext);
        //用于word展示
        FromulaEntity latexFromulaEntity = new FromulaEntity(JkzhConfigEnum.主动土压力.getLatexCal());
        latexFromulaEntity
                //添加首层土判断处理器
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()),0,0,jkzhConfigEnum)))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes())))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.jkzhContext.getSoilQualityTable(),this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()), waterWhichEnum)))
                //添加元素标记处理器
                .addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
                //添加展开公式处理器
                .addHandler(new ExpansionHandler().setParams(new ExpansionParam(0,0,0)))
                //添加公式修正处理器
                .addHandler(new ReviseHandler().setParams(ReviseEnum.公式修正))
                //添加值填充处理器
                .addHandler(new FillValueHandler().setParams(jkzhGetValues));
        return latexFromulaEntity;
    }

    private List<JkzhBasicParam> createJkzhBasicParam(){
        List<JkzhBasicParam> jkzhBasicParams = new ArrayList<>();
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        jkzhBasicParam.setSurcharge(20.0);
        jkzhBasicParam.setAxis(0.4);
        jkzhBasicParam.setDepth(7.0);
        jkzhBasicParam.setZDWarterDepth(2.5);
        jkzhBasicParam.setBDWarterDepth(10.5);
        jkzhBasicParam.setWaterConstant(20.0);
        CalResult calResult = new CalResult();
        jkzhBasicParam.setCalResult(calResult);
        jkzhBasicParam.setCalTimes(1);
        jkzhBasicParams.add(jkzhBasicParam);
        return jkzhBasicParams;
    }

    private String[][] createTable(){
        //土压力系数头
        String[][] table = {
                {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","黏聚力(kPa)\nc","内摩擦角(°)\nΨ","计算方式"},
                {"1", "人工填土","1.2","18.0","5.7","13.5","水土合算"},
                {"2", "淤泥质粉质黏土","5","17.8","8.2","9.6","水土合算"},
                {"3", "粉质黏土","3.8","20.0","14","16.2","水土分算"},
                {"4", "黏性土","7.4","20.5","22","20.8","水土合算"},
        };
        return table;
    }
}

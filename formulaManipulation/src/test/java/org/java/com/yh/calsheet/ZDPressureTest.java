package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.calculation.JkzhCalculation;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.constant.Constant;
import org.context.JkzhContext;
import org.element.FormulaElement;
import org.element.TextElement;
import org.entity.ExpansionParam;
import org.enums.ReviseEnum;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handle.*;
import org.handleParams.FirstFloorHandlerParam;
import org.handleParams.WaterHandlerParams;
import org.junit.Test;

import java.util.HashSet;

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
        JkzhCalculation jkzhCalculation = new JkzhCalculation();
        this.jkzhContext = (JkzhContext) jkzhCalculation.getContext(jkzhCalculation);
        FromulaEntity fromulaToCal = creatFromulaToCal(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        FromulaEntity fromulaToLatex = creatFromulaToLatex(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        int layer = this.jkzhContext.getJkzhBasicParam().getAllLands();
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

            this.jkzhContext.getTemporaryValue().put("主动土压力上" + i, calUp);
            this.jkzhContext.getElementTemplate().put("主动土压力上" + i, new FormulaElement(i, "主动土压力上", latexCalUp + "=" + calUp + "kPa"));
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
            this.jkzhContext.getElementTemplate().put("主动土层"+i,new TextElement(i,"主动土层",String.valueOf(i)));
            this.jkzhContext.getTemporaryValue().put("主动土压力下"+i,zdCalRtDown);
            this.jkzhContext.getElementTemplate().put("主动土压力下"+i,new FormulaElement(i,"主动土压力下",latexCalDown+"="+zdCalRtDown +"kPa"));
        }
    }
    private FromulaEntity creatFromulaToCal(JkzhGetValueModelEnum jkzhGetValueModelEnum,JkzhConfigEnum jkzhConfigEnum,WaterWhichEnum waterWhichEnum){
        JkzhGetValues jkzhGetValues = new JkzhGetValues(jkzhGetValueModelEnum,this.jkzhContext);
        //用于计算结果
        FromulaEntity calFromulaEntity = new FromulaEntity(jkzhConfigEnum.getCalculate());
        calFromulaEntity
                //添加首层土判断处理器
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.jkzhContext.getJkzhBasicParam())))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.jkzhContext.getJkzhBasicParam()))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.jkzhContext.getSoilQualityTable(),this.jkzhContext.getJkzhBasicParam(), waterWhichEnum)))
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
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.jkzhContext.getJkzhBasicParam())))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.jkzhContext.getJkzhBasicParam()))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.jkzhContext.getSoilQualityTable(),this.jkzhContext.getJkzhBasicParam(), waterWhichEnum)))
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
}

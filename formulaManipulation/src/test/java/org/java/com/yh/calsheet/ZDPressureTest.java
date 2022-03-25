package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.calculation.JkzhCalculation;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.constant.Constant;
import org.context.JkzhContext;
import org.element.FormulaElement;
import org.entity.ExpansionParam;
import org.enumUtils.StringUtil;
import org.enums.ReviseEnum;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handle.*;
import org.handleParams.FirstFloorHandlerParam;
import org.handleParams.WaterHandlerParams;
import org.junit.Test;

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
        JkzhCalculation jkzhCalculation = new JkzhCalculation();
        this.jkzhContext = (JkzhContext) jkzhCalculation.getContext(jkzhCalculation);
        FromulaEntity fromulaToCal = creatFromulaToCal(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        FromulaEntity fromulaToLatex = creatFromulaToLatex(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        int layer = this.jkzhContext.getJkzhBasicParam().getAllLands();
        //②、计算主动土压力强度
        //for (int i = 1; i <= layer; i++) {
            int i = 3;
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
        //}
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
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土压力零点所在土层,this.jkzhContext);
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


    /**
     * 修正不正确的计算公式和展示公式
     * (地面堆载_{1}+)*主动土压力系数_{1}-2*内聚力_{1}*根号主动土压力系数_{1}
     * 需要修正为
     * 地面堆载_{1}*主动土压力系数_{1}-2*内聚力_{1}*根号主动土压力系数_{1}
     * @return
     */
    @Test
    public void reviseFromula(){
        char[] chars = "(地面堆载_{1} \\times 重度 \\times)".toCharArray();
        int leftTimes = 0;//左括号出现的次数
        StringBuilder tempFromula = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            //表明这是latex 类型的操作数，但当前只对 \times 和 \div 做处理。
            //其他的都放过
            if(chars[i] == '('){
                StringBuilder sub = new StringBuilder();
                do{
                    if(chars[i] == '('){
                        leftTimes++;
                    }
                    if(chars[i] == ')'){
                        leftTimes--;
                    }
                    sub.append(chars[i]);
                    i++;
                }while (leftTimes != 0);
                //(地面堆载_{1}+)，判断括号内是否只有一个操作数和操作符号
                String doFilter = doFilter(sub.toString());
                tempFromula.append(doFilter);
                continue;
            }
            tempFromula.append(String.valueOf(chars[i]));
        }
        log.info(tempFromula.toString());
    }

    //(地面堆载_{1}+)，判断括号内是否只有一个操作数和操作符号
    private String doFilter(String subString){
        char[] chars = subString.toCharArray();
        StringBuilder sub1 = new StringBuilder();
        List<String> param = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' || chars[i] == ')') {
                continue;
            }
            if(chars[i] == '+' || chars[i] == '-' || chars[i] =='*' || chars[i] == '/' || chars[i]=='÷'){
                param.add(sub1.toString());
                sub1.delete(0,sub1.length());
                continue;
            }else if(chars[i] == '\\'){
                StringBuilder sub2 = new StringBuilder();
                do{
                    sub2.append(chars[i]);
                    i++;
                }while (StringUtil.isEnglishChar(chars[i]));
                String sOperation = sub2.toString();
                if(operand.contains(sOperation)){
                    param.add(sub1.toString());
                    sub1.delete(0,sub1.length());
                }
                continue;
            }
            sub1.append(chars[i]);
        }
        if(param.size()!=2){
            return param.get(0);
        }else{
            return subString;
        }
    }
}

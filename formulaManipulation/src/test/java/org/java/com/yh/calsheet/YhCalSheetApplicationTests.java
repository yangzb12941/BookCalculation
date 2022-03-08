package org.java.com.yh.calsheet;

import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aviatorPlugins.MathRadiansFunction;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.entity.Param;
import org.enumUtils.StringUtil;
import org.equationSolving.PrintEquationSet;
import org.handle.JkzhFromulaHandle;
import org.handle.JkzhGetValues;
import org.junit.Test;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.show.JkzhILayout;
import org.solutions.Solution;
import org.solveableManipulationBehavior.EquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Variable;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;
import org.symbols.Symbol;
import java.util.*;

@Slf4j
public class YhCalSheetApplicationTests {
    static {
        //装配角度转换函数
        AviatorEvaluator.addFunction(new MathRadiansFunction());
    }

    /**
     * 计算过程
     */
    @Test
    public void calculation(){
        //①、生成土压力系数表
        JkzhContext jkzhContext = new JkzhContext();
        HashMap<String,String> formate = new HashMap<>();
        jkzhContext.setFormate(formate);
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        //基础参数拼装
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        SoilQualityTable soilQualityTable = new SoilQualityTable();
        jkzhBasicParam.setSoilQualityTable(soilQualityTable);

        jkzhContext.setJkzhBasicParam(jkzhBasicParam);
        //土压力系数表计算
        createSoilPressureTable(jkzhContext,jkzhFromulaHandle);
        //获取计算层次
        int layer = jkzhContext.getJkzhBasicParam().getAllLands();
        JkzhILayout jkzhILayout = new JkzhILayout();
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.主动土压力计算);
        //②、计算主动土压力强度
        for(int i = 1; i <= layer; i++){
            String  latexCalUp = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle,i-1,1,i, jkzhILayout, JkzhConfigEnum.主动土压力.getLatexCal(),jkzhGetValues);
            String  calUp = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle,i-1,1,i,JkzhConfigEnum.主动土压力.getCalculate(),jkzhGetValues);
            Double zdCalRtUp = (Double) AviatorEvaluator.execute(calUp);
            formate.put("主动土压力"+i+"上",String.format("%.2f",zdCalRtUp));
            log.info("第{}层展示公式—上:{}={}",i,latexCalUp,zdCalRtUp);
            String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle, i, 1,i, jkzhILayout, JkzhConfigEnum.主动土压力.getLatexCal(),jkzhGetValues);
            String  calDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle, i, 1,i, JkzhConfigEnum.主动土压力.getCalculate(),jkzhGetValues);
            Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
            log.info("第{}层展示公式-下:{}={}",i,latexCalDown,zdCalRtDown);
            formate.put("主动土压力"+i+"下",String.format("%.2f",zdCalRtDown));
        }
        //③、计算被动土压力强度
        Double depth = Double.valueOf(jkzhContext.getJkzhBasicParam().getDepth());
        //判断开挖深度在第几层土层
        String[][] table = jkzhContext.getJkzhBasicParam().getSoilQualityTable().getTable();
        Double sumLands = 0.0;
        int atLand = 0;
        for(int floor = 1;floor <table.length; floor++){
            sumLands += Double.valueOf(table[floor-1][2]);
            if(sumLands.compareTo(depth) >= 0){
                atLand = floor;
                break;
            }
        }
        //开挖深度所在土层
        jkzhContext.getJkzhBasicParam().setAtLand(atLand);

        jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.被动土压力计算);
        for(int i = 0; i <= layer-atLand; i++){
            String  latexCalUp = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle,i,atLand,i+atLand, jkzhILayout, JkzhConfigEnum.被动土压力.getLatexCal(),jkzhGetValues);
            String  calUp = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle,i,atLand,i+atLand,JkzhConfigEnum.被动土压力.getCalculate(),jkzhGetValues);
            Double zdCalRtUp = (Double) AviatorEvaluator.execute(calUp);
            formate.put("被动土压力"+(atLand+i)+"上",String.format("%.2f",zdCalRtUp));
            log.info("第{}层展示公式—上:{}={}",i+atLand,latexCalUp,zdCalRtUp);
            String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle, i+1, atLand,i+atLand, jkzhILayout, JkzhConfigEnum.被动土压力.getLatexCal(),jkzhGetValues);
            String  calDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle, i+1, atLand,i+atLand, JkzhConfigEnum.被动土压力.getCalculate(),jkzhGetValues);
            Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
            formate.put("被动土压力"+(atLand+i)+"下",String.format("%.2f",zdCalRtDown));
            log.info("第{}层展示公式-下:{}={}",i+atLand,latexCalDown,zdCalRtDown);
        }
        //④、计算土压力零点
        jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力零点所在土层);
        String  latexCalUp = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle,atLand-1,1,atLand, jkzhILayout, JkzhConfigEnum.主动土压力.getLatexCal(),jkzhGetValues);
        String  calUp = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle,atLand-1,1,atLand, JkzhConfigEnum.主动土压力.getCalculate(),jkzhGetValues);
        Double zdCalRtUp = (Double) AviatorEvaluator.execute(calUp);
        formate.put("主动土压力"+atLand+"上",String.format("%.2f",zdCalRtUp));
        log.info("第{}层展示公式—上:{}={}",atLand,latexCalUp,zdCalRtUp);
        String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle, atLand, 1,atLand, jkzhILayout, JkzhConfigEnum.主动土压力.getLatexCal(),jkzhGetValues);
        String  calDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle, atLand, 1,atLand, JkzhConfigEnum.主动土压力.getCalculate(),jkzhGetValues);
        Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
        log.info("第{}层展示公式-下:{}={}",atLand,latexCalDown,zdCalRtDown);
        formate.put("主动土压力"+atLand+"下",String.format("%.2f",zdCalRtDown));
        //第一种情况判断
        //主动土压力上端-被动土压力上端>0 并且主动土压力低端-被动土压力低端<0。那么这个土压力零点就在这一层土。
        // 通过假设土压力零点位于开挖基坑底面x米，作为当前层主动土压力计算公式的土层厚度。
        // 同理，也作为当前层被动土压力计算公式的土层厚度。
        //第二种情况判断
        //主动土压力上端-被动土压力上端<0 并且主动土压力低端-被动土压力低端>0。同样1情况处理。
        //土压力零点所在 土层
        int zoneLand = firstCase(jkzhContext.getJkzhBasicParam().getAtLand(), jkzhContext.getJkzhBasicParam().getAllLands(), formate);
        if(zoneLand != 0){
            jkzhContext.getJkzhBasicParam().setAtZoneLand(zoneLand);
            //表明找到土压力零点所在土层
            //那么就已这一层的主动底+被动底，厚度都已x替换，解出x即可。
            //例如：计算出在第4层，那么计算公式如下：
            // 第4层主动底(20+18*0.7+18.9*1.9+1.8*18.7+18X)*0.67-2*15.9*0.82
            //底4层被动底18x*1.46+2*15.9*1.21
            jkzhGetValues = new JkzhGetValues();
            jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力零点深度计算);
            //主动土压力底层：
            String zdlatexCalDown = jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    zoneLand,
                    1,
                    zoneLand,
                    jkzhILayout,
                    JkzhConfigEnum.主动土压力.getLatexCal(),
                    jkzhGetValues);
            String  zdCalDown = jkzhFromulaHandle.getCalculateExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    zoneLand,
                    1,
                    zoneLand,
                    JkzhConfigEnum.主动土压力.getCalculate(),
                    jkzhGetValues);
            log.info("第{}层展示公式-下:{}={}",zoneLand,zdlatexCalDown,zdCalDown);

            //被动土压力底层：
            String bdLatexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext,
                    jkzhFromulaHandle,
                    zoneLand - jkzhContext.getJkzhBasicParam().getAtLand(),
                    jkzhContext.getJkzhBasicParam().getAtLand(),
                    zoneLand,
                    jkzhILayout,
                    JkzhConfigEnum.被动土压力.getLatexCal(),
                    jkzhGetValues);
            String  bdCalDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext,
                    jkzhFromulaHandle,
                    zoneLand - jkzhContext.getJkzhBasicParam().getAtLand(),
                    jkzhContext.getJkzhBasicParam().getAtLand(),
                    zoneLand,
                    JkzhConfigEnum.被动土压力.getCalculate(),
                    jkzhGetValues);
            log.info("第{}层展示公式-下:{}={}",zoneLand,bdLatexCalDown,bdCalDown);
            //解方程
            VariableIDDynamicTable manager = new VariableIDDynamicTable();
            Variable v = new Variable(new CalcNumber(1));
            Symbol left = new LatexUserString(zdCalDown).toExpression().toSymbol(manager);
            Symbol right = new LatexUserString(bdCalDown).toExpression().toSymbol(manager);
            StringBuilder build = new StringBuilder();

            Expression leftE, rightE;
            leftE = new Expression(left);
            rightE = new Expression(right);

            Equation eq = new Equation(leftE, rightE, v, new SolveableManipulateBehavior());

            log.info("解方程:{}",eq.printSolveable(manager));
            Solveable finalS = eq.fullSolve();
            Solution s = finalS.reachedSolution();
            log.info("结果:{}",s.printLatex(manager));
        }else{
            //第三种情况判断
            //若是土层分解处按照不同层公式计算出险一正一负，那么就以这一土层分界切面为土压力零点。
            // 例如：第三层土底层公式计算为正，以第四层土层公式计算为负。那么土压力零点就在这一处。
            zoneLand = secondCase(jkzhContext.getJkzhBasicParam().getAtLand(),jkzhContext.getJkzhBasicParam().getAllLands(),formate);
            if(zoneLand != 0){
                //表明土压力零点就是这个切面深度
                jkzhContext.getJkzhBasicParam().setAtZoneLand(zoneLand);
                jkzhContext.getJkzhBasicParam().setAtZoneLand(zoneLand);
                //表明找到土压力零点所在土层
                //那么就已这一层的主动底+被动底，厚度都已x替换，解出x即可。
                //例如：计算出在第4层，那么计算公式如下：
                // 第4层主动底(20+18*0.7+18.9*1.9+1.8*18.7+18X)*0.67-2*15.9*0.82
                //底4层被动底18x*1.46+2*15.9*1.21
                jkzhGetValues = new JkzhGetValues();
                jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力零点深度计算);
                //主动土压力底层：
                String zdlatexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext, jkzhFromulaHandle, zoneLand, 1,zoneLand, jkzhILayout, JkzhConfigEnum.主动土压力.getLatexCal(),jkzhGetValues);
                String  zdCalDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext, jkzhFromulaHandle, zoneLand, 1,zoneLand, JkzhConfigEnum.主动土压力.getCalculate(),jkzhGetValues);
                log.info("第{}层展示公式-下:{}={}",zoneLand,zdlatexCalDown,zdCalDown);

                //被动土压力底层：
                String bdLatexCalDown = jkzhFromulaHandle.getLatexCalExpression(jkzhContext,
                        jkzhFromulaHandle,
                        zoneLand - jkzhContext.getJkzhBasicParam().getAtLand(),
                        jkzhContext.getJkzhBasicParam().getAtLand(),
                        zoneLand,
                        jkzhILayout,
                        JkzhConfigEnum.被动土压力.getLatexCal(),
                        jkzhGetValues);
                String  bdCalDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext,
                        jkzhFromulaHandle,
                        zoneLand - jkzhContext.getJkzhBasicParam().getAtLand(),
                        jkzhContext.getJkzhBasicParam().getAtLand(),
                        zoneLand,
                        JkzhConfigEnum.被动土压力.getLatexCal(),
                        jkzhGetValues);
                log.info("第{}层展示公式-下:{}={}",zoneLand,bdLatexCalDown,bdCalDown);
                //解方程
                VariableIDDynamicTable manager = new VariableIDDynamicTable();
                Variable v = new Variable(new CalcNumber(1));
                Symbol left = new LatexUserString(zdCalDown).toExpression().toSymbol(manager);
                Symbol right = new LatexUserString(bdCalDown).toExpression().toSymbol(manager);
                StringBuilder build = new StringBuilder();

                Expression leftE, rightE;
                leftE = new Expression(left);
                rightE = new Expression(right);

                Equation eq = new Equation(leftE, rightE, v, new SolveableManipulateBehavior());

                log.info("解方程:{}",eq.printSolveable(manager));
                Solveable finalS = eq.fullSolve();
                Solution s = finalS.reachedSolution();
                log.info("结果:{}",s.printLatex(manager));
            }else{
                //第四种情况判断
                //若是1、2、3 都不出现，那么就按无土压力零点判断。默认取开挖深度1.2倍处作为土压力零点。
                // 例如：开挖深度6米，6×1.2 = 7.2米。即开挖深度下1.2米作为土压力零点。
                Double pressureZero = thirdCase(jkzhContext.getJkzhBasicParam().getDepth());
                jkzhContext.getJkzhBasicParam().setPressureZero(pressureZero);
            }
        }

        //⑤、主动土压力的合力

        //⑥、主动作用点位置计算

        //⑦、被动土压力的合力

        //⑧、被动作用点位置计算

        //⑨、支撑轴力计算
    }

    private int firstCase(int atLayer,int allLands,HashMap<String,String> formate){
        int result = 0;
        for (int i = atLayer; i <= allLands; i++) {
            //主动土压力上端-被动土压力上端
            Double zdUp = Double.valueOf(formate.get("主动土压力" + atLayer + "上"));
            Double bdUp = Double.valueOf(formate.get("被动土压力"+atLayer+"上"));
            //主动土压力下端-被动土压力下端
            Double zdDown = Double.valueOf(formate.get("主动土压力"+atLayer+"下"));
            Double bdDown = Double.valueOf(formate.get("被动土压力"+atLayer+"下"));
            Double tmpe = (zdUp - bdUp) * (zdDown - bdDown);
            if(tmpe.compareTo(0.0)<=0){
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * 3、若是土层分界处按照不同层公式计算出现一正一负，那么就以这一土层分界切面为土压力零点。
     * 例如：第三层土底层公式计算为正，以第四层土上层公式计算为负。那么土压力零点就在这一处。
     * @param atLayer
     * @param allLands
     * @param formate
     * @return
     */
    private int secondCase(int atLayer,int allLands,HashMap<String,String> formate){
        int result = 0;
        for (int i = atLayer; i <= allLands; i++) {
            //第i+1层主动土压力上端
            String zdDownTwos = formate.get("主动土压力" + (atLayer + 1) + "上");
            Double zdDownTwo = Double.valueOf(Objects.nonNull(zdDownTwos)?zdDownTwos:"0.0");
            //第i层主动土压力下端
            String zdDownOnes = formate.get("主动土压力" + atLayer + "下");
            Double zdDownOne = Double.valueOf(Objects.nonNull(zdDownOnes)?zdDownOnes:"0.0");
            Double tmpe = (zdDownTwo * zdDownOne);
            if(tmpe.compareTo(0.0)<0){
                result = i;
                break;
            }
        }
        return result;
    }

    private Double thirdCase(Double depth){
        return depth*1.2;
    }

    private void createSoilPressureTable(JkzhContext jkzhContext,JkzhFromulaHandle jkzhFromulaHandle){
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext,jkzhFromulaHandle);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("{}", JSON.toJSONString(soilPressureTable));
    }

    private void zdPressureCal(){}

    private void bdPressureCal(){}

    private void pressureZeroCal(){}


    /**
     * 测试计算过程公式带入数值和计算结果公式带入数值
     */
    @Test
    public void valueFillCal(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
            String[] values = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
            //①、计算过程代入数值
            String target_1 = jkzhFromulaHandle.expansionEquation(source.getLatexCal(),2,5,5);
            log.info("展开_1:{}",target_1);
            //②、获取当前公式对应的值集合
            //(载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3}) \times 数系力压土动主_{3}- 2 \times 力聚内_{3} \times \sqrt{数系力压土动主_{3}}
            String replaceChar_1 = jkzhFromulaHandle.placeholder(target_1);
            log.info("占位_1:{}",replaceChar_1);
            String s_1 = jkzhFromulaHandle.valuesFillCal(replaceChar_1, values);
            log.info("填充_1:{}",s_1);

            //②、计算结果代入数值
            //String target_2 = jkzhFromulaHandle.expansionEquationWithSubscript(source.getCalculate(),5,Boolean.FALSE);
            //log.info("展开_2:{}",target_2);
            //String replaceChar_2 = jkzhFromulaHandle.placeholder(target_2);
            //log.info("占位_2:{}",replaceChar_2);
            //String s_2 = jkzhFromulaHandle.valuesFillCal(replaceChar_2, values);
            //log.info("填充_2:{}",s_2);
        }
    }

    @Test
    public void latexEquation(){
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhILayout jkzhLayout = new JkzhILayout();
        //①、解析公式，把公式展开
        for (JkzhConfigEnum source : JkzhConfigEnum.values()) {
            //①、替换成现实的字符
            //LatexCal : 替换，是用于word展示不带参数的计算过程公式
            String replaceChar = jkzhFromulaHandle.replaceLayoutChar(source.getLatexCal(),jkzhLayout);
            log.info("替换:{}",replaceChar);
            //LatexCal : 展开，是用于word展示不带参数的计算过程公式
            String target = jkzhFromulaHandle.expansionEquation(replaceChar,3,3,4);
            log.info("展开:{}",target);
        }
    }
    @Test
    public void getParamTest(){
        String fromula = "(地面堆载_{5}+重度_{1} \\times 厚度_{1}+重度_{2} \\times 厚度_{2}+重度_{3} \\times 厚度_{3}+重度_{4} \\times 厚度_{4}+重度_{5} \\times 厚度_{5}) \\times 主动土压力系数_{5}- 2 \\times 内聚力_{5} \\times \\sqrt{主动土压力系数_{5}}";
        List<Param> params = getParam(fromula);
        log.info("元素:{}",params);
    }

    private List<Param> getParam(String fromula){
        List<Param> params = new ArrayList<Param>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        Boolean isChanged = Boolean.FALSE;
        char[] chars = fromula.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(StringUtil.isChineseChar(chars[i])){
                isChanged = Boolean.TRUE;
                deque.push(chars[i]);
                continue;
            }
            if (isChanged) {
                deque.push(chars[i]);
                isChanged = Boolean.FALSE;
                //往前跳过n个字符去掉 _{5}格式内容
                int skip = 0;
                do {
                    skip++;
                    deque.push(chars[i+skip]);
                }while (chars[skip+i]!='}');
                i=i+skip;
            }
            if (!deque.isEmpty()) {
                StringBuilder subPart = new StringBuilder();
                do{
                    subPart.append(deque.pollLast());
                }while (!deque.isEmpty());
                Param param = createParam(subPart.toString());
                params.add(param);
            }
        }
        return params;
    }

    private Param createParam(String element){
        Param param = new Param();
        String[] s = element.split("_");
        param.setName(s[0]);
        param.setIndex(s[1].substring(1,s[1].length() - 1));
        return param;
    }
}

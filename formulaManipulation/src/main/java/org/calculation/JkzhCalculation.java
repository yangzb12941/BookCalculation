package org.calculation;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.handle.ICreateContextHandle;
import org.handle.JkzhFromulaHandle;
import org.handle.JkzhGetValues;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.show.JkzhILayout;
import org.solutions.Solution;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Symbol;
import org.symbols.Variable;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class JkzhCalculation extends DefaultCalculation{
    private ICalculation iCalculation;
    private ICreateContextHandle iCreateContextHandle;
    private JkzhFromulaHandle jkzhFromulaHandle;
    private JkzhContext jkzhContext;
    private JkzhILayout jkzhILayout;

    public JkzhCalculation(ICalculation iCalculation,
                           JkzhFromulaHandle jkzhFromulaHandle,
                           ICreateContextHandle iCreateContextHandle) {
        this.iCalculation = iCalculation;
        this.iCreateContextHandle = iCreateContextHandle;
        this.jkzhFromulaHandle = jkzhFromulaHandle;
        this.jkzhILayout = new JkzhILayout();
        this.jkzhContext = (JkzhContext)this.getContext(this.iCalculation,this.jkzhFromulaHandle,this.iCreateContextHandle);
    }

    /**
     * 主动土压力强度计算
     */
    public void zdPressure(){
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.主动土压力计算);
        int layer = this.jkzhContext.getJkzhBasicParam().getAllLands();
        //②、计算主动土压力强度
        for(int i = 1; i <= layer; i++){
            String  latexCalUp = this.jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i-1,
                    1,i,
                    jkzhILayout,
                    JkzhConfigEnum.主动土压力.getLatexCal(),
                    jkzhGetValues);
            String  calUp = this.jkzhFromulaHandle.getCalculateExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i-1,
                    1,
                    i,
                    JkzhConfigEnum.主动土压力.getCalculate(),
                    jkzhGetValues);
            Double zdCalRtUp = (Double) AviatorEvaluator.execute(calUp);
            this.jkzhContext.getFormate().put("主动土压力"+i+"上",String.format("%.2f",zdCalRtUp));
            log.info("主动土压力第{}层展示公式—上:{}={}",i,latexCalUp,zdCalRtUp);
            String latexCalDown = this.jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i,
                    1,
                    i,
                    jkzhILayout,
                    JkzhConfigEnum.主动土压力.getLatexCal(),
                    jkzhGetValues);
            String  calDown = this.jkzhFromulaHandle.getCalculateExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i,
                    1,
                    i,
                    JkzhConfigEnum.主动土压力.getCalculate(),
                    jkzhGetValues);
            Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
            log.info("主动土压力第{}层展示公式-下:{}={}",i,latexCalDown,zdCalRtDown);
            this.jkzhContext.getFormate().put("主动土压力"+i+"下",String.format("%.2f",zdCalRtDown));
        }
    }

    /**
     * 被动土压力强度计算
     */
    public void bdPressure(){
        Double depth = jkzhContext.getJkzhBasicParam().getDepth();
        Integer atLand = depthAtLand(depth);
        //判断开挖深度在第几层土层
        jkzhContext.getJkzhBasicParam().setAtLand(atLand);
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.被动土压力计算);
        //获取计算层次
        int layer = jkzhContext.getJkzhBasicParam().getAllLands();
        for(int i = 0; i <= layer-atLand; i++){
            String  latexCalUp = jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i,
                    atLand,
                    i+atLand,
                    jkzhILayout,
                    JkzhConfigEnum.被动土压力.getLatexCal(),
                    jkzhGetValues);
            String  calUp = jkzhFromulaHandle.getCalculateExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i,
                    atLand,
                    i+atLand,
                    JkzhConfigEnum.被动土压力.getCalculate(),
                    jkzhGetValues);
            Double zdCalRtUp = (Double) AviatorEvaluator.execute(calUp);
            jkzhContext.getFormate().put("被动土压力"+(atLand+i)+"上",String.format("%.2f",zdCalRtUp));
            log.info("被动土压力第{}层展示公式—上:{}={}",i+atLand,latexCalUp,zdCalRtUp);
            String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i+1,
                    atLand,
                    i+atLand,
                    jkzhILayout,
                    JkzhConfigEnum.被动土压力.getLatexCal(),
                    jkzhGetValues);
            String  calDown = jkzhFromulaHandle.getCalculateExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i+1,
                    atLand,
                    i+atLand,
                    JkzhConfigEnum.被动土压力.getCalculate(),
                    jkzhGetValues);
            Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
            jkzhContext.getFormate().put("被动土压力"+(atLand+i)+"下",String.format("%.2f",zdCalRtDown));
            log.info("被动土压力第{}层展示公式-下:{}={}",i+atLand,latexCalDown,zdCalRtDown);
        }
    }

    /**
     * 土压力零点计算，从开挖深度所在土层开始计算。分为四种情况：
     * 1、主动土压力上端-被动土压力上端>0 并且 主动土压力低端-被动土压力低端<0。那么这个土压力零点就在这一层土。
     * 通过假设土压力零点位于开挖基坑底面 x 米，作为当前层主动土压力底层计算公式的土层厚度。
     * 同理，也作为当前层被动土压力底层计算公式的土层厚度。
     * 2、主动土压力上端-被动土压力上端<0 并且主动土压力低端-被动土压力低端>0。同样1情况处理。
     * 3、若是土层分界处按照不同层公式计算出现一正一负，那么就以这一土层分界切面为土压力零点。例如：第三层土底层公式计算为正，以第四层土层公式计算为负。那么土压力零点就在这一处。
     * 4、若是1、2、3 都不出现，那么就按无土压力零点判断。默认取开挖深度1.2倍处作为土压力零点。例如：开挖深度6米，6×1.2 = 7.2米。即开挖深度下1.2米作为土压力零点。
     *
     * 注意：在开挖深度这一层土层，需要重新计算主动土压力强度。按开挖深度这一切面厚度带入公式。
     * 例如：第一层土层厚度1米，第二层土层厚度3米，第三层土层厚度4米。开挖深度是6米，那么开挖深度是在第三层土位置。
     * 第三层土上层到开完深度还剩6-（1+3）= 2米的土层厚度。那么计算第三层主动土压力下的时候只需要把2米的厚度代入公式。
     */
    public void pressureZero(){
        //根据开挖深度，判断开挖基土在哪一层
        Double depth = jkzhContext.getJkzhBasicParam().getDepth();
        Integer atLand = depthAtLand(depth);
        //重新计算这一层的主动土压力底
        customCalZdPressure(atLand);
        //计算土压力零点在哪一层
        pressureZeroAtLand(atLand,jkzhContext.getJkzhBasicParam().getAllLands(),jkzhContext.getFormate());
    }

    //计算给定深度，返回深度所在土层
    private Integer depthAtLand(Double depth){
        //判断开挖深度在第几层土层
        String[][] table = this.jkzhContext.getJkzhBasicParam().getSoilQualityTable().getTable();
        Double sumLands = 0.0;
        int atLand = 0;
        for(int floor = 1;floor <table.length; floor++){
            sumLands += Double.valueOf(table[floor-1][2]);
            if(sumLands.compareTo(depth) >= 0){
                atLand = floor;
                break;
            }
        }
        return atLand;
    }

    //根据真实土层厚度重新计算指定土层主动土压力强度重新计算
    private void customCalZdPressure(int atLand){
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力零点所在土层);
        String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atLand,
                1,
                atLand,
                jkzhILayout,
                JkzhConfigEnum.主动土压力.getLatexCal(),
                jkzhGetValues);
        String  calDown = jkzhFromulaHandle.getCalculateExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atLand,
                1,
                atLand,
                JkzhConfigEnum.主动土压力.getCalculate(),
                jkzhGetValues);
        Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
        log.info("主动土压力第{}层展示公式-下:{}={}",atLand,latexCalDown,zdCalRtDown);
        jkzhContext.getFormate().put("主动土压力"+atLand+"下",String.format("%.2f",zdCalRtDown));
    }

    /**
     * 计算土压力零点在第几层土层。
     * 并且计算土压力零点数值。
     * @param atLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     */
    private void pressureZeroAtLand(int atLand, int allLands, HashMap<String,String> formate){
        //第一种情况
        int zoneLand = firstCase(atLand, allLands, formate);
        if(zoneLand != 0){
            // 解土压力零点方程
            Double pressureZero = calPressureZero(zoneLand);
            this.jkzhContext.getJkzhBasicParam().setPressureZero(pressureZero);
        }else{
            //第二种情况
            zoneLand = secondCase(atLand, allLands, formate);
            if(zoneLand != 0){
                Double pressureZero = getLandThickness(zoneLand);
                this.jkzhContext.getJkzhBasicParam().setPressureZero(pressureZero);
            }else{
                //第四种情况
                Double pressureZero = thirdCase(jkzhContext.getJkzhBasicParam().getDepth());
                this.jkzhContext.getJkzhBasicParam().setPressureZero(pressureZero);
            }
        }
    }
    /**
     * 第一种情况判断
     * 主动土压力上端-被动土压力上端>0 并且主动土压力低端-被动土压力低端<0。那么这个土压力零点就在这一层土。
     *  通过假设土压力零点位于开挖基坑底面x米，作为当前层主动土压力计算公式的土层厚度。
     *  同理，也作为当前层被动土压力计算公式的土层厚度。
     * 第二种情况判断
     * 主动土压力上端-被动土压力上端<0 并且主动土压力低端-被动土压力低端>0。同样1情况处理。
     * 土压力零点所在 土层
     * @param atLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     * @return
     */
    private int firstCase(int atLand, int allLands, HashMap<String,String> formate){
        int result = 0;
        for (int i = atLand; i <= allLands; i++) {
            //主动土压力上端-被动土压力上端
            Double zdUp = Double.valueOf(formate.get("主动土压力" + atLand + "上"));
            Double bdUp = Double.valueOf(formate.get("被动土压力"+atLand+"上"));
            //主动土压力下端-被动土压力下端
            Double zdDown = Double.valueOf(formate.get("主动土压力"+atLand+"下"));
            Double bdDown = Double.valueOf(formate.get("被动土压力"+atLand+"下"));
            Double tmpe = (zdUp - bdUp) * (zdDown - bdDown);
            if(tmpe.compareTo(0.0)<=0){
                result = i;
                jkzhContext.getJkzhBasicParam().setAtZoneLand(i);
                break;
            }
        }
        return result;
    }

    /**
     * 3、若是土层分界处按照不同层公式计算出现一正一负，那么就以这一土层分界切面为土压力零点。
     * 例如：第三层土底层公式计算为正，以第四层土上层公式计算为负。那么土压力零点就在这一处。
     * @param atLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     * @return
     */
    private int secondCase(int atLand,int allLands,HashMap<String,String> formate){
        int result = 0;
        for (int i = atLand; i <= allLands; i++) {
            //第i+1层主动土压力上端
            String zdDownTwos = formate.get("主动土压力" + (atLand + 1) + "上");
            Double zdDownTwo = Double.valueOf(Objects.nonNull(zdDownTwos)?zdDownTwos:"0.0");
            //第i层主动土压力下端
            String zdDownOnes = formate.get("主动土压力" + atLand + "下");
            Double zdDownOne = Double.valueOf(Objects.nonNull(zdDownOnes)?zdDownOnes:"0.0");
            Double tmpe = (zdDownTwo * zdDownOne);
            if(tmpe.compareTo(0.0)<0){
                result = i;
                jkzhContext.getJkzhBasicParam().setAtZoneLand(i);
                break;
            }
        }
        return result;
    }

    /**
     * 第四种情况判断
     * 若是1、2、3 都不出现，那么就按无土压力零点判断。默认取开挖深度1.2倍处作为土压力零点。
     *  例如：开挖深度6米，6×1.2 = 7.2米。即开挖深度下1.2米作为土压力零点。
     * @param depth
     * @return
     */
    private Double thirdCase(Double depth){
        Double pressureZero = depth*1.2;
        Integer atZoneLand = depthAtLand(pressureZero);
        jkzhContext.getJkzhBasicParam().setAtZoneLand(atZoneLand);
        return pressureZero;
    }

    /**
     * 表明找到土压力零点所在土层
     * 那么就已这一层的主动底+被动底，厚度都已x替换，解出x即可。
     * 例如：计算出在第4层，那么计算公式如下：
     * 第4层主动底(20+18*0.7+18.9*1.9+1.8*18.7+18X)*0.67-2*15.9*0.82
     * 底4层被动底18x*1.46+2*15.9*1.21
     * @return
     */
    private Double calPressureZero(int zoneLand){
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
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

        Expression leftE, rightE;
        leftE = new Expression(left);
        rightE = new Expression(right);

        Equation eq = new Equation(leftE, rightE, v, new SolveableManipulateBehavior());

        log.info("解方程:{}",eq.printSolveable(manager));
        Solveable finalS = eq.fullSolve();
        Solution s = finalS.reachedSolution();
        String latexResult = s.printLatex(manager);
        latexResult = String.format("%.2f",latexResult);
        log.info("结果:{}",latexResult);
        return Double.valueOf(latexResult);
    }


    /**
     * 获取某一层土的厚度
     * @param land 土层
     * @return
     */
    public Double getLandThickness(int land){
        String hdValue = "0.0";
        try {
            hdValue = jkzhContext.getJkzhBasicParam().getSoilQualityTable().getTable()[land][2];
        }catch (IndexOutOfBoundsException e) {
            log.error("getValuesFromSoilPressureTable:{}",e);
        }
        return Double.valueOf(hdValue);
    }

    //⑤、主动土压力的合力

    //⑥、主动作用点位置计算

    //⑦、被动土压力的合力

    //⑧、被动作用点位置计算

    //⑨、支撑轴力计算
}

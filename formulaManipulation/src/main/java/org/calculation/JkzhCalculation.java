package org.calculation;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.enumUtils.BigDecimalStringUtil;
import org.getValue.JkzhGetValues;
import org.handle.JkzhFromulaHandle;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.show.JkzhILayout;
import org.solutions.Solution;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.springframework.util.StringUtils;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Symbol;
import org.symbols.Variable;

import java.util.HashMap;
import java.util.Objects;

/**
 * 基坑支护计算
 */
@Slf4j
public class JkzhCalculation extends DefaultCalculation{
    private JkzhFromulaHandle jkzhFromulaHandle;
    private JkzhContext jkzhContext;
    private JkzhILayout jkzhILayout;

    public JkzhCalculation(JkzhFromulaHandle jkzhFromulaHandle) {
        this.jkzhFromulaHandle = jkzhFromulaHandle;
        this.jkzhILayout = new JkzhILayout();
        this.jkzhContext = (JkzhContext)this.getContext(this,this.jkzhFromulaHandle);
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
            this.jkzhContext.getFormate().put("主动土压力上"+i,String.format("%.2f",zdCalRtUp));
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
            this.jkzhContext.getFormate().put("主动土压力下"+i,String.format("%.2f",zdCalRtDown));
        }
    }

    /**
     * 被动土压力强度计算
     * @param depth 当前基坑深度,有可能有多个开挖阶段.每一个开挖阶段,开挖深度不一样.
     */
    public void bdPressure(Double depth){
        Integer atLand = depthAtLand(depth);
        //判断开挖深度在第几层土层
        jkzhContext.getJkzhBasicParam().setAtDepthLand(atLand);
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.被动土压力计算);
        //获取计算层次
        int allLands = jkzhContext.getJkzhBasicParam().getAllLands();
        for(int i = 0; i <= allLands-atLand; i++){
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
            jkzhContext.getFormate().put("被动土压力上"+(atLand+i),String.format("%.2f",zdCalRtUp));
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
            jkzhContext.getFormate().put("被动土压力下"+(atLand+i),String.format("%.2f",zdCalRtDown));
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
     * @param depth 当前基坑深度,有可能有多个开挖阶段.每一个开挖阶段,开挖深度不一样.
     */
    public void pressureZero(Double depth){
        //根据开挖深度，判断开挖基土在哪一层
        Integer atDepthLand = depthAtLand(depth);
        //重新计算这一层的主动土压力底
        JkzhGetValues jkzhGetValues = new JkzhGetValues();
        jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力零点所在土层);
        customCalZdPressure(atDepthLand,jkzhGetValues);
        //计算土压力零点在哪一层
        pressureZeroAtLand(depth,atDepthLand,jkzhContext.getJkzhBasicParam().getAllLands(),jkzhContext.getFormate());
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

    /**
     * 根据真实土层厚度重新计算指定土层主动土压力强度重新计算
     * @param atLand 重新计算第几层土的主动土压力底层数据
     * @param jkzhGetValues 获取这一层土厚度的方式
     */
    private void customCalZdPressure(int atLand,JkzhGetValues jkzhGetValues){
        String  calDown = jkzhFromulaHandle.getCalculateExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atLand,
                1,
                atLand,
                JkzhConfigEnum.主动土压力.getCalculate(),
                jkzhGetValues);
        Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
        log.info("主动土压力第{}层展示公式-下:{}={}",atLand,calDown,zdCalRtDown);
        jkzhContext.getFormate().put("主动土压力下"+atLand,String.format("%.2f",zdCalRtDown));
    }

    /**
     * 根据真实土层厚度重新计算指定土层被动土压力强度重新计算
     * @param atLand 重新计算第几层土的被动土压力底层数据
     * @param depthLand 当前开挖深度所在第几层土层
     * @param jkzhGetValues 获取这一层土厚度的方式
     */
    private void customCalBdPressure(int atLand,int depthLand, JkzhGetValues jkzhGetValues){
        String  calDown = jkzhFromulaHandle.getCalculateExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atLand-depthLand+1,
                depthLand,
                atLand,
                JkzhConfigEnum.被动土压力.getCalculate(),
                jkzhGetValues);
        Double zdCalRtDown = (Double) AviatorEvaluator.execute(calDown);
        log.info("被动土压力第{}层展示公式-下:{}={}",atLand,calDown,zdCalRtDown);
        jkzhContext.getFormate().put("被动土压力下"+atLand,String.format("%.2f",zdCalRtDown));
    }

    /**
     * 计算土压力零点在第几层土层。
     * 并且计算土压力零点数值。
     * @param depth 开挖深度所在土层
     * @param atDepthLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     */
    private void pressureZeroAtLand(Double depth,int atDepthLand, int allLands, HashMap<String,String> formate){
        //第一种情况
        int zoneLand = firstCase(atDepthLand, allLands, formate);
        if(zoneLand != 0){
            // 解土压力零点方程
            Double pressureZero = calPressureZero(zoneLand);
            firstCasePressureZero(zoneLand,atDepthLand,depth,pressureZero);
        }else{
            //第二种情况
            zoneLand = secondCase(atDepthLand, allLands, formate);
            if(zoneLand != 0){
                secondCasePressureZero(zoneLand);
            }else{
                //第四种情况
                zoneLand = thirdCase(depth);
                if(zoneLand != 0){
                    thirdCasePressureZero(depth);
                }
            }
        }
    }

    /**
     * 第二种情况，计算土压力零点深度
     * @param zoneLand 当前土层
     * @return
     */
    private void secondCasePressureZero(Integer zoneLand){
        Double zero = 0.0;
        for (int i = 0; i <= zoneLand; i++) {
            zero += Double.valueOf(this.jkzhContext.getSoilPressureTable().getTable()[i][2]);
        }
        this.jkzhContext.getJkzhBasicParam().setPressureZero(zero);
    }

    /**
     * 第二种情况，计算土压力零点深度
     * @param zoneLand
     * @param atDepthLand
     * @param depth
     * @return
     */
    private void firstCasePressureZero(Integer zoneLand,Integer atDepthLand,Double depth,Double pressureZero){
        Double zero = 0.0;
        if(zoneLand == atDepthLand){
            this.jkzhContext.getJkzhBasicParam().setPressureZero(depth+pressureZero);
        }else{
            for (int i = 1; i < zoneLand; i++) {
                zero += Double.valueOf(this.jkzhContext.getJkzhBasicParam().getSoilQualityTable().getTable()[i-1][2]);
            }
            this.jkzhContext.getJkzhBasicParam().setPressureZero(zero+pressureZero);
        }
    }

    /**
     * 第二种情况，计算土压力零点深度
     * @param depth 当前开挖深度
     * @return
     */
    private void thirdCasePressureZero(Double depth){
        this.jkzhContext.getJkzhBasicParam().setPressureZero(depth * 1.2);
    }

    /**
     * 第一种情况判断
     * 主动土压力上端-被动土压力上端>0 并且主动土压力低端-被动土压力低端<0。那么这个土压力零点就在这一层土。
     *  通过假设土压力零点位于开挖基坑底面x米，作为当前层主动土压力计算公式的土层厚度。
     *  同理，也作为当前层被动土压力计算公式的土层厚度。
     * 第二种情况判断
     * 主动土压力上端-被动土压力上端<0 并且主动土压力低端-被动土压力低端>0。同样1情况处理。
     * 土压力零点所在 土层
     * @param atDepthLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     * @return
     */
    private int firstCase(int atDepthLand, int allLands, HashMap<String,String> formate){
        int result = 0;
        for (int i = atDepthLand; i <= allLands; i++) {
            //主动土压力上端-被动土压力上端
            Double zdUp = Double.valueOf(formate.get("主动土压力上" + atDepthLand));
            Double bdUp = Double.valueOf(formate.get("被动土压力上"+atDepthLand));
            //主动土压力下端-被动土压力下端
            Double zdDown = Double.valueOf(formate.get("主动土压力下"+atDepthLand));
            Double bdDown = Double.valueOf(formate.get("被动土压力下"+atDepthLand));
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
            String zdDownTwos = formate.get("主动土压力上" + (atLand + 1));
            Double zdDownTwo = Double.valueOf(Objects.nonNull(zdDownTwos)?zdDownTwos:"0.0");
            //第i层主动土压力下端
            String zdDownOnes = formate.get("主动土压力下" + atLand);
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
    private Integer thirdCase(Double depth){
        Double pressureZero = depth*0.2;
        Integer atZoneLand = depthAtLand(pressureZero+depth);
        return atZoneLand;
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
                zoneLand - jkzhContext.getJkzhBasicParam().getAtDepthLand()+1,
                jkzhContext.getJkzhBasicParam().getAtDepthLand(),
                zoneLand,
                jkzhILayout,
                JkzhConfigEnum.被动土压力.getLatexCal(),
                jkzhGetValues);
        String  bdCalDown = jkzhFromulaHandle.getCalculateExpression(jkzhContext,
                jkzhFromulaHandle,
                zoneLand - jkzhContext.getJkzhBasicParam().getAtDepthLand()+1,
                jkzhContext.getJkzhBasicParam().getAtDepthLand(),
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
        log.info("结果:{}",latexResult);
        String result = "";
        if(StringUtils.isEmpty(latexResult)){
            result = "0";
        }else{
            String[] split = latexResult.split("=");
            result = BigDecimalStringUtil.str2BigDecimalKeepDouble(split[1]);
        }
        log.info("结果:{}",result);
        return Double.valueOf(result);
    }

    /**
     * 获取某一层土的厚度
     * @param land 土层
     * @return
     */
    private Double getLandThickness(int land){
        return 0.0;
    }

    /**
     * ⑤、主动土压力合力
     * 土压力合力和作用点位置，只计算土压力零点之上的土层。
     * 1、注意，是以各层土动土主压力强度计算。而不是被动土压力强度计算。
     * 2、从土压力零点这一层开始，往上计算各土层的土压力合力和作用点位置。
     * 需要注意，土压力零点所在土层的h要按真实土层厚度代入计算。例如：第一层土层厚度1米，
     * 第二层土层厚度3米，第三层土层厚度4米。开挖深度是6米，那么开挖深度是在第三层土位置。
     * 第三层土还剩6-(1+3) = 2米的土层厚度。
     * 那么第三层计算土压力的合力和作用点位置的时候只需要计算第三层土就是2米的厚度代入公式。
     * 3、主动土压力上层，底层以土压力零点为基准。
     * 4、被动土上层以开挖深度为基准，底层以零点位置为基准。
     * 5、各层土上层土主动压力a,下层土主动压力b的正负值不同，而采取不同公式计算。
     * 6、零点所在土层，h是以土层上层到零点位置的土层厚度为准。
     */
    public void zdResultantEarthPressures() {
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues();
        jkzhZDGetValues.setModel(JkzhGetValueModelEnum.主动土压力合力计算);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);

        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getFormate().get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getFormate().get("主动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }
        }
    }

    /**
     * ⑥、主动作用点位置计算
     */
    public void zdPositionAction(){
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues();
        jkzhZDGetValues.setModel(JkzhGetValueModelEnum.主动作用点位置);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);

        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getFormate().get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getFormate().get("主动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getLatexCal(),
                        jkzhZDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("主动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }
        }
    }

    /**
     * ⑦、被动土压力合力
     * 土压力合力和作用点位置，只计算土压力零点之上的土层。
     * 1、注意，是以各层土动土主压力强度计算。而不是被动土压力强度计算。
     * 2、从土压力零点这一层开始，往上计算各土层的土压力合力和作用点位置。
     * 需要注意，土压力零点所在土层的h要按真实土层厚度代入计算。例如：第一层土层厚度1米，
     * 第二层土层厚度3米，第三层土层厚度4米。开挖深度是6米，那么开挖深度是在第三层土位置。
     * 第三层土还剩6-(1+3) = 2米的土层厚度。
     * 那么第三层计算土压力的合力和作用点位置的时候只需要计算第三层土就是2米的厚度代入公式。
     * 3、主动土压力上层，底层以土压力零点为基准。
     * 4、被动土上层以开挖深度为基准，底层以零点位置为基准。
     * 5、各层土上层土主动压力a,下层土主动压力b的正负值不同，而采取不同公式计算。
     * 6、零点所在土层，h是以土层上层到零点位置的土层厚度为准。
     * @param depth 当前开挖深度
     */
    public void bdResultantEarthPressures(Double depth){
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        //重新计算土压力土压力零点这层土的被动土压力底
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues();
        jkzhBDGetValues.setModel(JkzhGetValueModelEnum.被动土压力合力计算);
        //根据开挖深度，判断开挖基土在哪一层
        Integer depthLand = depthAtLand(depth);
        customCalBdPressure(atZoneLand,depthLand,jkzhBDGetValues);
        for (int land = depthLand; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getFormate().get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getFormate().get("被动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("被动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("被动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,calculate,zdCalRtDown);
                jkzhContext.getFormate().put("被动土压力合力"+land,String.format("%.2f",zdCalRtDown));
            }
        }
    }

    /**
     * 被动作用点位置计算
     * @param depth 当前开挖深度
     */
    public void bdPositionAction(Double depth){
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        //重新计算土压力土压力零点这层土的被动土压力底
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues();
        jkzhBDGetValues.setModel(JkzhGetValueModelEnum.被动作用点位置);
        //根据开挖深度，判断开挖基土在哪一层
        Integer depthLand = depthAtLand(depth);
        customCalBdPressure(atZoneLand,depthLand,jkzhBDGetValues);
        for (int land = depthLand; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getFormate().get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getFormate().get("被动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                jkzhContext.getFormate().put("被动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                jkzhContext.getFormate().put("被动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                String latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getLatexCal(),
                        jkzhBDGetValues);
                String calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                jkzhContext.getFormate().put("被动土作用点位置"+land,String.format("%.2f",zdCalRtDown));
            }
        }
    }

    /**
     * ⑨、支撑轴力计算
     * 支撑轴力计算，是以 土压力合力以及作用点位置为数据基础计算。
     * 汇总各土层主动土(Σ主动土压力合力*(土压力零点深度 - 当前土层底处累计深度+主动土作用点位置) - Σ被动土压力合力*(土压力零点深度 - 当前土层底处累计深度+被动作用点位置))/(土压力零点-支撑的轴线)。
     * 注意，计算被动土各土层土压力合力和作用点位置，需要按土压力零点之上计算。
     * 零点深度 - 当前土层底处累计深度+当前土层的ha
     * 被动是以基坑开挖处当作算顶点来算。
     * @param depth 当前开挖深度
     */
    public void calStrutForce(Double depth){
        //土压力零点所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues();
        jkzhZDGetValues.setModel(JkzhGetValueModelEnum.主动支撑轴力计算);
        String zdLatexCal = jkzhFromulaHandle.getLatexCalExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                1,
                atZoneLand,
                this.jkzhILayout,
                JkzhConfigEnum.支撑轴力主动.getLatexCal(),
                jkzhZDGetValues);
        String zdCalculate = jkzhFromulaHandle.getCalculateExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                1,
                atZoneLand,
                JkzhConfigEnum.支撑轴力主动.getCalculate(),
                jkzhZDGetValues);
        this.jkzhContext.getFormate().put("支撑轴力主动"+atZoneLand,zdCalculate);
        log.info("支撑轴力计算主动土压力公式:{}={}",zdLatexCal,zdCalculate);
        //获取开挖基坑所在土层
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues();
        jkzhBDGetValues.setModel(JkzhGetValueModelEnum.被动支撑轴力计算);
        int atDepthLand = this.jkzhContext.getJkzhBasicParam().getAtDepthLand();
        int time = atZoneLand-atDepthLand+1;
        String bdLatexCal = jkzhFromulaHandle.getLatexCalExpression(
                jkzhContext,
                jkzhFromulaHandle,
                time,
                atDepthLand,
                atZoneLand,
                this.jkzhILayout,
                JkzhConfigEnum.支撑轴力被动.getLatexCal(),
                jkzhBDGetValues);
        String bdCalculate = jkzhFromulaHandle.getCalculateExpression(
                jkzhContext,
                jkzhFromulaHandle,
                time,
                atDepthLand,
                atZoneLand,
                JkzhConfigEnum.支撑轴力被动.getCalculate(),
                jkzhBDGetValues);
        this.jkzhContext.getFormate().put("支撑轴力被动"+atZoneLand,bdCalculate);
        log.info("支撑轴力计算被动土压力公式:{}={}",bdLatexCal,bdCalculate);

        HashMap<String, String> layoutMap = this.jkzhILayout.getLayoutMap();
        layoutMap.put("支撑轴力主动"+atZoneLand,zdLatexCal);
        layoutMap.put("支撑轴力被动"+atZoneLand,bdLatexCal);

        JkzhGetValues jkzhZCZLGetValues = new JkzhGetValues();
        jkzhZCZLGetValues.setModel(JkzhGetValueModelEnum.支撑轴力计算);
        String zlLatexCal = jkzhFromulaHandle.generalFromulaHandle(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                JkzhConfigEnum.支撑轴力.getLatexCal(),
                jkzhZCZLGetValues);

        String zlCalculate = jkzhFromulaHandle.generalFromulaHandle(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                JkzhConfigEnum.支撑轴力.getCalculate(),
                jkzhZCZLGetValues);
        Double zlCalRt = (Double) AviatorEvaluator.execute(zlCalculate);
        log.info("支撑轴力计算:{}={}={}",zlLatexCal,zlCalculate,zlCalRt);
    }
}

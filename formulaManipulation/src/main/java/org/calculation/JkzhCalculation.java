package org.calculation;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.calParam.JkzhBasicParam;
import org.config.GetValueModelEnum;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.element.*;
import org.enumUtils.ZDEqualsBDKindsEnum;
import org.enums.CalculateSectionEnum;
import org.enums.WaterWhichEnum;
import org.getValue.JkzhGetValues;
import org.handle.JkzhFromulaHandle;
import org.handler.CalHandler;
import org.handler.SolveEquationsHandler;
import org.handler.SolvePowEquationsHandler;
import org.show.JkzhCalTemporaryPart;
import org.show.JkzhFormulaLayout;
import org.show.JkzhPrefixLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 基坑支护计算
 */
@Slf4j
@Data
public class JkzhCalculation{

    private JkzhFromulaHandle jkzhFromulaHandle;
    private JkzhContext jkzhContext;
    private JkzhPrefixLayout jkzhPrefixLayout;
    private JkzhFormulaLayout jkzhFormulaLayout;

    public JkzhCalculation(JkzhContext jkzhContext) {
        this.jkzhFromulaHandle = new JkzhFromulaHandle();
        this.jkzhPrefixLayout = new JkzhPrefixLayout();
        this.jkzhFormulaLayout = new JkzhFormulaLayout();
        this.jkzhContext = jkzhContext;
        createFixedElement();
    }

    /**
     * 主动土压力强度计算
     */
    public void zdPressure(){
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力计算,this.jkzhContext);
        int layer = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getAllLands();
        //②、计算主动土压力强度
        for(int i = 1; i <= layer; i++){
            String  latexCalUp = this.jkzhFromulaHandle.soilPressureToLatex(
                    i,
                    1,
                    i,
                    CalculateSectionEnum.上顶面,
                    JkzhConfigEnum.主动土压力,
                    WaterWhichEnum.主动侧水位,
                    jkzhGetValues);
            String  calUp = this.jkzhFromulaHandle.soilPressureToCal(
                    i,
                    1,
                    i,
                    CalculateSectionEnum.上顶面,
                    JkzhConfigEnum.主动土压力,
                    WaterWhichEnum.主动侧水位,
                    jkzhGetValues);
            this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力上"+i,calUp);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力上"+i,new FormulaElement(i,this.jkzhPrefixLayout,"主动土压力上",latexCalUp+"="+calUp+"kPa"));
            log.info("主动土压力第{}层展示公式—上:{}={}",i,latexCalUp,calUp);
            String latexCalDown = this.jkzhFromulaHandle.soilPressureToLatex(
                    i,
                    1,
                    i,
                    CalculateSectionEnum.下底面,
                    JkzhConfigEnum.主动土压力,
                    WaterWhichEnum.主动侧水位,
                    jkzhGetValues);
            String  calDown = this.jkzhFromulaHandle.soilPressureToCal(
                    i,
                    1,
                    i,
                    CalculateSectionEnum.下底面,
                    JkzhConfigEnum.主动土压力,
                    WaterWhichEnum.主动侧水位,
                    jkzhGetValues);
            log.info("主动土压力第{}层展示公式-下:{}={}",i,latexCalDown,calDown);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土层"+i,new TextElement(i,"主动土层",String.valueOf(i)));
            this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+i,calDown);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+i,new FormulaElement(i,this.jkzhPrefixLayout,"主动土压力下",latexCalDown+"="+calDown+"kPa"));
        }
    }

    /**
     * 被动土压力强度计算
     * @param depth 当前基坑深度,有可能有多个开挖阶段.每一个开挖阶段,开挖深度不一样.
     */
    public void bdPressure(Double depth){
        Integer atLand = depthAtLand(depth);
        //判断开挖深度在第几层土层
        jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setAtDepthLand(atLand);
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动土压力计算,this.jkzhContext);
        //获取计算层次
        int allLands = jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getAllLands();
        int curFloor = atLand;
        for(int i = 1; i <= allLands-atLand+1; i++,curFloor++){
            String  latexCalUp = jkzhFromulaHandle.soilPressureToLatex(
                    i,
                    atLand,
                    curFloor,
                    CalculateSectionEnum.上顶面,
                    JkzhConfigEnum.被动土压力,
                    WaterWhichEnum.被动侧水位,
                    jkzhGetValues);
            String  calUp = jkzhFromulaHandle.soilPressureToCal(
                    i,
                    atLand,
                    curFloor,
                    CalculateSectionEnum.上顶面,
                    JkzhConfigEnum.被动土压力,
                    WaterWhichEnum.被动侧水位,
                    jkzhGetValues);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力上"+curFloor,calUp);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土压力上"+curFloor,new FormulaElement(curFloor,this.jkzhPrefixLayout,"被动土压力上",latexCalUp+"="+calUp+"kPa"));
            log.info("被动土压力第{}层展示公式—上:{}={}",curFloor,latexCalUp,calUp);
            String latexCalDown = jkzhFromulaHandle.soilPressureToLatex(
                    i,
                    atLand,
                    curFloor,
                    CalculateSectionEnum.下底面,
                    JkzhConfigEnum.被动土压力,
                    WaterWhichEnum.被动侧水位,
                    jkzhGetValues);
            String  calDown = jkzhFromulaHandle.soilPressureToCal(
                    i,
                    atLand,
                    curFloor,
                    CalculateSectionEnum.下底面,
                    JkzhConfigEnum.被动土压力,
                    WaterWhichEnum.被动侧水位,
                    jkzhGetValues);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土层"+curFloor,new TextElement(curFloor,"被动土层",String.valueOf(curFloor)));
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力下"+curFloor,calDown);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土压力下"+curFloor,new FormulaElement(curFloor,this.jkzhPrefixLayout,"被动土压力下",latexCalDown+"="+calDown+"kPa"));
            log.info("被动土压力第{}层展示公式-下:{}={}",curFloor,latexCalDown,calDown);
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
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土压力零点所在土层,this.jkzhContext);
        customCalZdPressure(atDepthLand,jkzhGetValues);
        //计算土压力零点在哪一层
        int zeroAtLand = pressureZeroAtLand(depth, atDepthLand, jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getAllLands(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()));
        reCalZDPressureDown(atDepthLand,zeroAtLand);
    }

    /**
     * 弯矩力计算
     */
    public void maxBendingMoment(){
        //最大弯矩模板元素
        List<HashMap<String, BaseElement>> bendingMomentTemplates = new ArrayList<HashMap<String, BaseElement>>(3);
        jkzhContext.setBendingMomentTemplates(bendingMomentTemplates);
        //最大弯矩计算过程的中间结果保存
        List<HashMap<String,String>> bendingMomentValues = new ArrayList<HashMap<String, String>>(3);
        jkzhContext.setBendingMomentValues(bendingMomentValues);

        this.jkzhContext.getBendingMomentTemplates().add(null);
        this.jkzhContext.getBendingMomentValues().add(null);
        calMaxBendingMoment();
    }

    /**
     * 计算各支点之间的最大弯矩
     * 1、计算剪力为零处主动土底面压力强度。
     * 2、重新计算剪力为零这层土的土压力合力。
     * 3、Tc1到剪力为零这层土各土层的土压力合力汇总。
     * 4、支撑位汇总。
     * 5、支撑位汇总=土压力合力合力汇总 求解一元二次方程。
     * 6、解方程，结果为剪力为零的位置。
     * 7、把解出的方程值，代入剪力为零这层土的土压力合力和作用点位置公式
     *    求出结果。
     * 8、列出求矩公式
     *    左边：Σ支撑力到剪力零点的距离+max
     *    右边：Σ土压力合力*作用点位置到剪力为零的距离。
     * 9、解出一元一次方程，结果既是Max
     */
    private void calMaxBendingMoment(){
        //有多少个工况，就会有多少个支点。不存在有工况没有支点的情况
        //并且，计算弯矩的数量大于等于工况数量+1。因为，弯矩还需要计算
        //基坑底面以下的一个。
        int allAxisaCount = this.jkzhContext.getJkzhBasicParams().size();
        boolean isCalLastLand = Boolean.FALSE;
        //找出剪力为零的位置
        for (int i = 1; i <= allAxisaCount; i++) {
            HashMap<String, BaseElement> baseElementMap = new HashMap<String, BaseElement>(64);
            this.jkzhContext.getBendingMomentTemplates().add(i,baseElementMap);
            HashMap<String,String> valuesMap = new HashMap<String, String>(64);
            this.jkzhContext.getBendingMomentValues().add(i,valuesMap);
            //下一个支点
            int twoAxisIndex = i+1;
            //最后一个支点到基坑底面 或者 基坑底面一下
            if(twoAxisIndex>=allAxisaCount){
                //最后一个支点到基坑底面没有算过，那么就计算这一部分
                if(!isCalLastLand){
                    //设置当前第几工况
                    this.jkzhContext.setCalTimes(i);
                    //设置当前计算第几个支点
                    this.jkzhContext.setTcTimes(i);
                    //重新土压力零点所在土层，主动土、被动土下底面的土压力强度
                    recalculateZdAndBdAtZoneLand();
                    //获取基坑底面所在土层
                    int twoAxisAtLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();
                    int tcAtLand = doSecondPartMaxBendingMoment(twoAxisAtLand, i);
                    isCalLastLand = Boolean.TRUE;
                    maxTcLandNotExist(tcAtLand,i);
                }else{
                    //设置当前第几工况
                    this.jkzhContext.setCalTimes(allAxisaCount-1);
                    //设置当前计算第几个支点
                    this.jkzhContext.setTcTimes(i);
                    //重新土压力零点所在土层，主动土、被动土下底面的土压力强度
                    recalculateZdAndBdAtZoneLand();
                    int tcAtLand = doThirdPartMaxBendingMoment(i,0);
                    maxTcLandNotExist(tcAtLand,i);
                }
            }else{
                //设置当前第几工况
                this.jkzhContext.setCalTimes(i);
                //设置当前计算第几个支点
                this.jkzhContext.setTcTimes(i);
                //重新土压力零点所在土层，主动土、被动土下底面的土压力强度
                recalculateZdAndBdAtZoneLand();
                //获取第二个支点所在土层
                int twoAxisAtLand = this.jkzhContext.getJkzhBasicParams().get(twoAxisIndex).getCalResult().getAxisAtLand();
                int tcAtLand = doFirstPartMaxBendingMoment(twoAxisAtLand, i);
                maxTcLandNotExist(tcAtLand,i);
            }
        }
    }

    /**
     * 重新土压力零点所在土层，主动土、被动土下底面的土压力强度
     */
    private void recalculateZdAndBdAtZoneLand(){
        //重新计算土压力零点所在土层的主动土下底面土压力强度；
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力计算,this.jkzhContext);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);
        //计算主动土压力合力
        recalZdResultantEarthPressures(atZoneLand);
        //主动土压力合力作用点位置
        recalZdPositionAction(atZoneLand);
        //重新计算土压力零点所在土层的被动土下底面土压力强度；
        int atDepthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动土压力计算,this.jkzhContext);
        customCalBdPressure(atZoneLand,atDepthLand,jkzhBDGetValues);
        //计算被动土压力合力
        recalBdResultantEarthPressures(atDepthLand);
        //被动土压力合力作用点位置
        recalBdPositionAction(atDepthLand);
    }

    /**
     * 计算基坑底面以上，各支点的弯矩
     * @param lastAtLand 后一支点所在第几层土层
     * @param index 当前计算第几个支点的弯矩
     */
    private int doFirstPartMaxBendingMoment(int lastAtLand,int index){
        //获取计算的支撑轴力
        String tcAtSLand = calStrutForceMax(index);
        Double tcAtDVLand = Double.valueOf(tcAtSLand);
        //试算一下剪力为零的点在第几层土
        int tcAtLand = isExistsTc(tcAtDVLand,lastAtLand);
        //表明存在弯矩为0的点，isExistsTc 是弯矩为零的点所在土层
        if(tcAtLand != 0){
            int befor = index;
            int after = befor +index;
            this.jkzhContext.getBendingMomentTemplates().get(index).put("前一支撑",new SingleFormulaElement(befor,jkzhPrefixLayout,"前一支撑",jkzhPrefixLayout.getLayoutMap().get("前一支撑")));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("后一支撑",new SingleFormulaElement(after,jkzhPrefixLayout,"后一支撑",jkzhPrefixLayout.getLayoutMap().get("后一支撑")));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("第几弯矩",new SingleFormulaElement(befor,jkzhPrefixLayout,"第几弯矩",jkzhPrefixLayout.getLayoutMap().get("最大弯矩")));
            String tcSValue = this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("支撑轴力");
            this.jkzhContext.getBendingMomentTemplates().get(index).put("支点反力值",new FormulaElement(befor,jkzhPrefixLayout,"支点反力值",tcSValue+"kN/m"));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("剪力为零点所在土层",new TextElement(befor,"剪力为零点所在土层",String.valueOf(tcAtLand)));
            //设置当前工况，弯矩为0的点在第几层土
            this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setMaxTcLand(tcAtLand);

            //1、计算剪力为零处主动土底面压力强度。
            reCalZDPressure(index,tcAtLand);
            //2、重新计算剪力为零这层土的土压力合力。
            reCalZDZeroStrutForce(index,tcAtLand);
            //3、重新计算剪力为零这层土的作用点位置。
            reZeroZDPositionAction(index,tcAtLand);
            //4、支点到剪力为零这层土各土层的土压力合力汇总。
            calZDEarthPressuresSum(index,tcAtLand);
            //5、两个支点之间支撑轴力汇总
            calStrutForceMax(index,index);
            //6、两个支点之间支撑轴力汇总=土压力合力合力汇总 求解一元二次方程。
            solveEquationsX(index,tcAtLand);
            //7、把解出的方程值，代入剪力为零这层土的土压力合力和作用点位置公式求出结果。
            reZdCalStrutForceSubstituteX(index,tcAtLand);
            //8、获取剪力为零的位置，重新计算某一土层的土压力作用点位置
            reZdPositionActionSubstituteX(index,tcAtLand);
            //9、列出求矩公式
            //  左边：Σ支撑力到剪力零点的距离+max
            sumStrutForce(index,index);
            //	右边：Σ主动土压力合力*作用点位置到剪力为零的距离。
            sumZdResultant(index,tcAtLand);
            //10、解出一元一次方程，结果既是Max
            calMaxBendingMoment(index,tcAtLand);
        }
        return tcAtLand;
    }

    /**
     * 计算最后一个支点到基坑底面的弯矩
     * @param lastAtLand
     */
    private int doSecondPartMaxBendingMoment(int lastAtLand,int index){
        //重新计算基坑底面切面，主动土压力值
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.重新计算基坑底面切面主动土压力,this.jkzhContext);
        reCalZdPressure(lastAtLand,jkzhGetValues,index);
        // 在开挖深度这一土层,为了估算最后一个支点到基坑底面之间是否存在剪力为零的点，
        // 需要重新计算这一层的主动土压力合力。在估算的时候，这一层土压力合力，以重算的代入。
        reCalStrutForce(index,lastAtLand);
        //试算一下剪力为零的点在第几层土
        String tcAtSLand = calStrutForceMax(index);
        //获取计算的支撑轴力
        Double tcAtDVLand = Double.valueOf(tcAtSLand);
        int tcAtLand = isExistsTc(tcAtDVLand,lastAtLand);
        //表明存在弯矩为0的点，isExistsTc 是弯矩为零的点所在土层
        if(tcAtLand != 0){
            int befor = index;
            this.jkzhContext.getBendingMomentTemplates().get(index).put("前一支撑",new SingleFormulaElement(befor,jkzhPrefixLayout,"前一支撑",jkzhPrefixLayout.getLayoutMap().get("前一支撑")));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("后一支撑",new TextElement(0,"后一支撑","基坑底面"));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("第几弯矩",new SingleFormulaElement(befor,jkzhPrefixLayout,"第几弯矩",jkzhPrefixLayout.getLayoutMap().get("第几弯矩")));
            String tcSValue = this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("支撑轴力");
            this.jkzhContext.getBendingMomentTemplates().get(index).put("支点反力值",new FormulaElement(befor,jkzhPrefixLayout,"支点反力值",tcSValue+"kN/m"));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("剪力为零点所在土层",new TextElement(befor,"剪力为零点所在土层",String.valueOf(tcAtLand)));
            //设置当前工况，弯矩为0的点在第几层土
            this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setMaxTcLand(tcAtLand);

            //1、计算剪力为零处主动土底面压力强度。
            reCalZDPressure(index,tcAtLand);
            //2、重新计算剪力为零这层土的土压力合力。
            reCalZDZeroStrutForce(index,tcAtLand);
            //3、重新计算剪力为零这层土的作用点位置。
            reZeroZDPositionAction(index,tcAtLand);
            //4、支点到剪力为零这层土各土层的土压力合力汇总。
            calZDEarthPressuresSum(index,tcAtLand);
            //5、两个支点之间支撑轴力汇总
            calStrutForceMax(index,index);
            //6、两个支点之间支撑轴力汇总=土压力合力合力汇总 求解一元二次方程。
            solveEquationsX(index,tcAtLand);
            //7、把解出的方程值，代入剪力为零这层土的土压力合力和作用点位置公式求出结果。
            reZdCalStrutForceSubstituteX(index,tcAtLand);
            //8、获取剪力为零的位置，重新计算某一土层的土压力作用点位置
            reZdPositionActionSubstituteX(index,tcAtLand);
            //9、列出求矩公式
            //  左边：Σ支撑力到剪力零点的距离+max
            sumStrutForce(index,index);
            //	右边：Σ土压力合力*作用点位置到剪力为零的距离。
            sumZdResultant(index,tcAtLand);
            //10、解出一元一次方程，结果既是Max
            calMaxBendingMoment(index,tcAtLand);
        }
        return tcAtLand;
    }

    /**
     * 计算基坑底面以下的弯矩
     */
    private int doThirdPartMaxBendingMoment(int index,int tcAtLand){
        int tcCount = this.jkzhContext.getJkzhBasicParams().size()-1;
        //获取计算的支撑轴力
        String tcAtSLand = calStrutForceMax(tcCount);
        Double tcAtDVLand = Double.parseDouble(tcAtSLand);
        //试算一下剪力为零的点在第几层土
        if(tcAtLand == 0){
            tcAtLand = isThirdExistsTc(tcAtDVLand);
        }
        //表明存在弯矩为0的点，isExistsTc 是弯矩为零的点所在土层
        if(tcAtLand != 0){
            int befor = index;
            this.jkzhContext.getBendingMomentTemplates().get(index).put("第几弯矩",new SingleFormulaElement(befor,jkzhPrefixLayout,"第几弯矩",jkzhPrefixLayout.getLayoutMap().get("最大弯矩")));
            this.jkzhContext.getBendingMomentTemplates().get(index).put("剪力为零点所在土层",new TextElement(befor,"剪力为零点所在土层",String.valueOf(tcAtLand)));
            //设置当前工况，弯矩为0的点在第几层土
            this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setMaxTcLand(tcAtLand);

            //1、计算剪力为零处主动土底面压力强度。
            reCalZDPressure(index,tcAtLand);
            //2、计算剪力为零处被动土底面压力强度。
            reCalBDPressure(index,tcAtLand);
            //3、重新计算剪力为零这层土的主动土压力合力。
            reCalZDZeroStrutForce(index,tcAtLand);
            //4、重新计算剪力为零这层土的被动土压力合力。
            reCalBDZeroStrutForce(index,tcAtLand);
            //5、重新计算剪力为零这层土的主动作用点位置。
            reZeroZDPositionAction(index,tcAtLand);
            //6、重新计算剪力为零这层土的被动作用点位置。
            reZeroBDPositionAction(index,tcAtLand);
            //7、支点到剪力为零这层土各土层的主动土压力合力汇总。
            calZDEarthPressuresSum(index,tcAtLand);
            //8、支点到剪力为零这层土各土层的被动土压力合力汇总。
            calBDEarthPressuresSum(index,tcAtLand);
            //9、两个支点之间支撑轴力汇总
            calStrutForceMax(index,tcCount);
            //10、两个支点之间支撑轴力汇总+被动土压力合力=土压力合力合力汇总 求解一元二次方程。
            Double solveEquations = solveEquationsX(index, tcAtLand);
            if(isOutsideLand(tcAtLand,solveEquations)){
                doThirdPartMaxBendingMoment(index,++tcAtLand);
                return tcAtLand;
            }
            //11、把解出的方程值，代入剪力为零这层土的主动土压力合力和作用点位置公式求出结果。
            reZdCalStrutForceSubstituteX(index,tcAtLand);
            //12、把解出的方程值，代入剪力为零这层土的被动土压力合力和作用点位置公式求出结果。
            reBdCalStrutForceSubstituteX(index,tcAtLand);
            //13、获取剪力为零的位置，重新计算某一土层的主动土压力作用点位置
            reZdPositionActionSubstituteX(index,tcAtLand);
            //14、获取剪力为零的位置，重新计算某一土层的被动土压力作用点位置
            reBdPositionActionSubstituteX(index,tcAtLand);
            //15、列出求矩公式
            //  左边：Σ支撑力到剪力零点的距离+max
            sumStrutForce(index,tcCount);
            //	左边：Σ被动土压力合力*作用点位置到剪力为零的距离。
            sumBdResultant(index,tcAtLand);
            //	右边：Σ主动土压力合力*作用点位置到剪力为零的距离。
            sumZdResultant(index,tcAtLand);
            //14、解出一元一次方程，结果既是Max
            calMaxBendingMoment(index,tcAtLand);
        }
        return tcAtLand;
    }

    /**
     * 重新计算基坑底面切面，主动土压力值
     * @param atLand 重新计算第几层土的主动土压力底层数据
     * @param jkzhGetValues 获取这一层土厚度的方式
     */
    private void reCalZdPressure(int atLand,JkzhGetValues jkzhGetValues,int index){
        String  calDown = jkzhFromulaHandle.soilPressureToCal(
                atLand,
                1,
                atLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                jkzhGetValues);
        log.info("重新计算基坑底面切面，主动土压力值:{}",calDown);
        jkzhContext.getBendingMomentValues().get(index).put("主动土压力下",calDown);
    }

    /**
     * 判断这个支撑轴力是否存在一个临界点。
     * T1~T2土层之间是否存在临界点、T2~T3土层之间存在临界点。
     * ...Tn到基坑底面是否存在临界点、基坑底面以下是否存在临界点。
     * @return
     */
    private int isExistsTc(Double tc,int twoAxisAtLand){
        int result = 0;
        //上一个支点所在土层位置
        Double value = 0.0;
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土层之上各主动土压力合力之和,this.jkzhContext);
        //当前支点以上，各支点的和。若是计算基坑底面以下，则需要加上被动土压力合力
        for (int i = 1; i <= twoAxisAtLand; i++) {
            String zdEarthPressuresSum = calZdEarthPressuresSum(i,jkzhGetValues);
            value = Double.valueOf(zdEarthPressuresSum);
            if(value.compareTo(tc)>=0){
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     *
     * @return
     */
    private int isThirdExistsTc(Double tc){
        int result = 0;
        //获取基坑所在第几层土
        int size = this.jkzhContext.getJkzhBasicParams().size();
        int depthLand = this.jkzhContext.getJkzhBasicParams().get(size-1).getCalResult().getAtDepthLand();
        JkzhGetValues jkzhZdGetValues = new JkzhGetValues(JkzhGetValueModelEnum.基坑底面以下主动土压力合力之和,this.jkzhContext);
        JkzhGetValues jkzhBdGetValues = new JkzhGetValues(JkzhGetValueModelEnum.基坑底面以下被动土压力合力之和,this.jkzhContext);
        for (int iLand = depthLand; iLand <= this.jkzhContext.getJkzhBasicParams().get(size-1).getAllLands(); iLand ++) {
            //基坑底面以上各层主动土合力汇总(包括基坑所在土层)。
            String zdEarthPressuresSum = calZdEarthPressuresSum(iLand,jkzhZdGetValues);
            Double zdEarthPressuresSumD = Double.parseDouble(zdEarthPressuresSum);
            //基坑底面以下各层被动土合力汇总(包括基坑所在土层)。
            String bdEarthPressuresSum = calBdEarthPressuresSum(iLand,jkzhBdGetValues);
            Double bdEarthPressuresSumD = Double.parseDouble(bdEarthPressuresSum);
            if(zdEarthPressuresSumD.compareTo(bdEarthPressuresSumD+tc)>=0){
                result = iLand;
                break;
            }
        }
        return result;
    }

    /**
     * 计算剪力为零处主动土底面压力强度。
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reCalZDPressure(int index,int tcAtLand){
        //获取这一层土顶面土压力强度
        String calUp = this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力上" + tcAtLand);
        this.jkzhContext.getBendingMomentTemplates().get(index).put("主动土压力上",new FormulaElement(tcAtLand,jkzhPrefixLayout,"主动土压力上",calUp+"kPa"));
        //获取这一层土底面土压力强度，这是带有参数x
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零处主动土底面压力强度,this.jkzhContext);
        String latexCalDown = this.jkzhFromulaHandle.soilPressureToLatex(
                tcAtLand,
                1,
                tcAtLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                jkzhGetValues);
        String  calDown = this.jkzhFromulaHandle.soilPressureToCalX(
                tcAtLand,
                1,
                tcAtLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                jkzhGetValues);
        log.info("求弯矩主动土压力第{}层展示公式-下:{}={}",index,latexCalDown,calDown);
        this.jkzhContext.getBendingMomentTemplates().get(index).put("主动土压力下",new FormulaElement(tcAtLand,jkzhPrefixLayout,"主动土压力下",latexCalDown));
        this.jkzhContext.getBendingMomentValues().get(index).put("主动土压力下",calDown);
    }

    /**
     * 计算剪力为零处主动土底面压力强度。
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reCalBDPressure(int index,int tcAtLand){
        //获取这一层土顶面土压力强度
        String calUp = this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力上" + tcAtLand);
        this.jkzhContext.getBendingMomentTemplates().get(index).put("被动土压力上",new FormulaElement(tcAtLand,jkzhPrefixLayout,"被动土压力上",calUp+"kPa"));
        //获取这一层土底面土压力强度，这是带有参数x
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零处被动土底面压力强度,this.jkzhContext);
        int depthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand();
        String latexCalDown = this.jkzhFromulaHandle.soilPressureToLatex(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.被动土压力,
                WaterWhichEnum.被动侧水位,
                jkzhGetValues);
        String  calDown = this.jkzhFromulaHandle.soilPressureToCalX(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.被动土压力,
                WaterWhichEnum.被动侧水位,
                jkzhGetValues);
        log.info("求弯矩被动土压力第{}层展示公式-下:{}={}",index,latexCalDown,calDown);
        this.jkzhContext.getBendingMomentTemplates().get(index).put("被动土压力下",new FormulaElement(tcAtLand,jkzhPrefixLayout,"被动土压力下",latexCalDown));
        this.jkzhContext.getBendingMomentValues().get(index).put("被动土压力下",calDown);
    }

    /**
     * 剪力为零这层土的土压力合力
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void reCalZDZeroStrutForce(int index,int tcAtLand){
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的主动土压力合力,this.jkzhContext,GetValueModelEnum.Latex模式);
        String latexCal = jkzhFromulaHandle.extendToLatex(
                tcAtLand,
                JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                jkzhZDGetValues);
        jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的主动土压力合力,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalX(
                tcAtLand,
                JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                jkzhZDGetValues);
        log.info("剪力为零这层土的主动土压力合力:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("主动土压力合力",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("主动土压力合力",new TextElement(tcAtLand,"主动土压力合力",latexCal));
    }

    /**
     * 剪力为零这层土的被动土压力合力
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void reCalBDZeroStrutForce(int index,int tcAtLand){
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的被动土压力合力,this.jkzhContext,GetValueModelEnum.Latex模式);
        String latexCal = jkzhFromulaHandle.extendToLatex(
                tcAtLand,
                JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                jkzhZDGetValues);
        jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的被动土压力合力,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalX(
                tcAtLand,
                JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                jkzhZDGetValues);
        log.info("求剪力为零这层土的被动土压力合力:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("被动土压力合力",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("被动土压力合力",new TextElement(tcAtLand,"被动土压力合力",latexCal));
    }

    /**
     * 重新计算某一层土的主动作用点位置
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void reZeroZDPositionAction(int index,int tcAtLand){
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的主动作用点位置,this.jkzhContext,GetValueModelEnum.Latex模式);
        String latexCal = jkzhFromulaHandle.extendToLatex(
                tcAtLand,
                JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                jkzhZDGetValues);
        jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的主动作用点位置,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalX(
                tcAtLand,
                JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                jkzhZDGetValues);
        log.info("重新计算某一层土的主动作用点位置:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("主动土作用点位置",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("主动土作用点位置",new TextElement(tcAtLand,"主动土作用点位置",latexCal));
    }

    /**
     * 重新计算某一层土的被动作用点位置
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void reZeroBDPositionAction(int index,int tcAtLand){
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的被动作用点位置,this.jkzhContext,GetValueModelEnum.Latex模式);
        String latexCal = jkzhFromulaHandle.extendToLatex(
                tcAtLand,
                JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                jkzhZDGetValues);
        jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.剪力为零这层土的被动作用点位置,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalX(
                tcAtLand,
                JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                jkzhZDGetValues);
        log.info("剪力为零这层土的被动作用点位置:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("被动土作用点位置",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("被动土作用点位置",new TextElement(tcAtLand,"被动土作用点位置",latexCal));
    }

    /**
     * 剪力为零以上土层的主动土压力合力汇总
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void calZDEarthPressuresSum(int index,int tcAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土层之上各主动土压力合力之和,this.jkzhContext, GetValueModelEnum.Latex模式);
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                tcAtLand,
                1,
                tcAtLand,
                JkzhConfigEnum.土层之上各主动土压力合力之和,
                jkzhGetValues);

        jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土层之上各主动土压力合力之和,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                tcAtLand,
                1,
                tcAtLand,
                JkzhConfigEnum.土层之上各主动土压力合力之和,
                jkzhGetValues);
        log.info("剪力为零以上土层的主动土压力合力汇总公式:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("土层之上各主动土压力合力之和",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("土层之上各主动土压力合力之和",new TextElement(tcAtLand,"土层之上各主动土压力合力之和",latexCal));
    }

    /**
     * 剪力为零以上土层的被动土压力合力汇总
     * @param index 当前计算第几个支点
     * @param tcAtLand 当前支点对应剪力为0位置所在土层
     */
    private void calBDEarthPressuresSum(int index,int tcAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土层之上各被动土压力合力之和,this.jkzhContext,GetValueModelEnum.Latex模式);
        int depthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand();
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                JkzhConfigEnum.土层之上各被动土压力合力之和,
                jkzhGetValues);
        jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土层之上各被动土压力合力之和,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                JkzhConfigEnum.土层之上各被动土压力合力之和,
                jkzhGetValues);
        log.info("土层之上各被动土压力合力之和:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("土层之上各被动土压力合力之和",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("土层之上各被动土压力合力之和",new TextElement(tcAtLand,"土层之上各被动土压力合力之和",latexCal));
    }

    /**
     * 重新计算某一层土的土压力合力
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reCalStrutForce(int index,int tcAtLand){
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.最后一个支点到基坑底面重算主动土压力合力,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCal(
                tcAtLand,
                JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                jkzhZDGetValues);
        log.info("最后一个支点到基坑底面重算主动土压力合力:{}",calculate);
        jkzhContext.getBendingMomentValues().get(index).put("主动土压力合力",calculate);
    }

    /**
     * 当前土层以上，各土层的主动土压力合力之和
     */
    private String calZdEarthPressuresSum(int whichAxisAtLand,JkzhGetValues jkzhGetValues){
        //等式右边部分
        String calculate = jkzhFromulaHandle.extendToCalN(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.土层之上各主动土压力合力之和,
                jkzhGetValues);
        log.info("土层之上各主动土压力合力之和:{}",calculate);
        return calculate;
    }

    /**
     * 当前土层以上，各土层的被动土压力合力之和
     */
    private String calBdEarthPressuresSum(int whichAxisAtLand,JkzhGetValues jkzhGetValues){
        //获取基坑所在第几层土
        int depthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand();
        String calculate = jkzhFromulaHandle.extendToCalN(
                whichAxisAtLand-depthLand+1,
                depthLand,
                whichAxisAtLand,
                JkzhConfigEnum.土层之上各被动土压力合力之和,
                jkzhGetValues);
        log.info("土层之上各被动土压力合力之和:{}",calculate);
        return calculate;
    }

    /**
     * 两个支点之间支撑轴力汇总
     */
    private String calStrutForceMax(int whichAxisAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.两个支点之间支撑轴力汇总,this.jkzhContext);
        String calculate = jkzhFromulaHandle.extendToCalN(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.两个支点之间支撑轴力汇总,
                jkzhGetValues);
        log.info("两个支点之间支撑轴力汇总:{}",calculate);
        return calculate;
    }

    /**
     * 两个支点之间支撑轴力汇总
     * @param index 当前计算第几个支点
     * @param whichAxisAtLand 当前第几个支点
     */
    private void calStrutForceMax(int index,int whichAxisAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.两个支点之间支撑轴力汇总,this.jkzhContext);
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.两个支点之间支撑轴力汇总,
                jkzhGetValues);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.两个支点之间支撑轴力汇总,
                jkzhGetValues);
        log.info("求两个支点之间支撑轴力汇总展示公式:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("两个支点之间支撑轴力汇总",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("两个支点之间支撑轴力汇总",new TextElement(whichAxisAtLand,"两个支点之间支撑轴力汇总",latexCal));
    }

    /**
     * 解剪力为零点的方程，这是一个一元二次方程
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private Double solveEquationsX(int index,int tcAtLand){
        //表明这个最大弯矩是在基坑底面，是需要加上被动土压力合力
        String[] replaces = null;
        if(tcAtLand >= jkzhContext.getJkzhBasicParams().get(jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand()){
            replaces = new String[]{"两个支点之间支撑轴力汇总", "土层之上各主动土压力合力之和","土层之上各被动土压力合力之和"};
        }else{
            replaces = new String[]{"两个支点之间支撑轴力汇总", "土层之上各主动土压力合力之和"};
        }
        JkzhCalTemporaryPart partLatex = new JkzhCalTemporaryPart(replaces);
        for(String item:replaces){
            BaseElement zdLatexCal = jkzhContext.getBendingMomentTemplates().get(index).get(item);
            partLatex.getLayoutMap().put(item,zdLatexCal.getValue().toString());
        }

        //获取最终开挖到第几层土
        int size = this.jkzhContext.getJkzhBasicParams().size();
        JkzhBasicParam jkzhBasicParam = this.jkzhContext.getJkzhBasicParams().get(size - 1);
        int lastDepthLand = jkzhBasicParam.getCalResult().getAtDepthLand();
        String zlLatexCal = jkzhFromulaHandle.maxBendingMomentToLatex(
                tcAtLand,
                lastDepthLand,
                JkzhConfigEnum.最大弯矩位置.getLatexCal(),
                partLatex);

        JkzhCalTemporaryPart partCal = new JkzhCalTemporaryPart(replaces);
        for(String item:replaces){
            String s = jkzhContext.getBendingMomentValues().get(index).get(item);
            partCal.getLayoutMap().put(item,s);
        }
        String zlCalculate = jkzhFromulaHandle.maxBendingMomentToLatex(
                tcAtLand,
                lastDepthLand,
                JkzhConfigEnum.最大弯矩位置.getCalculate(),
                partCal);
        log.info("支撑轴力计算:{}={}",zlLatexCal,zlCalculate);
        jkzhContext.getBendingMomentValues().get(index).put("解方程公式",zlCalculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("解方程公式",new FormulaElement(tcAtLand,jkzhPrefixLayout,"解方程公式",zlLatexCal));

        //解方程
        SolvePowEquationsHandler solvePowEquationsHandler = new SolvePowEquationsHandler();
        String result = solvePowEquationsHandler.execute(zlCalculate);
        jkzhContext.getBendingMomentValues().get(index).put("剪力为零位置值",result);
        jkzhContext.getBendingMomentTemplates().get(index).put("剪力为零位置值",new TextElement(index,"剪力为零位置值",result));
        Double aDouble = Double.valueOf(result);
        Double maxTcDepth = betweenLandDepth(1,tcAtLand,aDouble);
        jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().setMaxTcDepth(maxTcDepth);
        return aDouble;
    }

    /**
     * 获取剪力为零的位置，重新计算某一土层的土压力合力
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reZdCalStrutForceSubstituteX(int index,int tcAtLand){
        //获取x计算结果
        String jlZoreValue = jkzhContext.getBendingMomentValues().get(index).get("剪力为零位置值");
        String maxPointZdCal = jkzhContext.getBendingMomentValues().get(index).get("主动土压力合力");
        maxPointZdCal = maxPointZdCal.replace("x", jlZoreValue);

        BaseElement baseElement = jkzhContext.getBendingMomentTemplates().get(index).get("主动土压力合力");
        String maxPointZdLatex = (String)baseElement.getValue();
        maxPointZdLatex = maxPointZdLatex.replace("x", jlZoreValue);

        CalHandler calHandler = new CalHandler();
        String calculate = calHandler.execute(maxPointZdCal);
        jkzhContext.getBendingMomentValues().get(index).put("主动土压力合力",calculate);

        log.info("重新计算某一土层的土压力合力:{}={}",maxPointZdLatex,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("主动土压力合力计算",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("主动土压力合力计算",new FormulaElement(tcAtLand,jkzhPrefixLayout,"主动土压力合力计算",maxPointZdLatex+"="+calculate+"kN/m"));
    }

    /**
     * 获取剪力为零的位置，重新计算某一土层的土压力合力
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reBdCalStrutForceSubstituteX(int index,int tcAtLand){
        //获取x计算结果
        String jlZoreValue = jkzhContext.getBendingMomentValues().get(index).get("剪力为零位置值");
        String maxPointZdCal = jkzhContext.getBendingMomentValues().get(index).get("被动土压力合力");
        maxPointZdCal = maxPointZdCal.replace("x", jlZoreValue);

        BaseElement baseElement = jkzhContext.getBendingMomentTemplates().get(index).get("被动土压力合力");
        String maxPointZdLatex = (String)baseElement.getValue();
        maxPointZdLatex = maxPointZdLatex.replace("x", jlZoreValue);

        CalHandler calHandler = new CalHandler();
        String calculate = calHandler.execute(maxPointZdCal);
        jkzhContext.getBendingMomentValues().get(index).put("被动土压力合力",calculate);

        log.info("重新计算某一土层的土压力合力:{}={}",maxPointZdLatex,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("被动土压力合力计算",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("被动土压力合力计算",new FormulaElement(tcAtLand,jkzhPrefixLayout,"被动土压力合力计算",maxPointZdLatex+"="+calculate+"kN/m"));
    }

    /**
     * 获取剪力为零的位置，重新计算某一土层的土压力作用点位置
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reZdPositionActionSubstituteX(int index,int tcAtLand){
        //获取x计算结果
        //获取x计算结果
        String jlZoreValue = jkzhContext.getBendingMomentValues().get(index).get("剪力为零位置值");
        String maxPointZdCal = jkzhContext.getBendingMomentValues().get(index).get("主动土作用点位置");
        maxPointZdCal = maxPointZdCal.replace("x", jlZoreValue);

        BaseElement baseElement = jkzhContext.getBendingMomentTemplates().get(index).get("主动土作用点位置");
        String maxPointZdLatex = (String)baseElement.getValue();
        maxPointZdLatex = maxPointZdLatex.replace("x", jlZoreValue);

        CalHandler calHandler = new CalHandler();
        String calculate = calHandler.execute(maxPointZdCal);
        jkzhContext.getBendingMomentValues().get(index).put("主动土作用点位置",calculate);

        log.info("求某一土层的土压力作用点位置:{}={}",maxPointZdLatex,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("主动作用点位置计算",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("主动作用点位置计算",new FormulaElement(tcAtLand,jkzhPrefixLayout,"主动作用点位置计算",maxPointZdLatex+"="+calculate+"m"));
    }

    /**
     * 获取剪力为零的位置，重新计算某一土层的土压力作用点位置
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void reBdPositionActionSubstituteX(int index,int tcAtLand){
        //获取x计算结果
        String jlZoreValue = jkzhContext.getBendingMomentValues().get(index).get("剪力为零位置值");
        String maxPointZdCal = jkzhContext.getBendingMomentValues().get(index).get("被动土作用点位置");
        maxPointZdCal = maxPointZdCal.replace("x", jlZoreValue);

        BaseElement baseElement = jkzhContext.getBendingMomentTemplates().get(index).get("被动土作用点位置");
        String maxPointZdLatex = (String)baseElement.getValue();
        maxPointZdLatex = maxPointZdLatex.replace("x", jlZoreValue);

        CalHandler calHandler = new CalHandler();
        String calculate = calHandler.execute(maxPointZdCal);
        jkzhContext.getBendingMomentValues().get(index).put("被动土作用点位置",calculate);

        log.info("求某一土层的土压力作用点位置:{}={}",maxPointZdLatex,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("被动作用点位置计算",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("被动作用点位置计算",new FormulaElement(tcAtLand,jkzhPrefixLayout,"被动作用点位置计算",maxPointZdLatex+"="+calculate+"m"));
    }

    /**
     * Σ支撑力乘以支撑位置到剪力零点的距离+max
     * @param index 当前第几个支点
     * @param whichAxisAtLand 当前第几个支点
     */
    private void sumStrutForce(int index,int whichAxisAtLand){
        //等式左边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.最大弯矩支撑轴矩,this.jkzhContext);
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.最大弯矩支撑轴矩,
                jkzhGetValues);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                whichAxisAtLand,
                1,
                whichAxisAtLand,
                JkzhConfigEnum.最大弯矩支撑轴矩,
                jkzhGetValues);
        log.info("Σ支撑力乘以支撑位置到剪力零点的距离展示公式-下:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("最大弯矩支撑轴矩",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("最大弯矩支撑轴矩",new TextElement(whichAxisAtLand,"最大弯矩支撑轴矩",latexCal));
    }

    /**
     * Σ主动土合力*作用点位置到剪力为零的距离。
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void sumZdResultant(int index,int tcAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.最大弯矩主动土合矩,this.jkzhContext);
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                tcAtLand,
                1,
                tcAtLand,
                JkzhConfigEnum.最大弯矩主动土合矩,
                jkzhGetValues);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                tcAtLand,
                1,
                tcAtLand,
                JkzhConfigEnum.最大弯矩主动土合矩,
                jkzhGetValues);
        log.info("求弯矩主动土压力合力展示公式-下:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("最大弯矩主动土合矩",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("最大弯矩主动土合矩",new TextElement(tcAtLand,"最大弯矩主动土合矩",latexCal));
    }

    /**
     * Σ被动动土合力*作用点位置到剪力为零的距离。
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void sumBdResultant(int index,int tcAtLand){
        //等式右边部分
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.最大弯矩被动土合矩,this.jkzhContext);
        int depthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand();
        String latexCal = jkzhFromulaHandle.extendToLatexN(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                JkzhConfigEnum.最大弯矩被动土合矩,
                jkzhGetValues);
        String calculate = jkzhFromulaHandle.extendToCalNX(
                tcAtLand-depthLand+1,
                depthLand,
                tcAtLand,
                JkzhConfigEnum.最大弯矩被动土合矩,
                jkzhGetValues);
        log.info("求弯矩被动土压力合力展示公式-下:{}={}",latexCal,calculate);
        jkzhContext.getBendingMomentValues().get(index).put("最大弯矩被动土合矩",calculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("最大弯矩被动土合矩",new TextElement(tcAtLand,"最大弯矩被动土合矩",latexCal));
    }

    /**
     * 计算最大弯矩
     * @param index 当前第几个支点
     * @param tcAtLand 当前支点对应剪力为零的位置，在哪一个土层
     */
    private void calMaxBendingMoment(int index,int tcAtLand){
        String[] replaces = null;
        if(tcAtLand >= jkzhContext.getJkzhBasicParams().get(jkzhContext.getJkzhBasicParams().size()-1).getCalResult().getAtDepthLand()){
            replaces = new String[]{"最大弯矩支撑轴矩", "最大弯矩主动土合矩", "最大弯矩被动土合矩"};
        }else{
            replaces = new String[]{"最大弯矩支撑轴矩", "最大弯矩主动土合矩"};
        }
        JkzhCalTemporaryPart partLatex = new JkzhCalTemporaryPart(replaces);
        for(String item:replaces){
            BaseElement zdLatexCal = jkzhContext.getBendingMomentTemplates().get(index).get(item);
            partLatex.getLayoutMap().put(item,zdLatexCal.getValue().toString());
        }
        //获取最终开挖到第几层土
        int size = this.jkzhContext.getJkzhBasicParams().size();
        JkzhBasicParam jkzhBasicParam = this.jkzhContext.getJkzhBasicParams().get(size - 1);
        int lastDepthLand = jkzhBasicParam.getCalResult().getAtDepthLand();
        String zlLatexCal = jkzhFromulaHandle.maxBendingMomentToLatex(
                tcAtLand,
                lastDepthLand,
                JkzhConfigEnum.最大弯矩.getLatexCal(),
                partLatex);

        JkzhCalTemporaryPart partCal = new JkzhCalTemporaryPart(replaces);
        for(String item:replaces){
            String zdCal = jkzhContext.getBendingMomentValues().get(index).get(item);
            partCal.getLayoutMap().put(item,zdCal);
        }
        String zlCalculate = jkzhFromulaHandle.maxBendingMomentToLatex(
                tcAtLand,
                lastDepthLand,
                JkzhConfigEnum.最大弯矩.getCalculate(),
                partCal);
        log.info("最大弯矩计算:{}={}",zlLatexCal,zlCalculate);
        jkzhContext.getBendingMomentTemplates().get(index).put("求矩计算",new FormulaElement(index,jkzhPrefixLayout,"求矩计算",zlLatexCal));
        SolveEquationsHandler solveEquationsHandler = new SolveEquationsHandler();
        String execute = solveEquationsHandler.execute(zlCalculate);
        jkzhContext.getBendingMomentValues().get(index).put("矩值",execute);
        jkzhContext.getBendingMomentTemplates().get(index).put("矩值",new FormulaElement(index,jkzhPrefixLayout,"矩值",execute+"kN·m"));
    }

    /**
     * 把模板中的固定元素，填进模板元素集合中
     */
    private void createFixedElement(){
        /**jkzhContext begin*/
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("土层参数计算依据表",new TableElement(0,"土层参数计算依据表",jkzhContext.getSoilQualityTable().getTable()));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("土压力系数表",new TableElement(0,"土压力系数表",jkzhContext.getSoilPressureTable().getTable()));
        /**jkzhContext end*/

        String s_1 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动土压力计算公式",new FormulaElement(0,jkzhPrefixLayout,"主动土压力计算公式",s_1));
        String s_2 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力系数.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动土压力系数计算公式",new FormulaElement(0,jkzhPrefixLayout,"主动土压力系数计算公式",s_2));

        String s_3 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动土压力计算公式",new FormulaElement(0,jkzhPrefixLayout,"被动土压力计算公式",s_3));
        String s_4 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力系数.getLatex());
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动土压力系数计算公式",new FormulaElement(0,jkzhPrefixLayout,"被动土压力系数计算公式",s_4));

        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支护结构外侧",new SingleFormulaElement(0,jkzhPrefixLayout,"支护结构外侧",jkzhPrefixLayout.getLayoutMap().get("支护结构外侧")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支护结构外侧应力",new SingleFormulaElement(0,jkzhPrefixLayout,"支护结构外侧应力",jkzhPrefixLayout.getLayoutMap().get("支护结构外侧应力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支护结构内侧应力",new SingleFormulaElement(0,jkzhPrefixLayout,"支护结构内侧应力",jkzhPrefixLayout.getLayoutMap().get("支护结构内侧应力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("主动土压力系数",new SingleFormulaElement(0,jkzhPrefixLayout,"主动土压力系数",jkzhPrefixLayout.getLayoutMap().get("主动土压力系数")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("被动土压力系数",new SingleFormulaElement(0,jkzhPrefixLayout,"被动土压力系数",jkzhPrefixLayout.getLayoutMap().get("被动土压力系数")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("黏聚力",new SingleFormulaElement(0,jkzhPrefixLayout,"黏聚力",jkzhPrefixLayout.getLayoutMap().get("黏聚力")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("内摩擦角",new SingleFormulaElement(0,jkzhPrefixLayout,"内摩擦角",jkzhPrefixLayout.getLayoutMap().get("内摩擦角")));
        jkzhContext.getElementTemplates().get(jkzhContext.getCalTimes()).put("支护结构内侧",new SingleFormulaElement(0,jkzhPrefixLayout,"支护结构内侧",jkzhPrefixLayout.getLayoutMap().get("支护结构内侧")));
    }

    //计算给定深度，返回深度所在土层
    private Integer depthAtLand(Double depth){
        //判断开挖深度在第几层土层
        String[][] table = this.jkzhContext.getSoilQualityTable().getTable();
        Double sumLands = 0.0;
        int atLand = 0;
        for(int floor = 1;floor <table.length; floor++){
            sumLands += Double.valueOf(table[floor][2]);
            if(sumLands.compareTo(depth) > 0){
                atLand = floor;
                break;
            }else if(sumLands.compareTo(depth) == 0){
                //正好开挖到这层土的底面深度，那么这层土就不算，只算下层土
                atLand = floor+1;
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
        String latexCalDown = jkzhFromulaHandle.soilPressureToLatex(
                atLand,
                1,
                atLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                jkzhGetValues);
        String  calDown = jkzhFromulaHandle.soilPressureToCal(
                atLand,
                1,
                atLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                jkzhGetValues);
        log.info("主动土压力第{}层展示公式-下:{}={}",atLand,latexCalDown,calDown);
        jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+atLand,calDown);
    }

    /**
     * 根据真实土层厚度重新计算指定土层被动土压力强度重新计算
     * @param atLand 重新计算第几层土的被动土压力底层数据
     * @param depthLand 当前开挖深度所在第几层土层
     * @param jkzhGetValues 获取这一层土厚度的方式
     */
    private void customCalBdPressure(int atLand,int depthLand, JkzhGetValues jkzhGetValues){
        String  calDown = jkzhFromulaHandle.soilPressureToCal(
                atLand-depthLand+1,
                depthLand,
                atLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.被动土压力,
                WaterWhichEnum.被动侧水位,
                jkzhGetValues);
        log.info("被动土压力第{}层展示公式-下:{}={}",atLand,calDown,calDown);
        jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力下"+atLand,calDown);
    }

    /**
     * 计算土压力零点在第几层土层。
     * 并且计算土压力零点数值。
     * @param depth 开挖深度所在土层
     * @param atDepthLand 开挖深度所在土层
     * @param allLands 总的土层
     * @param formate 中间计算结果集
     * @return 土压力零点所在土层
     */
    private int pressureZeroAtLand(Double depth,int atDepthLand, int allLands, HashMap<String,String> formate){
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
                //第三种情况
                zoneLand = thirdCase(depth);
                if(zoneLand != 0){
                    thirdCasePressureZero(depth);
                }
            }
        }
        return zoneLand;
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
        String  str = String.format("%.2f",zero);
        double four = Double.parseDouble(str);
        this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setPressureZero(four);
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
            String  str = String.format("%.2f",depth+pressureZero);
            double four = Double.parseDouble(str);
            this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setPressureZero(four);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压零点位置",new TextElement(zoneLand,"土压零点位置","已开挖基坑底面以下x米处"));
        }else{
            for (int i = 1; i < zoneLand; i++) {
                zero += Double.valueOf(this.jkzhContext.getSoilQualityTable().getTable()[i][2]);
            }
            String  str = String.format("%.2f",zero+pressureZero);
            double four = Double.parseDouble(str);
            this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setPressureZero(four);
            this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压零点位置",new TextElement(zoneLand,"土压零点位置","第"+zoneLand+"层土顶面以下x米处"));
        }
    }

    /**
     * 第二种情况，计算土压力零点深度
     * @param depth 当前开挖深度
     * @return
     */
    private void thirdCasePressureZero(Double depth){
        double pressureZero = depth * 1.2;
        String  str = String.format("%.2f",pressureZero);
        double four = Double.parseDouble(str);
        this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setPressureZero(four);
        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压力零点值",new TextElement(1,"土压力零点值",String.format("%f.2",pressureZero)));
        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("零点土压力值",new TextElement(1,"零点土压力值",String.valueOf(pressureZero)));
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
            Double zdUp = Double.valueOf(formate.get("主动土压力上" + i));
            Double bdUp = Double.valueOf(formate.get("被动土压力上"+i));
            Double one = zdUp - bdUp;
            String oneS = String.format("%.2f",one);
            //主动土压力下端-被动土压力下端
            Double zdDown = Double.valueOf(formate.get("主动土压力下"+i));
            Double bdDown = Double.valueOf(formate.get("被动土压力下"+i));
            Double two = zdDown - bdDown;
            String twoS = String.format("%.2f",two);
            Double tmpe = one * two;

            if(tmpe.compareTo(0.0)>0){
                bdDown = Double.valueOf(formate.get("被动土压力上"+i));
                two = zdDown - bdDown;
                twoS = String.format("%.2f",two);
                tmpe = one * two;
            }

            if(tmpe.compareTo(0.0)<=0){
                result = i;
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setAtZoneLand(i);
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第一种情况.getType());
                this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动减被动顶",new FormulaElement(result,this.jkzhPrefixLayout,"主动减被动顶",zdUp+"-"+bdUp+"="+oneS+"Kpa"));
                this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动减被动底",new FormulaElement(result,this.jkzhPrefixLayout,"主动减被动底",zdDown+"-"+bdDown+"="+twoS+"Kpa"));
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
            String zdDownTwos = formate.get("主动土压力上" + (i + 1));
            Double zdDownTwo = Double.valueOf(Objects.nonNull(zdDownTwos)?zdDownTwos:"0.0");
            //第i层主动土压力下端
            String zdDownOnes = formate.get("主动土压力下" + i);
            Double zdDownOne = Double.valueOf(Objects.nonNull(zdDownOnes)?zdDownOnes:"0.0");
            Double tmpe = (zdDownTwo * zdDownOne);
            if(tmpe.compareTo(0.0)<0){
                result = i;
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第二种情况.getType());
                this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动减被动顶",new FormulaElement(result,this.jkzhPrefixLayout,"主动减被动顶","第"+(i+1)+"层土顶面土压力强度："+zdDownTwos+"Kpa"));
                this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动减被动底",new FormulaElement(result,this.jkzhPrefixLayout,"主动减被动底","第"+i+"层土底面土压力强度："+zdDownOnes+"Kpa"));
                this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压力零点位置",new TextElement(result,"土压力零点位置","第"+i+"层土底面"));
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setAtZoneLand(i);
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
        jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setAtZoneLand(atZoneLand);
        jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第三种情况.getType());
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
        JkzhGetValues zdJkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力零点深度计算,this.jkzhContext);
        //主动土压力底层：
        String zdlatexCalDown = jkzhFromulaHandle.soilPressureToLatex(
                zoneLand,
                1,
                zoneLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                zdJkzhGetValues);
        String  zdCalDown = jkzhFromulaHandle.calSolveEquations(
                zoneLand,
                1,
                zoneLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.主动土压力,
                WaterWhichEnum.主动侧水位,
                zdJkzhGetValues);
        log.info("第{}层展示公式-下:{}={}",zoneLand,zdlatexCalDown,zdCalDown);

        //被动土压力底层：
        JkzhGetValues bdJkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动土压力零点深度计算,this.jkzhContext);
        String bdLatexCalDown = jkzhFromulaHandle.soilPressureToLatex(
                zoneLand - jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand()+1,
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand(),
                zoneLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.被动土压力,
                WaterWhichEnum.被动侧水位,
                bdJkzhGetValues);
        String  bdCalDown = jkzhFromulaHandle.calSolveEquations(
                zoneLand - jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand()+1,
                jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand(),
                zoneLand,
                CalculateSectionEnum.下底面,
                JkzhConfigEnum.被动土压力,
                WaterWhichEnum.被动侧水位,
                bdJkzhGetValues);
        log.info("第{}层展示公式-下:{}={}",zoneLand,bdLatexCalDown,bdCalDown);

        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压力零点求解",new FormulaElement(zoneLand,this.jkzhPrefixLayout,"土压力零点求解",zdlatexCalDown+"="+bdLatexCalDown));

        //解方程
        String result = solveEquations(zdCalDown, bdCalDown);
        //计算结果
        String calResult = calResult(bdCalDown, result);
        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("土压力零点值",new TextElement(zoneLand,"土压力零点值",result));
        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("零点土压力值",new TextElement(zoneLand,"零点土压力值",calResult));
        return Double.valueOf(result);
    }

    /**
     * 解方程
     * @param zdCalDown
     * @param bdCalDown
     * @return
     */
    private String solveEquations(String zdCalDown,String bdCalDown){
        String result = jkzhFromulaHandle.solveEquationsToCal(zdCalDown, bdCalDown);
        return result;
    }

    /**
     * 代入x值，计算结果
     * @param zdCalDown
     * @return
     */
    private String calResult(String zdCalDown,String value){
        String x = zdCalDown.replace("x", value);
        Double zdCalRtDown = (Double) AviatorEvaluator.execute(x);
        String  result = String.format("%.2f",zdCalRtDown);
        return result;
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
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力合力计算,this.jkzhContext);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);

        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                        jkzhZDGetValues);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0,
                        jkzhZDGetValues);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0,
                        jkzhZDGetValues);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getLatex(),this.jkzhFormulaLayout);
            }
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力合力计算条件"+land,new TextElement(land,"主动土压力合力计算条件",
                    "第"+land+"层主动土顶面压力："+String.valueOf(zdUpPressure)+"Kpa;主动土底面压力："+String.valueOf(zdDownPressure)+"Kpa"));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力合力计算公式"+land,new FormulaElement(land,this.jkzhPrefixLayout,"主动土压力合力计算公式",replaceChar));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动土压力合力计算"+land,new FormulaElement(land,this.jkzhPrefixLayout,"主动土压力合力计算",latexCal+"="+calculate+"kN/m"));
        }
    }

    /**
     * 重新计算某一层土的主动土压力合力，这层土需按实际的
     * 土层厚度计算
     * @param land 重算的第几层土
     */
    private void recalZdResultantEarthPressures(int land) {
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力合力满算, this.jkzhContext);
        Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力上" + land));
        Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力下" + land));
        String latexCal = "", calculate = "";
        if (zdUpPressure.compareTo(0.0) > 0 && zdDownPressure.compareTo(0.0) > 0) {
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_主动上大于0_下大于0,
                    jkzhZDGetValues);
            log.info("主动土压力合力第{}层展示公式-下:{}={}", land, latexCal, calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力" + land, calculate);
        } else if (zdUpPressure.compareTo(0.0) < 0 && zdDownPressure.compareTo(0.0) > 0) {
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_主动上小于0_下大于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_主动上小于0_下大于0,
                    jkzhZDGetValues);
            log.info("主动土压力合力第{}层展示公式-下:{}={}", land, latexCal, calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力" + land, calculate);
        } else if (zdUpPressure.compareTo(0.0) > 0 && zdDownPressure.compareTo(0.0) < 0) {
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_主动上大于0_下小于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_主动上大于0_下小于0,
                    jkzhZDGetValues);
            log.info("主动土压力合力第{}层展示公式-下:{}={}", land, latexCal, calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力合力" + land, calculate);
        }
    }

    /**
     * ⑥、主动作用点位置计算
     */
    public void zdPositionAction(){
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动作用点位置,this.jkzhContext);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);
        String replaceChar = "",latexCal = "",calculate = "";
        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                        jkzhZDGetValues);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0,
                        jkzhZDGetValues);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0,
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0,
                        jkzhZDGetValues);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getLatex(),this.jkzhFormulaLayout);
            }
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动作用点位置计算公式"+land,new FormulaElement(land,this.jkzhPrefixLayout,"主动作用点位置计算公式",replaceChar));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("主动作用点位置计算"+land,new FormulaElement(land,this.jkzhPrefixLayout,"主动作用点位置计算",latexCal+"="+calculate+"m"));
        }
    }

    /**
     * 重新计算某一层土的主动土压力合力作用点位置，这层土需按实际的
     * 土层厚度计算
     * @param land 重算的第几层土
     */
    public void recalZdPositionAction(int land){
        //土压力零点在所在土层第几层
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动作用点位置满算,this.jkzhContext);
        String latexCal = "",calculate = "";
        Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力上"+land));
        Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("主动土压力下"+land));
        if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_主动上大于0_下大于0,
                    jkzhZDGetValues);
            log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_主动上小于0_下大于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_主动上小于0_下大于0,
                    jkzhZDGetValues);
            log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_主动上大于0_下小于0,
                    jkzhZDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_主动上大于0_下小于0,
                    jkzhZDGetValues);
            log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土作用点位置"+land,calculate);
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
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        //重新计算土压力土压力零点这层土的被动土压力底
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动土压力合力计算,this.jkzhContext);
        //根据开挖深度，判断开挖基土在哪一层
        Integer depthLand = depthAtLand(depth);
        customCalBdPressure(atZoneLand,depthLand,jkzhBDGetValues);
        for (int land = depthLand; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                        jkzhBDGetValues);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0,
                        jkzhBDGetValues);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0,
                        jkzhBDGetValues);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getLatex(),this.jkzhFormulaLayout);
            }
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土压力合力计算条件"+land,new TextElement(land,"被动土压力合力计算条件",
                    "第"+land+"层被动土顶面压力："+String.valueOf(zdUpPressure)+"Kpa;被动土底面压力："+String.valueOf(zdDownPressure)+"Kpa"));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土压力合力计算公式"+land,new FormulaElement(land,this.jkzhPrefixLayout,"被动土压力合力计算公式",replaceChar));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动土压力合力计算"+land,new FormulaElement(land,this.jkzhPrefixLayout,"被动土压力合力计算",latexCal+"="+calculate+"kN/m"));
        }
    }

    /**
     * 重新计算某一层土的被动土压力合力，这层土需按实际的
     * 土层厚度计算
     * @param land
     */
    private void recalBdResultantEarthPressures(int land){
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动土压力合力满算,this.jkzhContext);
        Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力上"+land));
        Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力下"+land));
        String latexCal = "",calculate = "";
        if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_被动上大于0_下大于0,
                    jkzhBDGetValues);
            log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_被动上小于0_下大于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_被动上小于0_下大于0,
                    jkzhBDGetValues);
            log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.土压力合力_被动上大于0_下小于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.土压力合力_被动上大于0_下小于0,
                    jkzhBDGetValues);
            log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土压力合力"+land,calculate);
        }
    }

    /**
     * 被动作用点位置计算
     * @param depth 当前开挖深度
     */
    public void bdPositionAction(Double depth){
        //土压力零点在所在土层第几层
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        //重新计算土压力土压力零点这层土的被动土压力底
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动作用点位置,this.jkzhContext);
        //根据开挖深度，判断开挖基土在哪一层
        Integer depthLand = depthAtLand(depth);
        customCalBdPressure(atZoneLand,depthLand,jkzhBDGetValues);
        for (int land = depthLand; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                        jkzhBDGetValues);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0,
                        jkzhBDGetValues);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getLatex(),this.jkzhFormulaLayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.extendToLatex(
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0,
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.extendToCal(
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0,
                        jkzhBDGetValues);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
                jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getLatex(),this.jkzhFormulaLayout);
            }
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动作用点位置计算公式"+land,new FormulaElement(land,this.jkzhPrefixLayout,"被动作用点位置计算公式",replaceChar));
            jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("被动作用点位置计算"+land,new FormulaElement(land,this.jkzhPrefixLayout,"被动作用点位置计算",latexCal+"="+calculate+"m"));
        }
    }

    /**
     * 被动作用点位置计算
     * @param land 当前开挖所在土层
     */
    public void recalBdPositionAction(int land){
        //土压力零点在所在土层第几层
        //重新计算土压力土压力零点这层土的被动土压力底
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动作用点位置满算,this.jkzhContext);
        Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力上"+land));
        Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).get("被动土压力下"+land));
        String latexCal = "",calculate = "";
        if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_被动上大于0_下大于0,
                    jkzhBDGetValues);
            log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_被动上小于0_下大于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_被动上小于0_下大于0,
                    jkzhBDGetValues);
            log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
        }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
            latexCal = jkzhFromulaHandle.extendToLatex(
                    land,
                    JkzhConfigEnum.作用点位置_被动上大于0_下小于0,
                    jkzhBDGetValues);
            calculate = jkzhFromulaHandle.extendToCal(
                    land,
                    JkzhConfigEnum.作用点位置_被动上大于0_下小于0,
                    jkzhBDGetValues);
            log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,calculate);
            jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("被动土作用点位置"+land,calculate);
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
        int atZoneLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动支撑轴力计算,this.jkzhContext);
        String zdLatexCal = jkzhFromulaHandle.extendToLatexN(
                atZoneLand,
                1,
                atZoneLand,
                JkzhConfigEnum.支撑轴力主动,
                jkzhZDGetValues);
        String zdCalculate = jkzhFromulaHandle.extendToCalN(
                atZoneLand,
                1,
                atZoneLand,
                JkzhConfigEnum.支撑轴力主动,
                jkzhZDGetValues);
        this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("支撑轴力主动",zdCalculate);
        log.info("支撑轴力计算主动土压力公式:{}={}",zdLatexCal,zdCalculate);
        //获取开挖基坑所在土层
        JkzhGetValues jkzhBDGetValues = new JkzhGetValues(JkzhGetValueModelEnum.被动支撑轴力计算,this.jkzhContext);
        int atDepthLand = this.jkzhContext.getJkzhBasicParams().get(this.jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();
        int time = atZoneLand-atDepthLand+1;
        String bdLatexCal = jkzhFromulaHandle.extendToLatexN(
                time,
                atDepthLand,
                atZoneLand,
                JkzhConfigEnum.支撑轴力被动,
                jkzhBDGetValues);
        String bdCalculate = jkzhFromulaHandle.extendToCalN(
                time,
                atDepthLand,
                atZoneLand,
                JkzhConfigEnum.支撑轴力被动,
                jkzhBDGetValues);
        this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("支撑轴力被动",bdCalculate);
        log.info("支撑轴力计算被动土压力公式:{}={}",bdLatexCal,bdCalculate);

        JkzhCalTemporaryPart jkzhCalTemporaryPart = new JkzhCalTemporaryPart(new String[]{"支撑轴力主动", "支撑轴力被动"});
        jkzhCalTemporaryPart.getLayoutMap().put("支撑轴力主动",zdLatexCal);
        jkzhCalTemporaryPart.getLayoutMap().put("支撑轴力被动",bdLatexCal);

        JkzhGetValues jkzhZCZLGetValues = new JkzhGetValues(JkzhGetValueModelEnum.支撑轴力计算,this.jkzhContext);
        jkzhZCZLGetValues.setModel(JkzhGetValueModelEnum.支撑轴力计算);
        String zlLatexCal = jkzhFromulaHandle.strutForceExtendToLatex(
                this.jkzhContext.getCalTimes(),
                jkzhBDGetValues,
                JkzhConfigEnum.支撑轴力.getLatexCal(),
                jkzhCalTemporaryPart);

        String zlCalculate = jkzhFromulaHandle.strutForceExtendToCal(
                this.jkzhContext.getCalTimes(),
                JkzhConfigEnum.支撑轴力.getCalculate(),
                jkzhZCZLGetValues);
        log.info("支撑轴力计算:{}={}={}",zlLatexCal,zlCalculate,zlCalculate);
        this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("支撑轴力",zlCalculate);
        this.jkzhContext.getElementTemplates().get(this.jkzhContext.getCalTimes()).put("支点反力计算",new FormulaElement(this.jkzhContext.getCalTimes(),this.jkzhPrefixLayout,"支点反力计算",zlLatexCal+"="+zlCalculate+"kN"));
    }

    /**
     * 土压力零点不与开挖深度同一土层主动土压力整层计算
     * @param atDepthLand
     * @param zeroAtLand
     */
    private void reCalZDPressureDown(int atDepthLand,int zeroAtLand){
        if(atDepthLand!=zeroAtLand){
            JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.主动土压力计算,this.jkzhContext);
            String  calDown = this.jkzhFromulaHandle.soilPressureToCal(
                    atDepthLand,
                    1,
                    atDepthLand,
                    CalculateSectionEnum.下底面,
                    JkzhConfigEnum.主动土压力,
                    WaterWhichEnum.主动侧水位,
                    jkzhGetValues);
            log.info("土压力零点不与开挖深度同一土层主动土压力整层计算:{}",atDepthLand);
            this.jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()).put("主动土压力下"+atDepthLand,calDown);
        }
    }

    /**
     * 给定两个土层，计算之间的深度
     * @param beginLand 第一个土层顶面
     * @param endLand 第二个土层顶面
     * @param depth 正数，则表示 第二个土层顶面一下多少米；负数表示，第二个图层顶面以上多少米
     * @return
     */
    private Double betweenLandDepth(int beginLand,int endLand,double depth){
        //判断开挖深度在第几层土层
        String[][] table = this.jkzhContext.getSoilQualityTable().getTable();
        Double sumLands = 0.0;
        for(int floor = beginLand;floor < endLand; floor++){
            sumLands += Double.valueOf(table[floor][2]);
        }
        return sumLands + depth;
    }

    /**
     * 当工况不存在剪力为零的位置，那么需要在这一工况数据中插入无效数据
     * @param tcAtLand 剪力为零的位置
     */
    private void maxTcLandNotExist(int tcAtLand,int index){
        if(tcAtLand == 0){
            this.jkzhContext.getBendingMomentTemplates().add(index,null);
            this.jkzhContext.getBendingMomentValues().add(index,null);
        }
    }

    /**
     * 判断计算出的结果，是否在某一土层内
     * @param atLand 土层
     * @param depth 深度
     * @return
     */
    private Boolean isOutsideLand(int atLand,Double depth){
        //判断开挖深度在第几层土层
        String[][] table = this.jkzhContext.getSoilQualityTable().getTable();
        Double aLandDepth = Double.valueOf(table[atLand][2]);
        if(depth.compareTo(aLandDepth)>=0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

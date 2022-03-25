package org.calculation;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.constant.Constant;
import org.context.JkzhContext;
import org.element.FormulaElement;
import org.element.TableElement;
import org.element.TextElement;
import org.entity.ExpansionParam;
import org.enumUtils.BigDecimalStringUtil;
import org.enumUtils.ZDEqualsBDKindsEnum;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handle.*;
import org.handleParams.FirstFloorHandlerParam;
import org.handleParams.WaterHandlerParams;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.show.JkzhElementLayout;
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
@Data
public class JkzhCalculation extends DefaultCalculation{
    private JkzhContext jkzhContext;
    private JkzhILayout jkzhILayout;
    private JkzhElementLayout jkzhElementLayout;

    public JkzhCalculation() {
        this.jkzhILayout = new JkzhILayout();
        this.jkzhElementLayout = new JkzhElementLayout();
        this.jkzhContext = (JkzhContext)this.getContext(this);
        createFixedElement();
    }

    /**
     * 主动土压力强度计算
     */
    public void zdPressure(){
        FromulaEntity fromulaToCal = creatFromulaToCal(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        FromulaEntity fromulaToLatex = creatFromulaToLatex(JkzhGetValueModelEnum.主动土压力计算, JkzhConfigEnum.主动土压力, WaterWhichEnum.主动侧水位);
        int layer = this.jkzhContext.getJkzhBasicParam().getAllLands();
        //②、计算主动土压力强度
        for(int i = 1; i <= layer; i++){
            ExpansionHandler handler = (ExpansionHandler)fromulaToCal.getHandler(ExpansionHandler.class);
            ExpansionParam expansionParam = handler.getExpansionParam();
            expansionParam.setExpansionTimes();
            String  latexCalUp = this.jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i-1,
                    1,i,
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
            String valueUp = String.format("%.2f", zdCalRtUp);
            this.jkzhContext.getTemporaryValue().put("主动土压力上"+i,valueUp);
            this.jkzhContext.getElementTemplate().put("主动土压力上"+i,new FormulaElement(i,"主动土压力上",latexCalUp+"="+valueUp+"kPa"));
            log.info("主动土压力第{}层展示公式—上:{}={}",i,latexCalUp,zdCalRtUp);
            String latexCalDown = this.jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i,
                    1,
                    i,
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
            String valueDown = String.format("%.2f",zdCalRtDown);
            this.jkzhContext.getElementTemplate().put("主动土层"+i,new TextElement(i,"主动土层",String.valueOf(i)));
            this.jkzhContext.getTemporaryValue().put("主动土压力下"+i,valueDown);
            this.jkzhContext.getElementTemplate().put("主动土压力下"+i,new FormulaElement(i,"主动土压力下",latexCalDown+"="+valueDown+"kPa"));
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
            String valueUp = String.format("%.2f", zdCalRtUp);
            jkzhContext.getTemporaryValue().put("被动土压力上"+(atLand+i),valueUp);
            this.jkzhContext.getElementTemplate().put("被动土压力上"+(atLand+i),new FormulaElement((atLand+i),"被动土压力上",latexCalUp+"="+valueUp+"kPa"));
            log.info("被动土压力第{}层展示公式—上:{}={}",i+atLand,latexCalUp,zdCalRtUp);
            String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(
                    jkzhContext,
                    jkzhFromulaHandle,
                    i+1,
                    atLand,
                    i+atLand,
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
            String valueDown = String.format("%.2f", zdCalRtDown);
            this.jkzhContext.getElementTemplate().put("被动土层"+(atLand+i),new TextElement(atLand+i,"被动土层",String.valueOf(atLand+i)));
            jkzhContext.getTemporaryValue().put("被动土压力下"+(atLand+i),valueDown);
            this.jkzhContext.getElementTemplate().put("被动土压力下"+(atLand+i),new FormulaElement((atLand+i),"被动土压力下",latexCalDown+"="+valueDown+"kPa"));
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
        JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土压力零点所在土层,this.jkzhContext);
        customCalZdPressure(atDepthLand,jkzhGetValues);
        //计算土压力零点在哪一层
        pressureZeroAtLand(depth,atDepthLand,jkzhContext.getJkzhBasicParam().getAllLands(),jkzhContext.getTemporaryValue());
    }

    /**
     * 把模板中的固定元素，填进模板元素集合中
     */
    private void createFixedElement(){
        /**固定计公式begin*/
        String s_1 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力.getLatex(),this.jkzhILayout);
        this.jkzhContext.getElementTemplate().put("主动土压力计算公式",new FormulaElement(0,"主动土压力计算公式",s_1));
        String s_2 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.主动土压力系数.getLatex(),this.jkzhILayout);
        this.jkzhContext.getElementTemplate().put("主动土压力系数计算公式",new FormulaElement(0,"主动土压力系数计算公式",s_2));

        String s_3 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力.getLatex(),this.jkzhILayout);
        this.jkzhContext.getElementTemplate().put("被动土压力计算公式",new FormulaElement(0,"被动土压力计算公式",s_3));
        String s_4 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.被动土压力系数.getLatex(),this.jkzhILayout);
        this.jkzhContext.getElementTemplate().put("被动土压力系数计算公式",new FormulaElement(0,"被动土压力系数计算公式",s_4));

        String s_5 = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.支撑轴力.getLatex(),this.jkzhILayout);
        this.jkzhContext.getElementTemplate().put("支反力计算公式",new FormulaElement(1,"支反力计算公式",s_5));
        /**固定计公式end*/

        /**jkzhILayout begin*/
        this.jkzhContext.getElementTemplate().put("内聚力",new FormulaElement(0,"内聚力",this.jkzhILayout.getLayoutMap().get("内聚力")));
        this.jkzhContext.getElementTemplate().put("内摩擦角",new FormulaElement(0,"内摩擦角",this.jkzhILayout.getLayoutMap().get("内摩擦角")));
        this.jkzhContext.getElementTemplate().put("支点至基坑底面的距离",new FormulaElement(1,"支点至基坑底面的距离",this.jkzhILayout.getLayoutMap().get("支点至基坑底面的距离")));
        this.jkzhContext.getElementTemplate().put("基坑底面至反弯点的距离",new FormulaElement(1,"基坑底面至反弯点的距离",this.jkzhILayout.getLayoutMap().get("基坑底面至反弯点的距离")));
        this.jkzhContext.getElementTemplate().put("土压力强度顶面",new TextElement(1,"土压力强度顶面",this.jkzhILayout.getLayoutMap().get("土压力强度顶面")));
        this.jkzhContext.getElementTemplate().put("土压力强度底面",new TextElement(1,"土压力强度底面",this.jkzhILayout.getLayoutMap().get("土压力强度底面")));
        this.jkzhContext.getElementTemplate().put("土层厚度",new TextElement(1,"土层厚度",this.jkzhILayout.getLayoutMap().get("土层厚度")));
        /**jkzhILayout end*/

        /**jkzhContext begin*/
        this.jkzhContext.getElementTemplate().put("土层参数计算依据表",new TableElement(0,"土层参数计算依据表",this.jkzhContext.getSoilQualityTable().getTable()));
        this.jkzhContext.getElementTemplate().put("土压力系数表",new TableElement(0,"土压力系数表",this.jkzhContext.getSoilPressureTable().getTable()));
        this.jkzhContext.getElementTemplate().put("地面堆载",new TextElement(0,"地面堆载",this.jkzhContext.getJkzhBasicParam().getSurcharge().toString()));
        this.jkzhContext.getElementTemplate().put("开挖深度",new TextElement(0,"开挖深度",this.jkzhContext.getJkzhBasicParam().getDepth().toString()));
        /**jkzhContext end*/

        /**jkzhElementLayout begin*/
        this.jkzhContext.getElementTemplate().put("支护结构外侧",new FormulaElement(0,"支护结构外侧",this.jkzhElementLayout.getLayoutMap().get("支护结构外侧")));
        this.jkzhContext.getElementTemplate().put("支护结构内侧",new FormulaElement(0,"支护结构内侧",this.jkzhElementLayout.getLayoutMap().get("支护结构内侧")));
        this.jkzhContext.getElementTemplate().put("支护结构外侧应力",new FormulaElement(0,"支护结构外侧",this.jkzhElementLayout.getLayoutMap().get("支护结构外侧应力")));
        this.jkzhContext.getElementTemplate().put("支护结构内侧应力",new FormulaElement(0,"支护结构外侧",this.jkzhElementLayout.getLayoutMap().get("支护结构内侧应力")));
        this.jkzhContext.getElementTemplate().put("主动土压力系数",new FormulaElement(0,"主动土压力系数",this.jkzhElementLayout.getLayoutMap().get("主动土压力系数")));
        this.jkzhContext.getElementTemplate().put("被动土压力系数",new FormulaElement(0,"主动土压力系数",this.jkzhElementLayout.getLayoutMap().get("被动土压力系数")));
        this.jkzhContext.getElementTemplate().put("被动合力至反弯点的距离",new FormulaElement(1,"被动合力至反弯点的距离",this.jkzhElementLayout.getLayoutMap().get("被动合力至反弯点的距离")));
        this.jkzhContext.getElementTemplate().put("各层土的被动合力",new FormulaElement(1,"各层土的被动合力",this.jkzhElementLayout.getLayoutMap().get("各层土的被动合力")));
        this.jkzhContext.getElementTemplate().put("轴向支反力",new FormulaElement(1,"轴向支反力",this.jkzhElementLayout.getLayoutMap().get("轴向支反力")));
        this.jkzhContext.getElementTemplate().put("主动合力至反弯点的距离",new FormulaElement(1,"主动合力至反弯点的距离",this.jkzhElementLayout.getLayoutMap().get("主动合力至反弯点的距离")));
        this.jkzhContext.getElementTemplate().put("各层土的主动合力",new FormulaElement(1,"各层土的主动合力",this.jkzhElementLayout.getLayoutMap().get("各层土的主动合力")));
        /**jkzhElementLayout end*/
    }

    //计算给定深度，返回深度所在土层
    private Integer depthAtLand(Double depth){
        //判断开挖深度在第几层土层
        String[][] table = this.jkzhContext.getSoilQualityTable().getTable();
        Double sumLands = 0.0;
        int atLand = 0;
        for(int floor = 1;floor <table.length; floor++){
            sumLands += Double.valueOf(table[floor][2]);
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
        String latexCalDown = jkzhFromulaHandle.getLatexCalExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atLand,
                1,
                atLand,
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
        String valueDown = String.format("%.2f", zdCalRtDown);
        jkzhContext.getTemporaryValue().put("主动土压力下"+atLand,valueDown);
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
        jkzhContext.getTemporaryValue().put("被动土压力下"+atLand,String.format("%.2f",zdCalRtDown));
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
            this.jkzhContext.getElementTemplate().put("土压零点位置",new TextElement(zoneLand,"土压零点位置","已开挖基坑底面以下x米处"));
        }else{
            for (int i = 1; i < zoneLand; i++) {
                zero += Double.valueOf(this.jkzhContext.getSoilQualityTable().getTable()[i-1][2]);
            }
            this.jkzhContext.getJkzhBasicParam().setPressureZero(zero+pressureZero);
            this.jkzhContext.getElementTemplate().put("土压零点位置",new TextElement(zoneLand,"土压零点位置","第"+zoneLand+"层土顶面以下x米处"));
        }
    }

    /**
     * 第二种情况，计算土压力零点深度
     * @param depth 当前开挖深度
     * @return
     */
    private void thirdCasePressureZero(Double depth){
        double pressureZero = depth * 1.2;
        this.jkzhContext.getJkzhBasicParam().setPressureZero(pressureZero);
        this.jkzhContext.getElementTemplate().put("零点土压力值",new TextElement(1,"零点土压力值",String.valueOf(pressureZero)));
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
            Double one = zdUp - bdUp;
            //主动土压力下端-被动土压力下端
            Double zdDown = Double.valueOf(formate.get("主动土压力下"+atDepthLand));
            Double bdDown = Double.valueOf(formate.get("被动土压力下"+atDepthLand));
            Double two = zdDown - bdDown;
            Double tmpe = one * two;
            if(tmpe.compareTo(0.0)<=0){
                result = i;
                jkzhContext.getJkzhBasicParam().setAtZoneLand(i);
                jkzhContext.getJkzhBasicParam().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第一种情况.getType());
                this.jkzhContext.getElementTemplate().put("主动减被动顶",new FormulaElement(result,"主动减被动顶",zdUp+"-"+bdUp+"="+one));
                this.jkzhContext.getElementTemplate().put("主动减被动底",new FormulaElement(result,"主动减被动底",zdDown+"-"+bdDown+"="+two));
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
                jkzhContext.getJkzhBasicParam().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第二种情况.getType());
                this.jkzhContext.getElementTemplate().put("主动减被动顶",new FormulaElement(result,"主动减被动顶","第"+(i+1)+"层土顶面土压力强度："+zdDownTwos+"Kpa"));
                this.jkzhContext.getElementTemplate().put("主动减被动底",new FormulaElement(result,"主动减被动底","第"+i+"层土底面土压力强度："+zdDownOnes+"Kpa"));
                this.jkzhContext.getElementTemplate().put("土压力零点位置",new TextElement(result,"土压力零点位置","第"+i+"层土底面"));
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
        jkzhContext.getJkzhBasicParam().setAtZoneLand(atZoneLand);
        jkzhContext.getJkzhBasicParam().setZDEqualsBDKinds(ZDEqualsBDKindsEnum.土压力零点第三种情况.getType());
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

        this.jkzhContext.getElementTemplate().put("土压力零点求解",new FormulaElement(zoneLand,"土压力零点求解",zdlatexCalDown+"="+bdLatexCalDown));

        //解方程
        String result = solveEquations(zdCalDown, bdCalDown);
        //计算结果
        String calResult = calResult(bdCalDown, result);
        this.jkzhContext.getElementTemplate().put("土压力零点值",new TextElement(zoneLand,"土压力零点值",result));
        this.jkzhContext.getElementTemplate().put("零点土压力值",new TextElement(zoneLand,"零点土压力值",calResult));
        return Double.valueOf(result);
    }

    /**
     * 解方程
     * @param zdCalDown
     * @param bdCalDown
     * @return
     */
    private String solveEquations(String zdCalDown,String bdCalDown){
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
        int atZoneLand = this.jkzhContext.getJkzhBasicParam().getAtZoneLand();
        JkzhGetValues jkzhZDGetValues = new JkzhGetValues();
        jkzhZDGetValues.setModel(JkzhGetValueModelEnum.主动土压力合力计算);
        //重新计算土压力土压力零点这层土的主动土压力底
        customCalZdPressure(atZoneLand,jkzhZDGetValues);

        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("主动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "",calValue = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上大于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上小于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_主动上大于0_下小于0.getLatex(),this.jkzhILayout);
            }
            jkzhContext.getElementTemplate().put("主动土压力合力计算条件"+land,new TextElement(land,"主动土压力合力计算条件",
                    "第"+land+"层主动土顶面压力："+String.valueOf(zdUpPressure)+"Kpa;主动土底面压力："+String.valueOf(zdDownPressure)+"Kpa"));
            jkzhContext.getElementTemplate().put("主动土压力合力计算公式"+land,new FormulaElement(land,"主动土压力合力计算公式",replaceChar));
            jkzhContext.getElementTemplate().put("主动土压力合力计算"+land,new FormulaElement(land,"主动土压力合力计算",latexCal+"="+calValue+"kN/m"));
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
        String replaceChar = "",latexCal = "",calculate = "",calValue = "";
        for (int land = 1; land <= atZoneLand;land++) {
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("主动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("主动土压力下"+land));
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上大于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上小于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getLatexCal(),
                        jkzhZDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getCalculate(),
                        jkzhZDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("主动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("主动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_主动上大于0_下小于0.getLatex(),this.jkzhILayout);
            }
            jkzhContext.getElementTemplate().put("主动作用点位置计算公式"+land,new FormulaElement(land,"主动作用点位置计算公式",replaceChar));
            jkzhContext.getElementTemplate().put("主动作用点位置计算"+land,new FormulaElement(land,"主动作用点位置计算",latexCal+"="+calValue+"m"));
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
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("被动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "",calValue = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上大于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上小于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土压力合力第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土压力合力"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.土压力合力_被动上大于0_下小于0.getLatex(),this.jkzhILayout);
            }
            jkzhContext.getElementTemplate().put("被动土压力合力计算条件"+land,new TextElement(land,"被动土压力合力计算条件",
                    "第"+land+"层被动土顶面压力："+String.valueOf(zdUpPressure)+"Kpa;被动土底面压力："+String.valueOf(zdDownPressure)+"Kpa"));
            jkzhContext.getElementTemplate().put("被动土压力合力计算公式"+land,new FormulaElement(land,"被动土压力合力计算公式",replaceChar));
            jkzhContext.getElementTemplate().put("被动土压力合力计算"+land,new FormulaElement(land,"被动土压力合力计算",latexCal+"="+calValue+"kN/m"));
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
            Double zdUpPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("被动土压力上"+land));
            Double zdDownPressure = Double.valueOf(jkzhContext.getTemporaryValue().get("被动土压力下"+land));
            String replaceChar = "",latexCal = "",calculate = "",calValue = "";
            if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上大于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)<0 && zdDownPressure.compareTo(0.0)>0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上小于0_下大于0.getLatex(),this.jkzhILayout);
            }else if(zdUpPressure.compareTo(0.0)>0 && zdDownPressure.compareTo(0.0)<0){
                latexCal = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getLatexCal(),
                        jkzhBDGetValues);
                calculate = jkzhFromulaHandle.generalFromulaHandle(
                        jkzhContext,
                        jkzhFromulaHandle,
                        land,
                        JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getCalculate(),
                        jkzhBDGetValues);
                Double zdCalRtDown = (Double) AviatorEvaluator.execute(calculate);
                log.info("被动土作用点位置第{}层展示公式-下:{}={}",land,latexCal,zdCalRtDown);
                calValue = String.format("%.2f", zdCalRtDown);
                jkzhContext.getTemporaryValue().put("被动土作用点位置"+land,calValue);
                replaceChar = jkzhFromulaHandle.replaceLayoutChar(JkzhConfigEnum.作用点位置_被动上大于0_下小于0.getLatex(),this.jkzhILayout);
            }
            jkzhContext.getElementTemplate().put("被动作用点位置计算公式"+land,new FormulaElement(land,"被动作用点位置计算公式",replaceChar));
            jkzhContext.getElementTemplate().put("被动作用点位置计算"+land,new FormulaElement(land,"被动作用点位置计算",latexCal+"="+calValue+"m"));
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
        this.jkzhContext.getTemporaryValue().put("支撑轴力主动"+atZoneLand,zdCalculate);
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
        this.jkzhContext.getTemporaryValue().put("支撑轴力被动"+atZoneLand,bdCalculate);
        log.info("支撑轴力计算被动土压力公式:{}={}",bdLatexCal,bdCalculate);

        HashMap<String, String> layoutMap = this.jkzhILayout.getLayoutMap();
        layoutMap.put("支撑轴力主动",zdLatexCal);
        layoutMap.put("支撑轴力被动",bdLatexCal);

        JkzhGetValues jkzhZCZLGetValues = new JkzhGetValues();
        jkzhZCZLGetValues.setModel(JkzhGetValueModelEnum.支撑轴力计算);
        String zlLatexCal = jkzhFromulaHandle.getLatexExpression(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                atZoneLand,
                atZoneLand,
                this.jkzhILayout,
                JkzhConfigEnum.支撑轴力.getLatexCal());

        zlLatexCal = jkzhFromulaHandle.generalFromulaHandle(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                zlLatexCal,
                jkzhZCZLGetValues);

        String zlCalculate = jkzhFromulaHandle.generalFromulaHandle(
                jkzhContext,
                jkzhFromulaHandle,
                atZoneLand,
                JkzhConfigEnum.支撑轴力.getCalculate(),
                jkzhZCZLGetValues);
        Double zlCalRt = (Double) AviatorEvaluator.execute(zlCalculate);
        log.info("支撑轴力计算:{}={}={}",zlLatexCal,zlCalculate,zlCalRt);
        String calValue = String.format("%.2f", zlCalRt);
        this.jkzhContext.getTemporaryValue().put("支撑处水平力",calValue);
        this.jkzhContext.getElementTemplate().put("支点反力计算",new FormulaElement(atZoneLand,"支点反力计算",zlLatexCal+"="+calValue+"kN"));
    }

    private FromulaEntity creatFromulaToCal(JkzhGetValueModelEnum jkzhGetValueModelEnum,JkzhConfigEnum jkzhConfigEnum,WaterWhichEnum waterWhichEnum){
        JkzhGetValues jkzhGetValues = new JkzhGetValues(jkzhGetValueModelEnum,this.jkzhContext);
        //用于计算结果
        FromulaEntity calFromulaEntity = new FromulaEntity(jkzhConfigEnum.getCalculate());
        calFromulaEntity
                //添加首层土判断处理器
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.getJkzhContext().getJkzhBasicParam())))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.getJkzhContext().getJkzhBasicParam()))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.getJkzhContext().getSoilQualityTable(),this.getJkzhContext().getJkzhBasicParam(), waterWhichEnum)))
                //添加元素标记处理器
                .addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
                //添加展开公式处理器
                .addHandler(new ExpansionHandler().setParams(new ExpansionParam(0,0)))
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
                .addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(this.getJkzhContext().getJkzhBasicParam())))
                //添加地面堆载处理器
                .addHandler(new SurchargeHandler().setParams(this.getJkzhContext().getJkzhBasicParam()))
                //添加水土分算处理器
                .addHandler(new WaterHandler().setParams(new WaterHandlerParams(this.getJkzhContext().getSoilQualityTable(),this.getJkzhContext().getJkzhBasicParam(), waterWhichEnum)))
                //添加元素标记处理器
                .addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
                //添加展开公式处理器
                .addHandler(new ExpansionHandler().setParams(new ExpansionParam(0,0)))
                //添加值填充处理器
                .addHandler(new FillValueHandler().setParams(jkzhGetValues));
        return latexFromulaEntity;
    }
}

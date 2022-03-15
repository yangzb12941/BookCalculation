package org.elementHandler;

import org.context.IContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.element.BlockElement;
import org.element.FormulaElement;
import org.element.TextElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JkzhBlockElementHandler extends BlockElementHandler{
    @Override
    public BlockElement getElementValue(IContext iContext, String key) {
        BlockElement blockElement = null;
        if(key.equals("主动土压力")){
            blockElement = zdPressure((JkzhContext) iContext,key);
        }else if(key.equals("被动土压力")){
            blockElement = bdPressure((JkzhContext) iContext,key);
        }else if(key.equals("主动土压力合力")){
            blockElement = zdPressureResultant((JkzhContext) iContext,key);
        }else if(key.equals("被动土压力合力")){
            blockElement = bdPressureResultant((JkzhContext) iContext,key);
        }else if(key.equals("是否存在主被动相等")){
            blockElement = isZDEqualsBD((JkzhContext) iContext,key);
        }
        return blockElement;
    }

    private BlockElement zdPressure(JkzhContext jkzhContext, String key){
        int allLands = jkzhContext.getJkzhBasicParam().getAllLands();
        List<BaseElement> baseElements = new ArrayList<BaseElement>(3);
        for(int land = 1;land <= allLands; land++){
            String zdFloorKey = "主动土层" + land;
            String zdFloorValue = (String)jkzhContext.getElementTemplate().get(zdFloorKey);
            TextElement textElement = new TextElement(zdFloorKey,zdFloorValue);
            baseElements.add(textElement);

            String zdPreUpKey = "主动土压力计算上" + land;
            String zdPreUpValue = (String)jkzhContext.getElementTemplate().get(zdPreUpKey);
            FormulaElement zdPreUpFormula = new FormulaElement(zdPreUpKey,zdPreUpValue);
            baseElements.add(zdPreUpFormula);
            String zdPreDownKey = "主动土压力计算下" + land;
            String zdPreDownValue = (String)jkzhContext.getElementTemplate().get(zdPreDownKey);
            FormulaElement zdpreDownFormula = new FormulaElement(zdPreDownKey,zdPreDownValue);
            baseElements.add(zdpreDownFormula);
        }
        return new BlockElement(key,baseElements);
    }

    private BlockElement bdPressure(JkzhContext jkzhContext, String key){
        int depthLand = jkzhContext.getJkzhBasicParam().getAtDepthLand();
        int allLands = jkzhContext.getJkzhBasicParam().getAllLands();
        List<BaseElement> baseElements = new ArrayList<BaseElement>(3);
        for(int land = depthLand;land <= allLands; land++){
            String bdFloorKey = "被动土层" + land;
            String bdFloorValue = (String)jkzhContext.getElementTemplate().get(bdFloorKey);
            TextElement textElement = new TextElement(bdFloorKey,bdFloorValue);
            baseElements.add(textElement);

            String bdPreUpKey = "被动土压力计算上" + land;
            String bdPreUpValue = (String)jkzhContext.getElementTemplate().get(bdPreUpKey);
            FormulaElement zdpreUpFormula = new FormulaElement(bdPreUpKey,bdPreUpValue);
            baseElements.add(zdpreUpFormula);
            String bdPreDownKey = "被动土压力计算下" + land;
            String bdPreDownValue = (String)jkzhContext.getElementTemplate().get(bdPreDownKey);
            FormulaElement bdpreDownFormula = new FormulaElement(bdPreDownKey,bdPreDownValue);
            baseElements.add(bdpreDownFormula);
        }
        return new BlockElement(key,baseElements);
    }

    private BlockElement zdPressureResultant (JkzhContext jkzhContext, String key){
        int zoneLand = jkzhContext.getJkzhBasicParam().getAtZoneLand();
        List<BaseElement> baseElements = new ArrayList<BaseElement>(12);
        for(int land = 1;land <= zoneLand; land++){
            HashMap<String, String> temporaryValue = jkzhContext.getTemporaryValue();
            Double upPressure = Double.valueOf(temporaryValue.get("主动土压力上"+land));
            Double downPressure = Double.valueOf(temporaryValue.get("主动土压力下"+land));
            if(upPressure.compareTo(0.0)>0 && downPressure.compareTo(0.0)>0){
                String value = "第"+land+"土层顶面主动土压力强度"+upPressure+">0"+"且土层底面主动土压力强度"+downPressure+">0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);

                String preResFomKey = "土压力合力_主动上大于0_下大于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_主动上大于0_下大于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);

            }else if(upPressure.compareTo(0.0)<0 && downPressure.compareTo(0.0)>0){
                String value = "第"+land+"土层顶面主动土压力强度"+upPressure+"<0"+"且土层底面主动土压力强度"+downPressure+">0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);

                String preResFomKey = "土压力合力_主动上小于0_下大于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_主动上小于0_下大于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);
            }else if(upPressure.compareTo(0.0)>0 && downPressure.compareTo(0.0)<0){
                String value = "第"+land+"土层顶面主动土压力强度"+upPressure+">0"+"且土层底面主动土压力强度"+downPressure+"<0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);
                String preResFomKey = "土压力合力_主动上大于0_下小于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_主动上大于0_下小于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);
            }

            String preResKey = "主动土压力合力计算"+land;
            String preResValue = (String)jkzhContext.getElementTemplate().get(preResKey);
            FormulaElement preRes = new FormulaElement(preResKey,preResValue);
            baseElements.add(preRes);

            String prePointKey = "主动作用点位置计算"+land;
            String prePointValue = (String)jkzhContext.getElementTemplate().get(prePointKey);
            FormulaElement prePoint = new FormulaElement(preResKey,prePointValue);
            baseElements.add(prePoint);
        }
        return new BlockElement(key,baseElements);
    }

    private BlockElement bdPressureResultant(JkzhContext jkzhContext, String key){
        int zoneLand = jkzhContext.getJkzhBasicParam().getAtZoneLand();
        int atDepthLand = jkzhContext.getJkzhBasicParam().getAtDepthLand();
        List<BaseElement> baseElements = new ArrayList<BaseElement>(12);
        for(int land = atDepthLand;land <= zoneLand; land++){
            HashMap<String, String> temporaryValue = jkzhContext.getTemporaryValue();
            Double upPressure = Double.valueOf(temporaryValue.get("被动土压力上"+land));
            Double downPressure = Double.valueOf(temporaryValue.get("被动土压力下"+land));
            if(upPressure.compareTo(0.0)>0 && downPressure.compareTo(0.0)>0){
                String value = "第"+land+"土层顶面被动土压力强度"+upPressure+">0"+"且土层底面被动土压力强度"+downPressure+">0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);

                String preResFomKey = "土压力合力_被动上大于0_下大于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_被动上大于0_下大于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);

            }else if(upPressure.compareTo(0.0)<0 && downPressure.compareTo(0.0)>0){
                String value = "第"+land+"土层顶面被动土压力强度"+upPressure+"<0"+"且土层底面被动土压力强度"+downPressure+">0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);

                String preResFomKey = "土压力合力_被动上小于0_下大于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_被动上小于0_下大于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);
            }else if(upPressure.compareTo(0.0)>0 && downPressure.compareTo(0.0)<0){
                String value = "第"+land+"土层顶面被动土压力强度"+upPressure+">0"+"且土层底面被动土压力强度"+downPressure+"<0";
                TextElement textElement = new TextElement("土压力合力计算条件",value);
                baseElements.add(textElement);
                String preResFomKey = "土压力合力_被动上大于0_下小于0";
                String preResFomValue = (String)jkzhContext.getElementTemplate().get(preResFomKey);
                FormulaElement preResFormula = new FormulaElement("土压力合力计算公式",preResFomValue);
                baseElements.add(preResFormula);

                String preResPointKey = "作用点位置_被动上大于0_下小于0";
                String preResPointValue = (String)jkzhContext.getElementTemplate().get(preResPointKey);
                FormulaElement prePointFormula = new FormulaElement("作用点位置计算公式",preResPointValue);
                baseElements.add(prePointFormula);
            }

            String preResKey = "被动土压力合力计算"+land;
            String preResValue = (String)jkzhContext.getElementTemplate().get(preResKey);
            FormulaElement preRes = new FormulaElement(preResKey,preResValue);
            baseElements.add(preRes);

            String prePointKey = "被动作用点位置计算"+land;
            String prePointValue = (String)jkzhContext.getElementTemplate().get(prePointKey);
            FormulaElement prePoint = new FormulaElement(preResKey,prePointValue);
            baseElements.add(prePoint);
        }
        return new BlockElement(key,baseElements);
    }

    private BlockElement isZDEqualsBD(JkzhContext jkzhContext, String key){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(1);
        BaseElement baseElement = new BaseElement("零点土压力值",jkzhContext.getJkzhBasicParam().getPressureZero());
        baseElements.add(baseElement);
        return new BlockElement(key, baseElements);
    }
}

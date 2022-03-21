package org.elementHandler;

import org.context.AbstractContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.element.BlockElement;

import java.util.ArrayList;
import java.util.List;

public class JkzhBlockElementHandler extends BlockElementHandler{

    private static volatile JkzhBlockElementHandler instance;

    public static JkzhBlockElementHandler getInstance(){
        if(instance == null){
            synchronized (instance){
                if (instance == null) {
                    instance = new JkzhBlockElementHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public Object getElementValue(AbstractContext abstractContext, String key) {
        List<BlockElement> blockElements = null;
        if(key.equals("主动土压力")){
            blockElements = zdPressure((JkzhContext) abstractContext,key);
        }else if(key.equals("被动土压力")){
            blockElements = bdPressure((JkzhContext) abstractContext,key);
        }else if(key.equals("主动土压力合力")){
            blockElements = zdPressureResultant((JkzhContext) abstractContext,key);
        }else if(key.equals("被动土压力合力")){
            blockElements = bdPressureResultant((JkzhContext) abstractContext,key);
        }else if(key.equals("第一种情况")){
            blockElements = firstZDEqualsBD((JkzhContext) abstractContext,key);
        }else if(key.equals("第二种情况")){
            blockElements = secondZDEqualsBD((JkzhContext) abstractContext,key);
        }else if(key.equals("第三种情况")){
            blockElements = thirdZDEqualsBD((JkzhContext) abstractContext,key);
        }
        return blockElements;
    }

    private List<BlockElement> zdPressure(JkzhContext jkzhContext, String key){
        int allLands = jkzhContext.getJkzhBasicParam().getAllLands();
        List<BlockElement> blockElements = pressure(jkzhContext, key, 1, allLands, "主动");
        return blockElements;
    }

    private List<BlockElement> bdPressure(JkzhContext jkzhContext, String key){
        int depthLand = jkzhContext.getJkzhBasicParam().getAtDepthLand();
        int allLands = jkzhContext.getJkzhBasicParam().getAllLands();
        List<BlockElement> blockElements = pressure(jkzhContext, key, depthLand, allLands, "被动");
        return blockElements;
    }

    private List<BlockElement> zdPressureResultant (JkzhContext jkzhContext, String key){
        int zoneLand = jkzhContext.getJkzhBasicParam().getAtZoneLand();
        List<BlockElement> blockElements = pressureResultant(jkzhContext, key, 1, zoneLand,"主动");
        return blockElements;
    }

    private List<BlockElement> bdPressureResultant(JkzhContext jkzhContext, String key){
        int zoneLand = jkzhContext.getJkzhBasicParam().getAtZoneLand();
        int atDepthLand = jkzhContext.getJkzhBasicParam().getAtDepthLand();
        List<BlockElement> blockElements = pressureResultant(jkzhContext, key, atDepthLand, zoneLand,"被动");
        return blockElements;
    }

    private List<BlockElement> thirdZDEqualsBD(JkzhContext jkzhContext, String key){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(1);
        List<BlockElement> blockElements = new ArrayList<BlockElement>(1);
        BaseElement textElement = jkzhContext.getElementTemplate().get("土压零点位置");
        baseElements.add(textElement);
        BlockElement blockElement = new BlockElement(1,key,baseElements);
        blockElements.add(blockElement);
        return blockElements;
    }

    private List<BlockElement> firstZDEqualsBD(JkzhContext jkzhContext, String key){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(8);
        List<BlockElement> blockElements = new ArrayList<BlockElement>(1);

        BaseElement textElement = jkzhContext.getElementTemplate().get("土压零点位置");
        baseElements.add(textElement);

        BaseElement zonePointValue = jkzhContext.getElementTemplate().get("土压力零点值");
        baseElements.add(zonePointValue);

        BaseElement zoneRepValue = jkzhContext.getElementTemplate().get("零点土压力值");
        baseElements.add(zoneRepValue);

        BaseElement zoneSolve = jkzhContext.getElementTemplate().get("土压力零点求解");
        baseElements.add(zoneSolve);

        BaseElement zdReduceBDUp = jkzhContext.getElementTemplate().get("主动减被动顶");
        baseElements.add(zdReduceBDUp);

        BaseElement zdReduceBDDown = jkzhContext.getElementTemplate().get("主动减被动底");
        baseElements.add(zdReduceBDDown);

        BlockElement blockElement = new BlockElement(1,key,baseElements);
        blockElements.add(blockElement);
        return blockElements;
    }

    private List<BlockElement> secondZDEqualsBD(JkzhContext jkzhContext, String key){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(3);
        List<BlockElement> blockElements = new ArrayList<BlockElement>(1);

        BaseElement textElement = jkzhContext.getElementTemplate().get("土压零点位置");
        baseElements.add(textElement);

        BaseElement zdReduceBDUp = jkzhContext.getElementTemplate().get("主动减被动顶");
        baseElements.add(zdReduceBDUp);

        BaseElement zdReduceBDDown = jkzhContext.getElementTemplate().get("主动减被动底");
        baseElements.add(zdReduceBDDown);
        baseElements.add(textElement);
        BlockElement blockElement = new BlockElement(1,key,baseElements);
        blockElements.add(blockElement);
        return blockElements;
    }

    private List<BlockElement> pressure(JkzhContext jkzhContext, String key,Integer startLand,Integer endLand,String prefix){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(3);
        List<BlockElement> blockElements = new ArrayList<BlockElement>(endLand);
        for(int land = startLand;land <= endLand; land++){
            String zdFloorKey = prefix+"土层" + land;
            BaseElement textElement = jkzhContext.getElementTemplate().get(zdFloorKey);
            baseElements.add(textElement);

            String zdPreUpKey = prefix+"土压力上" + land;
            BaseElement zdPreUpFormula = jkzhContext.getElementTemplate().get(zdPreUpKey);
            baseElements.add(zdPreUpFormula);

            String zdPreDownKey = prefix+"土压力下" + land;
            BaseElement zdPreDownValue = jkzhContext.getElementTemplate().get(zdPreDownKey);
            baseElements.add(zdPreDownValue);

            BlockElement blockElement = new BlockElement(land,key,baseElements);
            blockElements.add(blockElement);
        }
        return blockElements;
    }

    private List<BlockElement> pressureResultant(JkzhContext jkzhContext, String key,Integer startLand,Integer endLand,String prefix){
        List<BaseElement> baseElements = new ArrayList<BaseElement>(5);
        List<BlockElement> blockElements = new ArrayList<BlockElement>(endLand-startLand+1);

        for(int land = startLand;land <= endLand; land++){
            String calCondition = prefix+"土压力合力计算条件" + land;
            BaseElement baseElement = jkzhContext.getElementTemplate().get(calCondition);
            baseElements.add(baseElement);

            String preResFomKey = prefix+"土压力合力计算公式" + land;
            BaseElement preResFomValue = jkzhContext.getElementTemplate().get(preResFomKey);
            baseElements.add(preResFomValue);

            String prePointFomKey = prefix+"作用点位置计算公式" + land;
            BaseElement prePointFomValue = jkzhContext.getElementTemplate().get(prePointFomKey);
            baseElements.add(prePointFomValue);

            String preResKey = prefix+"土压力合力计算" + land;
            BaseElement preResValue = jkzhContext.getElementTemplate().get(preResKey);
            baseElements.add(preResValue);

            String prePointKey = prefix+"作用点位置计算" + land;
            BaseElement preResPointValue = jkzhContext.getElementTemplate().get(prePointKey);
            baseElements.add(preResPointValue);
        }
        BlockElement blockElement = new BlockElement(1,key,baseElements);
        blockElements.add(blockElement);
        return blockElements;
    }
}

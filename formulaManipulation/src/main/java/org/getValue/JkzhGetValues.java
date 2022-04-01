package org.getValue;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.entity.ElementParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class JkzhGetValues implements GetValues {

    /**
     * 参数解析模型：1-主动土压力计算、2-被动土压力计算、3-土压力零点计算、4-支撑轴力计算
     */
    private JkzhGetValueModelEnum model;

    /**
     * 计算上下文
     */
    private JkzhContext jkzhContext;

    /**
     * 参数
     */
    private List<ElementParam> elementParams;

    public JkzhGetValues(JkzhGetValueModelEnum model,JkzhContext jkzhContext) {
        this.model = model;
        this.jkzhContext = jkzhContext;
    }

    /**
     * 设置
     * @param elementParams
     */
    public void setElementParams(List<ElementParam> elementParams) {
        this.elementParams = elementParams;
    }

    @Override
    public String[] getValues() {
        return matchValues(this.jkzhContext);
    }

    private String[] matchValues(JkzhContext jkzhContext) {
        String[] valueArray = new String[elementParams.size()];
        for (int index = 0; index < elementParams.size(); index++) {
            ElementParam elementParam = elementParams.get(index);
            switch (elementParam.getName()){
                case "地面堆载": {
                    valueArray[index] = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getSurcharge().toString();
                    break;
                }
                case "支撑的轴线": {
                    valueArray[index] = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getAxis().toString();
                    break;
                }
                case "土压力零点": {
                    valueArray[index] = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero().toString();
                    break;
                }
                case "厚度":
                case "主动土作用点位置":
                case "被动土作用点位置": {
                    Integer floor = Integer.valueOf(elementParam.getIndex());
                    //被动土压力计算，需要以当前开挖层所在土层实际土层厚度计算。
                    //例如：第一层土层厚度1米，第二层土层厚度3米，第三层土层厚度4米。开挖深度是6米，那么开挖深度是在第三层土位置。第三层土还剩6-（1+3）= 2米的土层厚度。
                    //那么计算被动土压力时，第三层土的厚度就是2m。
                    if (this.model == JkzhGetValueModelEnum.主动土压力计算) {
                        String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor, 2);
                        valueArray[index] = hdValue;
                    } else if (this.model == JkzhGetValueModelEnum.土压力零点所在土层) {
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand()) {
                            valueArray[index] = String.format("%.2f",getDepthUpToSection(jkzhContext));
                        } else {
                            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor, 2);
                              valueArray[index] = hdValue;
                        }
                    } else if (this.model == JkzhGetValueModelEnum.被动土压力计算) {
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand()) {
                            valueArray[index] = String.format("%.2f",getDepthSectionToDown(jkzhContext));
                        } else {
                            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor, 2);
                            valueArray[index] = hdValue;
                        }
                    } else if (this.model == JkzhGetValueModelEnum.土压力零点深度计算) {
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()) {
                            valueArray[index] = "x";
                        } else {
                            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor, 2);
                            valueArray[index] = hdValue;
                        }
                    } else if (this.model == JkzhGetValueModelEnum.主动土压力合力计算
                            || this.model == JkzhGetValueModelEnum.主动作用点位置
                            || this.model == JkzhGetValueModelEnum.被动土压力合力计算
                            || this.model == JkzhGetValueModelEnum.被动作用点位置) {
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()) {
                            valueArray[index] = String.format("%.2f",getPressureZeroThickness(jkzhContext, this.model));
                        } else {
                            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor, 2);
                            valueArray[index] = hdValue;
                        }
                    } else if (this.model == JkzhGetValueModelEnum.主动支撑轴力计算) {
                        Double addm = 0.0;
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()) {
                            //若是在土压力零点这层土，那么主动合力至反弯点的距离，就按这层土顶面到零点的土层厚度
                            valueArray[index] = String.format("%.2f",getPressureZeroThickness(jkzhContext, this.model));
                        } else {
                            for (int i = 1; i <= floor; i++) {
                                String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), i, 2);
                                addm += Double.valueOf(hdValue);
                            }
                            valueArray[index] = String.format("%.2f",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero() - addm + Double.valueOf(getValuesFromMap(elementParam.getName() + elementParam.getIndex(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()))));
                        }
                    } else if (this.model == JkzhGetValueModelEnum.被动支撑轴力计算) {
                        Double addm = 0.0;
                        if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand() && floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()) {
                            //若是在土压力零点这层土，那么主动合力至反弯点的距离，就按这层土顶面到零点的土层厚度
                            valueArray[index] = String.format("%.2f",getPressureZeroThickness(jkzhContext, this.model));
                        } else if (floor == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()) {
                            //若是在土压力零点这层土，那么主动合力至反弯点的距离，就按这层土顶面到零点的土层厚度
                            valueArray[index] = String.format("%.2f",getPressureZeroThickness(jkzhContext, this.model));
                        } else {
                            for (int i = 1; i <= floor; i++) {
                                String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), i, 2);
                                addm += Double.valueOf(hdValue);
                            }
                            valueArray[index] = String.format("%.2f",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero() - addm + Double.valueOf(getValuesFromMap(elementParam.getName() + elementParam.getIndex(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()))));
                        }
                    } else if (this.model == JkzhGetValueModelEnum.支撑轴力计算) {
                        valueArray[index] = String.format("%.2f",Double.valueOf(getValuesFromMap(elementParam.getName() + elementParam.getIndex(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()))));
                    }
                    break;
                }
                case "重度": {
                    String zdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), Integer.valueOf(elementParam.getIndex()), 3);
                    valueArray[index] = zdValue;
                    break;
                }
                case "黏聚力": {
                    String njlValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), Integer.valueOf(elementParam.getIndex()), 4);
                    valueArray[index] = njlValue;
                    break;
                }
                case "内摩擦角": {
                    String nmcjValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), Integer.valueOf(elementParam.getIndex()), 5);
                    valueArray[index] = nmcjValue;
                    break;
                }
                case "主动土压力系数": {
                    String zdtylxsValue = getValuesFromSoilPressureTable(jkzhContext.getSoilPressureTable(), Integer.valueOf(elementParam.getIndex()), 2);
                    valueArray[index] = zdtylxsValue;
                    break;
                }
                case "被动土压力系数":{
                    String bdtylxsValue = getValuesFromSoilPressureTable(jkzhContext.getSoilPressureTable(), Integer.valueOf(elementParam.getIndex()),4);
                    valueArray[index] = bdtylxsValue;
                    break;
                }
                case "根号主动土压力系数": {
                    String ghZdtylxsValue = getValuesFromSoilPressureTable(jkzhContext.getSoilPressureTable(), Integer.valueOf(elementParam.getIndex()), 3);
                    valueArray[index] = ghZdtylxsValue;
                    break;
                }
                case "根号被动土压力系数": {
                    String ghBdtylxsValue = getValuesFromSoilPressureTable(jkzhContext.getSoilPressureTable(), Integer.valueOf(elementParam.getIndex()), 5);
                    valueArray[index] = ghBdtylxsValue;
                    break;
                }
                case "主动土压力合力":
                case "被动土压力合力":
                case "主动土压力上":
                case "被动土压力上":
                case "主动土压力下":
                case "被动土压力下": {
                    String vMap_1 = getValuesFromMap(elementParam.getName() + elementParam.getIndex(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()));
                    valueArray[index] = vMap_1;
                    break;
                }
                case "支撑轴力主动":
                case "支撑轴力被动": {
                    String vMap_2 = getValuesFromMap(elementParam.getName(), jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()));
                    valueArray[index] = vMap_2;
                    break;
                }
                case "水常量":{
                    valueArray[index] = String.format("%.2f",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getWaterConstant());
                    break;
                }
                case "基坑外水位":{
                    valueArray[index] = String.format("%.2f",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getZDWarterDepth());
                    break;
                }
                case "基坑内水位":{
                    valueArray[index] = String.format("%.2f",jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getBDWarterDepth());
                    break;
                }
                case "支撑轴力":{
                    String vMap_3 = getValuesFromMap(elementParam.getName() , jkzhContext.getTemporaryValues().get(this.jkzhContext.getCalTimes()-1));
                    valueArray[index] = vMap_3;
                    break;
                }
                case "支撑轴臂":{
                    //当前土层的土压力零点
                    Double pressureZero = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero();
                    //上一工况的支撑轴线位置
                    int beforCal = jkzhContext.getCalTimes()-1;
                    if(beforCal>0){
                        Double axis = jkzhContext.getJkzhBasicParams().get(beforCal).getAxis();
                        valueArray[index] = String.format("%.2f",pressureZero-axis);
                    }else {
                        valueArray[index] = String.format("%.2f",pressureZero);
                    }
                    break;
                }
                default : {
                    log.error("没有匹配的参数:{}", elementParam.getName());
                    break;
                }
            }
        }
        return valueArray;
    }

    /**
     * 从土压力系数表获取值
     * @param soilPressureTable 土压力系数表
     * @param line 第几行。
     * @param rows 第几列
     * @return
     */
    private String getValuesFromSoilPressureTable(SoilPressureTable soilPressureTable, int line, int rows){
        String value = "0.0";
        try {
            value = soilPressureTable.getTable()[line][rows];
        }catch (IndexOutOfBoundsException e) {
            log.error("getValuesFromSoilPressureTable:{}",e);
        }
        return value;
    }

    /**
     * 从土质参数表获取值
     * @param soilQualityTable 土压力系数表
     * @param line 第几行。
     * @param rows 第几列
     * @return
     */
    private String getValuesFromSoilQualityTable(SoilQualityTable soilQualityTable, int line, int rows){
        String value = "0.0";
        try {
            value = soilQualityTable.getTable()[line][rows];
        }catch (IndexOutOfBoundsException e) {
            log.error("getValuesFromSoilQualityTable:{}",e);
        }
        return value;
    }

    /**
     * 从Map中获取值
     * @param key
     * @param valueMap
     * @return
     */
    private String getValuesFromMap(String key, Map<String,String> valueMap){
        String value = valueMap.get(key);
        return value;
    }

    /**
     * 开挖土层切面到底面剩余厚度
     * @return
     */
    private Double getDepthSectionToDown(JkzhContext jkzhContext){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();floor++) {
            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor,2);
            addm += Double.valueOf(hdValue);
        }
        //剩余厚度
        Double surplusLand = addm-jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getDepth();
        return surplusLand;
    }

    /**
     * 开挖土层顶面到切面厚度
     * @return
     */
    private Double getDepthUpToSection(JkzhContext jkzhContext){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor < jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand();floor++) {
            String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor,2);
            addm += Double.valueOf(hdValue);
        }
        //剩余厚度
        Double surplusLand = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getDepth()-addm;
        return surplusLand;
    }

    /**
     * 土压力零点所在土层顶面到土压力零点之间的距离
     * @return
     */
    private Double getPressureZeroThickness(JkzhContext jkzhContext,JkzhGetValueModelEnum model){
        Double addm = 0.0;
        Double zeroThickness = 0.0;
        //土压力零点与开挖深度在同一土层
        if(this.model == JkzhGetValueModelEnum.被动土压力合力计算 || this.model == JkzhGetValueModelEnum.被动作用点位置){
            if(jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtDepthLand() == jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand()){
                zeroThickness = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero() - jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getDepth();
            }else{
                for (int floor = 1; floor < jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand(); floor++) {
                    String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor,2);
                    addm += Double.valueOf(hdValue);
                }
                zeroThickness = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero() - addm;
            }
        }else{
            for (int floor = 1; floor < jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getAtZoneLand(); floor++) {
                String hdValue = getValuesFromSoilQualityTable(jkzhContext.getSoilQualityTable(), floor,2);
                addm += Double.valueOf(hdValue);
            }
            zeroThickness = jkzhContext.getJkzhBasicParams().get(jkzhContext.getCalTimes()).getCalResult().getPressureZero() - addm;
        }
        //剩余厚度
        return zeroThickness;
    }
}

package org.context;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.calParam.JkzhBasicParam;
import org.calculation.ICreateFixedElement;
import org.element.BaseElement;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.HashMap;
import java.util.List;

/**
 * 基坑支护计算上下文对象
 */
@Data
@Slf4j
public class JkzhContext extends AbstractContext{

    //基坑支护计算 基本参数
    private List<JkzhBasicParam> jkzhBasicParams;

    //土层参数计算依据表
    private SoilQualityTable soilQualityTable;

    //基坑支护 土压力系数表
    private SoilPressureTable soilPressureTable;

    //模板元素
    private HashMap<String, BaseElement> elementTemplate;

    /**
     * 更具当前第几工况，刷新上下文数据
     * 总土层数、主动土层水位所在第几层土、被动土层水位所在第几层土、计算开挖面所在第几层土
     * 在计算各个工况前，都要重新刷新一下当前上下文
     */
    public void refresh(int calTimes, ICreateFixedElement iCreateFixedElement){
        //当前计算第几个工况
        super.setCalTimes(calTimes-1);
        //初始化集合
        initialize();
        iCreateFixedElement.createFixedElement();
        //总土层数
        calAllLands(soilQualityTable,this.getJkzhBasicParams().get(super.getCalTimes()));
        //计算主动土层 水位所在第几层土
        calZDWaterLand(soilQualityTable,this.getJkzhBasicParams().get(super.getCalTimes()));
        //计算被动土层 水位所在第几层土
        calBDWaterLand(soilQualityTable,this.getJkzhBasicParams().get(super.getCalTimes()));
        //计算开挖面所在第几层土
        calDepthLand(soilQualityTable,this.getJkzhBasicParams().get(super.getCalTimes()));
    }

    /**
     * 总土层数
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calAllLands(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        jkzhBasicParam.setAllLands(soilQualityTable.getTable().length -1);
    }

    /**
     * 计算主动土层 水位所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calZDWaterLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getZDWarterDepth())>=0){
                jkzhBasicParam.getCalResult().setZDWaterLand(floor);
                break;
            }
        }
    }

    /**
     * 计算被动土层 水位所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calBDWaterLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getBDWarterDepth())>=0){
                jkzhBasicParam.getCalResult().setBDWaterLand(floor);
                break;
            }
        }
    }


    /**
     * 计算开挖面所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calDepthLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getDepth())>=0){
                jkzhBasicParam.getCalResult().setAtDepthLand(floor);
                break;
            }
        }
    }

    /**
     * 初始化当前集合数据
     */
    private void initialize(){
        HashMap<String, BaseElement> elementTemplate = new HashMap<>(128);
        this.getElementTemplates().add(super.getCalTimes(),elementTemplate);

        HashMap<String,String> temporaryValue = new HashMap<>(128);
        this.getTemporaryValues().add(super.getCalTimes(),temporaryValue);
    }
}

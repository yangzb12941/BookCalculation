package org;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.calParam.CalResult;
import org.calculation.JkzhCalculation;
import org.context.JkzhContext;
import org.context.JkzhContextFactory;
import org.elementHandler.ElementHandlerUtils;
import org.junit.Test;
import org.calParam.JkzhBasicParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestTemplate {
    @Test
    //往表格里插入计算公式测试
    public void writeFormulaToTableCellTest() throws IOException {
        List<JkzhBasicParam> jkzhBasicParams = createJkzhBasicParam();
        final JkzhContext jkzhContext = JkzhContextFactory.getJkzhContext(jkzhBasicParams, createTable());
        jkzhContext.refresh(1);
        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhContext);
        //计算主动土压力
        jkzhCalculation.zdPressure();
        //计算被动土压力
        jkzhCalculation.bdPressure(7.0);
        //计算土压力零点
        jkzhCalculation.pressureZero(7.0);
        //计算主动土压力合力
        jkzhCalculation.zdResultantEarthPressures();
        //主动土压力合力作用点位置
        jkzhCalculation.zdPositionAction();
        //被动土压力合力及作用点位置
        jkzhCalculation.bdResultantEarthPressures(7.0);
        //被动土压力合力作用点位置
        jkzhCalculation.bdPositionAction(7.0);
        //支撑处水平力计算
        jkzhCalculation.calStrutForce(7.0);

        XWPFTemplate compile = XWPFTemplate.compile("src\\test\\templates\\铁男基坑支护模板.docx");
        List<MetaTemplate> elementTemplates = compile.getElementTemplates();
        Map<String, Object> values = new HashMap<String, Object>() {
            {
                for (MetaTemplate item:elementTemplates) {
                    if(item instanceof RunTemplate){
                        String tagName = ((RunTemplate) item).getTagName();
                        log.info("RunTemplate TagName:{}",tagName);
                        put(tagName, ElementHandlerUtils.getElementValue(jkzhContext,item));
                    }else if (item instanceof IterableTemplate){
                        String tagName = ((IterableTemplate)item).getStartMark().getTagName();
                        log.info("IterableTemplate TagName:{}",tagName);
                        put(tagName, ElementHandlerUtils.getElementValue(jkzhContext,item));
                    }
                }
            }
        };
        compile.render(values).writeToFile("out_基坑支护设计排桩法模板.docx");
    }

    private List<JkzhBasicParam> createJkzhBasicParam(){
        List<JkzhBasicParam> jkzhBasicParams = new ArrayList<>();
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        jkzhBasicParam.setSurcharge(20.0);
        jkzhBasicParam.setAxis(0.4);
        jkzhBasicParam.setDepth(7.0);
        jkzhBasicParam.setZDWarterDepth(2.5);
        jkzhBasicParam.setBDWarterDepth(10.5);
        jkzhBasicParam.setWaterConstant(20.0);
        CalResult calResult = new CalResult();
        jkzhBasicParam.setCalResult(calResult);
        jkzhBasicParam.setCalTimes(1);
        jkzhBasicParams.add(jkzhBasicParam);
        return jkzhBasicParams;
    }

    private String[][] createTable(){
        //土压力系数头
        String[][] table = {
                {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","黏聚力(kPa)\nc","内摩擦角(°)\nΨ","计算方式"},
                {"1", "人工填土","1.2","18.0","5.7","13.5","水土合算"},
                {"2", "淤泥质粉质黏土","5","17.8","8.2","9.6","水土合算"},
                {"3", "粉质黏土","3.8","20.0","14","16.2","水土分算"},
                {"4", "黏性土","7.4","20.5","22","20.8","水土合算"},
        };
        return table;
    }
}

package org;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.calParam.CalResult;
import org.calculation.CreateFixedElementHandle;
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
        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhContext);
        for (int i = 1; i <= 2;i++) {
            jkzhContext.refresh(i,new CreateFixedElementHandle(
                    jkzhCalculation.getJkzhFromulaHandle(),
                    jkzhCalculation.getJkzhPrefixLayout(),
                    jkzhCalculation.getJkzhContext()));
            //计算主动土压力
            jkzhCalculation.zdPressure();
            //计算被动土压力
            jkzhCalculation.bdPressure(jkzhBasicParams.get(i).getDepth());
            //计算土压力零点
            jkzhCalculation.pressureZero(jkzhBasicParams.get(i).getDepth());
            //计算主动土压力合力
            jkzhCalculation.zdResultantEarthPressures();
            //主动土压力合力作用点位置
            jkzhCalculation.zdPositionAction();
            //被动土压力合力及作用点位置
            jkzhCalculation.bdResultantEarthPressures(jkzhBasicParams.get(i).getDepth());
            //被动土压力合力作用点位置
            jkzhCalculation.bdPositionAction(jkzhBasicParams.get(i).getDepth());
            //支撑处水平力计算
            jkzhCalculation.calStrutForce(jkzhBasicParams.get(i).getDepth());
        }

        XWPFTemplate compile = XWPFTemplate.compile("src\\test\\templates\\铁男基坑支护模板.docx");
        List<MetaTemplate> elementTemplates = compile.getElementTemplates();
        Map<String, Object> values = new HashMap<String, Object>() {
            {
                //设置解析模板非多工况部分填充值
                jkzhContext.setCalTimes(0);
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
        //为空对象，没有工况参数值
        jkzhBasicParams.add(new JkzhBasicParam());

        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        jkzhBasicParam.setSurcharge(20.0);
        jkzhBasicParam.setAxis(0.4);
        jkzhBasicParam.setDepth(5.9);
        jkzhBasicParam.setZDWarterDepth(2.5);
        jkzhBasicParam.setBDWarterDepth(6.4);
        jkzhBasicParam.setWaterConstant(20.0);
        CalResult calResult = new CalResult();
        jkzhBasicParam.setCalResult(calResult);

        jkzhBasicParams.add(jkzhBasicParam);

        JkzhBasicParam jkzhBasicParam2 = new JkzhBasicParam();
        jkzhBasicParam2.setSurcharge(20.0);
        jkzhBasicParam2.setAxis(0.4);
        jkzhBasicParam2.setDepth(10.0);
        jkzhBasicParam2.setZDWarterDepth(2.5);
        jkzhBasicParam2.setBDWarterDepth(10.5);
        jkzhBasicParam2.setWaterConstant(20.0);
        CalResult calResult2 = new CalResult();
        jkzhBasicParam2.setCalResult(calResult2);
        jkzhBasicParams.add(jkzhBasicParam2);
        return jkzhBasicParams;
    }

    private String[][] createTable(){
        //土压力系数头
        String[][] table = {
                {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","黏聚力(kPa)\nc","内摩擦角(°)\nΨ","计算方式"},
                {"1", "①2素填土","2.30","19.30","8.00","4.00","水土合算"},
                {"2", "④1粘土","2.40","18.50","7.80","3.50","水土合算"},
                {"3", "⑥2淤泥质粘土","3.30","17.50","3.80","3.50","水土合算"},
                {"4", "⑥3粉质粘土","9.00","19.10","12.50","13.30","水土合算"},
                {"5", "⑦粘土","2.00","17.90","12.40","5.50","水土合算"},
                {"6", "⑧1粉质粘土","3.00","20.40","14.10","10.70","水土合算"},
                {"7", "⑧2粉土","4.90","20.00","10.80","26.90","水土分算"},
                {"8", "⑨1粉质粘土","6","19.70","14.80","16.50","水土合算"},
        };
        return table;
    }
}

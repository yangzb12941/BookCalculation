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
        for (int i = 1; i <= jkzhBasicParams.size()-1;i++) {
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

        //弯矩计算，以上的计算结果，有些是当作计算弯矩的参数
        jkzhCalculation.maxBendingMoment();

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
        jkzhBasicParam.setDepth(5.4);
        jkzhBasicParam.setZDWarterDepth(0d);
        jkzhBasicParam.setBDWarterDepth(0d);
        jkzhBasicParam.setWaterConstant(10.0);
        CalResult calResult = new CalResult();
        jkzhBasicParam.setCalResult(calResult);

        jkzhBasicParams.add(jkzhBasicParam);

        JkzhBasicParam jkzhBasicParam2 = new JkzhBasicParam();
        jkzhBasicParam2.setSurcharge(20.0);
        jkzhBasicParam2.setAxis(4.9);
        jkzhBasicParam2.setDepth(9.8);
        jkzhBasicParam2.setZDWarterDepth(0d);
        jkzhBasicParam2.setBDWarterDepth(0d);
        jkzhBasicParam2.setWaterConstant(10.0);
        CalResult calResult2 = new CalResult();
        jkzhBasicParam2.setCalResult(calResult2);
        jkzhBasicParams.add(jkzhBasicParam2);
        return jkzhBasicParams;
    }

    private String[][] createTable(){
        //土压力系数头
        String[][] table = {
                {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","黏聚力(kPa)\nc","内摩擦角(°)\nΨ","计算方式"},
                {"1","素填土","1.2","19.2","10.0","14.0","水土合算"},
                {"2","粉质粘土","7.1","19.6","15.0","16.0","水土合算"},
                {"3","淤泥质粉质粘土","4.2","19.7","18","18","水土合算"},
                {"4","砂质粉土","6.6","20.1","25","14","水土合算"},
                {"5","淤泥质粉质粘土","15","21","20","25","水土合算"}
        };
        return table;
    }
}

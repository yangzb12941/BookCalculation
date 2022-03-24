package org;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.calculation.JkzhCalculation;
import org.context.JkzhContext;
import org.elementHandler.ElementHandlerUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestTemplate {
    @Test
    //往表格里插入计算公式测试
    public void writeFormulaToTableCellTest() throws IOException {
        JkzhFromulaHandle jkzhFromulaHandle = new JkzhFromulaHandle();
        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhFromulaHandle);
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

        JkzhContext jkzhContext = jkzhCalculation.getJkzhContext();

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
}

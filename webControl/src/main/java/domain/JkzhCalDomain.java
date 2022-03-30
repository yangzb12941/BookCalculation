package domain;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.calculation.JkzhCalculation;
import org.context.JkzhContext;
import org.context.JkzhContextFactory;
import org.elementHandler.ElementHandlerUtils;
import org.springframework.stereotype.Component;
import org.table.JkzhBasicParam;
import request.JkzhRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class JkzhCalDomain {

    //往表格里插入计算公式测试
    public String writeToWord(String templatePath, String downloadPath, JkzhRequest jkzhRequest) throws IOException {

        JkzhBasicParam jkzhBasicParam = createJkzhBasicParam(jkzhRequest);
        final JkzhContext jkzhContext = JkzhContextFactory.getJkzhContext(jkzhBasicParam, jkzhRequest.getTable());

        JkzhCalculation jkzhCalculation = new JkzhCalculation(jkzhContext);
        //计算主动土压力
        jkzhCalculation.zdPressure();
        //计算被动土压力
        jkzhCalculation.bdPressure(jkzhRequest.getDepth());
        //计算土压力零点
        jkzhCalculation.pressureZero(jkzhRequest.getDepth());
        //计算主动土压力合力
        jkzhCalculation.zdResultantEarthPressures();
        //主动土压力合力作用点位置
        jkzhCalculation.zdPositionAction();
        //被动土压力合力及作用点位置
        jkzhCalculation.bdResultantEarthPressures(jkzhRequest.getDepth());
        //被动土压力合力作用点位置
        jkzhCalculation.bdPositionAction(jkzhRequest.getDepth());
        //支撑处水平力计算
        jkzhCalculation.calStrutForce(jkzhRequest.getDepth());

        XWPFTemplate compile = XWPFTemplate.compile(templatePath+"铁男基坑支护模板.docx");
        final List<MetaTemplate> elementTemplates = compile.getElementTemplates();
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
        String fileName = UUID.randomUUID().toString();
        String pathName= downloadPath+fileName+".docx";
        compile.render(values).writeToFile(pathName);
        return fileName;
    }

    private JkzhBasicParam createJkzhBasicParam(JkzhRequest jkzhRequest){
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        jkzhBasicParam.setSurcharge(jkzhRequest.getSurcharge());
        jkzhBasicParam.setAxis(jkzhRequest.getAxis());
        jkzhBasicParam.setDepth(jkzhRequest.getDepth());
        jkzhBasicParam.setZDWarterDepth(jkzhRequest.getZDWarterDepth());
        jkzhBasicParam.setBDWarterDepth(jkzhRequest.getBDWarterDepth());
        jkzhBasicParam.setWaterConstant(jkzhRequest.getWaterConstant());
        return jkzhBasicParam;
    }
}

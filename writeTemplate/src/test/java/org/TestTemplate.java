package org;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TestTemplate {
    @Test
    //往表格里插入计算公式测试
    public void writeFormulaToTableCellTest() throws IOException {
        XWPFTemplate compile = XWPFTemplate.compile("src\\test\\templates\\铁男基坑支护模板.docx");
        List<MetaTemplate> elementTemplates = compile.getElementTemplates();
        for (MetaTemplate item:elementTemplates) {
            if(item instanceof RunTemplate){
                log.info("TagName:{}",((RunTemplate)item).getTagName());
                log.info("Source:{}",((RunTemplate)item).getSource());
            }else if (item instanceof IterableTemplate){
                log.info("TagName:{}",((IterableTemplate)item).getStartMark().getTagName());
                log.info("Source:{}",((IterableTemplate)item).getStartMark().getSource());
            }
        }
    }
}

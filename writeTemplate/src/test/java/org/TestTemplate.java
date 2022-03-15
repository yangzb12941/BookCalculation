package org;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.MetaTemplate;
import lombok.extern.slf4j.Slf4j;
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
        XWPFTemplate compile = XWPFTemplate.compile("src\\test\\templates\\铁男基坑支护模板.docx");
        List<MetaTemplate> elementTemplates = compile.getElementTemplates();
        for (MetaTemplate item:elementTemplates) {
            log.info("TagName:{}",((ElementTemplate)item).getTagName());
        }
    }
}

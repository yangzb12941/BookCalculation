package org.writeTemplate;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import lombok.extern.slf4j.Slf4j;
import org.context.AbstractContext;
import org.context.JkzhContext;
import org.elementHandler.ElementHandlerUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JkzhWriteLatexToDoc {
    public void writelatexToDoc(AbstractContext abstractContext, XWPFTemplate xWPFTemplate,String toFileName) throws IOException {
        JkzhContext jkzhContext  = (JkzhContext)abstractContext;
        List<MetaTemplate> elementTemplates = xWPFTemplate.getElementTemplates();
        Map<String, Object> values = new HashMap<String, Object>(128) {
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
        xWPFTemplate.render(values).writeToFile(toFileName);
    }
}

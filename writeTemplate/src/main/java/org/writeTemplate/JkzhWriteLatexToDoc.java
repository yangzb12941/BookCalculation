package org.writeTemplate;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.IterableTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.context.AbstractContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JkzhWriteLatexToDoc {

    public void writelatexToDoc(AbstractContext AbstractContext, XWPFTemplate xWPFTemplate,String toFileName) throws IOException {
        List<MetaTemplate> elementTemplates = xWPFTemplate.getElementTemplates();
        Map<String, Object> values = new HashMap<String, Object>(128);
        for(MetaTemplate metaTemplate:elementTemplates){
            if(metaTemplate instanceof RunTemplate){
                ((RunTemplate)metaTemplate).getTagName();
            }else if (metaTemplate instanceof IterableTemplate){
                ((IterableTemplate)metaTemplate).getStartMark().getTagName();
            }
            xWPFTemplate.render(values).writeToFile(toFileName);
        }
    }
}

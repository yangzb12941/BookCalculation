package org.writeTemplate;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.template.ElementTemplate;
import org.context.IContext;
import org.handler.IDisplayHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractWriteLatexToDoc implements IWriteLatexToDoc{

    @Override
    public void latexToDocHandle(IContext iContext,
                                 XWPFTemplate xWPFTemplate,
                                 String toFileName,
                                 IDisplayHandler iDisplayHandler) throws IOException {
        Map<String, Object>  values= iDisplayHandler.handlerDataMap(iContext);
        Map<String, Object> beforValues = beforValuesHandler(values);
        xWPFTemplate.render(beforValues).writeToFile(toFileName);
    }

    /**
     * 对values
     * @param values
     */
    public abstract Map<String, Object> beforValuesHandler(Map<String, Object> values);

    /**
     * 对xWPFTemplate 元素的解析
     * @param eleTemplates
     */
    public abstract List<String> beforWordHandler(List<ElementTemplate> eleTemplates);
}

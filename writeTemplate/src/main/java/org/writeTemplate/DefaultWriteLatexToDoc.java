package org.writeTemplate;

import com.deepoove.poi.template.ElementTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultWriteLatexToDoc extends AbstractWriteLatexToDoc{
    @Override
    public Map<String, Object> beforValuesHandler(Map<String, Object> values) {
        return values;
    }

    @Override
    public List<String> beforWordHandler(List<ElementTemplate> eleTemplates) {
        return eleTemplates.stream().map(e->e.getTagName()).collect(Collectors.toList());
    }
}

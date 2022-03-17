package org.element;

import com.deepoove.poi.data.RowRenderData;

public class TableElement extends BaseElement<RowRenderData[]>{
    public TableElement(String tagName, RowRenderData[] value) {
        super(tagName, value);
    }
}

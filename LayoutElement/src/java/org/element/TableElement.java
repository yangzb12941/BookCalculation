package org.element;

import com.deepoove.poi.data.RowRenderData;

public class TableElement extends BaseElement<RowRenderData[]>{
    public TableElement(Integer index,String tagName, RowRenderData[] value) {
        super(index,tagName, value);
    }
}

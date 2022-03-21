package org.elementHandler;

import com.deepoove.poi.data.*;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.context.AbstractContext;
import org.element.BaseElement;

import java.util.Objects;


public class TableElementHandler implements IElementHandler<TableRenderData>{

    private static volatile TableElementHandler instance;

    public static TableElementHandler getInstance(){
        if(instance == null){
            synchronized (TableElementHandler.class){
                if (instance == null) {
                    instance = new TableElementHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public TableRenderData getElementValue(AbstractContext abstractContext, MetaTemplate metaTemplate) {
        RunTemplate runTemplate = (RunTemplate) metaTemplate;
        BaseElement baseElement = abstractContext.getElementTemplate().get(runTemplate.getTagName());
        RowRenderData[] rowRenderData = soilPressureTable(baseElement);
        return Tables.create(rowRenderData);
    }

    /**
     * 计算压力系数表
     *
     */
    private RowRenderData[] soilPressureTable(BaseElement baseElement){
        String[][] table = (String[][])baseElement.getValue();
        RowRenderData[] t1rows = new RowRenderData[table.length];
        for (int i = 0; i < table.length; i++) {
            RowRenderData t1row = null;
            for (int j = 0; j< table[i].length; j++) {
                if(Objects.nonNull(t1row)){
                    CellRenderData cellRenderData = Cells.of(table[i][j]).create();
                    t1row.addCell(cellRenderData);
                }else{
                    t1row = Rows.create(table[i][j]);
                }
            }
            t1rows[i] = t1row;
        }
        return t1rows;
    }
}

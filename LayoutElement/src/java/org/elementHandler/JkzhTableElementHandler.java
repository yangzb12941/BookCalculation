package org.elementHandler;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import org.context.IContext;
import org.context.JkzhContext;
import org.element.TableElement;

import java.util.Objects;

public class JkzhTableElementHandler extends TableElementHandler{

    @Override
    public TableElement getElementValue(IContext iContext, String key) {
        TableElement tableElement = null;
        if(key.equals("土层参数计算依据表")){
            RowRenderData[] rowRenderData = soilPressureTable((JkzhContext) iContext);
            tableElement = new TableElement(key,rowRenderData);
        }else if (key.equals("土压力系数表")){
            RowRenderData[] rowRenderData = soilParameters((JkzhContext) iContext);
            tableElement = new TableElement(key,rowRenderData);
        }
        return tableElement;
    }

    /**
     * 计算压力系数表
     *
     */
    private RowRenderData[] soilPressureTable(JkzhContext jkzhContext){
        //表格数据-土层参数计算依据表
        // 第0行居中且背景为蓝色的表格
        //表格数据-土压力系数表
        RowRenderData t1row0 = Rows.of(jkzhContext.getJkzhBasicParam().getSoilPressureTableHeader()).center().create();
        String[][] table = jkzhContext.getSoilPressureTable().getTable();
        RowRenderData[] t1rows = new RowRenderData[table.length+1];
        //表格首列
        t1rows[0]=t1row0;
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
            t1rows[i+1] = t1row;
        }
        return t1rows;
    }

    //土层参数计算依据表
    private RowRenderData[] soilParameters(JkzhContext jkzhContext){
        //表格数据-土层参数计算依据表
        // 第0行居中且背景为蓝色的表格
        RowRenderData t1row0 = Rows.of(jkzhContext.getJkzhBasicParam().getSoilQualityTableHeader()).center().create();
        String[][] table = jkzhContext.getJkzhBasicParam().getSoilQualityTable().getTable();
        RowRenderData[] t1rows = new RowRenderData[table.length+1];
        //表格首列
        t1rows[0]=t1row0;
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
            t1rows[i+1] = t1row;
        }
        return t1rows;
    }
}

package org.readTemplate;

import com.deepoove.poi.XWPFTemplate;

public interface IReadTemplate {

    /**
     * 读取文件，返回文件对象。用于POI读取文档，写入数学公式
     * 这种方式写入latex表达式，再通过通过mathType转换成可编辑的mathType
     * 表达式
     * @param fileName 文件名称
     * @return
     */
    public XWPFTemplate readPOIWordDoc(String fileName);
}

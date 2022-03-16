package org.writeTemplate;

import org.handler.IDisplayHandler;

import javax.swing.text.Document;
public interface IWriteLatexToOOXML {
    /**
     * 计算公式写入文档，OOXML格式。生成的文档不能手工编辑。
     * @param document
     * @param toFileName
     * @param iDisplayHandler
     */
    public void latexToOOXMLHandle(Document document, String toFileName, IDisplayHandler iDisplayHandler);
}

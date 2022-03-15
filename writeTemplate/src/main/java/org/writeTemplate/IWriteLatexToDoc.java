package org.writeTemplate;

import com.deepoove.poi.XWPFTemplate;
import org.context.IContext;
import org.handler.IDisplayHandler;

import javax.swing.text.Document;
import java.io.IOException;

public interface IWriteLatexToDoc {
    /**
     * 计算公式写入文档，latex格式。生成文档之后需要通过mathType
     * 处理一下。
     * @param iContext 上下文
     * @param xWPFTemplate POI模板类
     * @param toFileName 保存文档名称
     * @param iDisplayHandler
     */
    public void latexToDocHandle(IContext iContext,XWPFTemplate xWPFTemplate, String toFileName, IDisplayHandler iDisplayHandler) throws IOException;
}

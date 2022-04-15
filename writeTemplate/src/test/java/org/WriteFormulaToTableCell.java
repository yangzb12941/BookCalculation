package org;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.enumUtils.EnumUtils;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class WriteFormulaToTableCell {

    @Test
    //往表格里插入计算公式测试
    public void writeFormulaToTableCellTest() throws IOException {
        Double math = (Double) AviatorEvaluator.execute(TestConfigEnum.Tp.getLatexCal());
        String result = TestConfigEnum.Tp.getLatex()+"="+TestConfigEnum.Tp.getCalculate()+"="+String.format("%.2f",math)+"{N/{mm}^2}";
        Map<String, Object> values = new HashMap<String, Object>() {
            {
                put(TestConfigEnum.Tp.getKey(), TestConfigEnum.Tp.getKey());
            }
        };
        //word公式展示
        Map<String,String> formate = new HashMap<>(128);
        formate.put(TestConfigEnum.Tp.getKey(),result);
        //spireDocTable(formate,"src\\test\\templates\\表格写入计算公式.docx");
    }
    //在表格写入公式
    /*
      private void spireDocTable(Map<String,String> formate,String fileName) throws IOException {
        //加载Word测试文档
        Document doc = new Document();
        doc.loadFromFile(fileName);
        //获取第一节
        Section section = doc.getSections().get(0);
        //获取第一个表格
        ITable table = section.getTables().get(0);

        //遍历表格中的行
        for (int i = 0; i < table.getRows().getCount(); i++)
        {
            TableRow row = table.getRows().get(i);
            //遍历每行中的单元格
            for (int j = 0; j < row.getCells().getCount(); j++)
            {
                TableCell cell = row.getCells().get(j);
                //遍历单元格中的段落
                for (int k = 0; k < cell.getParagraphs().getCount(); k++)
                {
                    Paragraph paragraph = cell.getParagraphs().get(k);
                    log.info("{}",paragraph.getText());
                    String skey = formate.get(paragraph.getText().trim());
                    if(Objects.nonNull(skey)){
                        OfficeMath officeMath2 = new OfficeMath(doc);
                        paragraph.getItems().clear();
                        paragraph.getItems().add(officeMath2);
                        officeMath2.fromLatexMathCode(skey);
                        continue;
                    }
                }
            }
        }
        //保存文档
        doc.saveToFile("表格写入计算公式-1.docx", FileFormat.Docx_2013);
        doc.dispose();
    }

    @Test
    public void spireDoc() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //在初始化Document对象时加载示例Word文档
        Document doc = new Document("src/test/resources/testaaa.docx");
        //声明一个变量
        Paragraph paragraph;
        boolean isTrue=false;
        //循环遍历各个节
        for (int i = 0; i < doc.getSections().getCount(); i++) {
            isTrue=false;
            //循环遍历特定节的段落
            for (int j = 0; j < doc.getSections().get(i).getParagraphs().getCount(); j++) {
                //获取特定段落
                paragraph = doc.getSections().get(i).getParagraphs().get(j);
                log.info("{}",paragraph.getText());
                if(paragraph.getText().trim().equals("{{Pak}}")){
                    String mathName = paragraph.getText().replace("{","").replace("}","");
                    OfficeMath officeMath2 = new OfficeMath(doc);
                    paragraph.getItems().clear();
                    paragraph.getItems().add(officeMath2);
                    officeMath2.fromLatexMathCode(EnumUtils.getLatex(JkzhConfigEnum.class,mathName));
                    continue;
                }
                if(paragraph.getText().trim().equals("{{Kai}}")){
                    String mathName = paragraph.getText().replace("{","").replace("}","");
                    OfficeMath officeMath2 = new OfficeMath(doc);
                    paragraph.getItems().clear();
                    paragraph.getItems().add(officeMath2);
                    officeMath2.fromLatexMathCode(EnumUtils.getLatex(JkzhConfigEnum.class,mathName));
                    continue;
                }
                if(paragraph.getText().trim().equals("{{Ppk}}")){
                    String mathName = paragraph.getText().replace("{","").replace("}","");
                    OfficeMath officeMath2 = new OfficeMath(doc);
                    paragraph.getItems().clear();
                    paragraph.getItems().add(officeMath2);
                    officeMath2.fromLatexMathCode(EnumUtils.getLatex(JkzhConfigEnum.class,mathName));
                    continue;
                }
                if(paragraph.getText().trim().equals("{{Kpi}}")){
                    String mathName = paragraph.getText().replace("{","").replace("}","");
                    OfficeMath officeMath2 = new OfficeMath(doc);
                    paragraph.getItems().clear();
                    paragraph.getItems().add(officeMath2);
                    officeMath2.fromLatexMathCode(EnumUtils.getLatex(JkzhConfigEnum.class,mathName));
                    continue;
                }
            }
        }
        //保存文档
        doc.saveToFile("addMathEquation-2.docx", FileFormat.Docx_2013);
        doc.dispose();
    }


    @Test
    public void addOLEToDoc() throws IOException {
        //在初始化Document对象时加载示例Word文档
        Document doc = new Document("src/test/templates/addOLEToDoc.docx");
        //获取最后一节
        Section section = doc.getLastSection();
        //添加段落
        Paragraph par = section.addParagraph();
        //加载一个图片，它将作为外部文件的符号显示在Word文档中
        DocPicture pdfIcon = new DocPicture(doc);
        //pdfIcon.loadImage("src/test/templates/media/image1.wmf");
        pdfIcon.loadImage("src/test/templates/MommyTalk1645950671091.png");
        //将一个PDF文件作为OLE对象插入Word文档
        par.appendOleObject("src/test/templates/non-eps-aaa.eps", pdfIcon, OleLinkType.Embed);
        //另存为一个文档OleLinkType
        doc.saveToFile("EmbedDocument.docx", FileFormat.Docx_2013);
    }
     */
}

package org.readTemplate;

import com.deepoove.poi.XWPFTemplate;
import com.spire.doc.Document;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DefaultReadTemplate implements IReadTemplate{
    @Override
    public Document readSpireWordDoc(String fileName) {
        if(isFileExists(fileName)){
            return new Document(fileName);
        }
        return null;
    }

    @Override
    public XWPFTemplate readPOIWordDoc(String fileName) {
        if(isFileExists(fileName)){
            return XWPFTemplate.compile(fileName);
        }
        return null;
    }

    /**
     * 校验文件是否存在
     * @param fileName 文件名
     * @return
     */
    private Boolean isFileExists(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}

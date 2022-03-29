package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.element.FormulaElement;
import org.entity.ElementParam;
import org.junit.Test;
import org.show.JkzhILayout;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class FormulaElementTest {
    @Test
    public void formulaElementTest(){
        JkzhILayout jkzhILayout = new JkzhILayout();
        Set<Map.Entry<String, String>> entries = jkzhILayout.getLayoutMap().entrySet();
        for(Map.Entry<String, String> item:entries){
            FormulaElement formulaElement = new FormulaElement(3,jkzhILayout,item.getKey(),"aaaaaaa");
            log.info("元素:{}:{}",item.getKey(), formulaElement.getValue());
        }
    }
}

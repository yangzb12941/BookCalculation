package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.element.FormulaElement;
import org.junit.Test;
import org.show.JkzhPrefixLayout;

import java.util.Map;
import java.util.Set;

@Slf4j
public class FormulaElementTest {
    @Test
    public void formulaElementTest(){
        JkzhPrefixLayout jkzhPrefixLayout = new JkzhPrefixLayout();
        Set<Map.Entry<String, String>> entries = jkzhPrefixLayout.getLayoutMap().entrySet();
        for(Map.Entry<String, String> item:entries){
            //if(item.getKey().equals("被动土压力下")){
                FormulaElement formulaElement = new FormulaElement(3, jkzhPrefixLayout,item.getKey(),"aaaaaaa");
                log.info("元素:{}:{}",item.getKey(), formulaElement.getValue());
            //}
        }
    }
}

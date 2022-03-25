package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.ElementParam;
import org.enumUtils.StringUtil;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetParamTest {

    @Test
    public void getParamTest(){
        String fromula = "(地面堆载_{5}+重度_{1} \\times 厚度_{1}+重度_{2} \\times 厚度_{2}+重度_{3} \\times 厚度_{3}+重度_{4} \\times 厚度_{4}+重度_{5} \\times 厚度_{5}) \\times 主动土压力系数_{5}- 2 \\times 内聚力_{5} \\times \\sqrt{主动土压力系数_{5}}";
        List<ElementParam> elementParams = getParam(fromula);
        log.info("元素:{}", elementParams);
    }

    private List<ElementParam> getParam(String fromula){
        List<ElementParam> elementParams = new ArrayList<ElementParam>();
        ArrayDeque<Character> deque = new ArrayDeque<Character>();
        Boolean isChanged = Boolean.FALSE;
        char[] chars = fromula.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(StringUtil.isChineseChar(chars[i])){
                isChanged = Boolean.TRUE;
                deque.push(chars[i]);
                continue;
            }
            if (isChanged) {
                deque.push(chars[i]);
                isChanged = Boolean.FALSE;
                //往前跳过n个字符去掉 _{5}格式内容
                int skip = 0;
                do {
                    skip++;
                    deque.push(chars[i+skip]);
                }while (chars[skip+i]!='}');
                i=i+skip;
            }
            if (!deque.isEmpty()) {
                StringBuilder subPart = new StringBuilder();
                do{
                    subPart.append(deque.pollLast());
                }while (!deque.isEmpty());
                ElementParam elementParam = createParam(subPart.toString());
                elementParams.add(elementParam);
            }
        }
        return elementParams;
    }

    private ElementParam createParam(String element){
        ElementParam elementParam = new ElementParam();
        String[] s = element.split("_");
        elementParam.setName(s[0]);
        elementParam.setIndex(s[1].substring(1,s[1].length() - 1));
        return elementParam;
    }
}

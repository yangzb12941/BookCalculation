package org.handle;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.entity.ElementParam;
import org.enumUtils.StringUtil;
import org.getValue.GetValues;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * 对公式进行值填充处理
 */
@Slf4j
public class FillValueHandler implements IHandler<GetValues>{
    //为公式匹配对应的值
    private GetValues getValues;

    @Override
    public String execute(String fromula) {
        List<ElementParam> elementParams = getParams(fromula);
        log.info("FillValueHandler params:{}", JSONObject.toJSONString(elementParams));
        getValues.setElementParams(elementParams);
        String[] values = getValues.getValues();
        log.info("GetValues values:{}", JSONObject.toJSONString(values));
        String placeholder = placeholder(fromula);
        log.info("FillValueHandler placeholder:{}", placeholder);
        String fillCal = valuesFillCal(placeholder, values);
        log.info("FillValueHandler valuesFillCal:{}", fillCal);
        return fillCal;
    }

    @Override
    public FillValueHandler setParams(GetValues getValues) {
        this.getValues = getValues;
        return this;
    }

    /**
     * 通过解析公式把每一个字符替换为对应的具体数值。值的顺序要与解析的字符一一匹配，
     * 若是错位，则会填充错误。
     * 例如：
     * (地面堆载+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度) \times 主动土压力系数- 2 \times 内聚力 \times \sqrt{主动土压力系数}
     * 地面堆载、重度、厚度、主动土压力系数、内聚力
     * @param fromula
     * @return
     */
    private List<ElementParam> getParams(String fromula){
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

    /**
     * 生成参数对象
     * @param element
     * @return
     */
    private ElementParam createParam(String element){
        ElementParam elementParam = new ElementParam();
        String[] s = element.split("_");
        elementParam.setName(s[0]);
        elementParam.setIndex(s[1].substring(1,s[1].length() - 1));
        return elementParam;
    }

    /**
     * 把 (载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3}+度重_{4} \times 度厚_{4}+度重_{5} \times 度厚_{5}) \times 数系力压土动主_{5}- 2 \times 力聚内_{5} \times \sqrt{数系力压土动主_{5}}
     * 中文都需要替换成对应的参数，当前函数是把中文都替换为[1]、[2]、[3]用来匹配参数下标。
     * @param source
     * @return
     */
    private String placeholder(String source){
        StringBuilder subPart = new StringBuilder();
        int curIndex = 0;
        Boolean isChanged = Boolean.FALSE;
        char[] chars = source.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(StringUtil.isChineseChar(chars[i])){
                isChanged = Boolean.TRUE;
                continue;
            }
            if (isChanged) {
                subPart.append("[").append(curIndex).append("]");
                isChanged = Boolean.FALSE;
                curIndex++;
                //往前跳过n个字符去掉 _{5}格式内容
                int skip = 0;
                do {
                    skip++;
                }while (chars[skip+i]!='}');
                i=i+skip;
            }else{
                subPart.append(chars[i]);
            }
        }
        return subPart.toString();
    }

    /**
     * 公式参数替换，并且计算。
     * $([0]+[1] \times [2]+[3] \times [4]+[5] \times [6]+[7] \times [8]+[9] \times [10]) \times [11]- 2 \times [12] \times \sqrt{[13]}$
     * 例如，这种需要填充多个值的，values 按顺序存放值，然后一个一个代入。
     * @param source 展开但未填充值的公式表达式
     * @param values 填充值
     * @return
     */
    private String valuesFillCal(String source, String[] values){
        StringBuilder subPart = new StringBuilder();
        int curIndex = 0;
        Boolean isChanged = Boolean.FALSE;
        for(char ch:source.toCharArray()){
            if(ch == '['){
                isChanged = Boolean.TRUE;
                continue;
            }
            if(ch == ']'){
                isChanged = Boolean.FALSE;
                subPart.append(values[curIndex]);
                curIndex++;
                continue;
            }
            if (isChanged) {
                continue;
            }else{
                subPart.append(ch);
            }
        }
        return subPart.toString();
    }
}

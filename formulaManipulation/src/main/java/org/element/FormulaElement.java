package org.element;

import org.show.ILayout;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Stack;

public class FormulaElement extends BaseElement<String>{
    //前缀
    private ILayout iLayout;

    public FormulaElement(Integer index, ILayout iLayout, String tagName, String value) {
        super(index,tagName, value);
        this.iLayout = iLayout;
    }

    public String getValue(){
        String toPrefix = numToPrefix();
        if(StringUtils.isEmpty(toPrefix)){
            return "$"+super.getValue()+"$";
        }else{
            return "$"+toPrefix+"="+super.getValue()+"$";
        }
    }

    private String numToPrefix(){
        String prefix = iLayout.getLayoutMap().get(super.getTagName());
        if(!StringUtils.isEmpty(prefix) && super.getIndex()>0){
            Stack<Character> stack = new Stack<Character>();
            StringBuilder subEquation = new StringBuilder();
            //对{h}_{cn} 进行n值替换
            ArrayList<String> subAnalysis = new ArrayList<String>(2);
            boolean isPush = Boolean.FALSE;
            for(char ch:prefix.toCharArray()){
                if(ch == '{'){
                    isPush = Boolean.TRUE;
                    stack.push(ch);
                    continue;
                }
                if (ch == '}') {
                    stack.push(ch);
                    //持续出栈
                    StringBuilder subPart = new StringBuilder();
                    do{
                        subPart.append(stack.pop());
                    }while (!stack.empty() && stackConditions(ch,stack.peek()));
                    //最后一个元素也出栈
                    subPart.append(stack.pop());
                    String analysis = subPart.reverse().toString();
                    //展开计算公式
                    String expansionPart = nToIndex(analysis,String.valueOf(super.getIndex()));
                    subAnalysis.add(expansionPart);
                    boolean isPushContinue =  stack.search('{')>0;
                    if(isPushContinue){
                        isPush = Boolean.TRUE;
                    }else{
                        isPush = Boolean.FALSE;
                    }
                    continue;
                }
                if(isPush){
                    stack.push(ch);
                }else{
                    if(!CollectionUtils.isEmpty(subAnalysis)){
                        for (String item : subAnalysis) {
                            subEquation.append(item);
                        }
                        subAnalysis.clear();
                    }
                    subEquation.append(ch);
                }
            }
            if(!CollectionUtils.isEmpty(subAnalysis)){
                for (String item : subAnalysis) {
                    subEquation.append(item);
                }
                subAnalysis.clear();
            }
            return nToIndex(subEquation.toString(),String.valueOf(super.getIndex()));
        }
        return prefix;
    }

    /**
     * 展开公式时，判断出栈条件
     * @param stackBottom 终止出栈字符
     * @param curItem 当前字符
     * @return
     */
    private Boolean stackConditions(char stackBottom,char curItem){
        return stackBottom == '}' ? curItem != '{' : false;
    }

    /**
     * 把公式中"{}"中是否存在n,若存在则把n替换为具体的数值。
     * @param substring 源字符串
     * @param index 具体数字
     * @return
     */
    private String nToIndex(String substring,String index){
        Stack<String> stack = new Stack<String>();
        for(char ch:substring.toCharArray()){
            stack.push(String.valueOf(ch));
            if (ch == '}') {
                //持续出栈
                StringBuilder subPart = new StringBuilder();
                do{
                    String pop = stack.pop();
                    subPart.append(pop.equals("n")?index:pop);
                }while (!stack.empty() && !stack.peek().equals("{"));
                subPart.append(stack.pop());
                stack.push(subPart.toString());
            }
        }
        StringBuilder subPart = new StringBuilder();
        do{
            subPart.append(stack.pop());
        }while (!stack.empty());
        return subPart.reverse().toString();
    }
}

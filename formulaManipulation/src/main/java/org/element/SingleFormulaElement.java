package org.element;

import org.show.ILayout;
import org.springframework.util.StringUtils;

import java.util.Stack;

public class SingleFormulaElement extends BaseElement<String>{
    //前缀
    private ILayout iLayout;

    public SingleFormulaElement(Integer index, ILayout iLayout, String tagName, String value) {
        super(index,tagName, value);
        this.iLayout = iLayout;
    }

    public String getValue(){
        String toPrefix = numToPrefix();
        return "$"+toPrefix+"$";
    }

    private String numToPrefix(){
        //解析这种格式的数据："\\sum{{E}_{pc}}
        String prefix = iLayout.getLayoutMap().get(super.getTagName());
        if(!StringUtils.isEmpty(prefix) && super.getIndex()>0){
            Stack<String> stack = new Stack<String>();
            StringBuilder subEquation = new StringBuilder();
            boolean isPush = Boolean.FALSE;
            for(char ch:prefix.toCharArray()){
                if(ch == '{'){
                    isPush = Boolean.TRUE;
                    stack.push(String.valueOf(ch));
                    continue;
                }
                if (ch == '}') {
                    stack.push(String.valueOf(ch));
                    //持续出栈
                    StringBuilder subPart = new StringBuilder();
                    do{
                        subPart.append(stack.pop());
                    }while (!stack.isEmpty() && stackConditions(ch,stack.peek()));
                    //最后一个元素也出栈
                    subPart.append(stack.pop());
                    String analysis = subPart.reverse().toString();
                    //展开计算公式
                    String expansionPart = nToIndex(analysis,String.valueOf(super.getIndex()));
                    stack.push((new StringBuilder(expansionPart)).reverse().toString());
                    boolean isPushContinue =  stack.contains("{");
                    if(isPushContinue){
                        isPush = Boolean.TRUE;
                    }else{
                        isPush = Boolean.FALSE;
                        if(!stack.isEmpty()){
                            subEquation.append((new StringBuilder(stack.pop())).reverse().toString());
                        }
                    }
                    continue;
                }
                if(isPush){
                    stack.push(String.valueOf(ch));
                }else{
                    subEquation.append(ch);
                }
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
    private Boolean stackConditions(char stackBottom,String curItem){
        return stackBottom == '}' ? !curItem.equals("{") : false;
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

package org.element;

public class FormulaElement extends BaseElement<String>{
    public FormulaElement(Integer index,String tagName, String value) {
        super(index,tagName, value);
    }

    /**
     * 对字符做处理。比如：
     * {{主动土层n}}，n需要替换为具体的数值
     * @param floor 当前第几层
     * @return
     */
    public String getTagName(int floor){
        return this.getTagName().replace("n",String.valueOf(floor));
    }

    public String getValue(){
        return "$"+this.getValue()+"$";
    }
}
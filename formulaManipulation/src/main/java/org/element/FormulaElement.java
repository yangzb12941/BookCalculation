package org.element;

public class FormulaElement extends BaseElement<String>{
    public FormulaElement(Integer index,String tagName, String value) {
        super(index,tagName, value);
    }

    public String getValue(){
        return "$"+super.getValue()+"$";
    }
}

package org.element;

import lombok.Data;

@Data
public class BaseElement<T> {
    private Integer index;
    private String tagName;
    private T value;

    public BaseElement(Integer index,String tagName,T value){
        this.index = index;
        this.tagName = tagName;
        this.value = value;
    }
}

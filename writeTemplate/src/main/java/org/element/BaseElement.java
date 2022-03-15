package org.element;

import lombok.Data;

@Data
public class BaseElement<T> {
    private String tagName;
    private T value;

    public BaseElement(String tagName,T value){
        this.tagName = tagName;
        this.value = value;
    }
}

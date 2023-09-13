package org.entity;

import lombok.Data;

@Data
public class NoExpansionParam {

    private int curFloor;

    public NoExpansionParam(int curFloor){
        this.curFloor = curFloor;
    }
}

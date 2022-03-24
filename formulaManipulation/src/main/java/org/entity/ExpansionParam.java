package org.entity;

import lombok.Data;

@Data
public class ExpansionParam {

    private int expansionTimes;
    private int beginFloor;

    public ExpansionParam(int expansionTimes,int beginFloor){
        this.expansionTimes = expansionTimes;
        this.beginFloor = beginFloor;
    }
}

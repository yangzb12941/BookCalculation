package org.entity;

import lombok.Data;

@Data
public class ExpansionParam {

    private int times;
    private int beginFloor;
    private int endFloor;

    public ExpansionParam(int times,int beginFloor,int endFloor){
        this.times = times;
        this.beginFloor = beginFloor;
        this.endFloor = endFloor;
    }
}

package org.calParam;

import lombok.Data;

@Data
public class BasicParams {
    //地面堆载
    private Double surcharge = 0.0;

    //水常量
    private double waterConstant = 0.0;

    //总土层数
    private int allLands = 0;
}

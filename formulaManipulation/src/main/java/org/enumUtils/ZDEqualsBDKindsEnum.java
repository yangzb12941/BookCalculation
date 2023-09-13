package org.enumUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ZDEqualsBDKindsEnum {
    /**
     * 审核结论 1-土压力零点在某一土层之间， 2-土压力零点在某一土层分界点，3-直接取基坑底面往下0.2倍深度
     */
    土压力零点第一种情况 ("1","土压力零点在某一土层之间"),
    土压力零点第二种情况("2","土压力零点在某一土层分界点"),
    土压力零点第三种情况("3","直接取基坑底面往下0.2倍深度"),
    ;
    private String type;
    private String typeName;
}

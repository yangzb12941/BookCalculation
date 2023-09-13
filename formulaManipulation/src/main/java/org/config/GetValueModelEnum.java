package org.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum GetValueModelEnum {
    /**获取值的模式 1:计算模式 2:Latex模式**/
    Cal模式(1,"计算模式"),
    Latex模式(2,"latex展示模式"),
    ;
    private Integer type;
    private String typeName;
}

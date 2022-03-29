package org.handler;

import org.enums.ReviseEnum;

/**
 * 这个方法还不够完善，只能优化 (地面堆载_{1} \times) 或 (地面堆载_{1} -)。
 * 对于(地面堆载_{1} \times 重度 \times)没办法处理。
 */
public class ReviseHandler implements IHandler<ReviseEnum>{
    private ReviseEnum reviseEnum;

    @Override
    public String execute(String fromula) {
        String result = fromula;
        if(this.reviseEnum == ReviseEnum.公式修正){
            result = reviseFromula(fromula);
        }
        return result;
    }

    @Override
    public IHandler setParams(ReviseEnum reviseEnum) {
        this.reviseEnum = reviseEnum;
        return this;
    }

    /**
     * 修正不正确的计算公式和展示公式
     * (地面堆载_{1}+)*主动土压力系数_{1}-2*粘聚力_{1}*根号主动土压力系数_{1}
     * 需要修正为
     * 地面堆载_{1}*主动土压力系数_{1}-2*粘聚力_{1}*根号主动土压力系数_{1}
     * @param fromula
     * @return
     */
    private String reviseFromula(String fromula){
        return fromula.replace("(地面堆载_{1}+)","地面堆载_{1}");
    }
}

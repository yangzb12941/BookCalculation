package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.enums.ReviseEnum;
import org.handle.ExpansionHandler;
import org.handle.ReviseHandler;
import org.junit.Test;

@Slf4j
public class ReviseHandlerTest {
    @Test
    public void reviseHandlerTest() {
        String abc = "(地面堆载_{1}+厚度_{1} \\times 重度_{1}+厚度_{2} \\times 重度_{2}+厚度_{3} \\times 重度_{3})*主动土压力系数_{1}-2*内聚力_{1}*根号主动土压力系数_{1}";
        ReviseHandler reviseHandler = (ReviseHandler) new ReviseHandler().setParams(ReviseEnum.公式修正);
        String tempFromula = reviseHandler.execute(abc);
        log.info("展开:{}",tempFromula);
    }
}

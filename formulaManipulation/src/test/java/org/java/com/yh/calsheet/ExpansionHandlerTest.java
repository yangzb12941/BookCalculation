package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.handle.ExpansionHandler;
import org.junit.Test;

@Slf4j
public class ExpansionHandlerTest {

    @Test
    public void expansionHandlerTest() {
        String abc = "(地面堆载_{n}+[重度_{n}*厚度_{n}+...])*主动土压力系数_{n} - 2*内聚力_{n}*根号主动土压力系数_{n}";
        ExpansionHandler expansionHandler = new ExpansionHandler().setParams(new ExpansionParam(2,1,3));
        String tempFromula = expansionHandler.execute(abc);
        log.info("展开:{}",tempFromula);
    }
}

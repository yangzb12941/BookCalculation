package org.fromulaEntity;

import lombok.extern.slf4j.Slf4j;
import org.handler.IHandler;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FromulaEntity {
    //公式字符串
    private String fromula;
    //字符串处理器
    private List<IHandler> iHandlers;

    public FromulaEntity(String fromula){
        this.fromula = fromula;
    }

    public FromulaEntity addHandler(IHandler iHandler){
        if(CollectionUtils.isEmpty(iHandlers)){
            iHandlers = new ArrayList<>(8);
            iHandlers.add(iHandler);
        }else{
            iHandlers.add(iHandler);
        }
        return this;
    }

    public String compile(){
        String tempFromula = this.fromula;
        for (IHandler iHandle : iHandlers) {
            log.info("befor {}:{}",iHandle.getClass().getSimpleName(),tempFromula);
            tempFromula = iHandle.execute(tempFromula);
            log.info("after {}:{}",iHandle.getClass().getSimpleName(),tempFromula);
        }
        return tempFromula;
    }

    /**
     * 根据指定的Class 返回注册的处理器
     * @param clazz
     * @return
     */
    public IHandler getHandler(Class clazz) {
        for(IHandler item:iHandlers){
            if(item.getClass().isAssignableFrom(clazz)){
                return item;
            }
        }
        return null;
    }

    /**
     * 根据指定的Class 移除处理器，并且返回移除的处理器
     * @param clazz
     * @return
     */
    public IHandler removeHandler(Class clazz) {
        IHandler result = null;
        for(IHandler item:iHandlers){
            if(item.getClass().isAssignableFrom(clazz)){
                result = item;
                break;
            }
        }
        if(Objects.nonNull(result)){
            iHandlers.remove(result);
        }
        return result;
    }
}

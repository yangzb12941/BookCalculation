package org.fromulaEntity;

import org.handle.IHandler;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
            iHandlers = new ArrayList<>(4);
            iHandlers.add(iHandler);
        }else{
            iHandlers.add(iHandler);
        }
        return this;
    }

    public String compile(){
        String tempFromula = this.fromula;
        for (IHandler iHandle : iHandlers) {
            tempFromula = iHandle.execute(tempFromula);
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
}

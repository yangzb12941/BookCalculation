package org.getValue;

import org.entity.ElementParam;

import java.util.List;

public interface GetValues {
    public String[] getValues();
    public void setElementParams(List<ElementParam> elementParams);
}

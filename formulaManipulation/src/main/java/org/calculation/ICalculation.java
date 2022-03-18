package org.calculation;

import org.context.AbstractContext;
import org.handle.FromulaHandle;

public interface ICalculation {
    public AbstractContext getContext(ICalculation iCalculation,
                               FromulaHandle fromulaHandle);
}

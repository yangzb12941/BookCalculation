package org.calculation;

import org.context.IContext;
import org.handle.FromulaHandle;

public interface ICalculation {
    public IContext getContext(ICalculation iCalculation,
                               FromulaHandle fromulaHandle);
}

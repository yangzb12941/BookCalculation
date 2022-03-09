package org.calculation;

import org.context.IContext;
import org.handle.FromulaHandle;
import org.handle.ICreateContextHandle;

public interface ICalculation {
    public IContext getContext(ICalculation iCalculation,
                               FromulaHandle fromulaHandle,
                               ICreateContextHandle iCreateContextHandle);
}

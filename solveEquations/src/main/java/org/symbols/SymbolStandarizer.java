package org.symbols;

import org.utils.MutableBoolean;

import java.util.List;

public abstract class SymbolStandarizer{
    protected Symbol sym;

    ///This method should write to tookAction whether an action was taken, and return standarized symbol
    abstract public Symbol internalStandarize(MutableBoolean tookAction);

    public SymbolStandarizer() {

    }

    ///Standarize each parameter
    private void standarizeParameters() {
        List<Symbol> params = sym.getParams();
        if (params == null) return;
        for (int i = 0; i < params.size(); i++)
            params.set(i, params.get(i).standarizedForm());
    }

    ///Generally only standarizedForm(Symbol s) and StandarizerSet class use this function.
    public void updateContex(Symbol s) {
        sym = s;
    }

    final public Symbol standarizedForm(Symbol s) {
        updateContex(s);
        MutableBoolean isChanged = new MutableBoolean(false);
		/*do {
			standarizeParameters();
			sym = internalStandarize(isChanged);
		} while(isChanged.value());
		*/
        standarizeParameters();
        sym = internalStandarize(isChanged);
        if (isChanged.value()) sym = sym.standarizedForm();
        return sym;
    }
}

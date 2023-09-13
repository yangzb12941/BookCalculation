package org.symbols;

import org.utils.MutableBoolean;

//A standarizer which does nothing
public class NoStandarize extends SymbolStandarizer {
    public NoStandarize() {
        super();
    }
    @Override
    public Symbol internalStandarize(MutableBoolean tookAction) {
        tookAction.set(false);
        return sym;
    }
}

package org.symbols;

import org.utils.MutableBoolean;

public class AssociativeStandarizer extends SymbolStandarizer {
    public AssociativeStandarizer() {
        super();
    }

    protected boolean openAssociativeFunctions() {
        AssociativeOpener opener = new AssociativeOpener(sym);
        return opener.open();
    }
    @Override
    public Symbol internalStandarize(MutableBoolean tookAction) {
        if (sym.getParams() == null) return sym;
        tookAction.set(openAssociativeFunctions());
        return sym;
    }
}

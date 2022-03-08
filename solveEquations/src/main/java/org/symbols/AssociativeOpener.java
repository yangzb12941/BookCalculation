package org.symbols;

import java.util.List;

///"Opens up" associative functions: add(4, add(10, x)) -> add(4,10,x)
public class AssociativeOpener {
    private Symbol func;
    public AssociativeOpener(Symbol toOpen) {
        func = toOpen;
    }

    ///Removes parameter of func at index i, then adds to func parameters list the parameters of the removed parameter.
    protected void openParam(List<Symbol> params, int index) {
        Symbol opened = params.get(index);
        List<Symbol> addedSymbols = opened.getParams();
        params.remove(index);
        for (int i = 0; i < addedSymbols.size(); i++) {
            params.add(addedSymbols.get(i));
        }
    }

    ///Returns whether an action was taken
    public boolean open() {
        boolean action = false;
        SymType type = func.getType();
        List<Symbol> params = func.getParams();
        for (int i = 0; i < params.size(); i++) {
            if (type.equals(params.get(i).getType())) {
                action = true;
                openParam(params, i);
                i--; //i--: So if somehow no symbol is added to parameters and 1 symbol is removed,
                //we wont miss a parameter in the 'for' loop.
            }
        }
        return action;
    }
}

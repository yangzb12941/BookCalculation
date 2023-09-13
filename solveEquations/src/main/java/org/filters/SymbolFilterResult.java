package org.filters;

import org.symbols.Symbol;

import java.util.List;

///Holds an info object of type T, a symbol that the info is referring to and the symbol index in the queried list
public class SymbolFilterResult<T> {
    public static <T> void removeSymbols(List<Symbol> list, List<SymbolFilterResult<T>> toRemove) {
        for (SymbolFilterResult<T> info : toRemove) {
            list.remove(info.sym);
        }
    }

    public Symbol sym;
    public T info;
    public int index;
    public SymbolFilterResult(Symbol sP, T infoP, int indexP) {
        sym = sP;
        info = infoP;
        index = indexP;
    }
}

package org.utils;


import org.filters.SymbolFilter;
import org.filters.SymbolFilterResult;
import org.filters.SymbolListFilter;
import org.symbols.Symbol;

import java.util.List;

public class SymbolFilterUtils {
	public static <T> List<SymbolFilterResult<T>> getFilterResult(List<Symbol> toFilter, SymbolFilter<T> filter) {
		SymbolListFilter<T> numberFilter = new SymbolListFilter<T>(toFilter, filter);
		return numberFilter.filter();
	}
}

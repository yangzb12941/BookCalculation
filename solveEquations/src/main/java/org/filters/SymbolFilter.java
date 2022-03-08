package org.filters;


import org.symbols.Symbol;

public interface SymbolFilter<T> {
	public T filter(Symbol target);
	public boolean isMatching(T info);
}

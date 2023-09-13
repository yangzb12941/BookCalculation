package org.filters;


import org.numberIdentifiers.NumberInfo;
import org.symbols.Symbol;

public class TranslatedNumberFilter implements SymbolFilter<NumberInfo> {
	@Override
	public NumberInfo filter(Symbol target) {
		return target.getIdentifier().isTranslatedNumber();
	}
	@Override
	public boolean isMatching(NumberInfo info) {
		return info.getIsNum();
	}
}

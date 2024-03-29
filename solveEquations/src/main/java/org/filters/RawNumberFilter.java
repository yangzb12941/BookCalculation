package org.filters;

import org.numberIdentifiers.NumberInfo;
import org.symbols.Symbol;

public class RawNumberFilter implements SymbolFilter<NumberInfo> {
	@Override
	public NumberInfo filter(Symbol target) {
		return target.getIdentifier().isRawNumber();
	}
	@Override
	public boolean isMatching(NumberInfo info) {
		return info.getIsNum();
	}
}

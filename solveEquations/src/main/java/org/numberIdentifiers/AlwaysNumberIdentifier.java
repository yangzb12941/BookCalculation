package org.numberIdentifiers;

import org.symbols.Symbol;

public class AlwaysNumberIdentifier extends NumberIdentifier {
	private Symbol sym;
	public AlwaysNumberIdentifier(Symbol sym) {
		this.sym = sym;
	}

	@Override
	public NumberInfo isRawNumber() {
		return new NumberInfo(sym.id(), true);
	}
}

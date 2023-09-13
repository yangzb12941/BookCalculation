package org.polynomIdentifiers;

import org.symbols.Symbol;

public class NumberPolynomIdentifier extends PolynomIdentifier {
	public NumberPolynomIdentifier(Symbol s) {
		super(s);
	}

	@Override
	public PolynomInfo isPolynom() {
		inf.makeEmptyPolynom();
		inf.setZeroDeg(true);
		return inf;
	}
}

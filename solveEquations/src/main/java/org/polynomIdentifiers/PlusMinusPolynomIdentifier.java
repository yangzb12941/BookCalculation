package org.polynomIdentifiers;


import org.symbols.Symbol;

public class PlusMinusPolynomIdentifier extends PolynomIdentifier {

	public PlusMinusPolynomIdentifier(Symbol sP) {
		super(sP);
	}

	@Override
	public PolynomInfo isPolynom() {
		PolynomInfo parameterInf = this.s.getParams().get(0).getIdentifier().isPolynom();
		return parameterInf.getIsPolynom() ? parameterInf : PolynomInfo.notPolynom();
	}

}

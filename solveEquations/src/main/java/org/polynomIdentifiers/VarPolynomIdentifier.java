package org.polynomIdentifiers;


import org.symbols.Symbol;
import org.symbols.Variable;

public class VarPolynomIdentifier extends PolynomIdentifier {
	public VarPolynomIdentifier(Symbol sP) {
		super (sP);
	}

	private PolynomVarInfo getFirstDegreeVar() {
		 Variable var = Variable.copy(s);
		 PolynomVarInfo varInf = new PolynomVarInfo(var, 1);
		 varInf.setDegreeExists(1, true);
		 return varInf;
	}

	private void putSingleVarInfo() {
		 inf.makeEmptyPolynom();
		 inf.setInfo(getFirstDegreeVar());
	}

	@Override
	public PolynomInfo isPolynom() {
		putSingleVarInfo();
		return inf;
	}

}

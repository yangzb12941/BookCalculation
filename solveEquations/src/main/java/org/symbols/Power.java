package org.symbols;


import org.coefficients.DissolvedSymbol;
import org.directSimplifiers.DirectSimplifierSet;
import org.directSimplifiers.FullSimplifier;
import org.directSimplifiers.PowerNumberSimplifier;
import org.filters.RawNumberFilter;
import org.filters.TranslatedNumberFilter;
import org.implicatedNumberIdentifiers.NotImplicatedNumber;
import org.latexTranslation.LatexExpression;
import org.latexTranslation.LatexPower;
import org.latexTranslation.VariableIDTable;
import org.numberIdentifiers.NotRawNumber;
import org.openForm.OpenForm;
import org.openForm.PowerOpenForm;
import org.parameters.FixedSizeParameters;
import org.parameters.ParameterOrderedEquator;
import org.parameters.Parameters;
import org.polynomIdentifiers.PowPolynomIdentifier;
import org.symbolComponents.NoId;
import org.symbolComponents.SymbolID;
import org.symbolComponents.SymbolPatternIdentifier;
import org.symbolStandarizers.PowerNumberStandarizer;
import org.symbolVisitors.SymbolVisitor;
import org.translatedNumberIdentifiers.ParametersPowerIdentifier;

import java.util.Set;

public class Power extends Symbol {
	@Override
	public void accept(SymbolVisitor visitor) {
		visitor.visit(this);
	}

	public Power() {
		super();
	}

	public Power(Symbol base, Symbol exp) {
		this();
		setBase(base);
		setExponent(exp);
	}

	public void setBase(Symbol base) {
		parameters.getParameters().set(0, base);
	}

	public void setExponent(Symbol expo) {
		parameters.getParameters().set(1, expo);
	}

	public Symbol getBase() {
		return parameters.getParameters().get(0);
	}

	public Symbol getExponent() {
		return parameters.getParameters().get(1);
	}

	@Override
	protected Symbol copySymbolType() {
		return new Power();
	}

	@Override
	public LatexExpression latex(VariableIDTable idList) {
		return new LatexPower(this.getBase().latex(idList), this.getExponent().latex(idList));
	}

	/*@Override
	public String print() {
		StringBuilder sb = new StringBuilder();
		List<Symbol> params = parameters.getParameters();
		if (params.size() < 1) return "";
		else putParanthessesSymbol(sb, params.get(0));
		for (int i = 1; i < params.size(); i++) {
			sb.append("^");
			putParanthessesSymbol(sb, params.get(i));
		}
		return sb.toString();
	}*/

	@Override
	protected SymbolID createIdType() {
		return new NoId();
	}

	@Override
	protected Parameters createParameters() {
		return new FixedSizeParameters(new ParameterOrderedEquator(), 2);
	}

	@Override
	protected SymbolStandarizer createStd() {
		return new PowerNumberStandarizer(new RawNumberFilter());
	}

	@Override
	protected FullSimplifier createSimp() {
		///TODO Replace this
		DirectSimplifierSet simps = new DirectSimplifierSet(new PowerNumberSimplifier(this, new TranslatedNumberFilter()));
		return new FullSimplifier(this, simps);
	}

	@Override
	protected OpenForm createOpenForm() {
		return new PowerOpenForm();
	}

	@Override
	protected SymbolPatternIdentifier createId() {
		return new SymbolPatternIdentifier(
				new NotRawNumber(),
				new NotImplicatedNumber(),
				new ParametersPowerIdentifier(this),
				new PowPolynomIdentifier(this)
				);
	}

	@Override
	public SymType getType() {
		return SymType.Power;
	}

	@Override
	public DissolvedSymbol toDissolvedSymbol() {
		DissolvedSymbol dissolvedBase = this.getBase().toDissolvedSymbol();
		Symbol exponent = this.getExponent();
		Set<Symbol> bases = dissolvedBase.dissolvedSymbols();

		for (Symbol next : bases) {
			dissolvedBase.multiplyDissolvedSymbolExpo(next, exponent);
		}
		return dissolvedBase;
	}

}

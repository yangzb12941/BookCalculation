package org.symbols;


import org.directSimplifiers.DirectSimplifierSet;
import org.directSimplifiers.FullSimplifier;
import org.directSimplifiers.RootNumberSimplifier;
import org.implicatedNumberIdentifiers.RootImplicatedNumber;
import org.latexTranslation.LatexExpression;
import org.latexTranslation.LatexRoot;
import org.latexTranslation.VariableIDTable;
import org.numberIdentifiers.NotRawNumber;
import org.parameters.FixedSizeParameters;
import org.parameters.ParameterOrderedEquator;
import org.parameters.Parameters;
import org.polynomIdentifiers.RootPolynomIdentifier;
import org.symbolComponents.NoId;
import org.symbolComponents.SymbolID;
import org.symbolComponents.SymbolPatternIdentifier;
import org.symbolStandarizers.RootNumberStandarizer;
import org.symbolVisitors.RootBaseSimplifier;
import org.symbolVisitors.SymbolVisitor;
import org.translatedNumberIdentifiers.RootTranslatedNumber;

public class Root extends Symbol {
	@Override
	public void accept(SymbolVisitor visitor) {
		visitor.visit(this);
	}

	public Root(Symbol base, Symbol rootBy) {
		super(base, rootBy);
	}

	public Root() {
		this(null, null);
	}

	public Symbol base() {
		return this.getParams().get(0);
	}

	public Symbol rootBy() {
		return this.getParams().get(1);
	}

	public void setBase(Symbol newBase) {
		this.getParams().set(0, newBase);
	}

	public void setRootBy(Symbol newRootBy) {
		this.getParams().set(1, newRootBy);
	}

	@Override
	protected Symbol copySymbolType() {
		return new Root();
	}

	@Override
	public LatexExpression latex(VariableIDTable idList) {
		return new LatexRoot(this.base().latex(idList), this.rootBy().latex(idList));
	}

	@Override
	protected SymbolID createIdType() {
		return new NoId();
	}

	@Override
	protected SymbolStandarizer createStd() {
		return new RootNumberStandarizer();
	}

	@Override
	protected FullSimplifier createSimp() {
		return new FullSimplifier(this, new DirectSimplifierSet(new RootNumberSimplifier(this),
																new RootBaseSimplifier(this)));
	}

	@Override
	protected SymbolPatternIdentifier createId() {
		return new SymbolPatternIdentifier(new NotRawNumber(),
										   new RootImplicatedNumber(this),
										   new RootTranslatedNumber(this),
										   new RootPolynomIdentifier(this));
	}

	@Override
	protected Parameters createParameters() {
		return new FixedSizeParameters(new ParameterOrderedEquator(), 2);
	}

	@Override
	public SymType getType() {
		return SymType.Root;
	}

}

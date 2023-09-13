package org.symbols;


import org.directSimplifiers.DirectSimplifierSet;
import org.directSimplifiers.FullSimplifier;
import org.directSimplifiers.NoSimplify;
import org.implicatedNumberIdentifiers.AlwaysImplicatedNumber;
import org.latexTranslation.LatexExpression;
import org.latexTranslation.LatexNumber;
import org.latexTranslation.VariableIDTable;
import org.numberIdentifiers.AlwaysNumberIdentifier;
import org.parameters.NoParameters;
import org.parameters.Parameters;
import org.polynomIdentifiers.NumberPolynomIdentifier;
import org.symbolComponents.CalcNumber;
import org.symbolComponents.ConstantCalcNumberID;
import org.symbolComponents.SymbolID;
import org.symbolComponents.SymbolPatternIdentifier;
import org.symbolVisitors.SymbolVisitor;
import org.translatedNumberIdentifiers.NoTranslatedNumberIdentifier;

public class NumberSym extends Symbol {

	@Override
	public void accept(SymbolVisitor visitor) {
		visitor.visit(this);
	}

	public static NumberSym doubleSymbol(double num) {
		return new NumberSym(new CalcNumber(num));
	}

	public static NumberSym intSymbol(int num) {
		return new NumberSym(new CalcNumber(num));
	}

	public final static NumberSym zero = new NumberSym(new CalcNumber(0));

	public NumberSym(CalcNumber valP) {
		super();
		setId(valP);
	}

	public NumberSym() {
		super();
	}

	@Override
	public LatexExpression latex(VariableIDTable idList) {
		return new LatexNumber(this.id());
	}

	@Override
	protected Symbol copySymbolType() {
		return new NumberSym();
	}

	//@Override
	//protected ParameterEquator createParameterEquator() {
	//	return new NoParameters();
	//}

	@Override
	protected SymbolID createIdType() {
		return new ConstantCalcNumberID(); ///IMPORTANT: ID needs to be constant so a NumberSym will be
										   ///an immutable class which instance may be used in different
										   ///symbols.
	}

	@Override
	protected Parameters createParameters() {
		return new NoParameters();
	}

	@Override
	protected SymbolStandarizer createStd() {
		return new NoStandarize();
	}

	@Override
	protected FullSimplifier createSimp() {
		return new FullSimplifier(this, new DirectSimplifierSet(new NoSimplify()));
	}

	@Override
	protected SymbolPatternIdentifier createId() {
		return new SymbolPatternIdentifier(
				new AlwaysNumberIdentifier(this),
				new AlwaysImplicatedNumber(),
				new NoTranslatedNumberIdentifier(this),
				new NumberPolynomIdentifier(this)
				);
	}

	/*@Override
	public int hashCode() {
		///DEBUG System.out.println("id: " + id());
		return Objects.hash(this.id());
	}*/

	@Override
	public SymType getType() {
		return SymType.Number;
	}

	/*@Override
	public String print() {
		return id().toString();
	}*/

}

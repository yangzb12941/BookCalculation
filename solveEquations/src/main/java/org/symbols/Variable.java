package org.symbols;

import org.directSimplifiers.DirectSimplifierSet;
import org.directSimplifiers.FullSimplifier;
import org.directSimplifiers.NoSimplify;
import org.implicatedNumberIdentifiers.NotImplicatedNumber;
import org.latexTranslation.LatexExpression;
import org.latexTranslation.LatexVariable;
import org.latexTranslation.VariableIDTable;
import org.numberIdentifiers.NotRawNumber;
import org.parameters.NoParameters;
import org.parameters.Parameters;
import org.polynomIdentifiers.VarPolynomIdentifier;
import org.symbolComponents.CalcNumber;
import org.symbolComponents.ConstantCalcNumberID;
import org.symbolComponents.SymbolID;
import org.symbolComponents.SymbolPatternIdentifier;
import org.symbolVisitors.SymbolVisitor;
import org.translatedNumberIdentifiers.NoTranslatedNumberIdentifier;

import java.util.Objects;

///*DONE*
///VERY VERY IMPORTANT!!!!!!!
///TO IMPLEMENT THE equals() METHOD!!!

public class Variable extends Symbol {
	@Override
	public void accept(SymbolVisitor visitor) {
		visitor.visit(this);
	}
	///We make this field immutable so the same instance of Variable can be used in many places
	///without fear that it would be changed.
	///final private CalcNumber idval;
	public static Variable copy(Symbol vSym) {
		return new Variable(vSym.id());
	}

	public Variable(CalcNumber idP) {
		super();
		setId(idP);
	}

	@Override
	protected SymbolID createIdType() {
		return new ConstantCalcNumberID();
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
				new NotRawNumber(),
				new NotImplicatedNumber(),
				new NoTranslatedNumberIdentifier(this),
				new VarPolynomIdentifier(this)
				);
	}

	@Override
	public SymType getType() {
		return SymType.Variable;
	}

	@Override
	public boolean containsVariable(Variable v) {
		return this.equals(v);
	}

	@Override
	public LatexExpression latex(VariableIDTable idTable) {
		return new LatexVariable(idTable.getName(this.id()));
	}

	/*@Override
	public String print() {
		return "x" + this.id().toString(); ///TODO Extremely temporary printing... Change required!
	}*/

	@Override
	protected Symbol copySymbolType() {
		return new Variable(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id().getDouble());
	}
}

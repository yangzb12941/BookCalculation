package org.solutions;


import org.latexTranslation.VariableIDTable;
import org.symbols.Variable;

public class EveryRealNumberSolution extends EquationSolution {

	public EveryRealNumberSolution(Variable targetVariable) {
		super (targetVariable, null);
	}

	@Override
	public String printLatex(VariableIDTable idTable) {
		return "Every real " + idTable.getName(super.solvedFor.id());
	}

}

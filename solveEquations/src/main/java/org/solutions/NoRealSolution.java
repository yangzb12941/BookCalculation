package org.solutions;

import org.latexTranslation.VariableIDTable;

public class NoRealSolution extends EquationSolution {

	public NoRealSolution() {
		super(null,null);
	}

	@Override
	public String printLatex(VariableIDTable idTable) {
		return "No\\,\\,\\,real\\,\\,\\,solution";
	}

}

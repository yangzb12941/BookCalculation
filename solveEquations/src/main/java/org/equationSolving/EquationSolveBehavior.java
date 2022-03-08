package org.equationSolving;


import org.solveables.Equation;
import org.solveables.Solveable;

public abstract class EquationSolveBehavior {
	protected Equation equation;

	public EquationSolveBehavior(Equation equationP) {
		equation = equationP;
	}

	public abstract Solveable nextStep();
}

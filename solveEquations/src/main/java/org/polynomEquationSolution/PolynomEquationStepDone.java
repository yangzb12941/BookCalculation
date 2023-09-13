package org.polynomEquationSolution;

import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;

public class PolynomEquationStepDone extends PolynomEquationStepState {

	@Override
	public boolean isFinishedState() {
		return true;
	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior mani, PolynomEquation pe) {
		return null;
	}

}

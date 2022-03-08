package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.PolynomEquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;

public class PolynomEquationQuadraticFormula extends PolynomEquationStepState {

	public PolynomEquationQuadraticFormula() {

	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe) {
		PolynomEquationManipulateBehavior polyManipulate = super.polyManipulator(manipulator);
		((PolynomEquationManipulateBehavior) polyManipulate).applyQuadraticFunction(pe);
		return new PolynomEquationStepDone();
	}

}

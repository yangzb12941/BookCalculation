package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.CannotSolveException;

public class PolynomEquationCantSolve extends PolynomEquationStepState {

	public PolynomEquationCantSolve() {

	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe) {
		throw new CannotSolveException();
	}

}

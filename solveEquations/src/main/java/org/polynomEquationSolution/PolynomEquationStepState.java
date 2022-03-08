package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.PolynomEquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;

///Node in a polynom equation solution decision tree
public abstract class PolynomEquationStepState {

	public boolean isFinishedState() {
		return false;
	}

	protected final PolynomEquationManipulateBehavior polyManipulator(SolveableManipulateBehavior manipulator) {
		return manipulator.getEquationManipulator().getPolyManipulator();
	}

	public abstract PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe);
}

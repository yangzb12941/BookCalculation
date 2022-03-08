package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.PolynomEquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.utils.Side;

public class PolynomEquationIsolateDegree extends PolynomEquationStepState {
	private PolynomEquationStepState ifWasIsolated;
	private Side isolateToSide;

	public PolynomEquationIsolateDegree(Side isolateTo, PolynomEquationStepState transitionIfWasIsolated) {
		ifWasIsolated = transitionIfWasIsolated;
		isolateToSide = isolateTo;
	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe) {
		PolynomEquationManipulateBehavior polyManipulate = super.polyManipulator(manipulator);
		return polyManipulate.tryIsolateSingleDegree(pe, isolateToSide) ? new PolynomEquationStepDone() : ifWasIsolated;
	}

}

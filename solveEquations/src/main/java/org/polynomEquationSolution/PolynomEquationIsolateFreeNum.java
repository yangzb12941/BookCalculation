package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.PolynomEquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.utils.Side;

public class PolynomEquationIsolateFreeNum extends PolynomEquationStepState {
	private PolynomEquationStepState ifWasIsolated;
	private Side toSide;

	public PolynomEquationIsolateFreeNum(Side isolateToSide, PolynomEquationStepState transitionIfWasIsolated) {
		ifWasIsolated = transitionIfWasIsolated;
		toSide = isolateToSide;
	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe) {
		PolynomEquationManipulateBehavior polyManipulate = super.polyManipulator(manipulator);
		return polyManipulate.tryIsolateFreeNumber(pe, toSide) ? new PolynomEquationStepDone() : ifWasIsolated;
	}

}

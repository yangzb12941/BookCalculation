package org.polynomEquationSolution;


import org.equationSolving.PolynomEquation;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;

public class PolynomEquationIsFirstDegree extends PolynomEquationStepState {
	private PolynomEquationStepState ifFirstDegree, ifNotFirstDegree;

	public PolynomEquationIsFirstDegree(PolynomEquationStepState ifFirstDegreeP, PolynomEquationStepState ifNotFirstDegreeP) {
		ifFirstDegree = ifFirstDegreeP;
		ifNotFirstDegree = ifNotFirstDegreeP;
	}

	@Override
	public PolynomEquationStepState advance(SolveableManipulateBehavior manipulator, PolynomEquation pe) {
		return manipulator.getEquationManipulator().getPolyManipulator().isFirstDegreeEquation(pe) ?
				ifFirstDegree : ifNotFirstDegree;
	}

}

package org.coefficients;


import org.equationSolving.EquationSolveBehavior;
import org.solveables.CannotSolveException;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.utils.Side;

public class DefaultEquationSolveBehavior extends EquationSolveBehavior {

	public DefaultEquationSolveBehavior(Equation equationP) {
		super(equationP);
	}

	@Override
	public Solveable nextStep() {
		boolean leftSimp = super.equation.simplify(Side.Left);
		boolean rightSimp = super.equation.simplify(Side.Right);

		if (leftSimp || rightSimp) return super.equation;
		else throw new CannotSolveException();
	}

}

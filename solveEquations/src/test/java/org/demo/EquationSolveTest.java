package org.demo;


import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.solveableManipulationBehavior.EquationManipulateBehavior;
import org.solveableManipulationBehavior.EquationSetManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.symbols.Expression;
import org.symbols.NumberSym;
import org.symbols.Symbol;
import org.symbols.Variable;
import org.utils.MutableBoolean;
import org.utils.Side;

public class EquationSolveTest extends TestingEnvironment {

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		long endTime;

		LatexUserString userStr = new LatexUserString("sqrt(5) * 2 * x") ;
		VariableIDDynamicTable manager = new VariableIDDynamicTable();
		Variable x = new Variable(manager.getID("x"));
		Symbol leftSide = userStr.toExpression().toSymbol(manager);
		Symbol rightSide = NumberSym.zero;
		Expression leftSideExp = new Expression(leftSide);
		Expression rightSideExp = new Expression(rightSide);

		leftSideExp.standarize();
		rightSideExp.standarize();

		System.out.println(leftSide.standarizedForm().simplifiedForm(MutableBoolean.NULL).latexString(manager));

		SolveableManipulateBehavior manipulate = new SolveableManipulateBehavior(
																	new EquationManipulateBehavior(new PrintSolving(null, manager)),
																	new EquationSetManipulateBehavior());
		Equation eq = new Equation(leftSideExp, rightSideExp, x, manipulate);
		eq.lightSimplify(Side.Right);
		eq.lightSimplify(Side.Left);
		//eq.isolate(v, Side.Left);

		System.out.println("$$" + eq.printSolveable(manager) + "$$");

		System.out.println("Solving \\(" + eq.printSolveable(manager) + "\\)");
		Solveable solution = eq.fullSolve();
		System.out.println("\n\nSOLUTION: $$" + solution.reachedSolution().printLatex(manager) + "$$");
		//Equation eq = new Equation(leftSide, rightSide, v);

		endTime = System.nanoTime();
		//System.out.println((endTime-startTime) / 1000000000.0);
	}

}

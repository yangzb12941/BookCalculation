package org.demo;


import org.equationSolving.PrintEquationSet;
import org.latexTranslation.LatexUserString;
import org.latexTranslation.VariableIDDynamicTable;
import org.solutions.Solution;
import org.solveableManipulationBehavior.EquationManipulateBehavior;
import org.solveableManipulationBehavior.SolveableManipulateBehavior;
import org.solveables.Equation;
import org.solveables.Solveable;
import org.symbolComponents.CalcNumber;
import org.symbols.Expression;
import org.symbols.Symbol;
import org.symbols.Variable;

public class EquationOrSetTest {
	static VariableIDDynamicTable manager = new VariableIDDynamicTable();
	static Variable v = new Variable(new CalcNumber(1));

	//static Symbol left = new LatexUserString("x^2 + 45(x+3) - 45").toExpression().toSymbol(manager);
	//static Symbol right = new LatexUserString("136x").toExpression().toSymbol(manager);
	static Symbol left = new LatexUserString("(20+18*0.7+18.9*1.9+1.8*18.7+18x)*0.67-2*15.9*0.82").toExpression().toSymbol(manager);
	static Symbol right = new LatexUserString("18x*1.46+2*15.9*1.21").toExpression().toSymbol(manager);

//	static NumberSym numSym(int num) {
//		return NumberSym.intSymbol(num);
//	}

	public static void main(String[] args) {
		long t1 = System.nanoTime();
		//manager.getID("x");
		StringBuilder build = new StringBuilder();
		PrintSolving printSolving = new PrintSolving(build, manager);

		//Symbol coef = right.getTotalPolynomCoefficients(v);
		//Symbol coef = new PolynomDegreeCoefficient(left, v).getDegreeCoefficient(NumberSym.intSymbol(2));
		//System.out.println(coef.latexString(manager));

		Expression leftE, rightE;
		leftE = new Expression(left);
		rightE = new Expression(right);

		Equation eq = new Equation(leftE, rightE, v, new SolveableManipulateBehavior(new EquationManipulateBehavior(printSolving),
																					 new PrintEquationSet(build, manager)));

		System.out.println("Solving \\(" + eq.printSolveable(manager) + "\\): $$ $$");
		Solveable finalS = eq.fullSolve();
		Solution s = finalS.reachedSolution();
		long t2 = System.nanoTime();
		//System.out.println((t2-t1)/1000000000.0);
		System.out.println(build.toString());
		System.out.println("$$" + s.printLatex(manager) + "$$");
	}

}

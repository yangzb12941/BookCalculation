package org.demo;


import org.polynomIdentifiers.PolynomInfo;
import org.polynomIdentifiers.PolynomVarInfo;
import org.symbolComponents.CalcNumber;
import org.symbols.*;

public class SymbolDeepCopyTest {
	static final CalcNumber V_ID = new CalcNumber(1);
	static final CalcNumber B_ID = new CalcNumber(2);

	static Variable v = new Variable(V_ID);
	static Variable b = new Variable(B_ID);

	static Symbol testSym = new Addition(v,numSym(89), numSym(6), v, numSym(2));

	static NumberSym numSym(double v) {
		return new NumberSym(dCalc(v));
	}

	static CalcNumber dCalc(double v) {
		return new CalcNumber(v);
	}

	static void printVarDegs(PolynomVarInfo vInf) {
		for (int i = 1; i <= vInf.getMaxDegree(); i++) {
			if (vInf.getDegreeExists(i)) System.out.print(i + " ");
		}
		System.out.println();
	}

	public static void printInfoDegs(PolynomInfo inf) {
		for (int i = 0; i < inf.varCount(); i++) {
			System.out.println("for variable " + inf.getInfo(i).getVariable().id().getDouble() + " :");
			printVarDegs(inf.getInfo(i));
		}
	}

	public static void printInfo(PolynomInfo inf) {
		System.out.println("count: " + inf.varCount());
		//System.out.println("degree of first variable: " + inf.getInfo(0).getMaxDegree());
		System.out.println("is even a polynom: " + inf.getIsPolynom());
		System.out.println("contains a free number: " + inf.getZeroDeg());
		printInfoDegs(inf);
	}

	public static void printSymbol(Symbol s) {
		return;
	}


	public static void main(String[] args) {
		Symbol test = testSym;
		Expression testExp = new Expression(testSym);
		testExp.simplify();
		printSymbol(test);
	}

}

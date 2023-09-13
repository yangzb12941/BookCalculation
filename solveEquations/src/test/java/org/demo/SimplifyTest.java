package org.demo;


import org.symbols.Addition;
import org.symbols.Expression;
import org.symbols.Multiplication;
import org.symbols.Symbol;

public class SimplifyTest extends TestingEnvironment {

	public static void main(String[] args) {
		Symbol testSym = new Multiplication(new Addition(v, numSym(4)), new Addition(v, numSym(2)));
		Expression testExp = new Expression(testSym.openForm());
		printSymbol(testExp.getStoredSym());
		testExp.simplify();
		printSymbol(testExp.getStoredSym());
	}
}

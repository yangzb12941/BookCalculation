package org.latexTranslation;

///IMMUTABLE OBJECT
///A latex elementHandler is the base unit of latex strings.
///Latex elementHandler list is a code representation of latex strings.
public abstract class LatexElement {
	PutMultipliesBehavior putMultiplies;

	public LatexElement(PutMultipliesBehavior putMuls) {
		putMultiplies = putMuls;
	}

	public boolean isNesting() { ///Returns true if this elementHandler opens a nesting
		return false;
	}
	public boolean isDoneNesting() { ///Returns true if this elementHandler closes a nesting
		return false;
	}
	public boolean isNumber() {
		return false;
	}
	public boolean isVariable() {
		return false;
	}
	public boolean isWord() { ///A WORD is an elementHandler that may be translated to an expression only by itself (e.g. root)
		return false;
	}

	///ALL OF THE FOLLOWING METHODS are called by other object as part of an operation- splitting elements to addition
	///parameters, for example.

	///A number elementHandler should override this to write its numeric value to the context.
	public void getNumber(NumberElementContext context) {
		return;
	}

	public void getVariable(VarElementContext context) {
		return;
	}

	public boolean shouldRemoveMinus(LatexElement nextElement) {
		return false;
	}

	///Returns true if a multiply should be put before this elementHandler.
	public final boolean putMultiplies(PutMultipliesContext context) {
		return putMultiplies.putMultiplies(context);
	}


	///CHECKS: Returns true only if the elementHandler is making the symbol an addition/multiplication/power etc...
	boolean isOperator(Operator op) {
		return false;
	}

	public void arithOperationExtraction(ArithmaticOpContext context) {
		context.addToCurrentParameter(this);
	}

	public void wordExtraction(GetWordContext context) {
		return; ///Only the correct word (specified in the context) will add itself to the context's result.
				///Other elements shall do nothing.
	}

	/*void additionOperationExtraction(ArithmaticOpContext context) {
		context.addToCurrentParameter(this);
	}

	void multiplyOperationExtraction(ArithmaticOpContext context) {
		context.addToCurrentParameter(this);
	}

	void divisionOperationExtraction(ArithmaticOpContext context) {
		context.addToCurrentParameter(this);
	}

	void powerOperationExtraction(ArithmaticOpContext context) {
		context.addToCurrentParameter(this);
	}*/

}

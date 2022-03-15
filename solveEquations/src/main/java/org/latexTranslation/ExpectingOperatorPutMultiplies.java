package org.latexTranslation;

public class ExpectingOperatorPutMultiplies implements PutMultipliesBehavior {
	@Override
	public boolean putMultiplies(PutMultipliesContext context) {
		boolean ret = context.previousElementExpectingOperator; ///If previous elementHandler expecting operator, we should
																///add a multiply between us and the previous elementHandler.

		context.previousElementExpectingOperator = true;
		return ret;
	}

}

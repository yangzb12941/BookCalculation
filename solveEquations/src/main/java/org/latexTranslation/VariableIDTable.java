package org.latexTranslation;

import org.symbolComponents.CalcNumber;

public interface VariableIDTable {
	public CalcNumber getID(String name);
	public String getName(CalcNumber id);
}

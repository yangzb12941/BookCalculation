package org.parameters;
import org.symbols.Symbol;

import java.util.List;

public abstract class Parameters {
	protected ParameterEquator equator;
	public Parameters(ParameterEquator equatorP) {
		equator = equatorP;
	}

	public abstract List<Symbol> getParameters();

	public abstract void setParameters(List<Symbol> newParams);

	public final boolean parameterEquals(List<Symbol> otherParams) {
		return equator.equals(this.getParameters(), otherParams);
	}

	public final int parametersHashCode() {
		return equator.parametersHashCode(this.getParameters());
	}

}

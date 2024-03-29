package org.symbolStandarizers;

import org.symbols.Symbol;
import org.symbols.SymbolStandarizer;
import org.utils.MutableBoolean;

import java.util.List;

public class SingleParameterExtractionStandarizer extends SymbolStandarizer {
	public SingleParameterExtractionStandarizer() {
		super();
	}

	@Override
	public Symbol internalStandarize(MutableBoolean tookAction) {
		// TODO Auto-generated method stub
		tookAction.set(false);
		List<Symbol> params = sym.getParams();
		if (params == null) return sym;
		if (params.size() != 1) return sym;

		else {
			tookAction.set(true);
			Symbol param = params.get(0);
			return param;
		}
	}

}

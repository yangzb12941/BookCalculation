package org.symbolStandarizers;


import org.symbols.Symbol;
import org.symbols.SymbolStandarizer;
import org.utils.ListUtils;
import org.utils.MutableBoolean;

import java.util.List;

public class StandarizeSet extends SymbolStandarizer {
	private List<SymbolStandarizer> standarizers;

	///Standarizers need to manipulate the SAME SYMBOL!
	///IF THIS DOES NOT HOLD TRUE, EXPECT UNEXPECTED BEHAVIOR.
	public StandarizeSet(List<SymbolStandarizer> standarizersP) {
		super();
		standarizers = standarizersP;
	}

	public StandarizeSet(SymbolStandarizer...stds) {
		this(ListUtils.toLinkedList(stds));
	}

	@Override
	public Symbol internalStandarize(MutableBoolean tookAction) {
		tookAction.set(false);
		MutableBoolean nextStand = new MutableBoolean(false);
		for (SymbolStandarizer stand : standarizers) {
			stand.updateContex(sym);
			sym = stand.internalStandarize(nextStand);
			///NOT TESTED
			if (nextStand.value()) {
				tookAction.set(true);
				return sym;
			}
			///END OF NOT TESTED
		}
		return sym;
	}

}

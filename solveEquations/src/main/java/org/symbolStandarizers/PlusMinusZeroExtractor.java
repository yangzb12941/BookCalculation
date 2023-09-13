package org.symbolStandarizers;


import org.numberIdentifiers.NumberInfo;
import org.symbolComponents.CalcNumber;
import org.symbols.PlusMinus;
import org.symbols.Symbol;
import org.symbols.SymbolStandarizer;
import org.utils.MutableBoolean;

public class PlusMinusZeroExtractor extends SymbolStandarizer {
	PlusMinus pm;

	public PlusMinusZeroExtractor(PlusMinus pm) {
		this.pm = pm;
	}

	@Override
	public Symbol internalStandarize(MutableBoolean tookAction) {
		NumberInfo param = pm.parameter().getIdentifier().isRawNumber();
		if (param.getIsNum() && param.getNum().equals(CalcNumber.ZERO)) {
			tookAction.set(true);
			return pm.parameter();
		}
		else {
			tookAction.set(false);
			return pm;
		}

	}

}

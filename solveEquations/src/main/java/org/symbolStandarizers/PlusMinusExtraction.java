package org.symbolStandarizers;


import org.symbolVisitors.PlusMinusExtractionVisitor;
import org.symbols.PlusMinus;
import org.symbols.Symbol;
import org.symbols.SymbolStandarizer;
import org.utils.MutableBoolean;

public class PlusMinusExtraction extends SymbolStandarizer {
	private PlusMinus pm;

	public PlusMinusExtraction(PlusMinus pm) {
		this.pm = pm;
	}

	@Override
	public Symbol internalStandarize(MutableBoolean tookAction) {
		Symbol extractionResult = new PlusMinusExtractionVisitor().shouldExtractPlusMinus(pm.parameter());
		if (extractionResult == null ) {
			tookAction.set(false);
			return pm;
		}
		else {
			tookAction.set(true);
			return extractionResult;
		}
	}

}

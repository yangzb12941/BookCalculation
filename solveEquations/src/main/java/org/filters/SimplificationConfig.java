package org.filters;


import org.numberIdentifiers.NumberInfo;

public final class SimplificationConfig {
	public final static SymbolFilter<NumberInfo> numberDetector = new RawNumberFilter();
}

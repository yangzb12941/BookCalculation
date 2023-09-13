package org.translatedNumberIdentifiers;

import org.numberIdentifiers.NumberInfo;
import org.symbols.Symbol;

///An identifier which MAY detect a symbol which can be converted to a number
public abstract class TranslatedNumberIdentifier {
	protected Symbol sym;
	public TranslatedNumberIdentifier(Symbol sP) {
		sym = sP;
	}
	public abstract NumberInfo isTranslatedNumber();
}


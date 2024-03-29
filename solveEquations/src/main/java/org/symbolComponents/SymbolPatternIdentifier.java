package org.symbolComponents;

import org.implicatedNumberIdentifiers.ImplicatedNumberIdentifier;
import org.numberIdentifiers.NumberIdentifier;
import org.numberIdentifiers.NumberInfo;
import org.polynomIdentifiers.PolynomIdentifier;
import org.polynomIdentifiers.PolynomInfo;
import org.translatedNumberIdentifiers.TranslatedNumberIdentifier;

///Contains methods for each identifier class (isNumber, isPolynom...)
public class SymbolPatternIdentifier {
	NumberIdentifier numId;
	ImplicatedNumberIdentifier impNumId;
	TranslatedNumberIdentifier transNumId;
	PolynomIdentifier polyId;

	public SymbolPatternIdentifier(NumberIdentifier numIdP, ImplicatedNumberIdentifier impNumId, TranslatedNumberIdentifier transNumIdP,
								PolynomIdentifier polyIdP) {
		numId = numIdP;
		this.impNumId = impNumId;
		transNumId = transNumIdP;
		polyId = polyIdP;
	}

	public PolynomInfo isPolynom() {
		return polyId.isPolynom();
	}
	public NumberInfo isRawNumber() {
		return numId.isRawNumber();
	}
	///TODO Maybe refactor so returns NumberInfo instead of boolean. May be useful.
	public boolean isImplicatedNumber() {
		return impNumId.isImplicatedNumber();
	}
	public NumberInfo isTranslatedNumber() {
		return transNumId.isTranslatedNumber();
	}
}

///Base class for identifiers
//abstract class GeneralIdentifier {
//	protected Symbol s;
//
//}


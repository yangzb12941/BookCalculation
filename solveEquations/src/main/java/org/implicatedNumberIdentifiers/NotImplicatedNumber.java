package org.implicatedNumberIdentifiers;

public class NotImplicatedNumber extends ImplicatedNumberIdentifier {

	public NotImplicatedNumber() {

	}

	@Override
	public boolean isImplicatedNumber() {
		return false;
	}

}

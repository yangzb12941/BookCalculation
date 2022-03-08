package org.directSimplifiers;

import org.coefficients.Argument;
import org.symbols.Addition;
import org.symbols.Power;
import org.symbols.Symbol;

import java.util.List;

public class PowerUnifiableGroup extends UnifiableGroup {

	public PowerUnifiableGroup(Symbol unifiableP, int firstIndexP) {
		super(unifiableP, firstIndexP);
	}

	public PowerUnifiableGroup(Symbol unifiableP) {
		super(unifiableP);
	}

	private Addition argsAddition() {
		Addition addArgs = new Addition();
		List<Symbol> addArgsParams = addArgs.getParams();
		for (Argument nextArg : argumentsGroup) {
			addArgsParams.add(nextArg.arg);
		}
		return addArgs;
	}

	@Override
	protected Symbol internalUnify() {
		Power unified = new Power(unifiable, argsAddition());
		return unified.standarizedForm();
	}

}

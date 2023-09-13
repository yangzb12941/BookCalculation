package org.directSimplifiers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DirectSimplifierSet {
    private List<DirectSimplifier> simplifiers;

    public DirectSimplifierSet(List<DirectSimplifier> simps) {
        simplifiers = simps;
    }

    public DirectSimplifierSet(DirectSimplifier...simps) {
        this(new LinkedList<DirectSimplifier>(Arrays.asList(simps)));
    }

    public boolean simplifyNext(boolean onlyLight) {
        DirectSimplifier nextSimplifier;
        for (int i = 0; i < simplifiers.size(); i++) {
            nextSimplifier = simplifiers.get(i);
            if (onlyLight && !nextSimplifier.isLightSimplification()) continue;
            if (simplifiers.get(i).simplifyNext()) return true;
        }
        return false;
    }

	/*public boolean simplify(Symbol s) {
		boolean did = false;
		for (int i = 0; i < simplifiers.size(); i++) {
			did = simplifiers.get(i).simplify(s) || did;
		}
		return did;
	}*/
}

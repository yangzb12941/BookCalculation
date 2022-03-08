package org.filters;


import org.symbols.Symbol;

import java.util.LinkedList;
import java.util.List;

///Extracts symbol parameters which have some characteristic, for example numbers.
///T: Info object linked to the searched characteristic
public class SymbolListFilter<T> {
	List<Symbol> list;
	SymbolFilter<T> sf;

	public SymbolListFilter(List<Symbol> listP, SymbolFilter<T> filterMethod) {
		list = listP;
		sf = filterMethod;
	}

	public List<SymbolFilterResult<T>> filter() {
		List<SymbolFilterResult<T>> buff = new LinkedList<SymbolFilterResult<T>>();
		for (int i = 0; i < list.size(); i++) {
			Symbol sym = list.get(i);
			T nextInfo = sf.filter(sym);
			if (sf.isMatching(nextInfo)) buff.add(new SymbolFilterResult<T>(sym, nextInfo, i));
		}
		return buff;
	}
}

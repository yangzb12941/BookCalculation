package org.symbolComponents;

///an ID in a symbol- symbols may or may not keep a calc number id.
public abstract class SymbolID {
	public abstract CalcNumber Id();
	public abstract void setId(CalcNumber newId);
	public abstract int idHash();
}



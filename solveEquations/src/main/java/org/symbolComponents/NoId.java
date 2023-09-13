package org.symbolComponents;

public class NoId extends SymbolID{
    @Override
    public CalcNumber Id() {
        return null;
    }

    @Override
    public void setId(CalcNumber newId) {
        return;
    }

    @Override
    public int idHash() {
        return 0;
    }
}

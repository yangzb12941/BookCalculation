package org.symbolComponents;

public class CalcNumberID extends SymbolID {
    protected CalcNumber id;

    public CalcNumberID() {
        id = null;
    }

    public CalcNumberID(CalcNumber num) {
        id = num;
    }

    @Override
    public CalcNumber Id() {
        return id;
    }

    @Override
    public void setId(CalcNumber newId) {
        id = newId;
    }

    @Override
    public int idHash() {
        return id.hashCode();
    }
}

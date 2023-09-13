package org.symbolComponents;

public class ConstantCalcNumberID extends CalcNumberID{
    private boolean isSet;
    public ConstantCalcNumberID() {

    }

    public ConstantCalcNumberID(CalcNumber idP) {
        super(idP);
        isSet = true;
    }

    @Override
    public CalcNumber Id() {
        return id;
    }

    @Override
    public void setId(CalcNumber newId) {
        if (isSet) throw new IllegalArgumentException();
        if (newId == null) return;
        id = newId;
        isSet = true;
    }
}

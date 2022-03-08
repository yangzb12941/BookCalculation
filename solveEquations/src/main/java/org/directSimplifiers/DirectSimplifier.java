package org.directSimplifiers;

public interface DirectSimplifier {
    ///Responsible for the simplification of the Symbol WITHOUT pre-simplification of its parameters.
    ///public boolean simplify(Symbol s); ///Simplifies the symbol, returns true if took any action
    public boolean isLightSimplification(); ///A LIGHT SIMPLIFICATION is a simplification who is not shown as a step.
    ///Classic examples: 3*1 -> 3
    ///Useful, for example,  when opening expressions: 3(x+2) -> 3x + 6
    ///													        (not 3x + 3*2)
    public boolean simplifyNext(); 		   ///Takes next step in simplifying the symbol, returns true
    ///if took any action.
}
